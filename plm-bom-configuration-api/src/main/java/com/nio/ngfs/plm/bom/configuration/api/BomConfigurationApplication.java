package com.nio.ngfs.plm.bom.configuration.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.burningwave.core.assembler.StaticComponentContainer;
import org.dromara.dynamictp.core.spring.EnableDynamicTp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author luke.zhu
 */
@EnableDynamicTp
@EnableFeignClients(basePackages = "com.nio.ngfs")
@EnableDiscoveryClient
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@OpenAPIDefinition(servers = {@Server(url = "/", description = "BOM Configuration application default URL")})
@SpringBootApplication(scanBasePackages = {"com.nio.ngfs", "org.springdoc.webmvc.ui"})
public class BomConfigurationApplication {

    public static void main(String[] args) {
        // 解决jdk17使用cglib失败的问题
        StaticComponentContainer.Modules.exportAllToAll();
        SpringApplication.run(BomConfigurationApplication.class, args);
    }

}
