package com.nio.ngfs.plm.bom.configuration.application.command.oxo;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageFactory;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoFeatureOptionDomainService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoFeatureOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoOptionPackageDao;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditInfoCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoEditCommand extends AbstractLockCommand<OxoEditInfoCmd, List<String>> {


    private final BomsOxoFeatureOptionDao bomsOxoFeatureOptionDao;

    private final BomsOxoOptionPackageDao oxoOptionPackageDao;

    private final OxoFeatureOptionDomainService featureOptionDomainService;

    @Override
    protected String getLockKey(OxoEditInfoCmd cmd) {
        return RedisKeyConstant.MODEL_OXO_FEATURE_EDIT_PREFIX + cmd.getModelCode();
    }

    @Override
    protected List<String> executeWithLock(OxoEditInfoCmd cmd) {

        List<OxoEditCmd> cmdLists = cmd.getEditCmds();

        String modelCode= cmd.getModelCode();
        String userName =cmd.getUserName();

        List<OxoFeatureOptionAggr> oxoFeatureOptionAggrs = Lists.newArrayList();

        // 更新备注 和 ruleCheck
        cmdLists.stream().filter(x ->
                Objects.nonNull(x.getHeadId()) &&
                        (StringUtils.isNotBlank(x.getComments()) ||
                                StringUtils.isNotBlank(x.getRuleCheck()))).toList().forEach(editCmd -> {

            OxoFeatureOptionAggr optionAggr = new OxoFeatureOptionAggr();

            if (StringUtils.isNotBlank(editCmd.getComments())) {
                optionAggr.setComment(editCmd.getComments());
            }

            if (StringUtils.isNotBlank(editCmd.getRuleCheck())) {
                optionAggr.setRuleCheck(editCmd.getRuleCheck());
            }
            optionAggr.setId(editCmd.getHeadId());
            optionAggr.setUpdateUser(userName);
            oxoFeatureOptionAggrs.add(optionAggr);
        });


        //更新 oxo列表
        if (CollectionUtils.isNotEmpty(oxoFeatureOptionAggrs)) {
            bomsOxoFeatureOptionDao.updateOxoFeatureOptions(oxoFeatureOptionAggrs);
        }


        //更新 打点信息
        oxoOptionPackageDao.insertOxoOptionPackages(
                OxoOptionPackageFactory.createOxoOptionPackageAggrList(cmdLists, userName));

        List<String> messages = Lists.newArrayList();

        /**
         * OXO中是否存在
         * 在所有Base Vehicle下打点都为“-”的Option（通过Delete Code删除的Option排除在外）
         */
        List<String> optionCodes = featureOptionDomainService.checkOxoBasicVehicleOptions(modelCode);


        /**
         * 系统校验（软校验）：
         * 1.该Option是否应用于Status为Working的Configuration Rule中
         * 2.该Option是否在Product Configuration有勾选
         */



        return messages;
    }
}
