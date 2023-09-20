package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.google.common.collect.ImmutableMap;
import com.nio.bom.share.annotation.NotLogResult;
import com.nio.ngfs.common.utils.SpringContextHolder;
import org.dromara.dynamictp.core.thread.DtpExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaozhou.tu
 * @date 2023/9/20
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/getDtpExecutor")
    @NotLogResult
    public Object getDtpExecutor(@RequestParam("beanName") String beanName) {
        DtpExecutor executor = (DtpExecutor) SpringContextHolder.getApplicationContext().getBean(beanName);
        return ImmutableMap.of(
                "threadPoolName", executor.getThreadPoolName(),
                "queueCapacity", executor.getQueueCapacity(),
                "queueSize", executor.getQueueSize(),
                "corePoolSize", executor.getCorePoolSize(),
                "maximumPoolSize", executor.getMaximumPoolSize(),
                "rejectedTaskCount", executor.getRejectedTaskCount(),
                "activeCount", executor.getActiveCount(),
                "taskCount", executor.getTaskCount(),
                "completedTaskCount", executor.getCompletedTaskCount()
        );
    }

}
