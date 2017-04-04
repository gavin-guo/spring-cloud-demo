package com.gavin.repository;

import com.gavin.entity.WaitingForPaymentEventEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingForPaymentEventRepository extends JpaRepository<WaitingForPaymentEventEntity, String> {

    List<WaitingForPaymentEventEntity> findByStatusIs(MessageableEventStatusEnums _status, Pageable _pageable);

    WaitingForPaymentEventEntity findByOriginId(String _originId);

    WaitingForPaymentEventEntity findByUserId(String _userId);

}
