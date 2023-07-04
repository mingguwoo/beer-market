package com.nio.ngfs.plm.bom.configuration.api.config.interceptor;

import com.nio.bom.share.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author luke.zhu
 */
@Component
public class BrandInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String brand = request.getHeader("lesseecode");
        if(StringUtils.isBlank(brand)){
            brand = CommonConstants.NIO;
        }
        CommonConstants.brandName.set(brand);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        CommonConstants.brandName.remove();
    }
}
