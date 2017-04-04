package com.gavin.repository;

import com.gavin.entity.CancelReservationEventEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancelReservationEventRepository extends JpaRepository<CancelReservationEventEntity, String> {

    List<CancelReservationEventEntity> findByStatusIs(MessageableEventStatusEnums _status, Pageable _pageable);

    CancelReservationEventEntity findByOriginId(String _originId);

}
