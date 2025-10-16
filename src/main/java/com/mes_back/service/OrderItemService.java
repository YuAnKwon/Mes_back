package com.mes_back.service;

import com.mes_back.dto.MaterialListDto;
import com.mes_back.dto.OrderItemListDto;
import com.mes_back.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    final OrderItemRepository orderItemRepository;

    //수주 품목 대상 조회(Entity -> Dto)
    public List<OrderItemListDto> findAll() {
        return orderItemRepository.findAll().stream()
                .map(MaterialListDto::new)
                .collect(Collectors.toList());
    }
}
