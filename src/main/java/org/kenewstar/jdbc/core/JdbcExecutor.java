package org.kenewstar.jdbc.core;

import org.kenewstar.jdbc.transaction.Transaction;

import java.util.List;

/**
 * Jdbc执行器顶级接口，声明所有操作数据库的方法
 * @author kenewstar
 * @date 2020-08-08
 * @version 0.2
 */
public interface JdbcExecutor {
    /**
     * 获取事务
     * @return 返回事务
     */
    Transaction getTransaction();

    /**
     * 更新操作
     * @param sql 执行语句
     * @param args 执行参数
     * @return 返回影响行数
     */
    int updateEntity(String sql,Object...args);
    /**
     * 插入操作
     * @param sql 执行语句
     * @param args 执行参数
     * @return 返回影响行数
     */
    int insertEntity(String sql,Object...args);
    /**
     * 删除操作
     * @param sql 执行语句
     * @param args 执行参数
     * @return 返回影响行数
     */
    int deleteEntity(String sql, Object... args);
    /**
     * 根据id查询单个实体
     * @param sql sql语句
     * @param entityClass 实体类类型
     * @param id id参数
     * @param <T>
     * @return 返回查询结果对象
     */
    <T> T selectEntityById(String sql, Class<T> entityClass, Integer id);
    /**
     * 查询所有
     * @param sql 查询所有的SQL语句
     * @param entityClass 类
     * @param <T> 泛型
     * @return 返回查询结果集合
     */
    <T> List<T> selectAllEntity(String sql, Class<T> entityClass);
    /**
     * 根据列名查询数据
     * @param sql SQL查询语句
     * @param entityClass 类
     * @param args 查询列的参数
     * @param <T> 泛型
     * @return 返回查询结果集合
     */
    <T> List<T> selectListByColumns(String sql, Class<T> entityClass, Object... args);

    //========================封装 SQL 语句 START===============================//

    /**
     * 将一个对象数据插入到数据库中
     * @param obj 插入数据表中的对象参数
     * @return 返回影响的行数
     */
    int insert(Object obj);

    /**
     * 根据id进行删除数据表记录
     * @param id 参数id
     * @param entityClass 需要删除的数据记录对应的类
     * @return 返回影响行数
     */
    int deleteById(Integer id, Class<?> entityClass);

    /**
     * 根据id更新数据表记录
     * @param obj 更新的对象参数
     * @return 返回影响的行数
     */
    int updateById(Object obj);

    /**
     * 根据Id查询数据记录
     * @param id id参数
     * @param entityClass 类类型
     * @param <T> 泛型参数
     * @return 返回实体对象
     */
    <T> T selectById(Integer id, Class<T> entityClass);

    /**
     * 查询所有
     * @param entityClass 类类型
     * @param <T> 泛型
     * @return 返回查询结果集合
     */
    <T> List<T> selectAll(Class<T> entityClass);


    //========================封装 SQL 语句 END=================================//

    /**
     * 统计该实体类在数据库中对应表的记录条数
     * @param entityClass 类
     * @return 返回数据记录数
     */
    long count(Class<?> entityClass);


    //================排序和分页 START=============================//

    /**
     * 多条件排序查询
     * 查询条件封装在list中，先根据第一个条件查询，
     * 在第一个条件成立的情况下再进行第二个条件排序查询，
     * 依次往后执行，SQL语句如下示例
     * SQL ： select * from tableName order by column1 desc|asc,[column2 desc|asc]...
     * @param entityClass 查询数据表对应的类
     * @param sorts 排序条件集合
     * @param <T> 泛型
     * @return 返回查询结果集合
     */
    <T> List<T> selectAll(Class<T> entityClass, List<Sort> sorts);


    /**
     * 分页查询(可以先排序再分页) 即将排序的结果进行分页操作
     * @param entityClass 类
     * @param condition 分页查询条件
     * @param <T> 泛型
     * @return 返回分页查询结果
     */
    <T> Page<T> selectAll(Class<T> entityClass, PageCondition condition);

    //================排序和分页 END==============================//




}
