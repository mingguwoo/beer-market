package com.nio.ngfs.plm.bom.configuration.application.command.oxo;


import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums.OxoOptionPackageTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.basevehicle.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoFeatureOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoOptionPackageDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoAddCommand extends AbstractLockCommand<OxoAddCmd, Boolean> {


    private final BaseVehicleDomainService baseVehicleDomainService;

    private final BomsOxoFeatureOptionDao featureOptionDao;

    private final BomsOxoOptionPackageDao oxoOptionPackageDao;


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
        featureOptionDao.insertOxoFeatureOptions(rowInfoAggrs);

        //车型
        String modelCode = cmd.getModelCode();


        List<String> optionCodes = rowInfoAggrs.stream().filter(x -> StringUtils.equals(FeatureTypeEnum.OPTION.getType(),x.getType()))
                .map(OxoFeatureOptionAggr::getFeatureCode).distinct().toList();

        //查询表头数据
        List<OxoHeadQry> oxoHeads = baseVehicleDomainService.queryByModel(modelCode);

        //查询行数据
        List<BomsOxoFeatureOptionEntity> entityList = featureOptionDao.queryByModelAndFeatureCodeList(modelCode, optionCodes);


        String brandName = ConfigConstants.brandName.get();

        //则系统自动将Option在各个Base Vehicle下的打点赋值为实心圈
        oxoOptionPackageDao.insertOxoOptionPackages(OxoOptionPackageFactory.buildOptionPackages(oxoHeads,
                entityList.stream().map(BomsOxoFeatureOptionEntity::getId).distinct().toList(),
                OxoOptionPackageTypeEnum.DEFALUT.getType(), brandName, cmd.getUserName()));


        return true;
    }
}
