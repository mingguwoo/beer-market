package com.nio.ngfs.plm.bom.configuration.application.command.oxo;


import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoOptionPackageApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoAddCommand extends AbstractLockCommand<OxoAddCmd, Boolean> {



    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;


    private final OxoOptionPackageApplicationService oxoOptionPackageApplicationService;


    @Override
    protected String getLockKey(OxoAddCmd cmd) {
        return RedisKeyConstant.MODEL_FEATURE_LOCK_KEY_PREFIX + cmd.getModelCode();
    }


    /**
     * 添加 optionCode FeatureCode至OXO
     *
     * @param cmd
     * @return
     */
    @Override
    protected Boolean executeWithLock(OxoAddCmd cmd) {

        List<OxoFeatureOptionAggr> rowInfoAggrs = OxoFeatureOptionFactory.buildOxoRowInfoAggrs(cmd);

        //插入行数据
        oxoFeatureOptionRepository.insertOrUpdateOxoFeatureOptions(rowInfoAggrs);

        //插入 oxo打点信息
        oxoOptionPackageApplicationService.insertOxoOptionPackageDefault(rowInfoAggrs,cmd.getModelCode(),cmd.getUserName());
        return true;
    }
}
