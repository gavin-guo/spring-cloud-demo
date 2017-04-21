package com.gavin.repository;

import com.gavin.entity.UserActivatedEventEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivatedEventRepository extends JpaRepository<UserActivatedEventEntity, String> {

    List<UserActivatedEventEntity> findByStatusIs(MessageableEventStatusEnums _status, Pageable _pageable);

    UserActivatedEventEntity findByOriginId(String _originId);

    UserActivatedEventEntity findByUserId(String _userId);

}
