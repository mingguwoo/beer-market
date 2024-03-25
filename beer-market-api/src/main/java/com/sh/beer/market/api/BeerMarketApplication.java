package com.sh.beer.market.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author
 */
//@EnableDynamicTp
//@EnableFeignClients(basePackages = {"com.sh.beer.market", "com.sh.beer.market.api"})
//@EnableDiscoveryClient
//@EnableAsync
//@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
//@OpenAPIDefinition(servers = {@Server(url = "/", description = "BOM Configuration application default URL")})
//@SpringBootApplication(scanBasePackages = {"com.sh.beer.market", "org.springdoc.webmvc.ui"})
//@EnableCatClient
@SpringBootApplication
public class BeerMarketApplication {

    public static void main(String[] args) {
        // 解决jdk17使用cglib失败的问题
//        StaticComponentContainer.Modules.exportAllToAll();
        SpringApplication.run(BeerMarketApplication.class, args);
    }

}
