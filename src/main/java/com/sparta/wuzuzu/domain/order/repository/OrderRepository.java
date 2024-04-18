package com.sparta.wuzuzu.domain.order.repository;

import com.sparta.wuzuzu.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
