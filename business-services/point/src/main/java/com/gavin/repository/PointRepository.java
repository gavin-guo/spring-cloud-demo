package com.gavin.repository;

import com.gavin.entity.PointEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, String> {

    @Query("select p from PointEntity p " +
            "where p.userId = :userId " +
            "and to_days(p.expireDate) > to_days(current_date()) " +
            "and p.lockForOrderId is null"
    )
    List<PointEntity> findUsableByAccountId(@Param("userId") String _userId, Sort _sort);

    List<PointEntity> findByLockForOrderId(String _lockForOrderId);

    List<PointEntity> findByExpireDateLessThanEqual(String _today);

}
