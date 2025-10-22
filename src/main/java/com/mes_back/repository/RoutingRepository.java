package com.mes_back.repository;

import com.mes_back.constant.Yn;
import com.mes_back.entity.OrderItem;
import com.mes_back.entity.Routing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutingRepository extends JpaRepository<Routing,Long> {
    List<Routing> findByDelYn(Yn yn);

    boolean existsByProcessCodeAndDelYn(String processCode, Yn yn);
}
