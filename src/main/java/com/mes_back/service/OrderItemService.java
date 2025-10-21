package com.mes_back.service;

import com.mes_back.constant.CoatingMethod;
import com.mes_back.constant.MaterialType;
import com.mes_back.constant.OrderItemType;
import com.mes_back.constant.Yn;
import com.mes_back.dto.MaterialDto;
import com.mes_back.dto.MaterialListDto;
import com.mes_back.dto.OrderItemDto;
import com.mes_back.dto.OrderItemListDto;
import com.mes_back.entity.*;
import com.mes_back.repository.CompanyRepository;
import com.mes_back.repository.OrderItemImgRepository;
import com.mes_back.repository.OrderItemRepository;
import com.mes_back.repository.OrderItemRoutingRepository;
import com.mes_back.repository.RoutingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final CompanyRepository companyRepository;
    private final OrderItemImgService orderItemImgService;
    private final RoutingRepository routingRepository;
    private final OrderItemRoutingRepository orderItemRoutingRepository;
    private final OrderItemImgRepository orderItemImgRepository;

    //register and update
    //수주품목대상 등록(Dto에서 받은 값을 새로 생성한 OrderItem(엔티티) 객체에 넣기 ==> DB에 저장)
    @Transactional
    public OrderItem saveOrderItem(Long id, OrderItemDto dto, List<MultipartFile> imgFiles) {
        OrderItem orderItem;

        if (id == null) {
            // 신규 등록
            orderItem = new OrderItem();
        } else {
            // 수정
            //DB에서 기존 엔티티 조회
            orderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 업체가 존재하지 않습니다."));
        }

        // String → Company 엔티티 변환
        //DTO에 담긴 회사명으로 Company 엔티티를 찾아 연관관계 설정(외래키 매핑).
        Company company = companyRepository.findByCompanyName(dto.getCompany())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회사입니다."));


        // 공통 필드 설정(entity에 dto로 받은 값 넣기)
        orderItem.setItemCode(dto.getItemCode());
        orderItem.setItemName(dto.getItemName());
        orderItem.setCompany(company);
        orderItem.setType(OrderItemType.valueOf(dto.getType()));
        orderItem.setCoatingMethod(CoatingMethod.valueOf(dto.getCoatingMethod()));
        orderItem.setUnitPrice(dto.getUnitPrice());
        orderItem.setColor(dto.getColor());
        orderItem.setRemark(dto.getRemark());

        //엔티티를 DB에 저장
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        // 이미지 저장 서비스 호출
        //저장된 엔티티와 파일 리스트를 이미지 저장 서비스로 위임(파일 저장 및 이미지 메타 DB 기록).
        orderItemImgService.saveImages(savedOrderItem, imgFiles);

        // ✅ 공정 저장
        if (dto.getRouting() != null) {
            List<OrderItemRouting> routingList = dto.getRouting().stream()
                    .map(rDto -> {
                        Routing routing = routingRepository.findById(rDto.getRoutingId())
                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공정입니다."));
                        return OrderItemRouting.builder()
                                .orderItem(savedOrderItem)
                                .routing(routing)
                                .routingOrder(rDto.getRoutingOrder())
                                .build();
                    })
                    .toList();
            savedOrderItem.getOrderItemRoutings().clear();
            savedOrderItem.getOrderItemRoutings().addAll(routingList);
            orderItemRoutingRepository.saveAll(routingList);
        }

        //저장된 엔티티 반환.
        return savedOrderItem;
    }

    //수주 품목 대상 조회(Entity -> Dto)
    public List<OrderItemListDto> findByCompany_BusinessYn(Yn businessYn) {
        return orderItemRepository.findByCompany_BusinessYn(Yn.Y).stream()
                .map(OrderItemListDto::new)
                .collect(Collectors.toList());
    }

    //수주대상품목 거래상태 변경
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


    //수주대상품목 상세페이지 조회
    public OrderItemDto getOrderItemDetail(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 업체가 존재하지 않습니다."));
        return OrderItemDto.fromEntity(orderItem);
    }

    //이미지 삭제
    // OrderItemService.java
    @Transactional
    public void deleteOrderItemImage(Long imageId) {
        OrderItemImg image = orderItemImgRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("이미지를 찾을 수 없습니다."));

        // 실제 파일도 삭제 (FileService 사용 중이라면)
        orderItemImgService.deleteImage(image.getImgUrl());

        orderItemImgRepository.delete(image);
    }




}
