package com.mes_back.service;

import com.mes_back.constant.EnumKoreanMapper;
import com.mes_back.constant.Yn;
import com.mes_back.dto.OrderItemInDto;
import com.mes_back.entity.OrderItem;
import com.mes_back.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemInService {
    private final OrderItemRepository orderItemRepository;
    public List<OrderItemInDto> getOrderItemInRegi() {
        // 사용여부가 Y인것만 찾기
        List<OrderItem> ItemList = orderItemRepository.findByUseYn(Yn.Y);

        List<OrderItemInDto> itemDtoList = new ArrayList<>();
        for(OrderItem item : ItemList){
            OrderItemInDto itemInDto = OrderItemInDto.builder()
                    .id(item.getId())
                    .itemName(item.getItemName())
                    .itemCode(item.getItemCode())
                    .company(item.getCompany().getCompanyName())
                    .type(EnumKoreanMapper.getOrderItemTypeKorean(item.getType().name())) // 한글 변환
                    .remark(item.getRemark())
                    .build();

            itemDtoList.add(itemInDto);
        }
        return itemDtoList;
    }
}
