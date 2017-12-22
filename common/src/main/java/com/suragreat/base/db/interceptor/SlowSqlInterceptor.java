package com.suragreat.base.db.interceptor;

import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts(value = {
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }) })
public class SlowSqlInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(SlowSqlInterceptor.class);
    private long maxWaitingMills;
    public SlowSqlInterceptor(long maxWaitingMills){
        this.maxWaitingMills = maxWaitingMills;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnValue;

        long start = System.currentTimeMillis();
        try {
            returnValue = invocation.proceed();
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            if (time >= maxWaitingMills) {
                MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
                Object args = invocation.getArgs()[1];
                String sqlId = mappedStatement.getId();

                StringBuilder str = new StringBuilder(100);
                str.append("执行SQL:");
                str.append(sqlId);
                str.append(", 参数: ");
                str.append(args.toString());
                str.append("时间过长, 花费: ");
                str.append(time);
                str.append(" ms.");
                String sqlInfo = str.toString();
                logger.warn(sqlInfo);
            }
        }
        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
