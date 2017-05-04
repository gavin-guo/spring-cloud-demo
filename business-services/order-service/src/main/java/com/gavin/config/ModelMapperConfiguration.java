package com.gavin.config;

import com.gavin.entity.OrderEntity;
import com.gavin.model.dto.order.ItemDto;
import com.gavin.model.dto.order.OrderDetailsDto;
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

        modelMapper.createTypeMap(OrderEntity.class, OrderDetailsDto.class).setConverter(context -> {
            OrderEntity source = context.getSource();
            OrderDetailsDto destination = new OrderDetailsDto();

            destination.setId(source.getId());
            destination.setUserId(source.getUserId());
            destination.setStatus(source.getStatus().name());
            destination.setTotalAmount(source.getTotalAmount());
            destination.setRewardPoints(source.getRewardPoints());
            destination.setConsignee(source.getConsignee());
            destination.setAddress(source.getAddress());
            destination.setPhoneNumber(source.getPhoneNumber());

            List<ItemDto> itemVos = new ArrayList<>();
            source.getItemEntities().forEach(
                    itemEntity -> {
                        ItemDto itemVo = new ItemDto();
                        itemVo.setId(itemEntity.getId());
                        itemVo.setProductId(itemEntity.getProductId());
                        itemVo.setQuantity(itemEntity.getQuantity());
                        itemVos.add(itemVo);
                    }
            );
            destination.setItems(itemVos);

            return destination;
        });

        return modelMapper;
    }

}
