package com.gavin.repository;

import com.gavin.entity.PaymentSucceededEventEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentSucceededEventRepository extends JpaRepository<PaymentSucceededEventEntity, String> {

    List<PaymentSucceededEventEntity> findByStatusIs(MessageableEventStatusEnums _status, Pageable _pageable);

    PaymentSucceededEventEntity findByOriginId(String _originId);

}
