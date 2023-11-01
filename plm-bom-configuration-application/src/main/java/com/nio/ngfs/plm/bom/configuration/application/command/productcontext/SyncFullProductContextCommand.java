package com.nio.ngfs.plm.bom.configuration.application.command.productcontext;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event.SyncProductContextEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request.SyncFullProductContextCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.SyncFullProductContextRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/10/31
 */

@Component
@RequiredArgsConstructor
public class SyncFullProductContextCommand extends AbstractLockCommand<SyncFullProductContextCmd, SyncFullProductContextRespDto> {

    private final ProductContextRepository productContextRepository;
    private final ProductContextFeatureRepository productContextFeatureRepository;
    private final EventPublisher eventPublisher;


    @Override
    protected Long getLockTime(SyncFullProductContextCmd cmd) {
        return 60L;
    }

    @Override
    protected String getLockKey(SyncFullProductContextCmd cmd) {
        return RedisKeyConstant.PRODUCT_CONTEXT_FULL_SYNC_TO_ENOVIA;
    }

    @Override
    protected SyncFullProductContextRespDto executeWithLock(SyncFullProductContextCmd cmd) {
        try {
            List<ProductContextFeatureAggr> featureList = productContextFeatureRepository.queryAll();
            List<ProductContextAggr> optionList = productContextRepository.queryAll();
            Map<String,List<ProductContextAggr>> contextListMap = new HashMap<>();
            Map<String, List<ProductContextFeatureAggr>> contextFeatureListMap = new HashMap<>();
            Set<List<ProductContextAggr>> contextListSet = new HashSet<>();
            Set<List<ProductContextFeatureAggr>> contextFeatureListSet = new HashSet<>();
            optionList.forEach(option->{
                //不存在这个model的list，就新建
                if (!contextListSet.contains(option.getModelCode())){
                    List<ProductContextAggr> aggrList = new ArrayList<>();
                    aggrList.add(option);
                    contextListSet.add(aggrList);
                    contextListMap.put(option.getModelCode(),aggrList);
                }
                else {
                    contextListMap.get(option.getModelCode()).add(option);
                }
            });
            featureList.forEach(feature->{
                if (!contextFeatureListSet.contains(feature.getModelCode())){
                    List<ProductContextFeatureAggr> aggrList = new ArrayList<>();
                    aggrList.add(feature);
                    contextFeatureListSet.add(aggrList);
                    contextFeatureListMap.put(feature.getModelCode(),aggrList);
                }
                else {
                    contextFeatureListMap.get(feature.getModelCode()).add(feature);
                }
            });
            //发布事件
            contextListSet.forEach(model->{
                eventPublisher.publish(new SyncProductContextEvent(contextListMap.get(model),contextFeatureListMap.get(model)));
            });
        } catch(BusinessException e) {
            if (Objects.equals(e.getCode(), ConfigErrorCode.LOCK_FAILED)){
                throw new BusinessException(ConfigErrorCode.PRODUCT_CONTEXT_SYNC_FULL_LOCK_ERROR);
            }
        }
        return new SyncFullProductContextRespDto();
    }
}
