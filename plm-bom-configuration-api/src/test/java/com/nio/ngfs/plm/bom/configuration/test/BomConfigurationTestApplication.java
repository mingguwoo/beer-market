package com.nio.ngfs.plm.bom.configuration.test;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.dromara.dynamictp.core.spring.EnableDynamicTp;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author xiaozhou.tu
 * @date 2023/7/11
 */
@EnableDynamicTp
@EnableFeignClients(basePackages = "com.nio.ngfs")
@EnableDiscoveryClient
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@OpenAPIDefinition(servers = {@Server(url = "/", description = "BOM Configuration application default URL")})
@SpringBootApplication(scanBasePackages = {"com.nio.ngfs", "org.springdoc.webmvc.ui"})
public class BomConfigurationTestApplication {
}
