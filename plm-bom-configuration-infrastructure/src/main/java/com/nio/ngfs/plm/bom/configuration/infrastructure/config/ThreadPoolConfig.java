package com.nio.ngfs.plm.bom.configuration.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xiaozhou.tu
 * @date 2023/7/17
 */
@Configuration
@RequiredArgsConstructor
public class ThreadPoolConfig {

    /**
     * 异步事件线程池
     */
    @Bean("asyncEventExecutor")
    public ThreadPoolTaskExecutor asyncEventExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setKeepAliveSeconds(60);
        executor.setQueueCapacity(200);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(10);
        executor.setThreadNamePrefix("async-event-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
