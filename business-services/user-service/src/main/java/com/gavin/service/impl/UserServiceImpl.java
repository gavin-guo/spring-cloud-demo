package com.gavin.service.impl;

import com.gavin.domain.User;
import com.gavin.domain.UserAuthority;
import com.gavin.enums.AuthorityEnums;
import com.gavin.enums.UserStatusEnums;
import com.gavin.exception.RecordNotFoundException;
import com.gavin.exception.UserExistingException;
import com.gavin.messaging.UserActivatedProcessor;
import com.gavin.messaging.UserCreatedProcessor;
import com.gavin.dto.user.CreateUserDto;
import com.gavin.dto.user.UserDto;
import com.gavin.payload.UserActivatedPayload;
import com.gavin.payload.UserCreatedPayload;
import com.gavin.repository.jpa.UserAuthorityRepository;
import com.gavin.repository.jpa.UserRepository;
import com.gavin.service.UserService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    private UserCreatedProcessor userCreatedProcessor;

    @Autowired
    private UserActivatedProcessor userActivatedProcessor;

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
        log.info("create user successfully. {}", new Gson().toJson(userDto));

        // 发送消息至notification-service。
        UserCreatedPayload payload = modelMapper.map(user, UserCreatedPayload.class);
        payload.setUserId(user.getId());
        Message<UserCreatedPayload> message = MessageBuilder.withPayload(payload).build();
        userCreatedProcessor.output().send(message);

        return userDto;
    }

    @Override
    @Transactional
    public void activateUser(String _userId) {
        User user = Optional.ofNullable(userRepository.findOne(_userId))
                .orElseThrow(() -> new RecordNotFoundException("user", _userId));

        // 该用户已经被激活。
        if (UserStatusEnums.ENABLED.equals(user.getStatus())) {
            return;
        }

        user.setStatus(UserStatusEnums.ENABLED);
        userRepository.save(user);

        // 发送消息至point-service。
        UserActivatedPayload payload = new UserActivatedPayload();
        payload.setUserId(_userId);
        Message<UserActivatedPayload> message = MessageBuilder.withPayload(payload).build();
        userActivatedProcessor.output().send(message);
    }

    @Override
    @Transactional
    public void updateAuthorities(String _userId, String[] _authorities) {
        User user = Optional.ofNullable(userRepository.findOne(_userId))
                .orElseThrow(() -> new RecordNotFoundException("user", _userId));

        // 删除原来所有权限。
        List<UserAuthority> userAuthorityEntities = user.getUserAuthorities();
        userAuthorityRepository.delete(userAuthorityEntities);

        user.setUserAuthorities(null);

        if (_authorities != null && _authorities.length > 0) {
            Arrays.stream(_authorities).forEach(
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
