package com.mes_back.repository;

import com.mes_back.constant.Yn;
import com.mes_back.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByUseYn(Yn yn);
}
