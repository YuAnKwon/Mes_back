package com.mes_back.entity;


import com.mes_back.constant.CoatingMethod;
import com.mes_back.constant.OrderItemType;
import com.mes_back.constant.Yn;
import jakarta.persistence.*;
import lombok.*;

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
    private String itemName;

    @Column(nullable = false)
    private String itemCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderItemType type;

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CoatingMethod coatingMethod;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn useYn = Yn.Y;
}
