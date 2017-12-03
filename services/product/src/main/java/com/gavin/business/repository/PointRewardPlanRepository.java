package com.gavin.business.repository;

import com.gavin.business.domain.PointRewardPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointRewardPlanRepository extends JpaRepository<PointRewardPlan, String> {

    @Query("select p from PointRewardPlan p " +
            "where p.productId = :productId " +
            "and to_days(current_date()) BETWEEN to_days(p.startDate) AND to_days(p.endDate)"
    )
    Optional<PointRewardPlan> findApplicablePlanByProductId(@Param("productId") String _productId);

}
