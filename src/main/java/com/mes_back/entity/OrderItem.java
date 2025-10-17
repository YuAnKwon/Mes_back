package com.mes_back.entity;


import com.mes_back.constant.CoatingMethod;
import com.mes_back.constant.OrderItemType;
import com.mes_back.constant.Yn;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends BaseTimeEntity {

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @Column(nullable = false)
    private String itemName; // 품목명

    @Column(nullable = false)
    private String itemCode; // 품목코드

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderItemType type; // 분류

    @Column(nullable = false)
    private Integer unitPrice; // 품목단가

    @Column(nullable = false)
    private String color; // 색상

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CoatingMethod coatingMethod; // 도장방식

    @Column(columnDefinition = "TEXT")
    private String remark; // 비고

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn useYn = Yn.Y; // 사용여부

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemImg> images = new ArrayList<>();
}
