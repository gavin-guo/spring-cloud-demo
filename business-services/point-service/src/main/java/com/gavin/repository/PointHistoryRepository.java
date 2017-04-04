package com.gavin.repository;

import com.gavin.entity.PointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistoryEntity, String> {
}
