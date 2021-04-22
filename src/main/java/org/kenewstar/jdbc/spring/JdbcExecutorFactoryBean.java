package org.kenewstar.jdbc.spring;

import org.kenewstar.jdbc.core.JdbcExecutor;
import org.kenewstar.jdbc.core.factory.JdbcExecutorFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author xinke.huang@hand-china.com
 * @version 1.0
 * @date 2021/2/21
 */
public class JdbcExecutorFactoryBean implements FactoryBean<JdbcExecutor> {

    @Override
    public JdbcExecutor getObject() throws Exception {
        return JdbcExecutorFactory.getExecutor();
    }

    @Override
    public Class<?> getObjectType() {
        return JdbcExecutor.class;
    }
}
