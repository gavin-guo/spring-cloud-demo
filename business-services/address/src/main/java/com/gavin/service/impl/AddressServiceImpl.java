package com.gavin.service.impl;

import com.gavin.domain.Address;
import com.gavin.domain.District;
import com.gavin.exception.RecordNotFoundException;
import com.gavin.dto.address.AddressDto;
import com.gavin.dto.address.RegisterAddressDto;
import com.gavin.repository.jpa.AddressRepository;
import com.gavin.repository.jpa.DistrictRepository;
import com.gavin.service.AddressService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public AddressDto registerAddress(RegisterAddressDto _address) {
        String districtId = _address.getDistrictId();
        District district = Optional.ofNullable(districtRepository.findOne(districtId))
                .orElseThrow(() -> new RecordNotFoundException("district", districtId));

        Address address = modelMapper.map(_address, Address.class);
        address.setDistrict(district);

        addressRepository.save(address);

        AddressDto addressDto = modelMapper.map(address, AddressDto.class);
        log.debug("register address successfully. {}", new Gson().toJson(addressDto));

        return addressDto;
    }

    @Override
    public AddressDto findAddressById(String _addressId) {
        Address address = Optional.ofNullable(addressRepository.findOne(_addressId))
                .orElseThrow(() -> new RecordNotFoundException("address", _addressId));

        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public AddressDto findDefaultAddressByUserId(String _userId) {
        Address address = Optional.ofNullable(addressRepository.findByUserIdAndDefaultFlag(_userId, true))
                .orElseThrow(() -> new RecordNotFoundException("address", String.format("userId=%s", _userId)));

        return modelMapper.map(address, AddressDto.class);
    }

}
