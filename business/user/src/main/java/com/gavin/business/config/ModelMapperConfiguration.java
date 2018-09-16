package com.gavin.business.config;

import com.gavin.business.domain.Address;
import com.gavin.business.domain.User;
import com.gavin.common.dto.user.AddressDto;
import com.gavin.common.dto.user.AuthorityDto;
import com.gavin.common.dto.user.UserDto;
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
            destination.setNickName(source.getNickName());
            destination.setEmail(source.getEmail());
            destination.setPhoneNumber(source.getPhoneNumber());

            List<AuthorityDto> authorityDtos = new ArrayList<>();
            source.getUserAuthorities().forEach(
                    userAuthority -> {
                        AuthorityDto authorityDto = new AuthorityDto();
                        authorityDto.setAuthority(userAuthority.getAuthority().name());
                        authorityDtos.add(authorityDto);
                    }
            );
            destination.setAuthorities(authorityDtos);

            return destination;
        });

        modelMapper.createTypeMap(Address.class, AddressDto.class).setConverter(context -> {
            Address source = context.getSource();
            AddressDto destination = new AddressDto();

            destination.setId(source.getId());
            destination.setUserId(source.getUserId());
            destination.setConsignee(source.getConsignee());
            destination.setPhoneNumber(source.getPhoneNumber());
            destination.setZipCode(source.getZipCode());
            destination.setDefaultAddress(source.isDefaultAddress());
            destination.setComment(source.getComment());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(source.getDistrict().getCity().getProvince().getCountry().getName());
            stringBuilder.append(source.getDistrict().getCity().getProvince().getName());
            if (!source.getDistrict().getCity().isMunicipality()) {
                stringBuilder.append(source.getDistrict().getCity().getName());
            }
            stringBuilder.append(source.getDistrict().getName());
            stringBuilder.append(source.getStreet());
            stringBuilder.append(source.getBuilding());
            stringBuilder.append(source.getRoom());

            destination.setAddress(stringBuilder.toString());
            return destination;
        });

        return modelMapper;
    }

}
