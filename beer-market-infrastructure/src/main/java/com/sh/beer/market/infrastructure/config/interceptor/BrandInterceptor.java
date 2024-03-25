package com.sh.beer.market.infrastructure.config.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author
 */
@Component
@Slf4j
public class BrandInterceptor implements HandlerInterceptor {


    /*@Override
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
    }*/
}
