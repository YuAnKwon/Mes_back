package com.mes_back.service;

import com.mes_back.constant.CoatingMethod;
import com.mes_back.constant.MaterialType;
import com.mes_back.constant.OrderItemType;
import com.mes_back.constant.Yn;
import com.mes_back.dto.MaterialDto;
import com.mes_back.dto.MaterialListDto;
import com.mes_back.dto.OrderItemDto;
import com.mes_back.dto.OrderItemListDto;
import com.mes_back.entity.Company;
import com.mes_back.entity.Material;
import com.mes_back.entity.OrderItem;
import com.mes_back.repository.CompanyRepository;
import com.mes_back.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final CompanyRepository companyRepository;

    //register and update
    //업체 등록(Dto에서 받은 값을 새로 생성한 OrderItem(엔티티) 객체에 넣기 ==> DB에 저장)
    @Transactional
    public OrderItem saveOrderItem(Long id, OrderItemDto dto) {
        OrderItem orderItem;

        if (id == null) {
            // 신규 등록
            orderItem = new OrderItem();
        } else {
            // 수정
            orderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 업체가 존재하지 않습니다."));
        }

        System.out.println("입력된 회사명: " + dto.getCompany());

        // String → Company 엔티티 변환
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
        return orderItemRepository.save(orderItem);
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


}
