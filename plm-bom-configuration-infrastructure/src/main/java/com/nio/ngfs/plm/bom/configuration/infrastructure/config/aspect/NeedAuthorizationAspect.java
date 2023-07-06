package com.nio.ngfs.plm.bom.configuration.infrastructure.config.aspect;

import com.nio.bom.share.annotation.NeedAuthorization;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.model.UserSsoProfile;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.common.utils.SpringContextHolder;
import com.nio.ngfs.plm.bom.configuration.remote.SsoClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author luke.zhu
 */
@Aspect
@Slf4j
@Component
public class NeedAuthorizationAspect {

    public static ThreadLocal<String> userId = ThreadLocal.withInitial(() -> "");

    /**
     * 定义切入点
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Controller *)")
    public void aspect() {
    }

    /**
     * @param point             切点
     * @param needAuthorization 是否需要签名的注解
     * @return 方法反射
     */
    @Around("aspect()&&@annotation(needAuthorization)")
    public Object doAround(ProceedingJoinPoint point, NeedAuthorization needAuthorization) throws Throwable {
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert requestAttributes != null;
            HttpServletRequest request = requestAttributes.getRequest();
            String accessToken = request.getHeader(CommonConstants.ACCESS_TOKEN);
            if (StringUtils.isNotEmpty(accessToken)) {
                UserSsoProfile userSsoProfile = SpringContextHolder.getBean(SsoClient.class).getProfile(accessToken);
                if (userSsoProfile == null) {
                    log.error("userSsoProfile is null");
                    return ResultInfo.error("Authorization Error");
                }
                if (StringUtils.isNotEmpty(userSsoProfile.getError())) {
                    log.warn("sso getProfile error,accessToken= {}, errorMsg={}", accessToken, userSsoProfile.getError());
                    return ResultInfo.error("Authorization Error");
                }
                userId.set(userSsoProfile.getId());
            } else {
                log.error("accessToken is null");
                return ResultInfo.error("accessToken is null");
            }
        } catch (Exception e) {
            log.error("NeedAuthorizationAspect error: " + e);
        }
        return point.proceed();
    }


    public static String getUserId() {
        return userId.get();
    }

}

