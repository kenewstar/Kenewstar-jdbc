package org.kenewstar.jdbc.core.sql;

import org.kenewstar.jdbc.function.FunctionColumn;
import org.kenewstar.jdbc.util.FunctionUtil;
import org.kenewstar.jdbc.util.KenewstarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Sql构造对象
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/4/7
 */
public final class Sql {


    protected final StringBuilder sql;
    protected final List<Object> params;

    public Sql() {
        this.sql = new StringBuilder();
        this.params = new ArrayList<>();
    }

    public StringBuilder getSql() {
        return sql;
    }

    public List<Object> getParams() {
        return params;
    }

    /**
     * 构造 1=1
     * @return this
     */
    private Sql commonEq() {
        sql.append(" 1 = 1 ");
        return this;
    }

    /**
     * 关联的列名
     * @param column 关联的列
     * @param other 另一个列
     * @param <T> t
     * @param <V> v
     * @param <R> r
     * @return this
     */
    public <T, V, R> Sql joinEq(FunctionColumn<T, V> column, FunctionColumn<R, V> other) {
        String columnName = FunctionUtil.getColumnName(column);
        String otherName = FunctionUtil.getColumnName(other);
        sql.append(columnName)
           .append(SqlKeyWord.EQ)
           .append(otherName)
           .append(SqlKeyWord.BLANK_CHAR);
        return this;
    }

    /**
     * 等于
     * username = ?
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql eq(FunctionColumn<T, V> column, Object value) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.EQ)
           .append(SqlKeyWord.PLACEHOLDER);
        params.add(value);
        return this;
    }

    /**
     *  and column = ?
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql andEq(FunctionColumn<T, V> column, Object value) {
        if (Objects.isNull(value) || "".equals(value)) {
            return this;
        }
        return and().eq(column, value);
    }

    /**
     * 不等于
     * username <> ?
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql ne(FunctionColumn<T, V> column, Object value) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.NE)
           .append(SqlKeyWord.PLACEHOLDER);
        params.add(value);
        return this;
    }

    /**
     * 不等于
     * and column <> ?
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql andNe(FunctionColumn<T, V> column, Object value) {
        if (Objects.isNull(value) || "".equals(value)) {
            return this;
        }
        return and().ne(column, value);
    }

    /**
     * 小于
     * age < 20
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql lt(FunctionColumn<T, V> column, Object value) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.LT)
           .append(SqlKeyWord.PLACEHOLDER);
        params.add(value);
        return this;
    }
    /**
     * 小于
     * and age < 20
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql andLt(FunctionColumn<T, V> column, Object value) {
        if (Objects.isNull(value) || "".equals(value)) {
            return this;
        }
        return and().lt(column, value);
    }

    /**
     * 小于等于
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql le(FunctionColumn<T, V> column, Object value) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.LE)
           .append(SqlKeyWord.PLACEHOLDER);
        params.add(value);
        return this;
    }
    /**
     * 小于等于
     * and column <= ?
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql andLe(FunctionColumn<T, V> column, Object value) {
        if (Objects.isNull(value) || "".equals(value)) {
            return this;
        }
        return and().le(column, value);
    }

    /**
     * 大于
     * age > 20
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql gt(FunctionColumn<T, V> column, Object value) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.GT)
           .append(SqlKeyWord.PLACEHOLDER);
        params.add(value);
        return this;
    }
    /**
     * 大于
     * and age > 20
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql andGt(FunctionColumn<T, V> column, Object value) {
        if (Objects.isNull(value) || "".equals(value)) {
            return this;
        }
        return and().gt(column, value);
    }

    /**
     * 大于等于
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql ge(FunctionColumn<T, V> column, Object value) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.GE)
           .append(SqlKeyWord.PLACEHOLDER);
        params.add(value);
        return this;
    }

    /**
     * 大于等于
     * and column >= ?
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql andGe(FunctionColumn<T, V> column, Object value) {
        if (Objects.isNull(value) || "".equals(value)) {
            return this;
        }
        return and().ge(column, value);
    }

    /**
     * 范围值
     * age between v1 and v2
     * @param column 列名
     * @param v1 值1
     * @param v2 值2
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql between(FunctionColumn<T, V> column, Object v1, Object v2) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.BETWEEN)
           .append(SqlKeyWord.PLACEHOLDER)
           .append(SqlKeyWord.AND)
           .append(SqlKeyWord.PLACEHOLDER);
        params.add(v1);
        params.add(v2);
        return this;
    }
    /**
     * 范围值
     * and age between v1 and v2
     * @param column 列名
     * @param v1 值1
     * @param v2 值2
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql andBetween(FunctionColumn<T, V> column, Object v1, Object v2) {
        if ((Objects.isNull(v1) || "".equals(v1)) &&
            (Objects.isNull(v2) || "".equals(v2))) {
            return this;
        }
        return and().between(column, v1, v2);
    }


    /**
     * age not between v1 and v2
     * @param column 列名
     * @param v1 值1
     * @param v2 值2
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql notBetween(FunctionColumn<T, V> column, Object v1, Object v2) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.NOT_BETWEEN)
           .append(SqlKeyWord.PLACEHOLDER)
           .append(SqlKeyWord.AND)
           .append(SqlKeyWord.PLACEHOLDER);
        params.add(v1);
        params.add(v2);
        return this;
    }
    /**
     * 范围值
     * and age not between v1 and v2
     * @param column 列名
     * @param v1 值1
     * @param v2 值2
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql andNotBetween(FunctionColumn<T, V> column, Object v1, Object v2) {
        if ((Objects.isNull(v1) || "".equals(v1)) &&
                (Objects.isNull(v2) || "".equals(v2))) {
            return this;
        }
        return and().notBetween(column, v1, v2);
    }

    /**
     * 模糊查询[全模糊]
     * username like %kkk%
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql like(FunctionColumn<T, V> column, Object value) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.LIKE)
           .append(SqlKeyWord.PLACEHOLDER);
        params.add(SqlKeyWord.PERCENT_SIGN + value + SqlKeyWord.PERCENT_SIGN);
        return this;
    }
    /**
     * 模糊查询[全模糊]
     * and username like %kkk%
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql andLike(FunctionColumn<T, V> column, Object value) {
        if (Objects.isNull(value) || "".equals(value)) {
            return this;
        }
        return and().like(column, value);
    }

    /**
     * 不匹配
     * not like .....
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql notLike(FunctionColumn<T, V> column, Object value) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.NOT)
           .append(SqlKeyWord.LIKE)
           .append(SqlKeyWord.PLACEHOLDER);
        params.add(SqlKeyWord.PERCENT_SIGN + value + SqlKeyWord.PERCENT_SIGN);
        return this;
    }

    /**
     * 不匹配
     * and not like .....
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql andNotLike(FunctionColumn<T, V> column, Object value) {
        if (Objects.isNull(value) || "".equals(value)) {
            return this;
        }
        return and().notLike(column, value);
    }

    /**
     * 左模糊
     * username like %kkk
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql likeLeft(FunctionColumn<T, V> column, Object value) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.LIKE)
           .append(SqlKeyWord.PLACEHOLDER);
        params.add(SqlKeyWord.PERCENT_SIGN + value);
        return this;
    }

    /**
     * 左模糊
     * and username like %kkk
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql andLikeLeft(FunctionColumn<T, V> column, Object value) {
        if (Objects.isNull(value) || "".equals(value)) {
            return this;
        }
        return and().likeLeft(column, value);
    }

    /**
     * 右模糊
     * username like kkk%
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql likeRight(FunctionColumn<T, V> column, Object value) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.LIKE)
           .append(SqlKeyWord.PLACEHOLDER);
        params.add(value + SqlKeyWord.PERCENT_SIGN);
        return this;
    }

    /**
     * 右模糊
     * and username like kkk%
     * @param column 列名
     * @param value 值
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql andLikeRight(FunctionColumn<T, V> column, Object value) {
        if (Objects.isNull(value) || "".equals(value)) {
            return this;
        }
        return and().likeRight(column, value);
    }

    /**
     * in 操作
     * @param column 列名
     * @param values 值数组
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql in(FunctionColumn<T, V> column, Object ...values) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.IN)
           .append(SqlKeyWord.LEFT_BRACKETS);
        for (Object value : values) {
            sql.append(SqlKeyWord.PLACEHOLDER)
               .append(SqlKeyWord.COMMA);
            params.add(value);
        }
        sql.setCharAt(sql.length() - 1, SqlKeyWord.RIGHT_BRACKETS_CHAR);
        return this;
    }

    /**
     * not in 操作
     * @param column 列名
     * @param values 值数组
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql notIn(FunctionColumn<T, V> column, Object ...values) {
        sql.append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.NOT)
           .append(SqlKeyWord.IN)
           .append(SqlKeyWord.LEFT_BRACKETS);
        for (Object value : values) {
            sql.append(SqlKeyWord.PLACEHOLDER)
               .append(SqlKeyWord.COMMA);
            params.add(value);
        }
        sql.setCharAt(sql.length() - 1, SqlKeyWord.RIGHT_BRACKETS_CHAR);
        return this;
    }

    /**
     * group by name
     * @param column 列名
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql groupBy(FunctionColumn<T, V> column) {
        sql.append(SqlKeyWord.GROUP_BY)
           .append(FunctionUtil.getColumnName(column));
        return this;
    }

    /**
     * group by name,otherName
     * @param column 列名
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql by(FunctionColumn<T, V> column) {
        sql.append(SqlKeyWord.COMMA)
           .append(FunctionUtil.getColumnName(column));
        return this;
    }

    /**
     * 升序排序，第一个字段
     * @param column 列名
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql orderByAsc(FunctionColumn<T, V> column) {
        sql.append(SqlKeyWord.ORDER_BY)
           .append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.ASC);
        return this;
    }

    /**
     * 降序排序第一个字段
     * @param column 列名
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql orderByDesc(FunctionColumn<T, V> column) {
        sql.append(SqlKeyWord.ORDER_BY)
           .append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.DESC);
        return this;
    }

    /**
     * 升序排序其他字段
     * @param column 列名
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql asc(FunctionColumn<T, V> column) {
        sql.append(SqlKeyWord.COMMA)
           .append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.ASC);
        return this;
    }

    /**
     * 降序排序其他字段
     * @param column 列名
     * @param <T> t
     * @param <V> v
     * @return this
     */
    public <T, V> Sql desc(FunctionColumn<T, V> column) {
        sql.append(SqlKeyWord.COMMA)
           .append(FunctionUtil.getColumnName(column))
           .append(SqlKeyWord.DESC);
        return this;
    }

    /**
     * where
     * @return this
     */
    public Sql where() {
        sql.append(SqlKeyWord.WHERE);
        return this;
    }

    /**
     * where 1=1
     * @return this
     */
    public Sql whereCommonEq() {
        sql.append(SqlKeyWord.WHERE);
        return commonEq();
    }

    /**
     * and
     * @return this
     */
    public Sql and() {
        sql.append(SqlKeyWord.AND).append(SqlKeyWord.BLANK_CHAR);
        return this;
    }

    /**
     * or
     * @return this
     */
    public Sql or() {
        sql.append(SqlKeyWord.OR).append(SqlKeyWord.BLANK_CHAR);
        return this;
    }

    /**
     * left join
     * @param joinClass 连接的表对应的实体类
     * @return this
     */
    public Sql leftJoin(Class<?> joinClass) {
        sql.append(SqlKeyWord.LEFT_JOIN)
           .append(SqlKeyWord.BLANK_CHAR)
           .append(KenewstarUtil.getTableName(joinClass))
           .append(SqlKeyWord.BLANK_CHAR)
           .append(SqlKeyWord.ON);
        return this;
    }



}
