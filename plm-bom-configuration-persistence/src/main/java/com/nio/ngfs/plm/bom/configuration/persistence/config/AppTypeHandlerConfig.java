package com.nio.ngfs.plm.bom.configuration.persistence.config;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 处理枚举到 mybatis po 的映射关系
 *
 * @author ngfs codegen
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Configuration
public class AppTypeHandlerConfig {

    private final List<SqlSessionFactory> sqlSessionFactoryList;

    @PostConstruct
    public void registerHandlers() {
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            TypeHandlerRegistry registry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();

            //registry.register(JsonObj.class, JacksonTypeHandler.class);
            //registry.register(DictTypeEnum.class, StringEnumTypeHandler.class);
        }
    }
}
