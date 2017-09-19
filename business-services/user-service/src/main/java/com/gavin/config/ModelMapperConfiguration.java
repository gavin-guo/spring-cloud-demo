package com.gavin.config;

import com.gavin.domain.User;
import com.gavin.dto.user.AuthorityDto;
import com.gavin.dto.user.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(User.class, UserDto.class).setConverter(context -> {
            User source = context.getSource();
            UserDto destination = new UserDto();

            destination.setId(source.getId());
            destination.setLoginName(source.getLoginName());
            destination.setPassword(source.getPassword());
            destination.setGrade(source.getGrade());

            List<AuthorityDto> authorityDtos = new ArrayList<>();
            source.getUserAuthorities().forEach(
                    userAuthorityEntity -> {
                        AuthorityDto authorityDto = new AuthorityDto();
                        authorityDto.setAuthority(userAuthorityEntity.getAuthority().name());
                        authorityDtos.add(authorityDto);
                    }
            );
            destination.setAuthorities(authorityDtos);

            return destination;
        });

        return modelMapper;
    }

}
