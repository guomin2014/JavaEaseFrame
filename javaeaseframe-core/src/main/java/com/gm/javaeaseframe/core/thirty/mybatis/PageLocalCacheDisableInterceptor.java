package com.gm.javaeaseframe.core.thirty.mybatis;

import java.lang.reflect.Field;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ReuseExecutor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 通过将ms的flushCacheRequired设置为true来强行清除缓存。增加一个拦截器拦截Executor.query，利用反射强改flushCacheRequired属性（详见：MortalsPagePlugin）
 * 注意，这里只能拦截4参数的query方法而不能拦截到6参数的，由于6参数的query方法是通过内部调用的，无法被动态代理
 * @author	GM
 * @date	2021年12月24日
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PageLocalCacheDisableInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ReuseExecutor executor = (ReuseExecutor)invocation.getTarget();
        Object[] args = invocation.getArgs();
        RowBounds rowBounds = (RowBounds)args[2];
        if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
            return invocation.proceed();
        }
//        System.out.println(rowBounds.getOffset() + "-->" + rowBounds.getLimit());
        MappedStatement ms = (MappedStatement) args[0];
        Class<?> clazz = ms.getClass();
        Field flushLocalCache = clazz.getDeclaredField("flushCacheRequired");
        flushLocalCache.setAccessible(true);
        flushLocalCache.set(ms, true);//强制清除一级缓存
        executor.doFlushStatements(false);//清除缓存的预处理SQL
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        
    }

}
