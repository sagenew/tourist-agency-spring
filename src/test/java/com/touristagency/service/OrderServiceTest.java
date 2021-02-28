//package com.touristagency.service;
//
//import com.touristagency.entity.User;
//import com.touristagency.repository.OrderRepository;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.HashMap;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class OrderServiceTest {
//    @Autowired
//    private OrderService orderService;
//    @MockBean
//    private OrderRepository orderRepo;
//
//    @Test
//    public void createCheck(){
//        User user = new User();
//        user.setUsername("Ivan");
//
//        TemporaryCheck check = checkService.createCheck(user);
//        TemporaryCheck correctCheck = TemporaryCheck
//                .builder()
//                .userName("Ivan")
//                .productAmount(new HashMap<>())
//                .time(check.getTime())
//                .total(new BigDecimal(0.0)
//                        .setScale(2, RoundingMode.HALF_UP))
//                .build();
//
//        Assert.assertEquals(correctCheck,check);
//        Mockito.verify(temporaryCheckRepository, Mockito.times(1)).save(check);
//    }
//}
