package org.kenewstar.jdbc.core.sql;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/6
 */
public interface SqlKeyWord {

    char   BLANK_CHAR = ' ';
    char   RIGHT_BRACKETS_CHAR = ')';
    String BLANK  = " ";
    String SELECT = "select";
    String UPDATE = "update";
    String DELETE = "delete";
    String INSERT = "insert";
    String WHERE  = "where";
    String FROM   = "from";
    String SET    = "set";
    String INTO   = "into";
    String VALUES = "values";
    String AS     = "as";
    String IN     = "in";
    String AND    = "and";
    String OR     = "or";
    String ON     = "on";
    String NOT    = "not";
    String DESC   = "desc";
    String ASC    = "asc";
    String LIKE   = "like";
    String COUNT  = "count(*)";
    String LIMIT  = "limit";
    String OFFSET = "offset";
    String HAVING = "having";
    String NOT_BETWEEN = "not between";
    String BETWEEN  = "between";
    String DISTINCT = "distinct";
    String ORDER_BY = "order by";
    String GROUP_BY = "group by";

    String LEFT_JOIN = "left join";

    String PLACEHOLDER = "?";
    String PERCENT_SIGN = "%";
    String EQ = " = ";
    String NE = " <> ";
    String GT = " > ";
    String GE = " >= ";
    String LT = " < ";
    String LE = " <= ";

    String LEFT_BRACKETS = "(";
    String RIGHT_BRACKETS = ")";
    String COMMA = ",";
    String SPOT  = ".";




}
