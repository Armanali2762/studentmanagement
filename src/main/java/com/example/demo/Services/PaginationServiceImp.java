package com.example.demo.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.Dao.QueryConditionRepo;
import com.example.demo.Dao.QueryListRepository;
import com.example.demo.Entities.QueryList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaginationServiceImp implements PaginationService {

    private final QueryConditionRepo queryConditionOldRepository;
    private final JdbcTemplate jdbcTemplate;
    private final QueryListRepository queryListRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static String deleteCoulmn, defaultOrderByCol;

    @Transactional
    @Override
    public Map<String, Object> getData(Long id, HttpServletRequest request) {
        Integer rowCount = 0;
        String searchableString = "";

        Map<String, Object> returnMap = new HashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {

            entityManager.createNativeQuery(PaginationQuery.SET_GROUP_CONCAT_LENGTH).executeUpdate();

            String SQL = PaginationQuery.MAIN_QUERY;
            SQL = SQL.replaceAll(":queryId", String.valueOf(id));

            int localOffSet = Integer.parseInt(request.getParameter("start"));
            int limit = Integer.parseInt(request.getParameter("length"));

            // get Searchable String (Global or Individual)
            if (Util.isNullOrEmpty(request.getParameter("search[value]"))) {
                log.info("New Individual Search");
                searchableString = getIndividualSearch(id, request);
            } else {
                log.info("New GLOBAL Search");
                searchableString = getGlobalSearch(id,
                        request.getParameter("search[value]").trim(), request);
            }

            System.out.println("SQL {} " + SQL);

            List<Object[]> queryList = entityManager.createNativeQuery(SQL).getResultList();
            log.info("queryList {}", queryList);

            QueryMapper query = getQueryMapper(queryList);
            log.info("query {}", query);

            String dataQuery = query.getQuery().toLowerCase();
            deleteCoulmn = query.getDelete_column();
            defaultOrderByCol = query.getDefault_order_by_column();

            Map mapQuery = QueryConditionBuilder.generateQueryConditionV2(query, dataQuery, searchableString,
                    getOrderByString(id, request),
                    request, id, queryConditionOldRepository, limit, localOffSet);

            rowCount = jdbcTemplate.queryForObject((String) mapQuery.get("countQuery"),
                    Integer.class);

            dataList = jdbcTemplate.queryForList((String) mapQuery.get("mainQuery"));

            returnMap.put("status", true);
            returnMap.put("data", dataList);
            returnMap.put("recordsTotal", rowCount);
            returnMap.put("recordsFiltered", rowCount);

        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("status", false);
            returnMap.put("data", dataList);
            returnMap.put("recordsTotal", 0);
            returnMap.put("recordsFiltered", 0);
        }
        return returnMap;
    }

    private String getOrderByString(Long id, HttpServletRequest request) {
        List<String> returnData = null;
        StringBuilder orderByString = new StringBuilder();
        try {

            String query = PaginationQuery.ORDER_BY_STRING_QUERY.replaceAll(":id", String.valueOf(id));

            returnData = entityManager.createNativeQuery(query).getResultList();

            if (request.getParameter("order[0][column]") == null
                    || request.getParameter("order[0][column]").trim().equals("")) {
                log.info("ONNNNENEENNEN");
            } else {
                log.info("OUTSIDE ONNNNNNNNNNNNN");
                if (request.getParameter("columns[0][data]") != null
                        && request.getParameter("columns[0][data]").equalsIgnoreCase("#")) {

                    if (returnData.get(Integer.parseInt(request.getParameter("order[0][column]")) - 1) != null) {
                        log.info("TWOOOOOOOO");
                        orderByString.append(" Order By ".toUpperCase()
                                + returnData.get(Integer.parseInt(request.getParameter("order[0][column]")) - 1) + " "
                                + request.getParameter("order[0][dir]").toUpperCase());

                    }
                } else {

                    if (returnData.get(Integer.parseInt(request.getParameter("order[0][column]"))) != null) {

                        log.info("returnData.get(Integer.parseInt(request.getParameter(\"order[0][column]\")))   "
                                + returnData.get(Integer.parseInt(request.getParameter("order[0][column]"))));
                        String bvn = returnData.get(Integer.parseInt(request.getParameter("order[0][column]")));
                        if (returnData.get(Integer.parseInt(request.getParameter("order[0][column]"))).trim() == null
                                || returnData.get(Integer.parseInt(request.getParameter("order[0][column]"))).trim()
                                        .equals("")) {
                            orderByString.append(" Order By ".toUpperCase() + bvn + " " + "DESC");
                        } else {
                            orderByString.append(" Order By ".toUpperCase()
                                    + returnData.get(Integer.parseInt(request.getParameter("order[0][column]"))) + " "
                                    + request.getParameter("order[0][dir]").toUpperCase());
                        }

                    }
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        if (orderByString.length() == 0) {
            log.info("Builder is Empty    ");
            log.info("Default Order By Coulmn" + defaultOrderByCol);
            if (!Util.isNullOrEmpty(defaultOrderByCol)) {
                orderByString.append("Order By ").append(defaultOrderByCol);
            }
        }

        log.info("ORDER_BY===>>>> " + orderByString);

        return orderByString.toString();
    }

    @Override
    public Map<String, Object> getHeader(Long id, HttpServletRequest request) {

        Map<String, Object> rMap = new HashMap<>();

        List<Map<String, Object>> columns = null;
        List<Map<String, Object>> columnDefs = null;
        Map<String, Object> actionHeaderMap = new HashMap<>();
        try {
            QueryList actionStatus = queryListRepository.findById(id).get();

            if (actionStatus.getCheckbox() == null || !actionStatus.getCheckbox()) {

                columns = jdbcTemplate
                        .queryForList(PaginationQuery.COLUMNS_QUERY.replaceAll(":id", String.valueOf(id)));
                columnDefs = jdbcTemplate
                        .queryForList(PaginationQuery.COLUMN_DEFS_QUERY.replaceAll(":id", String.valueOf(id)));

                log.info("columns: " + columns);
                log.info("columnDefs: " + columnDefs);

                if (actionStatus.getAction() == 1) {

                    actionHeaderMap.put("aTargets", columnDefs.size());
                    actionHeaderMap.put("mDataProp", "Action");
                    actionHeaderMap.put("mData", null);
                    actionHeaderMap.put("bVisible", true);
                    actionHeaderMap.put("bSortable", false);
                    columnDefs.add(actionHeaderMap);

                }
            } else {

                columns = jdbcTemplate
                        .queryForList(PaginationQuery.COLUMNS_QUERY.replaceAll(":id", String.valueOf(id)));

                columnDefs = jdbcTemplate
                        .queryForList(PaginationQuery.COLUMN_DEFS_QUERY_CHECKBOX.replaceAll(":id", String.valueOf(id)));

                log.info("columns: " + columns);
                log.info("columnDefs: " + columnDefs);

                actionHeaderMap.put("aTargets", 0);
                actionHeaderMap.put("mDataProp", "#");
                actionHeaderMap.put("sWidth", "2%");
                actionHeaderMap.put("mData", null);
                actionHeaderMap.put("bVisible", true);
                actionHeaderMap.put("bSortable", false);
                columnDefs.add(0, actionHeaderMap);

                if (actionStatus.getAction() == 1) {
                    actionHeaderMap = new HashMap<>();
                    actionHeaderMap.put("aTargets", columnDefs.size());
                    actionHeaderMap.put("mDataProp", "Action");
                    actionHeaderMap.put("mData", null);
                    actionHeaderMap.put("bVisible", true);
                    actionHeaderMap.put("bSortable", false);
                    actionHeaderMap.put("sWidth", "2%");
                    columnDefs.add(actionHeaderMap);
                }
            }

            log.info("" + columnDefs);

            rMap.put("status", true);
        } catch (Exception e) {
            rMap.put("status", false);
            log.error("(Exception ==" + e.getMessage());
            e.printStackTrace();
        }

        rMap.put("columns", columns);
        rMap.put("columnDefs", columnDefs);
        return rMap;
    }

    @Override
    public Map<String, Object> getTableHeader(String id, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("headerSub", queryConditionOldRepository.getTableHeader(id));
            map.put("headerTop", queryConditionOldRepository.getTableHeaderTop(id));
            map.put("headerTitleList", queryConditionOldRepository.getTableHeaderTitleList(id));
            map.put("status", true);
        } catch (Exception e) {
            map.put("status", false);
            e.printStackTrace();
        }
        return map;

    }

    @Override
    public Map<String, Object> getDefaultData(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> returnData = new ArrayList<>();
        map.put("data", returnData);
        map.put("recordsTotal", 0);
        map.put("recordsFiltered", 0);
        return map;
    }

    private String getIndividualSearch(Long id, HttpServletRequest request) {
        List<String> returnData;
        StringBuilder searchString = new StringBuilder();
        try {
            String query = PaginationQuery.INDIVIDUAL_SEARCH_QUERY.replaceAll(":id",
                    String.valueOf(id));

            returnData = entityManager.createNativeQuery(query).getResultList();

            log.info("***getIndivisualSearch***");
            returnData.forEach(System.out::println);

            int k = 0;
            for (int i = 0; i < returnData.size(); i++) {
                if (request.getParameter("columns[" + i + "][search][value]") == null ||
                        request.getParameter("columns[" + i + "][search][value]").trim().equals("")) {
                } else {
                    if (request.getParameter("columns[0][data]") != null &&
                            request.getParameter("columns[0][data]").equalsIgnoreCase("#")) {
                        if (k == 0) {

                            searchString.append(returnData.get(i - 1).split("#")[1] + " LIKE '%" +
                                    request.getParameter("columns[" + returnData.get(i - 1).split("#")[0] +
                                            "][search][value]")
                                    + "%' ");
                        } else {
                            searchString.append(" AND ").append(returnData.get(i - 1).split("#")[1] + "LIKE '%"
                                    + request.getParameter(
                                            "columns[" + returnData.get(i - 1).split("#")[0] + "][search][value]")
                                    + "%' ");
                        }
                    } else {
                        if (k == 0) {
                            if ((returnData.get(i).split("#")[1].trim().indexOf("(") != 0) &&
                                    (returnData.get(i).split("#")[1].trim().indexOf(",") != -1)) {
                                String tempCol[] = returnData.get(i).split("#")[1].trim().split("\\.");
                                String tempTabName = tempCol[0];
                                String tempActCol[] = returnData.get(i).split("#")[1].trim().split(",");
                                for (int jy = 0; jy < tempActCol.length; jy++) {
                                    String tabStr = "";
                                    if (jy == 0) {
                                        tabStr = tempTabName + "." + tempCol[1].split(",")[0];
                                    } else {
                                        tabStr = tempTabName + "." + tempActCol[jy];
                                    }
                                    if (jy == 0) {
                                        searchString.append("(").append(tabStr + " LIKE '%" +
                                                request.getParameter("columns[" + returnData.get(i).split("#")[0] +
                                                        "][search][value]")
                                                + "%' ");
                                    }
                                    if (jy == 1) {
                                        searchString.append(" OR ").append(tabStr + " LIKE '%" +
                                                request.getParameter("columns[" + returnData.get(i).split("#")[0] +
                                                        "][search][value]")
                                                + "%' ").append(")");
                                    }

                                }
                            } else {
                                searchString.append(returnData.get(i).split("#")[1] + " LIKE '%" +
                                        request.getParameter("columns[" + returnData.get(i).split("#")[0] +
                                                "][search][value]")
                                        + "%' ");
                            }

                        } else {
                            if ((returnData.get(i).split("#")[1].trim().indexOf("(") != 0) &&
                                    (returnData.get(i).split("#")[1].trim().indexOf(",") != -1)) {
                                String tempCol[] = returnData.get(i).split("#")[1].trim().split("\\.");
                                String tempTabName = tempCol[0];
                                String tempActCol[] = returnData.get(i).split("#")[1].trim().split(",");
                                for (int jy = 0; jy < tempActCol.length; jy++) {
                                    String tabStr = "";
                                    if (jy == 0) {
                                        tabStr = tempTabName + "." + tempCol[1].split(",")[0];
                                    } else {
                                        tabStr = tempTabName + "." + tempActCol[jy];
                                    }
                                    if (jy == 0) {
                                        searchString.append("(").append(tabStr + " LIKE '%" +
                                                request.getParameter("columns[" + returnData.get(i).split("#")[0] +
                                                        "][search][value]")
                                                + "%' ");
                                    }
                                    if (jy == 1) {
                                        searchString.append(" OR ").append(tabStr + " LIKE '%" +
                                                request.getParameter("columns[" + returnData.get(i).split("#")[0] +
                                                        "][search][value]")
                                                + "%' ").append(")");
                                    }

                                }
                            } else {
                                searchString.append(" AND ")
                                        .append(returnData.get(i).split("#")[1] + " LIKE'%"
                                                + request.getParameter("columns[" + returnData.get(i).split("#")[0] +
                                                        "][search][value]")
                                                + "%' ");
                            }
                        }
                    }

                    k++;
                }

            }

            log.info("***Searchable String***" + searchString);
        } catch (Exception e) {
            log.error("(Exception ====" + e.getMessage());
            e.printStackTrace();
        }
        return searchString.toString();
    }

    private QueryMapper getQueryMapper(List<Object[]> queryList) {
        QueryMapper queryMapper = new QueryMapper();
        if (!Util.isNullOrEmpty(queryList)) {
            Object[] firstRow = queryList.get(0);
            if (firstRow.length > 0) {
                System.out.println(firstRow[0]);
                queryMapper.setCheckbox(Boolean.parseBoolean(String.valueOf(firstRow[0])) ? (byte) 1 : (byte) 0);
                queryMapper.setAction(Boolean.parseBoolean(String.valueOf(firstRow[1])) ? (byte) 1 : (byte) 0);
                queryMapper.setDelete_column((String) firstRow[2]);
                queryMapper.setDefault_order_by_column((String) firstRow[3]);
                queryMapper.setCount_column((String) firstRow[4]);
                queryMapper.setQuery((String) firstRow[5]);
                queryMapper.setColumns((String) firstRow[6]);

            }
        }
        return queryMapper;
    }

    public String getGlobalSearch(Long id, String searchableValue,
            HttpServletRequest request) {
        List<String> returnData;
        StringBuilder searchString = new StringBuilder();
        try {
            log.info("^^^^^^^^^^^^^^^^^^^^^^^^" + id);
            String query = PaginationQuery.GLOBAL_SEARCH_QUERY;
            query = query.replaceAll(":id", String.valueOf(id));
            Map map = new HashMap();
            map.put("query", query);

            returnData = entityManager.createNativeQuery(query).getResultList();

            for (int i = 0; i < returnData.size(); i++) {
                if (i == 0) {

                    if ((returnData.get(i).trim().indexOf("(") != 0) &&
                            (returnData.get(i).trim().indexOf(",") != -1)) {
                        String tempCol[] = returnData.get(i).trim().split("\\.");
                        String tempTabName = tempCol[0];
                        String tempActCol[] = returnData.get(i).trim().split(",");
                        for (int jy = 0; jy < tempActCol.length; jy++) {
                            String tabStr = "";
                            if (jy == 0) {
                                tabStr = tempTabName + "." + tempCol[1].split(",")[0];
                            } else {
                                tabStr = tempTabName + "." + tempActCol[jy];
                            }
                            if (jy == 0) {
                                searchString.append(" lower (" + tabStr + ") LIKE lower ( '%" +
                                        searchableValue + "%') ");
                            }
                            if (jy == 1) {
                                searchString.append(" OR ").append(" lower (" + tabStr + ") LIKE lower ( '%"
                                        + searchableValue + "%') ");
                            }

                        }
                    } else {
                        searchString.append(" lower (" + returnData.get(i) + ") LIKE lower ( '%" +
                                searchableValue + "%') ");
                    }

                } else {

                    if ((returnData.get(i).trim().indexOf("(") != 0) &&
                            (returnData.get(i).trim().indexOf(",") != -1)) {
                        String tempCol[] = returnData.get(i).trim().split("\\.");
                        String tempTabName = tempCol[0];
                        String tempActCol[] = returnData.get(i).trim().split(",");
                        for (int jy = 0; jy < tempActCol.length; jy++) {
                            String tabStr = "";
                            if (jy == 0) {
                                tabStr = tempTabName + "." + tempCol[1].split(",")[0];
                            } else {
                                tabStr = tempTabName + "." + tempActCol[jy];
                            }
                            if (jy == 0) {
                                searchString.append(" OR ").append(" lower (" + tabStr + ") LIKE lower ( '%"
                                        + searchableValue + "%') ");
                            }
                            if (jy == 1) {
                                searchString.append(" OR ").append(" lower (" + tabStr + ") LIKE lower ( '%"
                                        + searchableValue + "%') ");
                            }

                        }
                    } else {
                        searchString.append(" OR ").append(
                                " lower (" + returnData.get(i) + ") LIKE lower ( '%" + searchableValue + "%') ");
                    }
                }
            }

            log.info("Searchable String====B=========" + searchString);
        } catch (Exception e) {
            log.error("(Exception ====" + e.getMessage());
            e.printStackTrace();
        }

        return searchString.toString();
    }

}
