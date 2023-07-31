package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.netflix.appinfo.InstanceInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
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

    private final EurekaServiceRegistry eurekaServiceRegistry;
    private final EurekaRegistration eurekaRegistration;

    @GetMapping("/preStop")
    public void preStop() {
        try {
            //只有当服务注册到eureka上面的时候才graceful shutdown
            String status = eurekaRegistration.getEurekaClient().getInstanceRemoteStatus().toString();
            if (InstanceInfo.InstanceStatus.UP.toString().equals(status)) {
                eurekaServiceRegistry.setStatus(eurekaRegistration, InstanceInfo.InstanceStatus.DOWN.toString());
                //触发本地状态更新
                eurekaServiceRegistry.deregister(eurekaRegistration);
            } else {
                log.warn("eureka has not been registered,pls check the config");
            }
        } catch (Exception e) {
            log.error("eureka down failed,msg is {}", e.getMessage());
        }
    }
}
