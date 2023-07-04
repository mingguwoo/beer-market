package com.nio.ngfs.plm.bom.configuration.infrastructure.config;

import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.po.BasePO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * 自动填充 create_time / update_time
 *
 * @author ngfs codegen
 * @since 1.0.0
 */
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AutoFillInterceptor implements Interceptor {

    private static final String METHOD_NAME = "update";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        var mappedStatement = (MappedStatement) invocation.getArgs()[0];
        var methodName = invocation.getMethod().getName();
        var sqlCommandType = mappedStatement.getSqlCommandType();
        if (methodName.equals(METHOD_NAME)) {
            var args = invocation.getArgs();
            if (args.length < 2) {
                return invocation.proceed();
            }
            var object = invocation.getArgs()[1];
            if (object == null) {
                return invocation.proceed();
            }
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                insertInterceptor(object);
            } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                updateInterceptor(object);
            }
        }
        return invocation.proceed();
    }

    private void insertInterceptor(Object object) {
        var now = new Date();
        if (object instanceof BasePO basePO) {
            fillTimeForInsert(basePO, now);
        } else if (object instanceof List) {
            var objectList = (List<Object>) object;
            for (Object entity : objectList) {
                if (entity instanceof BasePO basePO) {
                    fillTimeForInsert(basePO, now);
                }
            }
        }
    }

    private void fillTimeForInsert(BasePO object, Date now) {
        if (Objects.isNull(object.getCreateTime())) {
            object.setCreateTime(now);
            object.setUpdateTime(now);
        } else if (Objects.isNull(object.getUpdateTime())) {
            object.setUpdateTime(now);
        }

        if (StringUtils.isBlank(object.getUpdateUser())) {
            object.setUpdateUser(object.getCreateUser());
        }
    }

    private void updateInterceptor(Object object) {
        Date now = new Date();
        if (object instanceof BasePO basePO) {
            fillTimeForUpdate(basePO, now);
        } else if (object instanceof List) {
            List<Object> objectList = (List<Object>) object;
            for (Object entity : objectList) {
                if (entity instanceof BasePO basePO) {
                    fillTimeForUpdate(basePO, now);
                }
            }
        }
    }

    private void fillTimeForUpdate(BasePO object, Date now) {
        if (Objects.isNull(object.getUpdateTime())) {
            object.setUpdateTime(now);
        }
    }


    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // do nothing
    }
}