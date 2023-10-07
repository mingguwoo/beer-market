package com.nio.ngfs.plm.bom.configuration.infrastructure.config.interceptor;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangchao.wang
 */
@Component
@Slf4j
public class BrandInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {


        String brand = request.getHeader("lesseecode");

        if(StringUtils.isBlank(brand)){
            brand = CommonConstants.NIO;
        }
        ConfigConstants.brandName.set(brand);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ConfigConstants.brandName.remove();
    }
}
