package com.nio.ngfs.plm.bom.configuration.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author xiaozhou.tu
 * @date 2023/9/20
 */
@Configuration
public class ThreadPoolConfig {

    @Bean("eventExecutor")
    public ThreadPoolTaskExecutor eventExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(200);
        taskExecutor.setKeepAliveSeconds(60 * 1000);
        taskExecutor.setThreadNamePrefix("event-executor-");
        taskExecutor.setAwaitTerminationSeconds(10 * 1000);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.initialize();
        return taskExecutor;
    }

}
