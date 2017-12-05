package com.gavin.business.repository;

import com.gavin.business.domain.ProductReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReservationRepository extends JpaRepository<ProductReservation, String> {

    List<ProductReservation> findByOrderId(String _orderId);

}
