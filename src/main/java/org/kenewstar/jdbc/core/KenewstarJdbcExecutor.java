package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.exception.PageNumberIllegalException;
import org.kenewstar.jdbc.transaction.JdbcTransaction;
import org.kenewstar.jdbc.transaction.Transaction;
import org.kenewstar.jdbc.util.DataTableInfo;
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
public class KenewstarJdbcExecutor implements JdbcExecutor{

    private final KenewstarStatement statement;

    private static final Logger log = Logger.getLogger("SQL");

    public KenewstarJdbcExecutor(){
        statement = new KenewstarStatement();
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
    public <T> T selectEntityById(String sql, Class<T> entityClass, Integer id){
        List<Map<String, Object>> maps = statement.preparedSelectExecutor(sql,id);
        // 查询结果为空
        if (maps.size()==0){
            return null;
        }
        // 获取所有列信息
        Map<String, String> columnNames = DataTableInfo.getColumnNames(entityClass);
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
        for (String columnName : columnNames.keySet()){
            // 遍历设置属性与属性值的对应关系
            fieldNameAndValue.put(columnNames.get(columnName),
                    maps.get(0).get(columnName) );
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
        List<T> list = selectListByColumns(sql, entityClass);
        // 返回所有数据集合
        return list;
    }


    @Override
    public <T> List<T> selectListByColumns(String sql, Class<T> entityClass, Object...args){

        // 执行查询语句
        List<Map<String, Object>> maps = statement.preparedSelectExecutor(sql, args);
        if (maps.size()==0){
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
        for (Map<String,Object> map: maps ) {
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
            index++;
            result.add(t);
        }
        // 返回List结果集合
        return result;
    }

    /**
     * 将查询出来的Map<columnName,columnValue>映射转换为
     * Map<fieldName,fieldValue>映射
     * @param maps 列名与列值银蛇
     * @return 返回属性名与属性值映射
     */
    private List<Map<String,Object>> getFieldNameAndValues(List<Map<String, Object>> maps,Class<?> entityClass){
        // 获取所有列信息
        Map<String, String> columnNames = DataTableInfo.getColumnNames(entityClass);
        // 存放属性与属性值的映射
        List<Map<String,Object>> fieldNameAndValues = new ArrayList<>(maps.size());
        for (Map<String,Object> map : maps){
            int index = 0;
            Map<String,Object> mapField = new HashMap<>();
            for (String columnName : columnNames.keySet()){
                // 遍历设置属性与属性值的对应关系
                mapField.put(columnNames.get(columnName),map.get(columnName));
            }
            // 添加到list中
            fieldNameAndValues.add(mapField);
        }
        return fieldNameAndValues;
    }

    //========================封装 SQL 语句 START===============================//


    @Override
    public int insert(Object obj){
        Class<?> entityClass = obj.getClass();
        // 获取表名
        String tableName = DataTableInfo.getTableName(entityClass);
        // 获取所有列名
        Map<String, String> columnNames =
                DataTableInfo.getColumnNames(entityClass);
        // 获取传入对象的属性以及get方法
        Field[] fields = entityClass.getDeclaredFields();
        // 存放属性名与属性值，以方便取用
        Map<String,Object> mapFields = new HashMap<>();
        for (Field field:fields){
            field.setAccessible(true);
            try {
                mapFields.put(field.getName(),field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // 声明参数数组,存储SQL所需参数
        Object[] params = new Object[fields.length];

        // insert into tableName(columnName,columnName) values(?,?)
        //===============构建SQL语句===============//
        StringBuilder sql = new StringBuilder("insert into "+tableName);
        sql.append('(');
        int index=0;
        for (String columnName :columnNames.keySet()) {
            sql.append(columnName+',');
            //通过列名获取属性名进行属性的匹配，将匹配的属性的值放入参数数组中
            params[index++] = mapFields.get(columnNames.get(columnName));
        }
        sql.setCharAt(sql.length()-1,')');
        sql.append(" values(");
        for(int i=0;i<columnNames.size();i++){
            sql.append("?,");
        }
        sql.setCharAt(sql.length()-1,')');
        //========================================//

        // 执行SQL
        int insert = insertEntity(sql.toString(), params);
        // 打印SQL语句
        log.info("Executed SQL ===> "+sql.toString());
        // 返回影响行数
        return insert;
    }


    @Override
    public int deleteById(Integer id, Class<?> entityClass){
        // 获取表名
        String tableName = DataTableInfo.getTableName(entityClass);
        // 获取表的id
        String idName = DataTableInfo.getIdName(entityClass);
        //============构建SQL语句=================//
        // example : delete from tableName where id = ?
        StringBuilder sql = new StringBuilder("delete from "+tableName);
        sql.append(" where "+idName+" = ?");
        //======================================//
        // 执行SQL语句
        int delete = deleteEntity(sql.toString(), id);
        // 打印SQL语句
        log.info("Executed SQL ===> "+sql.toString());
        // 返回影响的行数
        return delete;
    }


    @Override
    public int updateById(Object obj){
        Class<?> entityClass = obj.getClass();
        // 获取表名
        String tableName = DataTableInfo.getTableName(entityClass);
        // 获取id名
        String idName = DataTableInfo.getIdName(entityClass);
        // 获取所有列名
        Map<String, String> columnNames = DataTableInfo.getColumnNames(entityClass);
        // 存放SQL所需参数
        Object[] params = new Object[columnNames.size()];
        // 获取所有属性
        Field[] fields = entityClass.getDeclaredFields();
        // 存放属性名与属性值，以方便取用
        Map<String,Object> mapFields = new HashMap<>();
        for (Field field:fields){
            field.setAccessible(true);
            try {
                mapFields.put(field.getName(),field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //===============构建SQL语句并加入参数====================//
        // example : update tableName set columnName=?,columnName=? where id=?
        StringBuilder sql = new StringBuilder("update "+tableName+" set ");
        // 遍历所有列名
        int index = 0;
        for (String columnName:columnNames.keySet()) {
            // 判断该列是否是id
            if (columnName.equals(idName)){
                // 列名中有一个是id名
                params[columnNames.size()-1]=mapFields.get(columnNames.get(columnName));
            }else {
                sql.append(columnName+"=?,");
                // 列名不是id名
                params[index++]=mapFields.get(columnNames.get(columnName));
            }
        }
        // 去除最后一个逗号
        sql.setCharAt(sql.length()-1,' ');
        // 连接id查询条件
        sql.append("where "+idName+"=?");
        // 执行SQL语句
        int update = updateEntity(sql.toString(),params);
        // 打印SQL语句
        log.info("Executed SQL ===> "+sql.toString());
        // 返回影响的行数
        return update;
    }


    @Override
    public <T> T selectById(Integer id, Class<T> entityClass){
        // 获取表名
        String tableName = DataTableInfo.getTableName(entityClass);
        // 获取id名
        String idName = DataTableInfo.getIdName(entityClass);
        // 获取所有列名
        Map<String, String> columnNames = DataTableInfo.getColumnNames(entityClass);

        //==============构建SQL语句======================//
        // example : select * from tableName where id=?
        StringBuilder sql = new StringBuilder("select * from "+tableName);
        sql.append(" where "+idName+"=?");
        //=============================================//
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
        String tableName = DataTableInfo.getTableName(entityClass);
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


    //==========================封装 SQL 语句 END=========================//


    @Override
    public long count(Class<?> entityClass){
        // 获取表名与id名
        String tableName = DataTableInfo.getTableName(entityClass);
        String idName = DataTableInfo.getIdName(entityClass);

        // 构建SQL
        String sql = "select count("+idName+") count from "+tableName;

        // 执行SQL
        List<Map<String, Object>> maps = statement.preparedSelectExecutor(sql);

        // 打印SQL语句
        log.info("Executed SQL ===> "+sql);
        // 获取数量
        if (maps.size()==0){
            return 0;
        }else {
             return (long) maps.get(0).get("count");
        }

    }

    //================排序和分页==============================//


    @Override
    public <T> List<T> selectAll(Class<T> entityClass, List<Sort> sorts){
        if (Objects.isNull(sorts)||sorts.isEmpty()){
            return selectAll(entityClass);
        }
        // 获取表名
        String tableName = DataTableInfo.getTableName(entityClass);
        // 获取属性名与列名的集合
        Map<String, String> fieldNameAndColumnName = DataTableInfo.getFieldNameAndColumnName(entityClass);
        //===============构建SQL语句======================//
        // select * from tableName order by column1 desc|asc,[column2 desc|asc]...
        StringBuilder sql = new StringBuilder("select * from "+tableName+" order by ");
        // 遍历排序条件
        for (Sort sort : sorts){
            // 遍历排序条件
            // 根据属性名获取列名
            String columnName = fieldNameAndColumnName.get(sort.getFieldName());
            // 判断是升序或降序
            if (Objects.equals(sort.getOrder(),Sort.DESC)){
                // 排序为降序
                sql.append(columnName+" desc,");
            }else {
                // 升序
                sql.append(columnName+" asc,");
            }

        }
        // 去掉最后一个逗号
        sql.setCharAt(sql.length()-1,' ');
        // 执行SQL语句
        List<T> result = selectAllEntity(sql.toString(), entityClass);
        // 打印SQL语句
        log.info("Executed SQL ===> "+sql.toString());
        return result;
    }


    @Override
    public <T> Page<T> selectAll(Class<T> entityClass, PageCondition condition){
        // 根据分页中的排序条件查询所有
        List<T> list = selectAll(entityClass, condition.getSort());

        // 对排序后的数据进行分页操作
        // 创建Page<T>对象，封装返回结果
        Page<T> page = new Page<>();
        int size = list.size();
        int pageSize = condition.getPageSize();
        int pageNumber = condition.getPageNumber();

        // 设置总记录数
        page.setTotalRecords(size);
        // 设置当前页码
        page.setCurrentPage(pageNumber);
        // 设置每页记录数
        page.setPageSize(pageSize);
        // 设置总页数(总记录数/每页记录数[+1])
        if(size%pageSize==0){
            page.setTotalPages(size/pageSize);
        }else {
            page.setTotalPages(size/pageSize+1);
        }

        // 判断页码是否非法
        if (pageNumber<0||pageNumber>=page.getTotalPages()){
            // 抛出页码非法异常
            throw new PageNumberIllegalException(
                    "the pageNumber parameter is error "+
                    "pageNumber : "+pageNumber);
        }

        // 取出分页的数据记录
        int fromIndex = pageNumber*pageSize;
        int toIndex = (pageNumber+1)*pageSize;
        // 判断是否是最后一页
        if (condition.getPageNumber()==page.getTotalPages()-1){
            // 最后一页
            page.setContents(list.subList(fromIndex,size));
        }else {
            // 非最后一页
            page.setContents(list.subList(fromIndex,toIndex));
        }
        // 返回分页结果
        return page;

    }




    //======================================================//


}
