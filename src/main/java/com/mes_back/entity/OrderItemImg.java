package com.mes_back.entity;

import com.mes_back.constant.Yn;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_item_img")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemImg extends BaseTimeEntity {

    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderItem orderItem;

    @Column(nullable = false)
    private String imgOriName;

    @Column(nullable = false)
    private String imgFileName;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn repYn = Yn.Y;

}
