package com.seata.test.service.impl;

import com.seata.test.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
  * @author will.zjw
  * @date 2019-04-19 16:27
  */
public class StorageServiceImpl implements StorageService {

    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

    private JdbcTemplate jdbcTemplate;

    private JdbcTemplate oracleJdbcTemplate;

    @Override
    public void deduct(String commodityCode, int count) {
        jdbcTemplate.update("update seata_storage_tbl set count = count - ? where commodity_code = ?",
                new Object[] {count, commodityCode});
    }

    @Override
    public void add(String commodityCode, int count) {
        oracleJdbcTemplate.update("update storage_tbl set count = count + ? where commodity_code = ?",
                new Object[] {count, commodityCode});
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setOracleJdbcTemplate(JdbcTemplate oracleJdbcTemplate) {
        this.oracleJdbcTemplate = oracleJdbcTemplate;
    }
}
