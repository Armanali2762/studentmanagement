package com.example.demo.Services;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Setter
@Getter
@Slf4j
@ToString
public class QueryMapper implements RowMapper {
    private String query;
    private String columns;
    private String count_column;
    private byte action;
    private String delete_column, default_order_by_column;
    private byte checkbox;

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        QueryMapper queryMapper = new QueryMapper();
        try {

            queryMapper.setQuery(rs.getString("qury"));

        } catch (Exception e) {
            log.info("======================", e);
        }
        try {
            queryMapper.setColumns(rs.getString("cols"));

        } catch (Exception e) {
            log.info("======================", e);
        }
        try {
            queryMapper.setCount_column(rs.getString("count_column"));

        } catch (Exception e) {
            log.info("======================", e);
        }
        try {
            queryMapper.setAction(rs.getByte("action"));

        } catch (Exception e) {
            log.info("======================", e);
        }
        try {
            queryMapper.setDelete_column(rs.getString("delete_column"));

        } catch (Exception e) {
            log.info("======================", e);
        }
        try {
            queryMapper.setCheckbox(rs.getByte("checkbox"));

        } catch (Exception e) {
            log.info("======================", e);
        }
        return queryMapper;
    }
}
