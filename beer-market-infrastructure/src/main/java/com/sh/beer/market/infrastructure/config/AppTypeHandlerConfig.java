package com.sh.beer.market.infrastructure.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * 处理枚举到 mybatis po 的映射关系
 *
 * @author
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Configuration
public class AppTypeHandlerConfig {

    /*private final List<SqlSessionFactory> sqlSessionFactoryList;

    @PostConstruct
    public void registerHandlers() {
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            TypeHandlerRegistry registry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();

            //registry.register(JsonObj.class, JacksonTypeHandler.class);
            //registry.register(DictTypeEnum.class, StringEnumTypeHandler.class);
        }
    }*/
}
