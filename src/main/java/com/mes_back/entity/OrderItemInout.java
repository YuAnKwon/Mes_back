package com.mes_back.entity;

import com.mes_back.constant.Yn;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "order_item_inout")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemInout extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_inout_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @Column(nullable = false)
    private Integer inAmount;

    @Column(nullable = false)
    private String lotNum;

    @Column(nullable = false)
    private Date inDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn inDelYn = Yn.N;

    private Integer outAmount;

    private String outNum;

    private Date outDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn isProcessCompleted;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn outDelYn = Yn.N;

}
