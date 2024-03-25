package com.sh.beer.market.infrastructure.config.advice;


import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author
 */
@RestControllerAdvice
public class RestReturnAdvice implements ResponseBodyAdvice<Object> {

    private final static String PROMETHEUS="prometheus";
    /**
     * 判断是否需要处理
     * 处理spring doc 异常的问题
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getDeclaringClass().getName().contains("springdoc");
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //如果是prometheus则不做处理
        /*if (request.getURI().getPath().contains(PROMETHEUS)){
            return body;
        }
        //如果已经实现了ResultInfo封装结果返回则不处理
        if (Objects.nonNull(body) && (body instanceof ResultInfo || body instanceof RepresentationModel<?>)) {
            return body;
        }
        //处理string 返回值
        if (body instanceof String) {
            return JSON.toJSONString(ResultInfo.success(body));
        }
        //处理未封装ResultInfo的结果
        return ResultInfo.success(body);*/
        return null;
    }
}
