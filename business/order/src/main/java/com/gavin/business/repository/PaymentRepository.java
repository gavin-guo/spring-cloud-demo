package com.gavin.business.repository;

import com.gavin.business.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    Page<Payment> findByUserId(String _userId, Pageable _pageable);

}
