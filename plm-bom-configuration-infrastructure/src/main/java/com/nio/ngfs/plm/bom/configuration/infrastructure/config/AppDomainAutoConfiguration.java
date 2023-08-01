package com.nio.ngfs.plm.bom.configuration.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ngfs codegen
 * @since 1.0.0
 */
@Configuration
@Import({
        AppMybatisConfig.class,
        AppTypeHandlerConfig.class
})
@EnableTransactionManagement
public class AppDomainAutoConfiguration {
}
