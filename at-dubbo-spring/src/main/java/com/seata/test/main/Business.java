package com.seata.test.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.seata.test.service.AccountService;
import com.seata.test.service.BusinessService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
  * @author will.zjw
  * @date 2019-04-24 14:30
  */
public class Business {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"spring/dubbo-business.xml"});

        final BusinessService businessService = (BusinessService) context.getBean("businessService");

        //create
        //businessService.createAccount("U100004", 999);
        //businessService.createAccountForOracle("U100004", 999);

        //businessService.purchase("U100001", "C00321", 2);
        //businessService.deleteAccount("U100009");

        //businessService.debit("U100009", 0);
        //businessService.debitForOracle("U100001", 50);
        //businessService.debitByDiffentDataSource("U100009", 100);
        //businessService.updateAccountInformation("U100009", "hello world");
        //businessService.addStorage("C00321", 1);
    }
}
