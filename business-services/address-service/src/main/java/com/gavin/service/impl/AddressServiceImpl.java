package com.gavin.service.impl;

import com.gavin.entity.AddressEntity;
import com.gavin.entity.DistrictEntity;
import com.gavin.exception.RecordNotFoundException;
import com.gavin.model.dto.address.RegisterAddressDto;
import com.gavin.model.vo.address.AddressVo;
import com.gavin.repository.AddressRepository;
import com.gavin.repository.DistrictRepository;
import com.gavin.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final ModelMapper modelMapper;

    private final DistrictRepository districtRepository;

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(
            ModelMapper modelMapper,
            AddressRepository addressRepository,
            DistrictRepository districtRepository) {
        this.modelMapper = modelMapper;
        this.addressRepository = addressRepository;
        this.districtRepository = districtRepository;
    }

    @Override
    public AddressVo registerAddress(RegisterAddressDto _address) {
        String districtId = _address.getDistrictId();
        DistrictEntity districtEntity = Optional.ofNullable(districtRepository.findOne(districtId))
                .orElseThrow(() -> new RecordNotFoundException("district", districtId));

        AddressEntity addressEntity = modelMapper.map(_address, AddressEntity.class);
        addressEntity.setDistrictEntity(districtEntity);

        addressRepository.save(addressEntity);
        return modelMapper.map(addressEntity, AddressVo.class);
    }

    @Override
    public AddressVo findAddressById(String _addressId) {
        AddressEntity addressEntity = Optional.ofNullable(addressRepository.findOne(_addressId))
                .orElseThrow(() -> new RecordNotFoundException("address", _addressId));

        return modelMapper.map(addressEntity, AddressVo.class);
    }

}
