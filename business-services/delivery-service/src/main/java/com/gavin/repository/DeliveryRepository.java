package com.gavin.repository;

import com.gavin.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, String> {

    Delivery findByOrderId(String _orderId);

}
