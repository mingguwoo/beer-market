package com.nio.ngfs.plm.bom.configuration.api.config;

import com.nio.ngfs.common.exception.BusinessSilentException;
import com.nio.ngfs.common.model.BaseResponse;
import com.nio.ngfs.plm.bom.configuration.common.enums.ErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @author ngfs generated
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class AppExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public BaseResponse<Void> businessExceptionHandler(HttpServletRequest servletRequest, BusinessException e) {
        log.error(String.format("request method [%s] [%s] business exception: [%s]", servletRequest.getMethod(), servletRequest.getRequestURI(), e.toString()), e);
        return new BaseResponse<>(null, e.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse<Void> methodArgumentNotValidExceptionHandler(HttpServletRequest servletRequest, MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        log.error("request method [{}] [{}] MethodArgumentNotValidException: [{}]", servletRequest.getMethod(), servletRequest.getRequestURI(), e.toString());
        return new BaseResponse<>(null, ErrorCode.PARAMETER_ERROR.getCode(), StringUtils.defaultString(message, ErrorCode.PARAMETER_ERROR.getMessage()), null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse<Void> constraintViolationExceptionHandler(HttpServletRequest servletRequest, ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        log.error("request method [{}] [{}] ConstraintViolationException: [{}]", servletRequest.getMethod(), servletRequest.getRequestURI(), e.toString());
        return new BaseResponse<>(null, ErrorCode.PARAMETER_ERROR.getCode(), StringUtils.defaultString(message, ErrorCode.PARAMETER_ERROR.getMessage()), null);
    }

    @ExceptionHandler(value = BusinessSilentException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse<Void> businessSilentExceptionHandler(HttpServletRequest servletRequest, BusinessSilentException e) {
        log.error("request method [{}] [{}] business silent exception: [{}]", servletRequest.getMethod(), servletRequest.getRequestURI(), e.toString());
        return new BaseResponse<>(null, e.getResultCode(), e.getMessage(), null);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse<Void> basicExceptionHandler(HttpServletRequest request, Exception e) {
        log.error("request method [{}] [{}] exception: [{}]", request.getMethod(), request.getRequestURI(), e.toString(), e);
        return new BaseResponse<>(null, ErrorCode.SERVER_ERROR.getCode(), ErrorCode.SERVER_ERROR.getMessage(), null);
    }

}
