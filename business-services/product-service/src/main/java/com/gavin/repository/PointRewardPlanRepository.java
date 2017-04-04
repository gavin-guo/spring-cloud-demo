package com.gavin.repository;

import com.gavin.entity.PointRewardPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointRewardPlanRepository extends JpaRepository<PointRewardPlanEntity, String> {

    @Query("select p from PointRewardPlanEntity p " +
            "where p.productId = :productId " +
            "and to_days(current_date()) BETWEEN to_days(p.startDate) AND to_days(p.endDate)"
    )
    Optional<PointRewardPlanEntity> findApplicablePlanByProductId(@Param("productId") String _productId);

}
