package com.gavin.repository;

import com.gavin.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, String> {

    AddressEntity findByUserIdAndDefaultFlag(String _userId, boolean _defaultFlag);

}
