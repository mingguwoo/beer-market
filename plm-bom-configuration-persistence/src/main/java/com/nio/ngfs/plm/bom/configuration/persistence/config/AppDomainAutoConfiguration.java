package com.nio.ngfs.plm.bom.configuration.persistence.config;

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
        AppTypeHandlerConfig.class,
        AutoFillInterceptor.class,
})
@EnableTransactionManagement
public class AppDomainAutoConfiguration {
}
