package com.gavin.config;

import com.gavin.entity.OrderEntity;
import com.gavin.model.vo.order.ItemVo;
import com.gavin.model.vo.order.OrderDetailsVo;
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

        modelMapper.createTypeMap(OrderEntity.class, OrderDetailsVo.class).setConverter(context -> {
            OrderEntity source = context.getSource();
            OrderDetailsVo destination = new OrderDetailsVo();

            destination.setId(source.getId());
            destination.setUserId(source.getUserId());
            destination.setStatus(source.getStatus().name());
            destination.setTotalAmount(source.getTotalAmount());
            destination.setRewardPoints(source.getRewardPoints());
            destination.setConsignee(source.getConsignee());
            destination.setAddress(source.getAddress());
            destination.setPhoneNumber(source.getPhoneNumber());

            List<ItemVo> itemVos = new ArrayList<>();
            source.getItemEntities().forEach(
                    itemEntity -> {
                        ItemVo itemVo = new ItemVo();
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
