package com.mes_back.repository;

import com.mes_back.entity.OrderItemInout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemInoutRepository extends JpaRepository<OrderItemInout,Long> {
    long countByLotNumStartingWith(String prefix);
    List<OrderItemInout> findByOutNumIsNull();

}
