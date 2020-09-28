package org.kenewstar.jdbc.core.datasource;

/**
 * 配置数据库连接池的属性名称
 * @author kenewstar
 * @date 2020-08-12
 * @version 0.2
 */
public class ConnectionPoolPropertiesName {
    /**
     * c3p0属性名称
     */
    public static final String[] C3P0_PROP_NAME = new String[]{
            "acquireIncrement",
            "acquireRetryAttempts",
            "acquireRetryDelay",
            "autoCommitOnClose",
            "automaticTestTable",
            "breakAfterAcquireFailure",
            "checkoutTimeout",
            "connectionCustomizerClassName",
            "connectionTesterClassName",
            "contextClassLoaderSource",
            "dataSourceName",
            "debugUnreturnedConnectionStackTraces",
            "extensions",
            "factoryClassLocation",
            "forceIgnoreUnresolvedTransactions",
            "forceSynchronousCheckins",
            "forceUseNamedDriverClass",
            "idleConnectionTestPeriod",
            "initialPoolSize",
            "maxAdministrativeTaskTime",
            "maxConnectionAge",
            "maxIdleTime",
            "maxIdleTimeExcessConnections",
            "maxPoolSize",
            "maxStatements",
            "maxStatementsPerConnection",
            "minPoolSize",
            "numHelperThreads",
            "overrideDefaultUser",
            "overrideDefaultPassword",
            "preferredTestQuery",
            "privilegeSpawnedThreads",
            "propertyCycle",
            "statementCacheNumDeferredCloseThreads",
            "testConnectionOnCheckin",
            "testConnectionOnCheckout",
            "unreturnedConnectionTimeout",
            "description"
    };
    /**
     * dbcp2属性名称
     */
    public static final String[] DBCP2_PROP_NAME = new String[]{
            "defaultAutoCommit",
            "defaultReadOnly",
            "defaultTransactionIsolation",
            "defaultCatalog",
            "cacheState",
            "defaultQueryTimeout",
            "enableAutoCommitOnReturn",
            "rollbackOnReturn",
            "initialSize",
            "maxTotal",
            "maxIdle",
            "minIdle",
            "maxWaitMillis",
            "validationQuery",
            "validationQueryTimeout",
            "testOnCreate",
            "testOnBorrow",
            "testOnReturn",
            "testWhileIdle",
            "timeBetweenEvictionRunsMillis",
            "numTestsPerEvictionRun",
            "minEvictableIdleTimeMillis",
            "softMinEvictableIdleTimeMillis",
            "maxConnLifetimeMillis",
            "connectionInitSqls",
            "lifo",
            "poolPreparedStatements",
            "maxOpenPreparedStatements",
            "accessToUnderlyingConnectionAllowed",
            "removeAbandonedOnMaintenance",
            "removeAbandonedOnBorrow",
            "removeAbandonedTimeout",
            "logAbandoned",
            "abandonedUsageTracking",
            "jmxName"
    };
    /**
     * druid属性名称
     */
    public static final String[] DRUID_PROP_NAME = new String[]{
            "asyncInit",
            "maxActive",
            "maxIdle",
            "minIdle",
            "initialSize",
            "maxWait",
            "removeAbandoned",
            "removeAbandonedTimeout",
            "minEvictableIdleTimeMillis",
            "maxEvictableIdleTimeMillis",
            "timeBetweenEvictionRunsMillis",
            "useUnfairLock",
            "testWhileIdle",
            "testOnBorrow",
            "testOnReturn",
            "validationQuery",
            "validationQueryTimeout",
            "poolPreparedStatements",
            "maxOpenPreparedStatements",
            "connectionInitSqls",
            "exceptionSorter",
            "filters",
            "proxyFilters",
            "keepAlive",
            "phyMaxUseCount"
    };






}
