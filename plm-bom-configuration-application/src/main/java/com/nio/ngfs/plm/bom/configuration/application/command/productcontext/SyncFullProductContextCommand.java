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
            List<ProductContextFeatureAggr> featureList = productContextFeatureRepository.queryByModelCode(cmd.getModelCode());
            List<ProductContextAggr> optionList = productContextRepository.queryByModelCode(cmd.getModelCode());
            //发布事件
            eventPublisher.publish(new SyncProductContextEvent(optionList,featureList));
        return new SyncFullProductContextRespDto();
    }
}
