package com.gavin.business.repository;

import com.gavin.business.domain.Order;
import com.gavin.common.enums.OrderStatusEnums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Page<Order> findByUserIdAndStatusNot(String _userId, OrderStatusEnums status, Pageable _pageable);

}
