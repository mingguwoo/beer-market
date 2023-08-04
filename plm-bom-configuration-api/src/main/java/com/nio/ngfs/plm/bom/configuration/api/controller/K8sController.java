package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author luke.zhu
 */
@Component
@RestControllerEndpoint(id = "k8s")
@Slf4j
@RequiredArgsConstructor
public class K8sController {

    private final EurekaClient eurekaClient;

    @GetMapping("/preStop")
    public void preStop() {
        try{
            //只有当服务注册到eureka上面的时候才graceful shutdown
            String status = eurekaClient.getInstanceRemoteStatus().toString();
            if(InstanceInfo.InstanceStatus.UP.toString().equals(status)){
                eurekaClient.shutdown();
            }else{
                log.warn("Eureka client is NOT up, please check the configuration...");
            }
        }catch (Exception e){
            log.error("Eureka down is failed, the message is {}",e.getMessage());
        }
        log.info("Stop eureka successfully.");
    }
}
