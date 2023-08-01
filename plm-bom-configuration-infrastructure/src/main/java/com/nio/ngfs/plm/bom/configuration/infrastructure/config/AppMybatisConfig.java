package com.nio.ngfs.plm.bom.configuration.infrastructure.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author ngfs codegen
 * @since 1.0.0
 */
@Configuration
@MapperScan({"com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper"})
@PropertySource(value = {"classpath:app-infrastructure.properties"})
@Slf4j
public class AppMybatisConfig {

    /**
     * mybatis 全局设置
     */
    @Bean
    @ConditionalOnMissingBean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            log.info("load commons mybatis config customizer");
            configuration.setMapUnderscoreToCamelCase(true); //下划线转驼峰
            configuration.setUseGeneratedKeys(true); //使用jdbc的getGeneratedKeys获取数据库自增主键值
        };
    }
}
