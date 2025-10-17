package com.mes_back.repository;

import com.mes_back.entity.Material;
import com.mes_back.entity.OrderItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemImgRepository extends JpaRepository<OrderItemImg,Long>  {
}
