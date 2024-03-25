package com.sh.beer.market.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 * @date 2023/9/20
 */
@Configuration
@RequiredArgsConstructor
public class ThreadPoolConfig {

    /*private final EventPoolProperties eventPoolProperties;

    @Bean("eventExecutor")
    public ThreadPoolTaskExecutor eventExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(eventPoolProperties.getCoreSize());
        taskExecutor.setMaxPoolSize(eventPoolProperties.getMaxCoreSize());
        taskExecutor.setQueueCapacity(eventPoolProperties.getQueueCapacity());
        taskExecutor.setKeepAliveSeconds(eventPoolProperties.getKeepAliveSeconds());
        taskExecutor.setThreadNamePrefix(eventPoolProperties.getNamePrefix());
        taskExecutor.setAwaitTerminationSeconds(10 * 1000);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }*/

}
