package com.gavin.repository;

import com.gavin.entity.ArrangeShipmentEventEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArrangeShipmentEventRepository extends JpaRepository<ArrangeShipmentEventEntity, String> {

    List<ArrangeShipmentEventEntity> findByStatusIs(MessageableEventStatusEnums _status, Pageable _pageable);

    ArrangeShipmentEventEntity findByOriginId(String _originId);

}
