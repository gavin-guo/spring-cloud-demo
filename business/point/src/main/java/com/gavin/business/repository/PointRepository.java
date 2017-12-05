package com.gavin.business.repository;

import com.gavin.business.domain.Point;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, String> {

    @Query("select p from Point p " +
            "where p.userId = :userId " +
            "and to_days(p.expireDate) > to_days(current_date()) " +
            "and p.lockForOrderId is null"
    )
    List<Point> findUsableByAccountId(@Param("userId") String _userId, Sort _sort);

    List<Point> findByLockForOrderId(String _lockForOrderId);

    List<Point> findByExpireDateLessThanEqual(String _today);

}
