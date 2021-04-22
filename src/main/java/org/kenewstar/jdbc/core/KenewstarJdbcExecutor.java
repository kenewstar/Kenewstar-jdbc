package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.core.factory.ObjectFactory;
import org.kenewstar.jdbc.core.page.Page;
import org.kenewstar.jdbc.core.page.PageCondition;
import org.kenewstar.jdbc.core.sql.Sql;
import org.kenewstar.jdbc.core.sql.SqlKeyWord;
import org.kenewstar.jdbc.exception.PageNumberIllegalException;
import org.kenewstar.jdbc.function.MapTo;
import org.kenewstar.jdbc.transaction.JdbcTransaction;
import org.kenewstar.jdbc.transaction.Transaction;
import org.kenewstar.jdbc.util.DataTableInfo;
import org.kenewstar.jdbc.util.KenewstarUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;
import java.util.logging.Logger;

/**
 * JDBC执行器
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.1
 */
public class KenewstarJdbcExecutor extends CommonExecutor {


    private volatile static JdbcExecutor jdbcExecutor;

    private KenewstarJdbcExecutor() {
        super();
    }

    private KenewstarJdbcExecutor(String configPath) {
        super(configPath);
    }

    /**
     * JdbcExecutor对象
     * @param configPath 配置文件地址
     * @return KenewstarJdbcExecutor
     */
    public static JdbcExecutor getInstance(String configPath) {
        if (jdbcExecutor == null) {
            synchronized (KenewstarJdbcExecutor.class) {
                if (jdbcExecutor == null) {
                    if (Objects.isNull(configPath)) {
                        jdbcExecutor = new KenewstarJdbcExecutor();
                    } else {
                        jdbcExecutor = new KenewstarJdbcExecutor(configPath);
                    }
                }

            }

        }
        return jdbcExecutor;
    }


    @Override
    public Transaction getTransaction() {
        // 获取连接
        Connection conn = statement.getConnection();
        // 创建事务控制类
        // 返回事务
        return new JdbcTransaction(conn);
    }

    @Override
    public int updateEntity(String sql, Object...args){
        return statement.preparedUpdateExecutor(sql, args);
    }

    @Override
    public int insertEntity(String sql, Object...args){
        return updateEntity(sql,args);
    }

    @Override
    public int deleteEntity(String sql, Object...args){
        return updateEntity(sql,args);
    }


    @Override
    public <T> T selectEntityById(String sql, Class<T> entityClass, Integer id) {
        // 执行查询
        List<Map<String, Object>> maps = statement.preparedSelectExecutor(sql, id);
        // 返回结果对象
        return ObjectFactory.buildReturnObject(entityClass, maps);
    }


    @Override
    public <T> List<T>  selectAllEntity(String sql, Class<T> entityClass){
        // 不给列参数，则相当于查询所有数据
        // 返回所有数据集合
        return selectListByColumns(sql, entityClass);
    }


    @Override
    public <T> List<T> selectListByColumns(String sql, Class<T> entityClass, Object...args) {
        // 执行查询语句
        List<Map<String, Object>> maps = statement.preparedSelectExecutor(sql, args);

        // 返回List结果集合
        return ObjectFactory.buildReturnList(entityClass, maps);
    }


    @Override
    public int insert(Object entity) {
        Sql sql = buildInsertSqlFragment(entity);
        // 获取插入Sql
        StringBuilder insertSql = sql.getSql();
        // 获取插入参数
        List<Object> params = sql.getParams();
        // 执行Sql
        return statement.preparedUpdateExecutor(insertSql.toString(),
                params.toArray(new Object[]{}));

    }


    @Override
    public int deleteById(Integer id, Class<?> entityClass){
        Sql sql = buildDeleteSqlFragment(entityClass);
        // 获取插入Sql
        StringBuilder deleteSql = sql.getSql();
        // 执行Sql
        return statement.preparedUpdateExecutor(deleteSql.toString(), id);
    }


    @Override
    public int updateById(Object entity){
        Sql sql = buildUpdateSqlFragment(entity);
        // 获取Sql语句
        StringBuilder updateSql = sql.getSql();
        // 获取参数
        List<Object> params = sql.getParams();
        // 执行Sql
        return statement.preparedUpdateExecutor(updateSql.toString(),
                params.toArray(new Object[]{}));
    }


    @Override
    public <T> T selectById(Integer id, Class<T> entityClass){
        // 获取id名
        String idName = DataTableInfo.getIdName(entityClass);
        // 构建select sql前缀对象
        Sql sql = buildSelectSqlFragment(entityClass);
        // sql
        StringBuilder selectSql = sql.getSql();
        selectSql.append(SqlKeyWord.BLANK_CHAR)
                .append(idName).append(SqlKeyWord.EQ)
                .append(SqlKeyWord.PLACEHOLDER);

        // 执行SQL语句
        T t = selectEntityById(selectSql.toString(), entityClass, id);
        // 打印SQL语句
        LOG.info("Executed SQL ===> "+sql.toString());
        //返回查询的对象
        return t;
    }


    @Override
    public <T> List<T> selectAll(Class<T> entityClass){
        Sql sql = buildSelectSqlFragment(entityClass);
        // 执行SQL语句
        List<T> list = selectAllEntity(sql.getSql().toString(), entityClass);
        // 打印SQL语句
        LOG.info(EXECUTED_SQL + sql.getSql().toString());
        // 返回查询结果
        return list;
    }


    @Override
    public long count(Class<?> entityClass){
        Sql sql = buildCountSqlFragment(entityClass);
        // 返回计数结果
        return sqlCountExecutor(sql);
    }


    @Override
    public long count(Class<?> entityClass, MapTo mapTo) {

        Sql sql = buildCountSqlFragment(entityClass);

        mapTo.conditionSql(sql);

        List<Map<String, Object>> maps =
                statement.preparedSelectExecutor(sql.getSql().toString(),
                        sql.getParams().toArray(new Object[]{}));

        // 打印SQL语句
        LOG.info(EXECUTED_SQL + sql.getSql().toString());

        return (long) maps.get(0).get(SqlKeyWord.COUNT);
    }


    @Override
    public <T> List<T> selectAll(Class<T> entityClass, SortList sorts){
        if (Objects.isNull(sorts)){
            return selectAll(entityClass);
        }
        Sql sql = buildSelectSqlFragment(entityClass);
        StringBuilder selectSql = sql.getSql();

        Sql orderBy = buildOrderBySqlFragment(sorts);

        selectSql.append(orderBy.getSql());

        // 执行SQL语句
        List<T> result = selectAllEntity(selectSql.toString(), entityClass);
        // 打印SQL语句
        LOG.info(EXECUTED_SQL + selectSql.toString());
        return result;
    }


    @Override
    @Deprecated
    public <T> Page<T> selectAll(Class<T> entityClass, PageCondition condition) {
        // 根据分页中的排序条件查询所有
        List<T> list = selectAll(entityClass, condition.getSort());

        // 对排序后的数据进行分页操作
        // 创建Page<T>对象，封装返回结果
        Page<T> page = new Page<>();
        int size = list.size();
        int pageSize = condition.getPageSize();
        int pageNumber = condition.getPageNumber();

        // 设置总记录数
        page.setTotal(size);
        // 设置当前页码
        page.setPageNum(pageNumber);
        // 设置每页记录数
        page.setPageSize(pageSize);
        // 设置总页数(总记录数/每页记录数[+1])
        if(size % pageSize == 0) {
            page.setPages(size / pageSize);
        }else {
            page.setPages(size / pageSize+1);
        }

        // 判断页码是否非法
        if (pageNumber < 0 || pageNumber >= page.getTotal()) {
            // 抛出页码非法异常
            throw new PageNumberIllegalException(
                    "the pageNumber parameter is error "+
                    "pageNumber : " + pageNumber);
        }

        // 取出分页的数据记录
        int fromIndex = pageNumber * pageSize;
        int toIndex = (pageNumber + 1) * pageSize;
        // 判断是否是最后一页
        if (condition.getPageNumber() == page.getTotal() - 1) {
            // 最后一页
            page.setRows(list.subList(fromIndex,size));
        }else {
            // 非最后一页
            page.setRows(list.subList(fromIndex,toIndex));
        }
        // 返回分页结果
        return page;

    }


}
