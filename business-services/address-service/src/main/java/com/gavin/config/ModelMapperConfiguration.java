package com.gavin.config;

import com.gavin.entity.AddressEntity;
import com.gavin.model.dto.address.AddressDto;
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

        modelMapper.createTypeMap(AddressEntity.class, AddressDto.class).setConverter(context -> {
            AddressEntity source = context.getSource();
            AddressDto destination = new AddressDto();

            destination.setId(source.getId());
            destination.setUserId(source.getUserId());
            destination.setConsignee(source.getConsignee());
            destination.setPhoneNumber(source.getPhoneNumber());
            destination.setZipCode(source.getZipCode());
            destination.setDefaultFlag(source.isDefaultFlag());
            destination.setComment(source.getComment());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(source.getDistrictEntity().getCityEntity().getProvinceEntity().getCountryEntity().getName());
            stringBuilder.append(" ");
            stringBuilder.append(source.getDistrictEntity().getCityEntity().getProvinceEntity().getName());
            stringBuilder.append(" ");
            stringBuilder.append(source.getDistrictEntity().getCityEntity().getName());
            stringBuilder.append(" ");
            stringBuilder.append(source.getDistrictEntity().getName());
            stringBuilder.append(" ");
            stringBuilder.append(source.getStreet());
            stringBuilder.append(" ");
            stringBuilder.append(source.getBuilding());
            stringBuilder.append(" ");
            stringBuilder.append(source.getRoom());

            destination.setAddress(stringBuilder.toString());
            return destination;
        });

        return modelMapper;
    }

}
