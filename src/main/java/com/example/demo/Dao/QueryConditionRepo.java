package com.example.demo.Dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.Entities.QueryCondition;

public interface QueryConditionRepo extends JpaRepository<QueryCondition, Long> {

    QueryCondition findAllByQueryListPk(Long id);

    QueryCondition findAllByQueryListPkAndActive(Long id, boolean active);

    @Query(value = "SELECT\n"
            + "	query_list_table_field.pk,\n"
            + "	query_list_table_field.table_field,\n"
            + "	query_list_table_field.display_name \n"
            + "FROM\n"
            + "	query_list\n"
            + "	INNER JOIN query_list_table_field ON query_list_table_field.query_list_pk = query_list.pk\n"
            + "	INNER JOIN top_header ON top_header.pk = query_list_table_field.top_header_pk \n"
            + "WHERE\n"
            + "query_list.pk=?1 ORDER BY\n"
            + "	query_list_table_field.`order`", nativeQuery = true)
    List<Map<String, Object>> getTableHeader(String id);

    @Query(value = "SELECT\n"
            + "	top_header.pk,\n"
            + "	top_header.table_field, \n"
            + " top_header.colspan "
            + "FROM\n"
            + "	query_list\n"
            + "	INNER JOIN query_list_table_field ON query_list_table_field.query_list_pk = query_list.pk\n"
            + "	INNER JOIN top_header ON top_header.pk = query_list_table_field.top_header_pk \n"
            + "WHERE\n"
            + "	query_list.pk = ?1 \n"
            + "	GROUP BY\n"
            + "	top_header.pk", nativeQuery = true)
    List<Map<String, Object>> getTableHeaderTop(String id);

    @Query(value = "SELECT\n" +
            "\tquery_list_table_field.display_name,\n" +
            "\tquery_list_table_field.rowspan,\n" +
            "\tquery_list_table_field.start_top_header,\n" +
            "\ttop_header.table_field,\n" +
            "\ttop_header.colspan \n" +
            "FROM\n" +
            "\tquery_list\n" +
            "\tINNER JOIN query_list_table_field ON query_list_table_field.query_list_pk = query_list.pk\n" +
            "\tLEFT JOIN top_header ON top_header.pk = query_list_table_field.start_top_header \n" +
            "WHERE\n" +
            "\tquery_list.pk = ?1 \n" +
            "GROUP BY\n" +
            "\tquery_list_table_field.pk \n" +
            "ORDER BY\n" +
            "\tquery_list_table_field.`order`", nativeQuery = true)
    List<Map<String, Object>> getTableHeaderTitleList(String id);
}
