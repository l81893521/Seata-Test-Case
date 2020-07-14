package com.seata.test.service.impl;

import com.seata.test.service.AccountService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
  * @author will.zjw
  * @date 2019-09-26 17:16
  */
@Slf4j
public class OracleAccountServiceImpl implements AccountService {

    private JdbcTemplate oracleAccountJdbcTemplate;

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "gts-account-for-update")
    public void forUpdate(int id) {
        oracleAccountJdbcTemplate.queryForList("select * from account_tbl where id = ? for update", id);
        oracleAccountJdbcTemplate.queryForList("select * from \"ACCOUNT_TBL\" where id = ? for update", id);
        oracleAccountJdbcTemplate.queryForList("select * from test.account_tbl where id = ? for update", id);
        oracleAccountJdbcTemplate.queryForList("select * from test.\"ACCOUNT_TBL\" where id = ? for update", id);
        throw new RuntimeException("查询锁失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "gts-account-for-update-with-in")
    public void forUpdateWithIn(int id) {
        oracleAccountJdbcTemplate.queryForList("select * from account_tbl where id in (?) for update", id);
        throw new RuntimeException("查询锁失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "gts-account-for-update-with-between")
    public void forUpdateWithBetween(int id) {
        oracleAccountJdbcTemplate.queryForList("select * from account_tbl where id between ? and ? for update", id, id);
        throw new RuntimeException("查询锁失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 30000, name = "gts-debit")
    public void debit(String userId, int money) {
        oracleAccountJdbcTemplate.update("update account_tbl set money = money - ? where user_id = ?", new Object[] {money, userId});
        oracleAccountJdbcTemplate.update("update \"ACCOUNT_TBL\" set money = money - ? where user_id = ?", new Object[] {money, userId});
        oracleAccountJdbcTemplate.update("update test.account_tbl set money = money - ? where user_id = ?", new Object[] {money, userId});
        oracleAccountJdbcTemplate.update("update test.\"ACCOUNT_TBL\" set money = money - ? where user_id = ?", new Object[] {money, userId});
        throw new RuntimeException("扣除余额失败");
    }

    @Override
    public void batchDebit(String[] userIds, int money) {

    }

    @Override
    @GlobalTransactional(timeoutMills = 30000, name = "gts-debit-with-in")
    public void debitWithIn(String userId, int money) {
        oracleAccountJdbcTemplate.update("update account_tbl set money = money - ? where user_id in (?)", new Object[] {money, userId});
        throw new RuntimeException("扣除余额失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "gts-debit-with-between")
    public void debitWithBetween(String userId, int money) {
        oracleAccountJdbcTemplate.update("update account_tbl set money = money - ? where user_id between ? and ?", new Object[] {money, userId, userId});
        throw new RuntimeException("扣除余额失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "gts-debit-with-exist")
    public void debitWithExist(String userId, int money) {
        oracleAccountJdbcTemplate.update("update account_tbl a set money = money - ? "
            + "where exists (select 1 from order_tbl o where a.user_id = o.user_id and o.user_id = ?)", money, userId);
        throw new RuntimeException("扣除余额失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "gts-debit-with-not-exist")
    public void debitWithNotExist(String userId, int money) {
        oracleAccountJdbcTemplate.update("update account_tbl a set money = money - ? "
            + "where not exists (select 1 from order_tbl o where a.user_id = o.user_id and o.user_id = ?)", money, userId);
        throw new RuntimeException("扣除余额失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "gts-create-account")
    public void createAccount(String userId, int money) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        oracleAccountJdbcTemplate.update(con ->  {
            PreparedStatement preparedStatement = con.prepareStatement("insert into account_tbl(id, user_id, money, information, description) values (account_tbl_seq.nextval, ?, ?, ?, ?)");
            int i = 1;
            preparedStatement.setString(i++, userId);
            preparedStatement.setInt(i++, money);
            preparedStatement.setString(i++, "a");
            preparedStatement.setString(i++, "a");
            return preparedStatement;
        }, keyHolder);
        log.info("key holder size: {}", keyHolder.getKeyList().size());

        oracleAccountJdbcTemplate.update("insert into account_tbl(id, user_id, money, information, description) values (1, ?, ?, ?, ?)", userId, money, "a", "a");
        oracleAccountJdbcTemplate.update("insert into account_tbl(id, user_id, money, information, description) values (?, ?, ?, ?, ?)", 2, userId, money, "a", "a");
        oracleAccountJdbcTemplate.update("insert into account_tbl(id, user_id, money, information, description) values (account_tbl_seq.nextval, ?, ?, ?, ?)", userId, money, "a", "a");
        oracleAccountJdbcTemplate.update("insert into \"ACCOUNT_TBL\"(id, user_id, money, information, description) values (account_tbl_seq.nextval, ?, ?, ?, ?)", userId, money, "a", "a");
        oracleAccountJdbcTemplate.update("insert into test.account_tbl(id, user_id, money, information, description) values (account_tbl_seq.nextval, ?, ?, ?, ?)", userId, money, "a", "a");
        oracleAccountJdbcTemplate.update("insert into test.\"ACCOUNT_TBL\"(id, user_id, money, information, description) values (account_tbl_seq.nextval, ?, ?, ?, ?)", userId, money, "a", "a");
        oracleAccountJdbcTemplate.update("insert into \"test\".account_tbl(id, user_id, money, information, description) values (account_tbl_seq.nextval, ?, ?, ?, ?)", userId, money, "a", "a");
        oracleAccountJdbcTemplate.update("insert into \"test\".\"ACCOUNT_TBL\"(id, user_id, money, information, description) values (account_tbl_seq.nextval, ?, ?, ?, ?)", userId, money, "a", "a");
        throw new RuntimeException("新增账户失败");
    }

    @Override
    public void batchCreateAccount(String[] userIds, int money) {

    }

    @Override
    public void createAccountWithPk(int id, String userId, int money) {

    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "gts-delete-account")
    public void deleteAccount(String userId) {
        oracleAccountJdbcTemplate.update("delete from account_tbl where user_id = ?", userId);
        oracleAccountJdbcTemplate.update("delete from \"ACCOUNT_TBL\" where user_id = ?", userId);
        oracleAccountJdbcTemplate.update("delete from test.account_tbl where user_id = ?", userId);
        oracleAccountJdbcTemplate.update("delete from test.\"ACCOUNT_TBL\" where user_id = ?", userId);
        throw new RuntimeException("账户删除失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "gts-delete-account-with-in")
    public void deleteAccountWithIn(String userId) {
        oracleAccountJdbcTemplate.update("delete from account_tbl where user_id in (?)", userId);
        throw new RuntimeException("账户删除失败");
    }

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "gts-delete-account-with-between")
    public void deleteAccountWithBetween(int id) {
        oracleAccountJdbcTemplate.update("delete from account_tbl where id between ? and ?", id, id);
        throw new RuntimeException("账户删除失败");
    }

    @Override
    public void updateAccountInformation(String userId, String information) {

    }

    public void setOracleAccountJdbcTemplate(JdbcTemplate oracleAccountJdbcTemplate) {
        this.oracleAccountJdbcTemplate = oracleAccountJdbcTemplate;
    }
}
