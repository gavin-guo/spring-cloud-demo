package com.gavin.repository;

import com.gavin.entity.UserCreatedEventEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCreatedEventRepository extends JpaRepository<UserCreatedEventEntity, String> {

    List<UserCreatedEventEntity> findByStatusIs(MessageableEventStatusEnums _status, Pageable _pageable);

    UserCreatedEventEntity findByOriginId(String _originId);

    UserCreatedEventEntity findByUserId(String _userId);

}
