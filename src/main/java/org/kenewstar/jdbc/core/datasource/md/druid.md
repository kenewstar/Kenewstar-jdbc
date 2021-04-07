1.1 maxActive :连接池支持的最大连接数。一般取值20就可以了，一般把maxActive设置成可能的并发量就行了设 0 为没有限制。

1.2 maxIdle : 连接池中最多可空闲maxIdle个连接 ，这里取值为20，表示即使没有数据库连接时依然可以保持20空闲的连接，而不被清除，随时处于待命状态。设 0 为没有限制。已经不再使用，配置了也没效果

1.3 minIdle: 连接池中最小空闲连接数，当连接数少于此值时，连接池会创建连接来补充到该值的数量

1.4 initialSize: 初始化连接数目

1.5 maxWait: 连接池中连接用完时,新的请求等待时间,毫秒，这里取值-1，表示无限等待，直到超时为止，也可取值9000，表示9秒后超时。超过时间会出错误信息

1.6 removeAbandoned: 是否清除已经超过“removeAbandonedTimout”设置的无效连接。如果值为“true”则超过“removeAbandonedTimout”设置的无效连接将会被清除。设置此属性可以从那些没有合适关闭连接的程序中恢复数据库的连接。

1.7 removeAbandonedTimeout: 活动连接的最大空闲时间,单位为秒 超过此时间的连接会被释放到连接池中,针对未被close的活动连接

1.8 minEvictableIdleTimeMillis: 连接池中连接可空闲的时间,单位为毫秒 针对连接池中的连接对象

1.9 timeBetweenEvictionRunsMillis / minEvictableIdleTimeMillis: 每timeBetweenEvictionRunsMillis毫秒检查一次连接池中空闲的连接,把空闲时间超过minEvictableIdleTimeMillis毫秒的连接断开,直到连接池中的连接数到minIdle为止

1.10 useUnfairLock: 是否启用非公平锁

1.11 testWhileIdle: 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。

1.12 testOnBorrow: 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能

1.13 testOnReturn: 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能

1.14 validationQuery: 在连接池返回连接给调用者前用来对连接进行验证的查询 SQL，要求为一条查询语句

1.15 validationQueryTimeout: SQL 查询验证超时时间（秒），小于或等于 0 的数值表示禁用

1.16 poolPreparedStatements: 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭

1.17 maxOpenPreparedStatements: 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100