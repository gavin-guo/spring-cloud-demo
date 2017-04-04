package com.gavin.service.impl;

import com.gavin.entity.CarrierEntity;
import com.gavin.entity.DeliveryEntity;
import com.gavin.enums.DeliveryStatusEnums;
import com.gavin.exception.RecordNotFoundException;
import com.gavin.model.dto.delivery.AssignCarrierDto;
import com.gavin.model.vo.delivery.DeliveryVo;
import com.gavin.repository.CarrierRepository;
import com.gavin.repository.DeliveryRepository;
import com.gavin.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final ModelMapper modelMapper;

    private final DeliveryRepository deliveryRepository;

    private final CarrierRepository carrierRepository;

    @Autowired
    public DeliveryServiceImpl(
            ModelMapper modelMapper,
            CarrierRepository carrierRepository,
            DeliveryRepository deliveryRepository) {
        this.modelMapper = modelMapper;
        this.carrierRepository = carrierRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public void createDelivery(String _orderId, String _consignee, String _address, String _phoneNumber) {
        DeliveryEntity deliveryEntity = new DeliveryEntity();
        deliveryEntity.setOrderId(_orderId);
        deliveryEntity.setConsignee(_consignee);
        deliveryEntity.setAddress(_address);
        deliveryEntity.setPhoneNumber(_phoneNumber);
        deliveryEntity.setStatus(DeliveryStatusEnums.CREATED);
        deliveryRepository.save(deliveryEntity);
    }

    @Override
    public DeliveryVo assignCarrier(String _deliveryId, AssignCarrierDto _assignment) {
        DeliveryEntity deliveryEntity = Optional.ofNullable(deliveryRepository.findOne(_deliveryId))
                .orElseThrow(() -> new RecordNotFoundException("delivery", _deliveryId));

        String carrierId = _assignment.getCarrierId();
        CarrierEntity carrierEntity = Optional.ofNullable(carrierRepository.findOne(carrierId))
                .orElseThrow(() -> new RecordNotFoundException("carrier", carrierId));

        deliveryEntity.setCarrierEntity(carrierEntity);
        deliveryEntity.setTrackingNumber(_assignment.getTrackingNumber());
        deliveryEntity.setStatus(DeliveryStatusEnums.ASSIGNED);
        deliveryRepository.save(deliveryEntity);

        DeliveryVo deliveryVo = modelMapper.map(deliveryEntity, DeliveryVo.class);
        deliveryVo.setCarrierName(carrierEntity.getName());
        return deliveryVo;
    }

    @Override
    public DeliveryVo findDeliveryByOrderId(String _orderId) {
        DeliveryEntity deliveryEntity = Optional.ofNullable(deliveryRepository.findByOrderId(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("delivery with order", _orderId));

        DeliveryVo deliveryVo = modelMapper.map(deliveryEntity, DeliveryVo.class);
        Optional.ofNullable(deliveryEntity.getCarrierEntity())
                .ifPresent(carrierEntity -> deliveryVo.setCarrierName(carrierEntity.getName()));

        return deliveryVo;
    }

    @Override
    public void updateDeliveryStatus(String _deliveryId, String _status) {
        DeliveryEntity deliveryEntity = Optional.ofNullable(deliveryRepository.findOne(_deliveryId))
                .orElseThrow(() -> new RecordNotFoundException("delivery", _deliveryId));

        deliveryEntity.setStatus(DeliveryStatusEnums.valueOf(_status));
        deliveryRepository.save(deliveryEntity);
    }

}
