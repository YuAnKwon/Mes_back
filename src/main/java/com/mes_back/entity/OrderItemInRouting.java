package com.mes_back.entity;

import com.mes_back.constant.CompletedStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "order_item_in_routing")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemInRouting extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_in_id")
    private OrderItemInout orderItemInout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_routing_id")
    private OrderItemRouting orderItemRouting;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CompletedStatus completedStatus = CompletedStatus.N;

    private Date startTime;

}
