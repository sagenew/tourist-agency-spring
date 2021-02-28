package com.touristagency.entity;

import com.touristagency.entity.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @SequenceGenerator(name = "orderIdSeq", sequenceName = "orders_id_seq", allocationSize = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderIdSeq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tour tour;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column(name = "price")
    private double price;

    @Column(name = "discount")
    private double discount;

    @Column(name = "fixed_price")
    private double fixedPrice;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    public boolean isPending() {
        return status == OrderStatus.PENDING;
    }
    public boolean isPaid() {
        return status == OrderStatus.PAID;
    }
    public boolean isDenied() {
        return status == OrderStatus.DENIED;
    }

    public static double fixPrice(double price, double discount) {
        return  price * (1 - (discount * 0.01));
    }
}
