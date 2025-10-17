package com.mes_back.repository;

import com.mes_back.entity.OrderItemInRouting;
import com.mes_back.entity.OrderItemInout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemInRoutingRepository extends JpaRepository<OrderItemInRouting,Long> {
    List<OrderItemInRouting> findByOrderItemInout(OrderItemInout orderItemInout);

    List<OrderItemInRouting> findByOrderItemInoutId(Long orderItemId);
}
