package com.gavin.business.config;

import com.gavin.business.domain.Order;
import com.gavin.common.dto.order.ItemDto;
import com.gavin.common.dto.order.OrderDetailsDto;
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

        modelMapper.createTypeMap(Order.class, OrderDetailsDto.class).setConverter(context -> {
            Order source = context.getSource();
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
            source.getItems().forEach(
                    item -> {
                        ItemDto itemVo = new ItemDto();
                        itemVo.setId(item.getId());
                        itemVo.setProductId(item.getProductId());
                        itemVo.setQuantity(item.getQuantity());
                        itemVos.add(itemVo);
                    }
            );
            destination.setItems(itemVos);

            return destination;
        });

        return modelMapper;
    }

}
