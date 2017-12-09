package com.gavin.business.repository;

import com.gavin.business.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    Address findByUserIdAndDefaultAddress(String _userId, boolean _defaultAddress);

    List<Address> findByUserId(String _userId);

}
