package com.mes_back.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_item_routing")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRouting extends BaseTimeEntity {

    @Id
    @Column(name = "order_item_routing_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routing_id")
    private Routing routing;

    @Column(nullable = false)
    private Integer routingOrder;

}
