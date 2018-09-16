package com.gavin.business.service;

import com.gavin.common.dto.user.AddressDto;
import com.gavin.common.dto.user.RegisterAddressDto;

import java.util.List;

public interface AddressService {

    /**
     * 录入地址信息
     *
     * @param _userId
     * @param _address
     * @return
     */
    AddressDto registerAddress(String _userId, RegisterAddressDto _address);

    /**
     * 根据地址ID查询地址信息
     *
     * @param _addressId
     * @return
     */
    AddressDto findAddressById(String _addressId);

    /**
     * 根据用户ID查询该用户的所有地址信息
     *
     * @param _userId
     * @return
     */
    List<AddressDto> findAddressesByUserId(String _userId);

    /**
     * 根据用户ID查询该用户的默认地址信息
     *
     * @param _userId
     * @return
     */
    AddressDto findDefaultAddressByUserId(String _userId);

}
