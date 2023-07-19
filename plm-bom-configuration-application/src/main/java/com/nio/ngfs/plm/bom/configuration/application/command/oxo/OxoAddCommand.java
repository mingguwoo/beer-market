package com.nio.ngfs.plm.bom.configuration.application.command.oxo;


import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoRowInfoAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.repository.OxoRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoAddCommand extends AbstractLockCommand<OxoAddCmd, Object> {


    private final OxoRepository oxoRepository;


    @Override
    protected String getLockKey(OxoAddCmd cmd) {
        return RedisKeyConstant.MODEL_FEATURE_LOCK_KEY_PREFIX + cmd.getModelCode();
    }


    /**
     * 添加 optionCode FeatureCode至OXO
     * @param cmd c
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    protected Object executeWithLock(OxoAddCmd cmd) {

        //插入行数据
        oxoRepository.insertOxoRows(OxoRowInfoAggr.buildOxoRowInfoAggrs(cmd));


        //查询列数据


        //插入打点数据
        //oxoRepository.insertOxoOptionPackageInfo();





        return new Object();
    }
}
