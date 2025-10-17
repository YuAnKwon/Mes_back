package com.mes_back.service;

import com.mes_back.constant.Yn;
import com.mes_back.dto.MaterialListDto;
import com.mes_back.dto.OrderItemListDto;
import com.mes_back.entity.Material;
import com.mes_back.entity.OrderItem;
import com.mes_back.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    final OrderItemRepository orderItemRepository;

    //수주 품목 대상 조회(Entity -> Dto)
    public List<OrderItemListDto> findByCompany_BusinessYn(Yn businessYn) {
        return orderItemRepository.findByCompany_BusinessYn(Yn.Y).stream()
                .map(OrderItemListDto::new)
                .collect(Collectors.toList());
    }

    //원자재 거래상태 변경
    @Transactional
    public Long updateUseYn(Long id, Yn updatedYn) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 업체가 존재하지 않습니다."));
        if( updatedYn == Yn.Y ) {
            orderItem.setUseYn(Yn.N);
        }else {
            orderItem.setUseYn(Yn.Y);
        }
        orderItemRepository.save(orderItem);
        return orderItem.getId();
    }
}
