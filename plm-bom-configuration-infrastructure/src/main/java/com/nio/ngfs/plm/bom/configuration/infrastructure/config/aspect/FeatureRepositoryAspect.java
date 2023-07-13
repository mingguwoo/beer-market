package com.nio.ngfs.plm.bom.configuration.infrastructure.config.aspect;

import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.common.FeatureAggrThreadLocal;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureAttributeChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
@Slf4j
@Aspect
@Component
@Order
@RequiredArgsConstructor
public class FeatureRepositoryAspect {

    private final EventPublisher eventPublisher;

    @Pointcut("execution(* com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl.FeatureRepositoryImpl.save())")
    public void save() {
    }

    @Around("save()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Object[] objects = point.getArgs();
        if (objects == null || objects.length != 1 || !(objects[0] instanceof FeatureAggr saveFeatureAggr)) {
            return point.proceed();
        }
        Map<Long, FeatureAggr> featureAggrMap = FeatureAggrThreadLocal.get();
        FeatureAggr findFeatureAggr = featureAggrMap.get(saveFeatureAggr.getId());
        eventPublisher.publish(new FeatureAttributeChangeEvent(findFeatureAggr, saveFeatureAggr));
        return point.proceed();
    }

}
