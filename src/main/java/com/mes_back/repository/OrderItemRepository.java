package com.mes_back.repository;

import com.mes_back.constant.Yn;
import com.mes_back.entity.OrderItem;
import com.mes_back.entity.OrderItemInout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByUseYn(Yn Yn);
    List<OrderItem> findByCompany_BusinessYn(Yn businessYn);
    boolean existsByItemCode(String itemCode);
}
