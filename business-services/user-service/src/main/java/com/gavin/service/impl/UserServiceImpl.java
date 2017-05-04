package com.gavin.service.impl;

import com.gavin.entity.UserAuthorityEntity;
import com.gavin.entity.UserEntity;
import com.gavin.enums.AuthorityEnums;
import com.gavin.enums.UserStatusEnums;
import com.gavin.exception.RecordNotFoundException;
import com.gavin.exception.UserExistingException;
import com.gavin.messaging.UserActivatedProcessor;
import com.gavin.messaging.UserCreatedProcessor;
import com.gavin.model.dto.user.CreateUserDto;
import com.gavin.model.dto.user.UserDto;
import com.gavin.payload.UserActivatedPayload;
import com.gavin.payload.UserCreatedPayload;
import com.gavin.repository.UserAuthorityRepository;
import com.gavin.repository.UserRepository;
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
                        userEntity -> {
                            throw new UserExistingException(String.format("user(login_name = %s) already exists.", _user.getLoginName()));
                        }
                );

        UserEntity userEntity = modelMapper.map(_user, UserEntity.class);
        String encodedPassword = encoder.encode(_user.getPassword());
        userEntity.setPassword(encodedPassword);
        userEntity.setStatus(UserStatusEnums.CREATED);

        // 赋予用户默认权限。
        UserAuthorityEntity userAuthorityEntity = new UserAuthorityEntity();
        userAuthorityEntity.setAuthority(AuthorityEnums.AUTHORITY_DEFAULT);
        userEntity.addUserAuthorityEntity(userAuthorityEntity);

        userRepository.save(userEntity);

        UserDto userDto = modelMapper.map(userEntity, UserDto.class);
        userDto.setPassword(null);
        log.info("create user successfully. {}", new Gson().toJson(userDto));

        // 发送消息至notification-service。
        UserCreatedPayload payload = modelMapper.map(userEntity, UserCreatedPayload.class);
        payload.setUserId(userEntity.getId());
        Message<UserCreatedPayload> message = MessageBuilder.withPayload(payload).build();
        userCreatedProcessor.output().send(message);

        return userDto;
    }

    @Override
    @Transactional
    public void activateUser(String _userId) {
        UserEntity userEntity = Optional.ofNullable(userRepository.findOne(_userId))
                .orElseThrow(() -> new RecordNotFoundException("user", _userId));

        // 该用户已经被激活。
        if (UserStatusEnums.ENABLED.equals(userEntity.getStatus())) {
            return;
        }

        userEntity.setStatus(UserStatusEnums.ENABLED);
        userRepository.save(userEntity);

        // 发送消息至point-service。
        UserActivatedPayload payload = new UserActivatedPayload();
        payload.setUserId(_userId);
        Message<UserActivatedPayload> message = MessageBuilder.withPayload(payload).build();
        userActivatedProcessor.output().send(message);
    }

    @Override
    @Transactional
    public void updateAuthorities(String _userId, String[] _authorities) {
        UserEntity userEntity = Optional.ofNullable(userRepository.findOne(_userId))
                .orElseThrow(() -> new RecordNotFoundException("user", _userId));

        // 删除原来所有权限。
        List<UserAuthorityEntity> userAuthorityEntities = userEntity.getUserAuthorityEntities();
        userAuthorityRepository.delete(userAuthorityEntities);

        userEntity.setUserAuthorityEntities(null);

        if (_authorities != null && _authorities.length > 0) {
            Arrays.stream(_authorities).forEach(
                    authority -> {
                        UserAuthorityEntity userAuthorityEntity = new UserAuthorityEntity();
                        userAuthorityEntity.setAuthority(AuthorityEnums.valueOf(authority));
                        userEntity.addUserAuthorityEntity(userAuthorityEntity);
                    }
            );
        }

        userRepository.save(userEntity);
    }

    @Override
    public UserDto findUserByLoginName(String _loginName) {
        UserEntity userEntity = Optional.ofNullable(userRepository.findByLoginName(_loginName))
                .orElseThrow(() -> new RecordNotFoundException("user", _loginName));

        return modelMapper.map(userEntity, UserDto.class);
    }

}
