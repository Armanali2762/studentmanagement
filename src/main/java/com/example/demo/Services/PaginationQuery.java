package com.example.demo.Services;

public class PaginationQuery {

    public static final String MAIN_QUERY = "SELECT\n" +
            "\tcheckbox,\n" +
            "\taction,\n" +
            "\tdelete_column,\n" +
            "\tquery_list.default_order_by_column,\n" +
            "\tcount_column,\n" +
            "\tquery_list.`query`,\n" +
            "\tGROUP_CONCAT(\n" +
            "\t\tCONCAT(\n" +
            "\t\t\tquery_list_table_field.table_field,\n" +
            "\t\t\t' AS `',\n" +
            "\t\t\tquery_list_table_field.display_name,\n" +
            "\t\t\t'`' \n" +
            "\t\t) \n" +
            "\tORDER BY\n" +
            "\t\tquery_list_table_field.`order` \n" +
            "\t) AS COLUMNS \n" +
            "FROM\n" +
            "\tquery_list\n" +
            "\tINNER JOIN query_list_table_field ON query_list_table_field.query_list_pk = query_list.pk \n" +
            "\tAND query_list.pk = :queryId\n" +
            "GROUP BY\n" +
            "\tquery_list.pk";

    public static final String ORDER_BY_STRING_QUERY = "SELECT\n" +
            "IF\n" +
            "\t(\n" +
            "\t\tquery_list_table_field.is_alias_filter = 1,\n" +
            "\t\tCONCAT( '(', query_list_table_field.table_field, ')' ),\n" +
            "\tIF\n" +
            "\t\t( query_list_table_field.is_formated_column = 0, query_list_table_field.table_field, query_list_table_field.non_formated_search_column ) \n"
            +
            "\t) AS DATA \n" +
            "FROM\n" +
            "\tquery_list_table_field \n" +
            "\tWHERE query_list_table_field.query_list_pk = :id \n" +
            "ORDER BY\n" +
            "\tquery_list_table_field.`order`";

    public final static String INDIVIDUAL_SEARCH_QUERY = "SELECT\n" +
            "IF\n" +
            "\t(\n" +
            "\t\tquery_list_table_field.is_alias_filter = 1,\n" +
            "\t\t\n" +
            "\t\tconcat( query_list_table_field.ORDER, '#', '(', query_list_table_field.table_field, ')' ),\n" +
            "\t\t\n" +
            "\t\tconcat(\n" +
            "\t\t\tquery_list_table_field.ORDER,\n" +
            "\t\t\t'#',\n" +
            "\t\tIF\n" +
            "\t\t( query_list_table_field.is_formated_column = 0, query_list_table_field.table_field, query_list_table_field.non_formated_search_column ))) AS DATA \n"
            +
            "FROM\n" +
            "\tquery_list_table_field \n" +
            "WHERE\n" +
            "\tquery_list_table_field.query_list_pk = :id \n" +
            "ORDER BY\n" +
            "\tquery_list_table_field.`order`;";

    public static final String GLOBAL_SEARCH_QUERY = "SELECT\n" +
            "IF\n" +
            "\t(\n" +
            "\t\tquery_list_table_field.is_alias_filter = 1,\n" +
            "\t\tCONCAT( '(', query_list_table_field.table_field, ')' ),\n" +
            "\tIF\n" +
            "\t\t( query_list_table_field.is_formated_column = 0, query_list_table_field.table_field, query_list_table_field.non_formated_search_column ) \n"
            +
            "\t) AS DATA \n" +
            "FROM\n" +
            "\tquery_list_table_field \n" +
            "WHERE\n" +
            "\tquery_list_table_field.query_list_pk = :id \n" +
            "\tAND query_list_table_field.is_searchable = 1 \n" +
            "ORDER BY\n" +
            "\tquery_list_table_field.`order`";

    public static final String COLUMNS_QUERY = "SELECT\n" +
            "\tquery_list_table_field.display_name AS `name`,\n" +
            "\tquery_list_table_field.display_name AS `data` \n" +
            "FROM\n" +
            "\tquery_list_table_field \n" +
            "WHERE\n" +
            "\tquery_list_table_field.query_list_pk = :id \n" +
            "ORDER BY\n" +
            "\tquery_list_table_field.`order`;";

    public static final String COLUMN_DEFS_QUERY = "SELECT\n" +
            "\tquery_list_table_field.`order` AS targets,\n" +
            "\tquery_list_table_field.width AS width,\n" +
            "\tquery_list_table_field.is_orderable AS orderable,\n" +
            "\tquery_list_table_field.is_searchable AS searchable,\n" +
            "\tquery_list_table_field.is_visible AS visible,\n" +
            "CASE\n" +
            "\t\t\n" +
            "\t\tWHEN query_list_table_field.right_align = 1 THEN\n" +
            "\t\t'text-right' \n" +
            "\t\tWHEN query_list_table_field.right_align = 2 THEN\n" +
            "\t\t'text-center' ELSE 'text-left' \n" +
            "\tEND AS className \n" +
            "FROM\n" +
            "\tquery_list_table_field \n" +
            "WHERE\n" +
            "\tquery_list_table_field.query_list_pk = :id \n" +
            "ORDER BY\n" +
            "\tquery_list_table_field.`order`;";

    public static final String COLUMN_DEFS_QUERY_CHECKBOX = "SELECT\n" +
            "\tquery_list_table_field.`order` + 1 AS targets,\n" +
            "\tquery_list_table_field.width AS width,\n" +
            "\tquery_list_table_field.is_orderable AS orderable,\n" +
            "\tquery_list_table_field.is_searchable AS searchable,\n" +
            "\tquery_list_table_field.is_visible AS visible,\n" +
            "CASE\n" +
            "\t\t\n" +
            "\t\tWHEN query_list_table_field.right_align = 1 THEN\n" +
            "\t\t'text-right' \n" +
            "\t\tWHEN query_list_table_field.right_align = 2 THEN\n" +
            "\t\t'text-center' ELSE 'text-left' \n" +
            "\tEND AS className \n" +
            "FROM\n" +
            "\tquery_list_table_field \n" +
            "WHERE\n" +
            "\tquery_list_table_field.query_list_pk = 2 \n" +
            "ORDER BY\n" +
            "\tquery_list_table_field.`order`;";

    public static final String SET_GROUP_CONCAT_LENGTH = "SET Session group_concat_max_len = 4294967295";
}
