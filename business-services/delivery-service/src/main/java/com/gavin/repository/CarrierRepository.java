package com.gavin.repository;

import com.gavin.entity.CarrierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrierRepository extends JpaRepository<CarrierEntity, String> {
}
