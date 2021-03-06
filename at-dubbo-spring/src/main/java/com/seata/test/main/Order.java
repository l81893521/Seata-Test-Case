package com.seata.test.main;

import com.seata.test.ApplicationKeeper;
import com.seata.test.service.OrderService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
  * @author will.zjw
  * @date 2019-04-24 14:29
  */
public class Order {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext orderContext = new ClassPathXmlApplicationContext(new String[]{"spring/dubbo-order-service.xml"});

        OrderService orderService = (OrderService)orderContext.getBean("orderService");
        //orderService.insertOrder("U0000010", "C00321", 2, 400);
        //orderService.testSeataForSuccess();
        orderService.testSeataForFail();
        new ApplicationKeeper(orderContext).keep();
    }
}
