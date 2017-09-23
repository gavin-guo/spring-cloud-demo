package com.gavin.config;

import com.gavin.domain.Address;
import com.gavin.dto.address.AddressDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(Address.class, AddressDto.class).setConverter(context -> {
            Address source = context.getSource();
            AddressDto destination = new AddressDto();

            destination.setId(source.getId());
            destination.setUserId(source.getUserId());
            destination.setConsignee(source.getConsignee());
            destination.setPhoneNumber(source.getPhoneNumber());
            destination.setZipCode(source.getZipCode());
            destination.setDefaultFlag(source.isDefaultFlag());
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
