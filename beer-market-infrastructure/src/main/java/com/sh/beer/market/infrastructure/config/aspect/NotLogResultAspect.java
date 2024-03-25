package com.sh.beer.market.infrastructure.config.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
@Aspect
@Slf4j
public class NotLogResultAspect {

    /**
     * 忽略以下uri，不记录日志
     *//*
    private final static List<String> IgnoreUriList = Lists.newArrayList("/error", "/status", "/health");

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void webLog() {
    }

    *//**
     * 环绕执行
     *
     * @param point 切入点
     * @return 结果
     * @throws Throwable 异常
     *//*
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable{
        // 执行类名
        String className = point.getTarget().getClass().getName();
        // 方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 执行名称
        String execName = className + "." + signature.getMethod().getName();
        //是否包含NotLogResult注解
        boolean isLogResult = true;
        NotLogResult notLogResult = signature.getMethod().getAnnotation(NotLogResult.class);
        if(!ObjectUtils.isEmpty(notLogResult)){
            isLogResult = false;
        }
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        OpLog opLog = new OpLog();
        if(null!=sra){
            HttpServletRequest request = sra.getRequest();
            opLog.setUri(request.getRequestURI());
            opLog.setMethod(request.getMethod());
            opLog.setHttpParams(request.getParameterMap());
            opLog.setExecName(execName);
            opLog.setClientAddr(request.getRemoteAddr());
        }
        // 参数列表
        List<Object> argList = Lists.newArrayList();
        Object[] args = point.getArgs();
        if (args != null) {
            for (Object arg : args) {
                if (arg == null || arg.getClass().isPrimitive()) {
                    argList.add(arg);
                } else if (arg instanceof HttpServletRequest
                        || arg instanceof HttpServletResponse
                        || arg instanceof ModelAndView
                        || arg instanceof Model
                        || arg instanceof MultipartFile
                ) {
                    argList.add(arg.toString());
                } else if (arg.getClass().isArray()) {
                    argList.add(arg.toString());
                } else {
                    try {
                        argList.add(JSON.parseObject(JSON.toJSONString(arg)));
                    } catch (Exception e) {
                        // 转换出错
                        argList.add(arg.toString());
                    }
                }
            }
        }

        opLog.setArgs(argList);
        opLog.setStartTime(new Date());

        try {
            MDC.setContextMap(Maps.newHashMap());
            MDC.getCopyOfContextMap().put("traceId", TraceContext.traceId());
            Object result = point.proceed();
            // 执行耗时，单位毫秒
            opLog.setDuration(System.currentTimeMillis() - opLog.getStartTime().getTime());

            if (result != null) {
                try {
                    if (isLogResult) {
                        opLog.setResult(JSON.parse(JSON.toJSONString(result)));
                    }
                } catch (Exception e) {
                    // json转换失败
                    opLog.setResult(result.toString());
                }
            }

            return result;
        } catch (Exception e) {
            // 执行耗时，单位毫秒
            opLog.setDuration(System.currentTimeMillis() - opLog.getStartTime().getTime());
            opLog.setException(ExceptionUtil.stacktraceToString(e));
            throw e;
        } finally {
            //忽略健康检查和未知路径的日志记录
            if(!IgnoreUriList.contains(opLog.getUri())){
                log.info("[NotLogResultAspect]Controller调用日志" + JSON.toJSONString(opLog));
            }
            MDC.remove("traceId");
        }

    }

    @Data
    public static class OpLog {

        private String uri;

        private String method;

        private Map<String, String[]> httpParams;

        private Date startTime;

        private Long duration;

        private String execName;

        private List<Object> args;

        private String exception;

        private Object result;

        private String clientAddr;

    }*/
}
