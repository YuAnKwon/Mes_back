package com.mes_back.service;

import com.mes_back.constant.EnumKoreanMapper;
import com.mes_back.constant.Yn;
import com.mes_back.dto.OrderItemInDto;
import com.mes_back.entity.OrderItem;
import com.mes_back.entity.OrderItemInout;
import com.mes_back.repository.OrderItemInoutRepository;
import com.mes_back.repository.OrderItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemInService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemInoutRepository orderItemInoutRepository;

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

    public void registerIn(List<OrderItemInDto> orderItemInDto) {

        // 입고일자별로 LOT 번호 순번 관리를 위해 Map 사용. DB 조회 최소화 (같은 날짜 여러 건이라도 한 번만 조회)
        // Key : 입고일자, Value : 해당일자의 lot순번 마지막 번호
        Map<LocalDate, Long> lotCountMap = new HashMap<>();

        for (OrderItemInDto dto : orderItemInDto) {
            OrderItem orderItem = orderItemRepository.findById(dto.getId())
                    .orElseThrow(EntityNotFoundException::new);

            // String → LocalDate 변환
            Instant instant = Instant.parse(dto.getInDate());
            LocalDate inDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            // 입고일자로 기존 LOT 개수 확인
            long count;
            //Map에 이미 있으면 기존 순번 사용, 없으면 DB에서 조회
            if (lotCountMap.containsKey(inDate)) {
                count = lotCountMap.get(inDate);
            } else {
                count = orderItemInoutRepository.countByLotNumStartingWith(
                        "LOT-" + inDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                );
            }

            // LOT 번호 생성: LOT-YYYYMMDD-00N
            String lotNum = String.format("LOT-%s-%03d",
                    inDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                    count + 1);

            // Map에 순번 갱신
            lotCountMap.put(inDate, count + 1);

            // Entity 생성 및 저장
            OrderItemInout orderItemInout = OrderItemInout.builder()
                    .orderItem(orderItem)
                    .inAmount(dto.getInAmount())
                    .lotNum(lotNum)
                    .inDate(Date.valueOf(inDate))
                    .inDelYn(Yn.N)
                    .outDelYn(Yn.N)
                    .build();

            orderItemInoutRepository.save(orderItemInout);
        }
    }

    public List<OrderItemInDto> getOrderItemIn() {
        // 입고된 수주 조회 (출고번호가 null인것만)
        List<OrderItemInout> itemList = orderItemInoutRepository.findByOutNumIsNull();

        // dto 빈리스트
        List<OrderItemInDto> itemDtoList = new ArrayList<>();

        // order_item_id로 다른거 가져와서 넣어야하는데
        for(OrderItemInout item : itemList){
            OrderItemInDto itemInDto = OrderItemInDto.builder()
                    .id(item.getId())
                    .lotNum(item.getLotNum())
                    .itemName(item.getOrderItem().getItemName())
                    .itemCode(item.getOrderItem().getItemCode())
                    .company(item.getOrderItem().getCompany().getCompanyName())
                    .type(EnumKoreanMapper.getOrderItemTypeKorean(item.getOrderItem().getType().name())) // 한글 변환
                    .inAmount(item.getInAmount())
                    .inDate(String.valueOf(item.getInDate()))
                    .build();

            itemDtoList.add(itemInDto);
        }
        System.out.println(itemDtoList);
        return itemDtoList;

    }
}
