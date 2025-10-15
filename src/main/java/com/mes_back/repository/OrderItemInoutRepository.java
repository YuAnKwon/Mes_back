package com.mes_back.repository;

import com.mes_back.constant.Yn;
import com.mes_back.entity.OrderItemInout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemInoutRepository extends JpaRepository<OrderItemInout,Long> {
    long countByLotNumStartingWith(String yyyyMMdd);
    List<OrderItemInout> findByOutNumIsNullAndInDelYn(Yn yn);
    long countByOutNumStartingWith(String yyyyMMdd);
    List<OrderItemInout> findByOutNumIsNotNullAndOutDelYn(Yn yn);
}
