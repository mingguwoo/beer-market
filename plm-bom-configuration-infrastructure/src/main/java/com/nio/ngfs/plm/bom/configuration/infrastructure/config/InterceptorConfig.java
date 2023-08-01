package com.nio.ngfs.plm.bom.configuration.infrastructure.config;


import com.nio.ngfs.plm.bom.configuration.infrastructure.config.interceptor.BrandInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BrandInterceptor());
    }
}
