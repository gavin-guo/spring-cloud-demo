package com.gavin.repository;

import com.gavin.entity.ProductReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReservationRepository extends JpaRepository<ProductReservationEntity, String> {

    List<ProductReservationEntity> findByOrderId(String _orderId);

}
