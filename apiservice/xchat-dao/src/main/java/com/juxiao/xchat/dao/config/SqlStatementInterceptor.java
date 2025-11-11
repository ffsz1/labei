package com.juxiao.xchat.dao.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;
import java.util.Properties;


@Slf4j
@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class SqlStatementInterceptor implements Interceptor {

    private final Logger daoslow = LoggerFactory.getLogger("com.juxiao.xchat.daoslow");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } catch (Throwable throwable) {
            this.error(invocation);
            throw throwable;
        } finally {
            long time = System.currentTimeMillis() - startTime;
            this.log(invocation, time);
        }
    }

    @Override
    public Object plugin(Object object) {
        return Plugin.wrap(object, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private void error(Invocation invocation) {
        try {
            String sql = this.sql(invocation);
            ErrorContext.instance().sql(sql);
        } catch (Exception e) {
            log.error("[ SQL ] 异常：", e);
        }
    }

    /**
     * 输出日志
     *
     * @param invocation
     * @param time
     */
    private void log(Invocation invocation, long time) {
        try {
            String sql = sql(invocation);
            log.info("{},耗时:>{}ms", sql, time);
            if (time > 200) {
                daoslow.info("{},耗时:>{}ms", sql, time);
            }
        } catch (Exception e) {
            log.error("[ SQL ] 异常：", e);
        }
    }

    private String sql(Invocation invocation) {
        BoundSql boundSql;
        String paramter;
        if (invocation.getTarget() instanceof StatementHandler) {
            StatementHandler handler = (StatementHandler) invocation.getTarget();
            ParameterHandler parameterHandler = handler.getParameterHandler();
            boundSql = handler.getBoundSql();
            paramter = (parameterHandler == null || parameterHandler.getParameterObject() == null) ? "" : JSON.toJSONString(parameterHandler.getParameterObject());
        } else if (invocation.getArgs()[0] instanceof MappedStatement) {
            MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
            boundSql = statement.getBoundSql(invocation.getArgs()[1]);
            paramter = (invocation.getArgs()[1] == null) ? "" : JSON.toJSONString(invocation.getArgs()[1]);
        } else {
            return null;
        }

//        String sql = boundSql == null || StringUtils.isBlank(boundSql.getSql()) ? "" : boundSql.getSql().replace(" ", "").replace("\n", "").replace("\t", "").replaceAll("\\s+", " ");
        String sql = boundSql == null || StringUtils.isBlank(boundSql.getSql()) ? "" : boundSql.getSql().replace("\n", "").replace("\t", "").replaceAll("\\s+", " ");
        return new StringBuilder(sql).append(":>").append(paramter).toString();
    }
}
