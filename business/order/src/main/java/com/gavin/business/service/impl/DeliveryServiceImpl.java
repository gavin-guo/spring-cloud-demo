package com.gavin.business.service.impl;

import com.gavin.business.domain.Carrier;
import com.gavin.business.domain.Delivery;
import com.gavin.common.enums.DeliveryStatusEnums;
import com.gavin.common.exception.RecordNotFoundException;
import com.gavin.common.dto.delivery.AssignCarrierDto;
import com.gavin.common.dto.delivery.DeliveryDto;
import com.gavin.business.repository.CarrierRepository;
import com.gavin.business.repository.DeliveryRepository;
import com.gavin.business.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private CarrierRepository carrierRepository;

    @Override
    public void createDelivery(String _orderId, String _consignee, String _address, String _phoneNumber) {
        Delivery delivery = new Delivery();
        delivery.setOrderId(_orderId);
        delivery.setConsignee(_consignee);
        delivery.setAddress(_address);
        delivery.setPhoneNumber(_phoneNumber);
        delivery.setStatus(DeliveryStatusEnums.CREATED);
        deliveryRepository.save(delivery);
    }

    @Override
    public DeliveryDto assignCarrier(String _deliveryId, AssignCarrierDto _assignment) {
        Delivery delivery = Optional.ofNullable(deliveryRepository.findOne(_deliveryId))
                .orElseThrow(() -> new RecordNotFoundException("delivery", _deliveryId));

        String carrierId = _assignment.getCarrierId();
        Carrier carrier = Optional.ofNullable(carrierRepository.findOne(carrierId))
                .orElseThrow(() -> new RecordNotFoundException("carrier", carrierId));

        delivery.setCarrier(carrier);
        delivery.setTrackingNumber(_assignment.getTrackingNumber());
        delivery.setStatus(DeliveryStatusEnums.ASSIGNED);
        deliveryRepository.save(delivery);

        DeliveryDto deliveryDto = modelMapper.map(delivery, DeliveryDto.class);
        deliveryDto.setCarrierName(carrier.getName());
        return deliveryDto;
    }

    @Override
    public DeliveryDto findDeliveryByOrderId(String _orderId) {
        Delivery delivery = Optional.ofNullable(deliveryRepository.findByOrderId(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("delivery with order", _orderId));

        DeliveryDto deliveryDto = modelMapper.map(delivery, DeliveryDto.class);
        Optional.ofNullable(delivery.getCarrier())
                .ifPresent(carrier -> deliveryDto.setCarrierName(carrier.getName()));

        return deliveryDto;
    }

    @Override
    public void updateDeliveryStatus(String _deliveryId, String _status) {
        Delivery delivery = Optional.ofNullable(deliveryRepository.findOne(_deliveryId))
                .orElseThrow(() -> new RecordNotFoundException("delivery", _deliveryId));

        delivery.setStatus(DeliveryStatusEnums.valueOf(_status));
        deliveryRepository.save(delivery);
    }

}
