package com.gm.javaeaseframe.core.thirty.mybatis;

import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;

/**
 * mybatis分页插件
 * 缺点：插件拦截目标为StatementHandler，而在同一个SqlSession中，在StatementHandler.prepare之前，MyBatis的已经命中了一级缓存，所以直接返回了缓存中的内容。导致只有分页参数不同的方法在短时间内使用不同分页参数查询出来的结果相同
 * 解决：重写自定义MyBatis分页插件使之拦截Executor，或增加新的插件，使之拦截Executor清除一级缓存。（参见PageLocalCacheDisableInterceptor）
 * @author	GM
 * @date	2021年12月24日
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class CustomPagePlugin implements Interceptor {

    private final static Log log = LogFactory.getLog(CustomPagePlugin.class);
    private String dialect = null;
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY_FACTORY = new DefaultReflectorFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY_FACTORY);
        RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
        if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
            return invocation.proceed();
        }
        String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        if ("mysql".equalsIgnoreCase(dialect)) {
            originalSql = originalSql + " limit " + rowBounds.getOffset() + "," + rowBounds.getLimit();
        } else if ("oracle".equalsIgnoreCase(dialect)) {
        } else if ("sybase".equalsIgnoreCase(dialect)) {
        } else if ("sqlserver".equalsIgnoreCase(dialect)) {

        }

        metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
        metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
        metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);

        if (log.isDebugEnabled()) {
            log.debug("生成分页SQL : " + boundSql.getSql());
        }

        return invocation.proceed();

    }

    @Override
    public Object plugin(Object target) {

        return Plugin.wrap(target, this);

    }

    @Override
    public void setProperties(Properties prop) {
        dialect = prop.getProperty("dialect");
    }

}
