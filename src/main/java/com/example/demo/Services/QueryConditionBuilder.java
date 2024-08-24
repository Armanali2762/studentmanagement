package com.example.demo.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.example.demo.Dao.QueryConditionRepo;
import com.example.demo.Entities.QueryCondition;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class QueryConditionBuilder {

    public static Map generateQueryConditionV2(QueryMapper query,
            String dataQuery, String searchableString, String orderByString,
            HttpServletRequest request, Long id,
            QueryConditionRepo queryConditionRepo, int limit, int localOffSet) throws ParseException {

        String mainQuery = dataQuery;
        String countQuery = "";

        Map<String, Object> dataMap = new HashMap<>();
        String conditionQuery = "";

        String allColumns = query.getColumns();
        String deleteColumn = query.getDelete_column();
        String countColumn = query.getCount_column();

        try {

            QueryCondition condition = queryConditionRepo.findAllByQueryListPkAndActive(id, true);
            if (condition != null && !Util.isNullOrEmpty(condition.getCondition())) {

                conditionQuery = condition.getCondition().replaceAll("[\\t\\n\\r]+", "").replaceAll("\\s{2,}", " ");

                // Get list of all parameters from conditionQuery
                List<String> paramList = extractParametersFromConditionQuery(conditionQuery);

                log.info("conditionQuery {}", conditionQuery);
                log.info("paramList: {}", paramList);

                // Replace all parameters with actual values
                if (!Util.isNullOrEmpty(paramList)) {
                    for (String param : paramList) {
                        log.info("param: {}", param);
                        log.info("param value: {}", request.getParameter(param));
                        conditionQuery = conditionQuery.replaceAll("#" + param + "#",
                                getRequestParamValue(request.getParameter(param)));
                    }
                }

                conditionQuery = conditionQuery.toLowerCase();
            }

            log.info("conditionQuery: before {}", conditionQuery);

            // check Group By, if found then split it
            String conditionQueryFirst = "";
            String conditionQuerySecond = "";
            if (conditionQuery.contains("group by")) {
                String[] splitConditionQuery = conditionQuery.split("group by");
                conditionQueryFirst = splitConditionQuery[0];
                conditionQuerySecond = " group by " + splitConditionQuery[1];

            } else if (conditionQuery.contains("having")) {
                String[] splitConditionQuery = conditionQuery.split("having");
                conditionQueryFirst = splitConditionQuery[0];
                conditionQuerySecond = " having " + splitConditionQuery[1];
            } else {
                conditionQueryFirst = conditionQuery;
            }

            // Handle main query with generated queries
            mainQuery = mainQuery.replaceAll(":search",
                    conditionQueryFirst).concat(SPACE).concat(":search")
                    .concat(SPACE);
            log.info("searchableString: before" + searchableString);
            if (mainQuery.contains("where")) {
                if (!Util.isNullOrEmpty(searchableString)) {
                    searchableString = " and ( " + searchableString + ") ";
                }
            } else if (!Util.isNullOrEmpty(searchableString)) {
                searchableString = " WHERE (" + searchableString + " ) ";
            }
            log.info("searchableString: after" + searchableString);

            // add count column in query
            countQuery = mainQuery;
            countQuery = countQuery.replaceAll(":pk", "USERID");
            if (Util.isNullOrEmpty(searchableString)) {
                countQuery = countQuery.replace(":fields", "count(" + countColumn +
                        ")").replaceAll(":search", BLANK)
                        .concat(SPACE);
            } else {
                countQuery = countQuery.replace(":fields", "count(" + countColumn + ")")
                        .replaceAll(":search", searchableString)
                        .concat(SPACE);
            }

            // add remaining part of condition query
            countQuery = countQuery.concat(SPACE).concat(conditionQuerySecond).concat(SPACE);

            // creating full main query
            mainQuery = mainQuery.replace(":fields", allColumns + "," + deleteColumn + "as DT_RowId");
            if (Util.isNullOrEmpty(searchableString)) {
                mainQuery = mainQuery.replaceAll(":search", BLANK).concat(SPACE);
            } else {
                mainQuery = mainQuery.replaceAll(":search", searchableString).concat(SPACE);
            }

            // add remaining part of condition query
            mainQuery = mainQuery.concat(SPACE).concat(conditionQuerySecond).concat(SPACE);

            // handle all length per page
            String limitStr = "";
            if (limit != -1) {
                limitStr = " LIMIT " + limit + " OFFSET " + localOffSet;
            }

            mainQuery = mainQuery.concat(orderByString)
                    .concat(limitStr + " ) as table1 ");
            mainQuery = mainQuery.replace("CAST(@row := @row + 1 as char) AS `S No`,",
                    SPACE);
            mainQuery = "SELECT CAST(@row := @row + 1 as char) AS `S No`, table1.* FROM(SELECT @row := " + localOffSet
                    + ") t, ( " + mainQuery;
            if (countQuery.contains("group by")) {
                countQuery = " SELECT count(*) FROM (" + countQuery + ") as temp";
            }

            dataMap.put("countQuery", countQuery);
            dataMap.put("mainQuery", mainQuery);

            log.info("COUNT QUERY {}", countQuery);
            log.info("MAIN QUERY: {}", mainQuery);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return dataMap;

    }

    private static List<String> extractParametersFromConditionQuery(String queryCondition) {
        List<String> placeholders = new ArrayList<>();
        Pattern pattern = Pattern.compile("#([^#]+)#");
        Matcher matcher = pattern.matcher(queryCondition);

        while (matcher.find()) {
            placeholders.add(matcher.group(1));
        }

        return placeholders;
    }

    public static String getRequestParamValue(String paramValue) {
        if (!Util.isNullOrEmpty(paramValue)) {
            log.info(" paramValue1: " + paramValue);
            String decryptedValue = Util.decrypt(paramValue);
            log.info(" decryptedValue: " + decryptedValue);
            if (!Util.isNullOrEmpty(decryptedValue)) {

                paramValue = decryptedValue;
                log.info(" paramValue2: " + paramValue);
            }
        }

        return paramValue;
    }

    /**
     * The Constant BLANK.
     */
    private static final String BLANK = "";

    /**
     * The Constant SPACE.
     */
    private static final String SPACE = " ";

    /**
     * The Constant PAGE_NO.
     */
    public static final String PAGE_NO = "start";

    /**
     * The Constant PAGE_SIZE.
     */
    public static final String PAGE_SIZE = "length";

    /**
     * The Constant DRAW.
     */
    public static final String DRAW = "draw";

}
