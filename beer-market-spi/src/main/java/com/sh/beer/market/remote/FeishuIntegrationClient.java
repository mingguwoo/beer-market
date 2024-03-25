package com.sh.beer.market.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author
 */
//@FeignClient(url = "https://open.feishu.cn/", name = "feishuNotice")
public interface FeishuIntegrationClient {

    /**
     * 发送飞书消息到日志告警群
     *
     * @param message message
     *//*
    @PostMapping("${warn.log.url}")
    void sendMessageToLogGroup(@RequestBody String message);

    *//**
     * 发送飞书消息到3DE告警群
     *
     * @param message message
     *//*
    @PostMapping("${warn.3de.url}")
    void sendMessageTo3deGroup(@RequestBody String message);*/

}
