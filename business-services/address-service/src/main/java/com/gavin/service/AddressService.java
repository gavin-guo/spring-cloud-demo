package com.gavin.service;

import com.gavin.model.dto.address.RegisterAddressDto;
import com.gavin.model.vo.address.AddressVo;

public interface AddressService {

    /**
     * 录入地址信息
     *
     * @param _address
     * @return
     */
    AddressVo registerAddress(RegisterAddressDto _address);

    /**
     * 根据地址ID查询地址信息
     *
     * @param _addressId
     * @return
     */
    AddressVo findAddressById(String _addressId);

}
