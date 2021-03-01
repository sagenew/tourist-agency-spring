package com.touristagency.service;

import com.touristagency.entity.Discount;
import com.touristagency.entity.Order;
import com.touristagency.entity.Tour;
import com.touristagency.entity.User;
import com.touristagency.entity.enums.Authority;
import com.touristagency.entity.enums.HotelType;
import com.touristagency.entity.enums.OrderStatus;
import com.touristagency.entity.enums.TourType;
import com.touristagency.repository.DiscountRepository;
import com.touristagency.repository.OrderRepository;
import com.touristagency.repository.TourRepository;
import com.touristagency.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @MockBean
    private OrderRepository orderRepo;
    @MockBean
    private UserRepository userRepo;
    @MockBean
    private TourRepository tourRepo;
    @MockBean
    private DiscountRepository discountRepo;

    @Test
    public void orderTour(){
        User user = initTestUser();
        Tour tour = initTestTour();

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(tourRepo.findById(tour.getId())).thenReturn(Optional.of(tour));

        orderService.orderTour(user.getId(), tour.getId());

        verify(orderRepo, times(1)).save(any(Order.class));
    }

    @Test
    public void markOrderAsPaid() {
        User user = initTestUser();
        Tour tour = initTestTour();
        Discount discount = initTestDiscount();

        Order order = Order.builder()
                .id(1L)
                .user(user)
                .tour(tour)
                .status(OrderStatus.PENDING)
                .price(tour.getPrice())
                .discount(user.getCurrentDiscount())
                .build();

        List<Order> orderList = new ArrayList<>();
        orderList.add(order);

        user.setOrders(orderList);
        tour.setOrders(orderList);

        when(orderRepo.findById(order.getId())).thenReturn(Optional.of(order));
        when(discountRepo.findById(1L)).thenReturn(Optional.of(discount));

        orderService.markOrderAsPaid(order.getId());

        verify(orderRepo, times(1)).save(order);
    }

    @Test
    public void markOrderAsDenied() {
        User user = initTestUser();
        Tour tour = initTestTour();

        Order order = Order.builder()
                .id(1L)
                .user(user)
                .tour(tour)
                .status(OrderStatus.PENDING)
                .price(tour.getPrice())
                .discount(user.getCurrentDiscount())
                .build();

        when(orderRepo.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.markOrderAsDenied(order.getId());

        verify(orderRepo, times(1)).save(order);
    }

    @Test
    public void deleteOrder() {
        User user = initTestUser();
        Tour tour = initTestTour();

        Order order = Order.builder()
                .id(1L)
                .user(user)
                .tour(tour)
                .status(OrderStatus.PENDING)
                .price(tour.getPrice())
                .discount(user.getCurrentDiscount())
                .build();

        List<Order> orderList = new ArrayList<>();
        orderList.add(order);

        user.setOrders(orderList);
        tour.setOrders(orderList);

        when(orderRepo.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.deleteOrder(order.getId());

        verify(orderRepo, times(1)).deleteById(order.getId());
    }

    @Test
    public void getDiscount() {
        Discount discount = initTestDiscount();
        when(discountRepo.findById(1L)).thenReturn(Optional.of(discount));
        orderService.getDiscount();
        verify(discountRepo, times(1)).findById(1L);
    }

    @Test
    public void setDiscount() {
        Discount discount = initTestDiscount();
        orderService.setDiscount(discount.getStep(), discount.getThreshold());
        verify(discountRepo, times(1)).save(discount);
    }

    private static User initTestUser() {
        return User.builder()
                .id(1L)
                .username("user")
                .fullName("Userino Userious")
                .email("user@gmail.com")
                .bio("21 y.o. designer from San Francisco")
                .currentDiscount(5.0)
                .authorities(Collections.singleton(Authority.USER))
                .build();
    }

    private static Tour initTestTour() {
        return Tour.builder()
                .id(1L)
                .name("Test tour")
                .type(TourType.EXCURSION)
                .price(200L)
                .groupSize(10)
                .hotel(HotelType.APARTMENTS)
                .description("Test tour description")
                .isHot(false)
                .build();
    }

    private static Discount initTestDiscount() {
        return Discount.builder()
                .id(1L)
                .step(1.5)
                .threshold(20.0).build();
    }
}
