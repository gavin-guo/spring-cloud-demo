package com.gavin.repository.jpa;

import com.gavin.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    Address findByUserIdAndDefaultFlag(String _userId, boolean _defaultFlag);

    List<Address> findByUserId(String _userId);

}
