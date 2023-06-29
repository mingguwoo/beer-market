package com.nio.ngfs.plm.bom.configuration.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author guangjin.gao
 */
@FeignClient(url = "${warn.log.url}", name = "feishuNotice")
public interface FeishuIntegrationClient {

    /**
     * 飞书消息
     *
     * @param message message
     */
    @PostMapping()
    void sendMessageToFeishu(@RequestBody String message);

}
