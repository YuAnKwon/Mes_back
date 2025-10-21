package com.mes_back.repository;

import com.mes_back.entity.Material;
import com.mes_back.entity.OrderItem;
import com.mes_back.entity.OrderItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemImgRepository extends JpaRepository<OrderItemImg,Long>  {

    // OrderItem 객체로 이미지 리스트 조회
    List<OrderItemImg> findByOrderItem(OrderItem orderItem);

    // orderItemId로 조회하면서 repYn이 'Y'인 이미지 찾기
    Optional<OrderItemImg> findByOrderItemIdAndRepYn(Long orderItemId, String repYn);
}
