package com.touristagency.service;

import com.touristagency.entity.Discount;
import com.touristagency.entity.Order;
import com.touristagency.entity.Tour;
import com.touristagency.entity.User;
import com.touristagency.entity.enums.OrderStatus;
import com.touristagency.repository.DiscountRepository;
import com.touristagency.repository.OrderRepository;
import com.touristagency.repository.TourRepository;
import com.touristagency.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Log4j2
public class OrderService {
    private final UserRepository userRepo;
    private final TourRepository tourRepo;
    private final OrderRepository orderRepo;
    private final DiscountRepository discountRepo;

    public OrderService(UserRepository userRepo,
                        TourRepository tourRepo,
                        OrderRepository orderRepo, DiscountRepository discountRepo) {
        this.userRepo = userRepo;
        this.tourRepo = tourRepo;
        this.orderRepo = orderRepo;
        this.discountRepo = discountRepo;
    }

    public Page<Order> findAllOrdersPageable(Pageable pageable) {
        return orderRepo.findAll(pageable);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void orderTour(Long userId, Long tourId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + userId));
        Tour tour = tourRepo.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tour id: " + tourId));
        Order order = Order.builder()
                .user(user)
                .tour(tour)
                .status(OrderStatus.PENDING)
                .price(tour.getPrice())
                .discount(user.getCurrentDiscount())
                .fixedPrice(Order.fixPrice(tour.getPrice(), user.getCurrentDiscount()))
                .orderDate(LocalDateTime.now())
                .build();
        orderRepo.save(order);
        log.info("New order " + order);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void markOrderAsPaid(Long orderId) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.PAID);
        User user = order.getUser();
        double newDiscount = calcDiscount(user.getCurrentDiscount(), getDiscount());
        user.setCurrentDiscount(newDiscount);
        user.getOrders().stream()
                .filter((x) -> x.getStatus() == OrderStatus.PENDING)
                .forEach((x) -> {
                    x.setDiscount(newDiscount);
                    x.setFixedPrice(Order.fixPrice(x.getPrice(), newDiscount));
                });
        orderRepo.save(order);
        log.info("Order paid " + order);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void markOrderAsDenied(Long orderId) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.DENIED);
        orderRepo.save(order);
        log.info("Order denied " + order);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.getUser().getOrders().remove(order);
        order.getTour().getOrders().remove(order);
        orderRepo.deleteById(orderId);
        log.info("Order deleted " + order);
    }

    public void setDiscount(double step, double threshold) {
        Discount discount = Discount.builder()
                .id(1L)
                .step(step)
                .threshold(threshold)
                .build();
        discountRepo.save(discount);
        log.info("Discount set " + discount);
    }

    public Discount getDiscount() {
        return discountRepo.findById(1L).orElseThrow(() ->
                new IllegalArgumentException("Discount not initialised"));
    }

    public Order getOrderById(Long id) {
        return orderRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid order id: " + id));
    }

    private static double calcDiscount(double currDiscount, Discount discount) {
        return Math.min(currDiscount + discount.getStep(), discount.getThreshold());
    }
}
