# Kenewstar-jdbc
***Kenewstar-jdbc*** 框架是对 ***JDBC*** 进行封装的一个 ***ORM*** 框架

打包命令：***mvn clean install -Dmaven.test.skip***

## kenewstar-jdbc-0.1
date : 2020-08-08
##### 1) 封装基本CURD操作

## kenewstar-jdbc-0.2
date : 2020-09-28
##### 1) 集成数据库连接池

## kenewstar-jdbc-0.3
date : 2021-04-05
##### 1) 新增kns连接池
##### 2) 新增条件构造执行器
> * 多表条件构造查询
> * 单表条件构造查询

## kenewstar-jdbc-0.4
date : 2021-04-17
##### 1) 新增批量执行操作
##### 2) 新增分页查询操作


# 声明式事务
如何在Spring框架中使用kenewstar-jdbc进行声明式事务
必须结合SpringAOP使用,kenewstar-jdbc的声明式事务是基于SpringAOP
而实现的,需引入如下jar
```xml
<dependencys>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.2.12.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>1.9.6</version>
    </dependency>
</dependencys>
```
配置jdbcExecutor交给Spring管理
```java
/**
 * 配置jdbcExecutor交给spring容器管理
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/17
 */
@Configuration
public class BeanConfig {

    @Bean
    public JdbcExecutor jdbcExecutor() {
        return JdbcExecutorFactory.getExecutor();
    }

}
```
配置事务管理切面
```java
package org.kenewstar.muke.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.kenewstar.jdbc.annotation.JdbcTransaction;
import org.kenewstar.jdbc.core.JdbcExecutor;
import org.kenewstar.jdbc.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * kenewstar-jdbc 声明式事物
 * @author kenewstar
 * @version 1.0
 * @date 2021/4/17
 */
@Component
@EnableAspectJAutoProxy
@Aspect
public class TransactionAspect {
    /**
     * 获取日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(TransactionAspect.class);

    @Resource
    private JdbcExecutor jdbcExecutor;

    @Pointcut("execution(* org.kenewstar.muke.service.*.*(..))")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object transactionAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取事物注解
        JdbcTransaction jdbcTransaction = method.getAnnotation(JdbcTransaction.class);
        if (Objects.isNull(jdbcTransaction)) {
            return joinPoint.proceed();
        }
        // 获取事物
        Transaction transaction = jdbcExecutor.getTransaction();
        // 开启事物
        transaction.begin();
        logger.info("start jdbcTransaction......");
        try {
            proceed = joinPoint.proceed();
            // 提交事物
            transaction.commit();
            logger.info("commit jdbcTransaction......");
        } catch (Throwable e) {
            // 事物回滚
            transaction.rollBack();
            logger.info("rollback jdbcTransaction......");
            throw new RuntimeException(e);
        }
        // 返回结果
        return proceed;
    }
}

```

使用声明式事务进行编程
```java
@Service 
public class UserService {
    @Resource
    private JdbcExecutor jdbcExecutor;

    @Override
    @JdbcTransaction
    public int saveUser(MicroUser user) {
        jdbcExecutor.insertSelective(user);
        jdbcExecutor.insertSelective(user);
        return 1;
    }
} 
```