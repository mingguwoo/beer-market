package com.nio.ngfs.plm.bom.configuration.infrastructure.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author luke.zhu
 * @date 01/30/2023
 */

@Configuration
@Slf4j
@EnableApolloConfig
@RequiredArgsConstructor
public class ApolloConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @ApolloConfigChangeListener(interestedKeyPrefixes = {"warn.log.","common.pool.","dboption.pool.","redisson.cluster."})
    public void onChange(ConfigChangeEvent configChangeEvent){
        log.info("start refresh properties ");
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(configChangeEvent.changedKeys()));
        log.info("stop refresh properties");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
