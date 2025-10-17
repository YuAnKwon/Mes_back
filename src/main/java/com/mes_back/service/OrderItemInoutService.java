package com.mes_back.service;

import com.mes_back.constant.CompletedStatus;
import com.mes_back.constant.EnumKoreanMapper;
import com.mes_back.constant.Yn;
import com.mes_back.dto.OrderItemInOutDto;
import com.mes_back.dto.ProcessStatusDto;
import com.mes_back.dto.ShipInvoiceDto;
import com.mes_back.entity.OrderItem;
import com.mes_back.entity.OrderItemInRouting;
import com.mes_back.entity.OrderItemInout;
import com.mes_back.entity.OrderItemRouting;
import com.mes_back.repository.OrderItemInRoutingRepository;
import com.mes_back.repository.OrderItemInoutRepository;
import com.mes_back.repository.OrderItemRepository;
import com.mes_back.repository.OrderItemRoutingRepository;
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
public class OrderItemInoutService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemInoutRepository orderItemInoutRepository;
    private final OrderItemRoutingRepository orderItemRoutingRepository;
    private final OrderItemInRoutingRepository orderItemInRoutingRepository;

    public List<OrderItemInOutDto> getOrderItemInRegi() {
        // 사용여부가 Y인것만 찾기
        List<OrderItem> ItemList = orderItemRepository.findByUseYn(Yn.Y);

        List<OrderItemInOutDto> itemDtoList = new ArrayList<>();
        for (OrderItem item : ItemList) {
            OrderItemInOutDto itemInDto = OrderItemInOutDto.builder()
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

    public void registerIn(List<OrderItemInOutDto> orderItemInOutDto) {

        // 입고일자별로 LOT 번호 순번 관리를 위해 Map 사용. DB 조회 최소화 (같은 날짜 여러 건이라도 한 번만 조회)
        // Key : 입고일자, Value : 해당일자의 lot순번 마지막 번호
        Map<LocalDate, Long> lotCountMap = new HashMap<>();

        for (OrderItemInOutDto dto : orderItemInOutDto) {
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

            // 입고 라우팅 저장
            List<OrderItemRouting> routings = orderItemRoutingRepository.findByOrderItem(orderItem);

            for (OrderItemRouting routing : routings) {
                OrderItemInRouting orderItemInRouting = OrderItemInRouting.builder()
                        .orderItemInout(orderItemInout)
                        .orderItemRouting(routing)
                        .completedStatus(CompletedStatus.N)
                        .startTime(null)
                        .build();

                orderItemInRoutingRepository.save(orderItemInRouting);
            }
        }
    }

    public List<OrderItemInOutDto> getOrderItemIn() {
        // 입고된 수주 조회 (출고번호가 null인것만)
        List<OrderItemInout> itemList = orderItemInoutRepository.findByOutNumIsNullAndInDelYn(Yn.N);

        List<OrderItemInOutDto> itemDtoList = new ArrayList<>();

        for (OrderItemInout item : itemList) {
            OrderItemInOutDto itemInDto = OrderItemInOutDto.builder()
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

    public Long updateOrderItemIn(Long id, OrderItemInOutDto dto) {
        OrderItemInout orderItemInout = orderItemInoutRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        // date 변환
        Instant instant = Instant.parse(dto.getInDate()); // "2025-10-15T15:00:00.000Z"
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        orderItemInout.setInAmount(dto.getInAmount());
        orderItemInout.setInDate(Date.valueOf(localDate));
        orderItemInoutRepository.save(orderItemInout);
        return orderItemInout.getId();
    }

    public Long deleteOrderItemIn(Long id) {
        OrderItemInout orderItemInout = orderItemInoutRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        orderItemInout.setInDelYn(Yn.Y);
        orderItemInoutRepository.save(orderItemInout);
        return orderItemInout.getId();
    }

    // 수주대상품목 입출고 id 를 받아서. 그것의 출고 번호,수량,일자 수정하기.
    public void registerOut(List<OrderItemInOutDto> orderItemInOutDto) {
        // Key : 출고일자, Value : 해당일자의 out순번 마지막 번호
        Map<LocalDate, Long> outCountMap = new HashMap<>();

        for (OrderItemInOutDto dto : orderItemInOutDto) {
            // 수주대상품목 입출고 id로 찾기.
            OrderItemInout item = orderItemInoutRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

            // String → LocalDate 변환
            Instant instant = Instant.parse(dto.getOutDate());
            LocalDate outDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            // 출고일자로 기존 OUT 개수 확인
            long count;
            //Map에 이미 있으면 기존 순번 사용, 없으면 DB에서 조회
            if (outCountMap.containsKey(outDate)) {
                count = outCountMap.get(outDate);
            } else {
                count = orderItemInoutRepository.countByOutNumStartingWith(
                        "OUT-" + outDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                );
            }
            // OUT 번호 생성: OUT-YYYYMMDD-00N
            String outNum = String.format("OUT-%s-%03d",
                    outDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                    count + 1);
            // Map에 순번 갱신
            outCountMap.put(outDate, count + 1);

            // 출고 정보 업데이트
            item.setOutNum(outNum);
            item.setOutAmount(dto.getOutAmount());
            item.setOutDate(Date.valueOf(outDate));

            orderItemInoutRepository.save(item);
        }
    }

    public List<OrderItemInOutDto> getOrderItemOut() {
        // 출고된 수주 조회 (출고번호가 notnull인거랑 출고삭제여부 N인거)
        List<OrderItemInout> itemList = orderItemInoutRepository.findByOutNumIsNotNullAndOutDelYn(Yn.N);

        List<OrderItemInOutDto> itemDtoList = new ArrayList<>();
        for (OrderItemInout item : itemList) {
            OrderItemInOutDto itemInDto = OrderItemInOutDto.builder()
                    .id(item.getId())
                    .outNum(item.getOutNum())
                    .itemName(item.getOrderItem().getItemName())
                    .itemCode(item.getOrderItem().getItemCode())
                    .company(item.getOrderItem().getCompany().getCompanyName())
                    .type(EnumKoreanMapper.getOrderItemTypeKorean(item.getOrderItem().getType().name())) // 한글 변환
                    .outAmount(item.getOutAmount())
                    .outDate(String.valueOf(item.getOutDate()))
                    .build();

            itemDtoList.add(itemInDto);
        }
        System.out.println(itemDtoList);
        return itemDtoList;
    }

    // 출고 수정
    public Long updateOrderItemOut(Long id, OrderItemInOutDto dto) {
        OrderItemInout orderItemInout = orderItemInoutRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        // date 변환
        Instant instant = Instant.parse(dto.getOutDate()); // "2025-10-15T15:00:00.000Z"
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        orderItemInout.setOutAmount(dto.getOutAmount());
        orderItemInout.setOutDate(Date.valueOf(localDate));
        orderItemInoutRepository.save(orderItemInout);
        return orderItemInout.getId();
    }

    // 출고 삭제
    public Long deleteOrderItemOut(Long id) {
        OrderItemInout orderItemInout = orderItemInoutRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        orderItemInout.setOutDelYn(Yn.Y);
        orderItemInoutRepository.save(orderItemInout);
        return orderItemInout.getId();
    }


    // 출하증
    public ShipInvoiceDto getShipmentInvoice(Long id) {
        //id로 수주입출고 entity 찾기
        OrderItemInout orderItemInout = orderItemInoutRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        // 주소
        String addr = orderItemInout.getOrderItem().getCompany().getAddressBase() + " " + orderItemInout.getOrderItem().getCompany().getAddressDetail();

        // 출하증 dto에 값 넣기
        ShipInvoiceDto dto = ShipInvoiceDto.builder()
                .id(id)
                .companyName(orderItemInout.getOrderItem().getCompany().getCompanyName())
                .outNum(orderItemInout.getOutNum())
                .inDate(String.valueOf(orderItemInout.getInDate()))
                .outDate(String.valueOf(orderItemInout.getOutDate()))
                .companyAddr(addr)
                .itemCode(orderItemInout.getOrderItem().getItemCode())
                .itemName(orderItemInout.getOrderItem().getItemName())
                .outAmount(orderItemInout.getOutAmount())
                .build();
        return dto;
    }

    //여기 id는 입출고 id임
    public List<ProcessStatusDto> getRouting(Long id) {
        // 입출고 id 받아와서 입고라우팅에서 찾기
        OrderItemInout inout = orderItemInoutRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<OrderItemInRouting> list = orderItemInRoutingRepository.findByOrderItemInout(inout);

        List<ProcessStatusDto> dtoList = new ArrayList<>();
        for(OrderItemInRouting routing : list){
            ProcessStatusDto dto = ProcessStatusDto.builder()
                    .id(routing.getId())
                    .routingOrder(routing.getOrderItemRouting().getRoutingOrder())
                    .processCode(routing.getOrderItemRouting().getRouting().getProcessCode())
                    .processName(routing.getOrderItemRouting().getRouting().getProcessName())
                    .processTime(routing.getOrderItemRouting().getRouting().getProcessTime())
                    .remark(routing.getOrderItemRouting().getRouting().getRemark())
                    .startTime(String.valueOf(routing.getStartTime()))
                    .completedStatus(String.valueOf(routing.getCompletedStatus()))
                    .build();

            dtoList.add(dto);
        }
        return dtoList;
    }

    public Long updateProcessStatus(Long id, ProcessStatusDto dto) {
        // 수주입고 라우팅 id로 수주입출고 라우팅 테이블 찾기
        OrderItemInRouting orderItemInRouting = orderItemInRoutingRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        Instant instant = Instant.parse(dto.getStartTime()); // "2025-10-15T15:00:00.000Z"
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        orderItemInRouting.setStartTime(Date.valueOf(localDate));
        orderItemInRouting.setCompletedStatus(CompletedStatus.valueOf(dto.getCompletedStatus()));
        orderItemInRoutingRepository.save(orderItemInRouting);
        return orderItemInRouting.getId();
    }
}
