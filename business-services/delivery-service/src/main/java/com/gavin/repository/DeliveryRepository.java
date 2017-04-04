package com.gavin.repository;

import com.gavin.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, String> {

    DeliveryEntity findByOrderId(String _orderId);

}
