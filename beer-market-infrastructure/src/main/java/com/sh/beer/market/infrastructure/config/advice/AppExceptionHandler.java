package com.sh.beer.market.infrastructure.config.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class AppExceptionHandler {

    /*@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultInfo<String> methodArgumentNotValidExceptionHandler(HttpServletRequest servletRequest, MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        log.error("request method [{}] [{}] MethodArgumentNotValidException: []", servletRequest.getMethod(), servletRequest.getRequestURI(), e);
        return new ResultInfo<>(CommonErrorCode.PARAMETER_ERROR.getCode(), StringUtils.defaultString(message, CommonErrorCode.PARAMETER_ERROR.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultInfo<String> constraintViolationExceptionHandler(HttpServletRequest servletRequest, ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        log.error("request method [{}] [{}] ConstraintViolationException: []", servletRequest.getMethod(), servletRequest.getRequestURI(), e);
        return new ResultInfo<>(CommonErrorCode.PARAMETER_ERROR.getCode(), StringUtils.defaultString(message, CommonErrorCode.PARAMETER_ERROR.getMessage()));
    }

    @ExceptionHandler(value = com.sh.beer.market.common.exception.BusinessException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultInfo<String> businessExceptionHandler(HttpServletRequest servletRequest, com.sh.beer.market.common.exception.BusinessException ex) {
        ResultInfo<String> res = new ResultInfo<>();
        res.setCode(CommonErrorCode.SERVER_ERROR.getCode());
        res.setMessage(ex.getMessage());
        log.error("request method [{}] [{}] business exception: []", servletRequest.getMethod(), servletRequest.getRequestURI(), ex);
        return res;
    }

    @ExceptionHandler(value = BusinessSilentException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultInfo<String> businessSilentExceptionHandler(HttpServletRequest servletRequest, BusinessSilentException ex) {
        ResultInfo<String> res = new ResultInfo<>();
        res.setCode(CommonErrorCode.SERVER_ERROR.getCode());
        res.setMessage(ex.getMessage());
        log.error("request method [{}] [{}] business silent exception: []", servletRequest.getMethod(), servletRequest.getRequestURI(), ex);
        return res;
    }

    @ExceptionHandler(value = ClassNotFoundException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultInfo<String> classNotFoundExceptionHandler(HttpServletRequest servletRequest, Exception ex) {
        ResultInfo<String> res = new ResultInfo<>();
        res.setCode(CommonErrorCode.SERVER_ERROR.getCode());
        res.setMessage(ex.getMessage());
        log.error("request method [{}] [{}] class not found exception: []", servletRequest.getMethod(), servletRequest.getRequestURI(), ex);
        return res;
    }

    @ExceptionHandler(value = {ExecutionException.class, InterruptedException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultInfo<String> threadPoolExceptionHandler(HttpServletRequest servletRequest, Exception ex) {
        ResultInfo<String> res = new ResultInfo<>();
        res.setCode(CommonErrorCode.SERVER_ERROR.getCode());
        res.setMessage(ex.getMessage());
        log.error("request method [{}] [{}] execution or interrupted exception: []", servletRequest.getMethod(), servletRequest.getRequestURI(), ex);
        return res;
    }

    *//**
     * 业务异常处理
     *//*
    @ExceptionHandler(value = {BusinessException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultInfo<String> configurationBusinessExceptionHandler(HttpServletRequest servletRequest, BusinessException e) {
        ResultInfo<String> res = new ResultInfo<>();
        res.setCode(e.getCode());
        res.setMessage(e.getMessage());
        log.error("request method [{}] [{}] business exception: []", servletRequest.getMethod(), servletRequest.getRequestURI(), e);
        return res;
    }

    *//**
     * 异常处理
     *//*
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultInfo<String> exceptionHandler(HttpServletRequest servletRequest, Exception e) {
        ResultInfo<String> res = new ResultInfo<>();
        res.setCode(CommonErrorCode.SERVER_ERROR.getCode());
        res.setMessage(e.getMessage());
        log.error("request method [{}] [{}] exception: []", servletRequest.getMethod(), servletRequest.getRequestURI(), e);
        return res;
    }*/

}
