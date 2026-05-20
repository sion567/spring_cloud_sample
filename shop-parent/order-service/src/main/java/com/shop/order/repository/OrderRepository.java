package com.shop.order.repository;

import com.shop.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNo(String orderNo);
    Page<Order> findByUserId(Long userId, Pageable pageable);
    Page<Order> findByUserIdAndStatus(Long userId, Integer status, Pageable pageable);
}
