package com.seata.test.service.impl;


import com.seata.test.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
  * @author will.zjw
  * @date 2019-04-19 16:25
  *            佛主保佑,不要出BUG
  *                 _ooOoo_
  *                o8888888o
  *                88" . "88
  *                (| -_- |)
  *                O\  =  /O
  *             ____/`---'\____
  *           .'  \\|     |//  `.
  *          /  \\|||  :  |||//  \
  *         /  _||||| -:- |||||-  \
  *         |   | \\\  -  /// |   |
  *         | \_|  ''\---/''  |   |
  *          \  .-\__  `-`  ___/-. /
  *        ___`. .'  /--.--\  `. . __
  *      ."" '<  `.___\_<|>_/___.'  >'"".
  *    | | :  `- \`.;`\ _ /`;.`/ - ` : | |
  *    \  \ `-.   \_ __\ /__ _/   .-` /  /
  * ======`-.____`-.___\_____/___.-`____.-'======
  *                 `=---='
  */
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private JdbcTemplate jdbcTemplate;

    private JdbcTemplate platformJdbcTemplate;

    private JdbcTemplate oracleAccountJdbcTemplate;


    @Override
    public void debit(String userId, int money) {
        jdbcTemplate.update("update seata_account_tbl set money = money - ? where user_id = ?", new Object[] {money, userId});
    }

    @Override
    public void debitForOracle(String userId, int money) {
        oracleAccountJdbcTemplate.update("update account_tbl set money = money - ?, information = 'bbbb', description = 'ccccc' where user_id = ?", new Object[] {money, userId});
    }

    @Override
    public void platformDebit(String userId, int money) {
        platformJdbcTemplate.update("update seata_account_tbl set money = money - ? where user_id = ?", new Object[] {money, userId});
    }

    /**
     * 创建账户
     *
     * @param userId
     * @param money
     */
    @Override
    public void createAcount(String userId, int money) {
        jdbcTemplate.update("insert into seata_account_tbl(user_id, money, information) values (?, ?, ?)", userId, money, "hello world".getBytes());
    }

    @Override
    public void createAccountForOracle(String userId, int money) {
        oracleAccountJdbcTemplate.update("insert into account_tbl(id, user_id, money, information, description) values (account_tbl_seq.nextval, ?, ?, ?, ?)", userId, money, "a", "a");
    }

    @Override
    public void createAccount(int id, String userId, int money) {
        jdbcTemplate.update("insert into seata_account_tbl(id, user_id, money) values (?, ?, ?)", id, userId, money);
    }

    @Override
    public void deleteAccount(String userId) {
        jdbcTemplate.update("delete from seata_account_tbl where user_id = ?", userId);
    }

    @Override
    public void updateAccountInformation(String userId, String information) {
        jdbcTemplate.update("update seata_account_tbl set information = ? where user_id = ?", new Object[]{information.getBytes(), userId});
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setPlatformJdbcTemplate(JdbcTemplate platformJdbcTemplate) {
        this.platformJdbcTemplate = platformJdbcTemplate;
    }

    public void setOracleAccountJdbcTemplate(JdbcTemplate oracleAccountJdbcTemplate) {
        this.oracleAccountJdbcTemplate = oracleAccountJdbcTemplate;
    }
}
