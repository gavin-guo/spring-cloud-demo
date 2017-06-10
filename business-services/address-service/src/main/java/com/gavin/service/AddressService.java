package com.gavin.service;

import com.gavin.model.dto.address.AddressDto;
import com.gavin.model.dto.address.RegisterAddressDto;

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

    /**
     * 根据用户ID查询该用户的默认地址信息
     *
     * @param _userId
     * @return
     */
    AddressDto findDefaultAddressByUserId(String _userId);

}
