package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.core.page.Page;
import org.kenewstar.jdbc.core.page.PageCondition;
import org.kenewstar.jdbc.core.sql.Sql;
import org.kenewstar.jdbc.core.sql.SqlKeyWord;
import org.kenewstar.jdbc.exception.PageNumberIllegalException;
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

    private static final Logger log = Logger.getLogger("jdbcExecutor");

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

    /**
     * 将查询出来的Map<columnName,columnValue>映射转换为
     * Map<fieldName,fieldValue>映射
     * @param maps 列名与列值映射
     * @return 返回属性名与属性值映射
     */
    private List<Map<String,Object>> getFieldNameAndValues(List<Map<String, Object>> maps, Class<?> entityClass) {
        // 获取所有列信息
        Map<String, String> columnAndField = DataTableInfo.getColumnAndField(entityClass);
        // 存放属性与属性值的映射
        List<Map<String,Object>> fieldNameAndValues = new ArrayList<>(maps.size());
        for (Map<String,Object> map : maps){
            Map<String,Object> mapField = new HashMap<>(columnAndField.size());
            for (String columnName : columnAndField.keySet()){
                // 遍历设置属性与属性值的对应关系
                mapField.put(columnAndField.get(columnName), map.get(columnName));
            }
            // 添加到list中
            fieldNameAndValues.add(mapField);
        }
        return fieldNameAndValues;
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
        List<Map<String, Object>> maps = statement.preparedSelectExecutor(sql, id);
        // 查询结果为空
        if (maps.size() == 0){
            return null;
        }
        // 获取所有列信息
        Map<String, String> columnAndField = DataTableInfo.getColumnAndField(entityClass);
        // 获取所有属性信息
        Field[] fields = entityClass.getDeclaredFields();
        // 实例化该类对象
        T t = null;
        try {
            t = entityClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 存放属性与属性值的映射
        Map<String,Object> fieldNameAndValue = new HashMap<>();
        for (String columnName : columnAndField.keySet()) {
            // 遍历设置属性与属性值的对应关系
            fieldNameAndValue.put(columnAndField.get(columnName),
                                  maps.get(0).get(columnName));
        }

        for (Field field : fields) {
            // 设置私有属性可访问
            field.setAccessible(true);
            // 给私有属性设置值
            try {
                field.set(t,fieldNameAndValue.get(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // 查询返回结果
        return t;
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
        if (maps.size() == 0) {
            return null;
        }
        // 获取属性与属性值的映射关系
        List<Map<String, Object>> fieldNameAndValues = getFieldNameAndValues(maps, entityClass);

        //============================================//
        // 存储返回结果
        List<T> result = new ArrayList<>(maps.size());
        // 声明返回的类对象
        T t = null;
        // 获取所有属性信息
        Field[] fields = entityClass.getDeclaredFields();
        // 遍历所有结果
        int index = 0;
        for (Map<String,Object> ignored : maps) {
            // 创建一个对象，用于存储单条信息
            try {
                t = entityClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Field field : fields) {
                // 设置私有属性可访问
                field.setAccessible(true);
                // 给私有属性设置值
                try {
                    field.set(t,fieldNameAndValues.get(index).get(field.getName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            index ++;
            result.add(t);
        }
        // 返回List结果集合
        return result;
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
        // 获取表名
        String tableName = KenewstarUtil.getTableName(entityClass);
        // 获取id名
        String idName = DataTableInfo.getIdName(entityClass);

        // example : select * from tableName where id=?
        StringBuilder sql = new StringBuilder("select * from " + tableName);
        sql.append(" where ").append(idName).append("=?");

        // 执行SQL语句
        T t = selectEntityById(sql.toString(), entityClass, id);
        // 打印SQL语句
        log.info("Executed SQL ===> "+sql.toString());
        //返回查询的对象
        return t;
    }


    @Override
    public <T> List<T> selectAll(Class<T> entityClass){
        // 获取表名
        String tableName = KenewstarUtil.getTableName(entityClass);
        //==============构建SQL语句====================//
        // example : select * from tableName
        StringBuilder sql = new StringBuilder("select * from "+tableName);
        //===========================================//
        // 执行SQL语句
        List<T> list = selectAllEntity(sql.toString(), entityClass);
        // 打印SQL语句
        log.info("Executed SQL ===> "+sql.toString());
        // 返回查询结果
        return list;
    }


    @Override
    public long count(Class<?> entityClass){
        Sql sql = buildCountSqlFragment(entityClass);
        // 获取Sql语句
        StringBuilder countSql = sql.getSql();
        // 执行
        List<Map<String, Object>> maps =
                statement.preparedSelectExecutor(countSql.toString());
        // 返回计数结果
        return (long) maps.get(0).get("count(*)");
    }


    @Override
    public long count(Object entity) {
        // 获取class
        Class<?> entityClass = entity.getClass();
        // 获取表名
        String tableName = KenewstarUtil.getTableName(entityClass);
        // 获取列名
        Map<String, String> columnNames = DataTableInfo.getColumnAndField(entityClass);
        // 参数
        Object[] params = new Object[columnNames.size()];
        // select count(*) from tableName where columnName=? and ....
        StringBuilder sql = new StringBuilder("select count(*) count from ");
        sql.append(tableName);
        sql.append(" where 1=1 ");
        Field[] fields = entityClass.getDeclaredFields();
        int index = 0;
        for (Field field : fields) {
            Object o = null;
            try {
                field.setAccessible(true);
                o = field.get(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            String columnName = DataTableInfo.getColumnNameByField(field);
            if (Objects.nonNull(o) && !Objects.equals(columnName, "")) {
                sql.append("and ")
                   .append(columnName)
                   .append("=? ");
                params[index ++] = o;
            }
        }
        Object[] paramsArray = Arrays.copyOf(params, index);

        List<Map<String, Object>> maps = statement.preparedSelectExecutor(sql.toString(), paramsArray);

        // 打印SQL语句
        log.info("Executed SQL ===> "+sql.toString());

        return (long) maps.get(0).get("count");
    }


    @Override
    public <T> List<T> selectAll(Class<T> entityClass, List<Sort> sorts){
        if (Objects.isNull(sorts)||sorts.isEmpty()){
            return selectAll(entityClass);
        }
        // 获取表名
        String tableName = KenewstarUtil.getTableName(entityClass);

        // select * from tableName order by column1 desc|asc,[column2 desc|asc]...
        StringBuilder sql = new StringBuilder("select * from "+tableName+" order by ");
        // 遍历排序条件
        for (Sort sort : sorts){
            // 遍历排序条件
            // 判断是升序或降序
            if (Objects.equals(sort.getOrder(),Sort.DESC)){
                // 排序为降序
                sql.append(sort.getColumn())
                   .append(SqlKeyWord.DESC)
                   .append(SqlKeyWord.COMMA);
            }else {
                // 升序
                sql.append(sort.getColumn())
                   .append(SqlKeyWord.ASC)
                   .append(SqlKeyWord.COMMA);
            }

        }
        // 去掉最后一个逗号
        sql.setCharAt(sql.length()-1,SqlKeyWord.BLANK_CHAR);
        // 执行SQL语句
        List<T> result = selectAllEntity(sql.toString(), entityClass);
        // 打印SQL语句
        log.info("Executed SQL ===> "+sql.toString());
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


    @Override
    public int updateByIdSelective(Object entity) {
        Class<?> entityClass = entity.getClass();

        String tableName = KenewstarUtil.getTableName(entityClass);
        Map<String, String> columnNames = DataTableInfo.getColumnAndField(entityClass);
        // id列名
        String idName = DataTableInfo.getIdName(entityClass);
        // id属性名
        String idFieldName = columnNames.get(idName);
        Object idValue = null;
        // 参数
        Object[] params = new Object[columnNames.size()];

        StringBuilder sql = new StringBuilder("update ");
        sql.append(tableName)
           .append(" set ");
        Field[] fields = entityClass.getDeclaredFields();
        int index = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            Object o;
            String columnName = DataTableInfo.getColumnNameByField(field);
            try {
                o = field.get(entity);
                if (Objects.nonNull(o) && !Objects.equals(idFieldName, field.getName())) {
                    sql.append(columnName)
                       .append("=? ,");
                    params[index ++] = o;
                }
                if (Objects.equals(idFieldName, field.getName())) {
                    idValue = field.get(entity);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (index == 0) {
            return 0;
        }
        sql.setCharAt(sql.length() - 1, SqlKeyWord.BLANK_CHAR);
        sql.append("where ").append(idName).append("=?");
        params[index ++] = idValue;

        Object[] paramsArray = Arrays.copyOf(params, index);
        // 打印SQL语句
        log.info("Executed SQL ===> "+sql.toString());

        return updateEntity(sql.toString(), paramsArray);
    }


    @Override
    public int insertSelective(Object entity) {
        // 获取实体类
        Class<?> entityClass = entity.getClass();
        // 获取表名
        String tableName = KenewstarUtil.getTableName(entityClass);
        // 获取属性名列名映射
        Map<String, String> fieldNameAndColumnName = DataTableInfo.getFieldAndColumn(entityClass);

        StringBuilder sql = new StringBuilder(SqlKeyWord.INSERT);
        sql.append(SqlKeyWord.INTO)
           .append(tableName).append(SqlKeyWord.LEFT_BRACKETS);
        // 获取所有属性
        Field[] fields = entityClass.getDeclaredFields();

        StringBuilder valus = new StringBuilder(SqlKeyWord.VALUES);
        valus.append(SqlKeyWord.LEFT_BRACKETS);

        List<Object> params = new ArrayList<>(fieldNameAndColumnName.size());
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object o = field.get(entity);
                if (Objects.nonNull(o) && !"".equals(o)) {
                    sql.append(fieldNameAndColumnName.get(field.getName()))
                       .append(SqlKeyWord.COMMA);
                    valus.append(SqlKeyWord.PLACEHOLDER)
                         .append(SqlKeyWord.COMMA);
                    params.add(o);
                }
            }
            sql.setCharAt(sql.length() - 1, SqlKeyWord.RIGHT_BRACKETS_CHAR);
            valus.setCharAt(valus.length() - 1, SqlKeyWord.RIGHT_BRACKETS_CHAR);
            sql.append(valus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int row = statement.preparedUpdateExecutor(sql.toString(), params.toArray(new Object[]{}));
        // 打印SQL语句
        log.info("Executed SQL ===> "+sql.toString());
        return row;
    }
}
