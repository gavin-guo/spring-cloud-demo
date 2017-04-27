package com.gavin.service;

import com.gavin.model.dto.address.RegisterAddressDto;
import com.gavin.model.dto.address.AddressDto;

public interface AddressService {

    /**
     * 录入地址信息
     *
     * @param _address
     * @return
     */
    AddressDto registerAddress(RegisterAddressDto _address);

    /**
     * 根据地址ID查询地址信息
     *
     * @param _addressId
     * @return
     */
    AddressDto findAddressById(String _addressId);

}
