package com.nio.ngfs.plm.bom.configuration.infrastructure.config.interceptor;

import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangchao.wang
 */
@Component
public class BrandInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        String brand = request.getHeader("lesseecode");
        if(StringUtils.isBlank(brand)){
            brand = ConfigConstants.NIO;
        }
        ConfigConstants.brandName.set(brand);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ConfigConstants.brandName.remove();
    }
}
