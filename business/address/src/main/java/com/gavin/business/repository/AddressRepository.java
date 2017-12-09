package com.gavin.business.repository;

import com.gavin.business.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    Address findByUserIdAndDefaultAddress(String _userId, boolean _defaultAddress);

    List<Address> findByUserId(String _userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "update address set default_address = 0 where user_id = :userId",
            nativeQuery = true
    )
    void clearDefaultAddress(@Param("userId") String _userId);

}
