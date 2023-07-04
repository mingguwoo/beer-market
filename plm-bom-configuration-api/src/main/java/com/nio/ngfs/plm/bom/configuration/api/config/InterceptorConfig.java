package com.nio.ngfs.plm.bom.configuration.api.config;

import com.nio.ngfs.plm.bom.configuration.api.config.interceptor.BrandInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author luke.zhu
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BrandInterceptor());
    }
}
