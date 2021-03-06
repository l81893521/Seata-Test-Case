package com.seata.test.service.impl;

import com.seata.test.service.AccountService;
import com.seata.test.service.BusinessService;
import com.seata.test.service.OrderService;
import com.seata.test.service.StorageService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
  * @author will.zjw
  * @date 2019-04-19 16:26
  */
public class BusinessServiceImpl implements BusinessService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessService.class);

    private StorageService storageService;

    private OrderService orderService;

    private AccountService accountService;

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
    public void purchase(String userId, String commodityCode, int orderCount) {
        logger.info("purchase begin ... xid: " + RootContext.getXID());

        storageService.deduct(commodityCode, orderCount);
        orderService.create(userId, commodityCode, orderCount);
        //制造异常
        throw new RuntimeException("xxx");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "debit-account-tx")
    public void debit(String userId, int money){
        accountService.debit(userId, money, true);
        throw new RuntimeException("扣除余额失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "debit-oracle-account-tx")
    public void debitForOracle(String userId, int money) {
        try {
            accountService.debit(userId, money, true);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        throw new RuntimeException("扣除余额失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "debit-d-account-tx")
    public void debitByDiffentDataSource(String userId, int money) {
        accountService.debit(userId, money, true);
        throw new RuntimeException("扣除余额失败");
    }

    /**
     * 创建账户
     *
     * @param userId
     * @param money
     */
    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-create-account")
    public void createAccount(String userId, int money) {
        accountService.createAccount(userId, money, true);
        throw new RuntimeException("创建账户失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-create-account-oracle")
    public void createAccountForOracle(String userId, int money) {
        accountService.createAccount(userId, money, true);
        throw new RuntimeException("创建账户失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "delete-account-tx")
    public void deleteAccount(String userId) {
        accountService.deleteAccount(userId, true);
        throw new RuntimeException("删除账户失败");
    }

    /**
     * 修改账户信息
     * @param userId
     * @param information
     */
    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "update-account-information-tx")
    public void updateAccountInformation(String userId, String information) {
        accountService.updateAccountInformation(userId, information);
        throw new RuntimeException("修改账户信息失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "add-storage-oracle-tx")
    public void addStorage(String commodityCode, int count) {
        storageService.add(commodityCode, count);
        throw new RuntimeException("增加库存失败");
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
