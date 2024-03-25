package com.sh.beer.market.test;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author
 * @date 2023/7/11
 */
//@EnableDynamicTp
@EnableFeignClients(basePackages = "com.sh.beer.market")
@EnableDiscoveryClient
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
//@OpenAPIDefinition(servers = {@Server(url = "/", description = "BOM Configuration application default URL")})
@SpringBootApplication(scanBasePackages = {"com.sh.beer.market", "org.springdoc.webmvc.ui"})
public class BeerMarketTestApplication {
}
