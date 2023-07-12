package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureFactory;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddOptionCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.AddGroupRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.AddOptionRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddOptionCommand extends AbstractLockCommand<AddOptionCmd, AddOptionRespDto> {
    @Override
    protected String getLockKey(AddOptionCmd cmd) {
        return RedisKeyConstant.OPTION_FEATURE_LOCK_KEY_PREFIX + cmd.getFeatureCode();
    }

    @Override
    protected AddOptionRespDto executeWithLock(AddOptionCmd cmd) {
        FeatureAggr featureAggr = FeatureFactory.createOption(cmd);

        return new AddOptionRespDto();
    }
}
