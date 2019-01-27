package com.gavin.business.service.impl;

import com.gavin.business.domain.User;
import com.gavin.business.domain.UserAuthority;
import com.gavin.business.exception.UserExistingException;
import com.gavin.business.repository.UserAuthorityRepository;
import com.gavin.business.repository.UserRepository;
import com.gavin.business.service.UserService;
import com.gavin.common.dto.user.CreateUserDto;
import com.gavin.common.dto.user.UserDto;
import com.gavin.common.enums.AuthorityEnums;
import com.gavin.common.enums.UserStatusEnums;
import com.gavin.common.exception.RecordNotFoundException;
import com.gavin.common.messaging.RewardPointsProcessor;
import com.gavin.common.messaging.UserCreatedProcessor;
import com.gavin.common.payload.RewardPointsPayload;
import com.gavin.common.payload.UserCreatedPayload;
import com.gavin.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${points.user-activated-rewards}")
    private Integer rewards;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    private UserCreatedProcessor userCreatedProcessor;

    @Autowired
    private RewardPointsProcessor rewardPointsProcessor;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public UserDto createUser(CreateUserDto _user) {
        // 判断用户名是否已经存在。
        Optional.ofNullable(userRepository.findByLoginName(_user.getLoginName()))
                .ifPresent(
                        user -> {
                            throw new UserExistingException(String.format("user(login_name = %s) already exists.", _user.getLoginName()));
                        }
                );

        User user = modelMapper.map(_user, User.class);
        String encodedPassword = encoder.encode(_user.getPassword());
        user.setPassword(encodedPassword);
        user.setStatus(UserStatusEnums.CREATED);

        // 赋予用户默认权限。
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setAuthority(AuthorityEnums.AUTHORITY_DEFAULT);
        user.addUserAuthority(userAuthority);

        userRepository.save(user);

        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(null);
        log.info("create user successfully. {}", JsonUtils.toJson(userDto));

        // 发送消息至notification。
        UserCreatedPayload payload = modelMapper.map(user, UserCreatedPayload.class);
        payload.setUserId(user.getId());
        Message<UserCreatedPayload> message = MessageBuilder.withPayload(payload).build();
        userCreatedProcessor.producer().send(message);

        return userDto;
    }

    @Override
    @Transactional
    public void activateUser(String _userId) {
        User user = userRepository.findById(_userId)
                .orElseThrow(() -> new RecordNotFoundException("user", _userId));

        // 该用户已经被激活。
        if (UserStatusEnums.ENABLED.equals(user.getStatus())) {
            return;
        }

        user.setStatus(UserStatusEnums.ENABLED);
        userRepository.save(user);

        // 发送消息至point。
        RewardPointsPayload payload = new RewardPointsPayload();
        payload.setUserId(_userId);
        payload.setAmount(new BigDecimal(rewards));
        payload.setReason("new user activated");
        Message<RewardPointsPayload> message = MessageBuilder.withPayload(payload).build();
        rewardPointsProcessor.producer().send(message);
    }

    @Override
    @Transactional
    public void updateAuthorities(String _userId, List<String> _authorities) {
        User user = userRepository.findById(_userId)
                .orElseThrow(() -> new RecordNotFoundException("user", _userId));

        // 删除原来所有权限。
        List<UserAuthority> userAuthorities = user.getUserAuthorities();
        userAuthorityRepository.deleteAll(userAuthorities);

        user.setUserAuthorities(null);

        if (_authorities != null && _authorities.size() > 0) {
            _authorities.stream()
                    .forEach(
                            authority -> {
                                UserAuthority userAuthority = new UserAuthority();
                                userAuthority.setAuthority(AuthorityEnums.valueOf(authority));
                                user.addUserAuthority(userAuthority);
                            }
                    );
        }

        userRepository.save(user);
    }

    @Override
    public UserDto findUserByLoginName(String _loginName) {
        User user = Optional.ofNullable(userRepository.findByLoginName(_loginName))
                .orElseThrow(() -> new RecordNotFoundException("user", _loginName));

        return modelMapper.map(user, UserDto.class);
    }

}
