package com.nio.ngfs.plm.bom.configuration.application.query.productconfig.assemble;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.enums.YesOrNoEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums.ProductConfigOptionTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetPcOptionListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.QueryProductConfigRespDto;

import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public class QueryProductConfigAssembler {

    public static QueryProductConfigRespDto.PcDto assemble(BomsProductConfigEntity entity) {
        QueryProductConfigRespDto.PcDto pcDto = new QueryProductConfigRespDto.PcDto();
        pcDto.setPcPkId(entity.getId());
        pcDto.setPcId(entity.getPcId());
        pcDto.setPcName(entity.getName());
        pcDto.setModel(entity.getModelCode());
        pcDto.setModelYear(entity.getModelYear());
        pcDto.setSkipCheck(Objects.equals(entity.getSkipCheck(), CommonConstants.OPEN));
        return pcDto;
    }

    public static QueryProductConfigRespDto.FeatureDto assemble(BomsFeatureLibraryEntity entity) {
        QueryProductConfigRespDto.FeatureDto featureDto = new QueryProductConfigRespDto.FeatureDto();
        featureDto.setFeatureCode(entity.getFeatureCode());
        featureDto.setGroup(entity.getParentFeatureCode());
        featureDto.setDisplayName(entity.getDisplayName());
        featureDto.setChineseName(entity.getChineseName());
        return featureDto;
    }

    public static QueryProductConfigRespDto.OptionDto assemble(BomsFeatureLibraryEntity entity, String group) {
        QueryProductConfigRespDto.OptionDto optionDto = new QueryProductConfigRespDto.OptionDto();
        optionDto.setOptionCode(entity.getFeatureCode());
        optionDto.setGroup(group);
        optionDto.setDisplayName(entity.getDisplayName());
        optionDto.setChineseName(entity.getChineseName());
        return optionDto;
    }

    public static QueryProductConfigRespDto.PcOptionConfigDto assemble(BomsProductConfigEntity pc, BomsProductConfigOptionEntity optionEntity,
                                                                       BomsProductContextEntity productContextEntity, boolean edit) {
        QueryProductConfigRespDto.PcOptionConfigDto configDto = new QueryProductConfigRespDto.PcOptionConfigDto();
        configDto.setPcPkId(pc.getId());
        configDto.setPcId(pc.getPcId());
        // 是否勾选，打点存在且为勾选状态
        configDto.setSelect(optionEntity != null && Objects.equals(YesOrNoEnum.YES.getCode(), optionEntity.getSelectStatus()));
        // 是否编辑模式
        if (edit) {
            if (optionEntity != null && Objects.equals(ProductConfigOptionTypeEnum.FROM_BASE_VEHICLE.getType(), optionEntity.getType())
                    && Objects.equals(YesOrNoEnum.NO.getCode(), pc.getCompleteInitSelect())) {
                // 1、打点Copy From Base Vehicle，且未完成初始化勾选
                if (Objects.equals(YesOrNoEnum.NO.getCode(), optionEntity.getSelectCanEdit())) {
                    // 情况1、2、3、4，人工不可勾选
                    configDto.setSelectCanEdit(false);
                    configDto.setSetGray(true);
                } else {
                    // 情况4，人工可勾选
                    configDto.setSelectCanEdit(true);
                    configDto.setSetGray(false);
                }
            } else {
                // 2、其它情况，可勾选包括: Product Context勾选了、Product Config勾选了
                configDto.setSelectCanEdit(productContextEntity != null || configDto.isSelect());
                // 是否置灰，Product Context未勾选时，置灰
                configDto.setSetGray(productContextEntity == null);
            }
        }
        return configDto;
    }

    public static QueryProductConfigRespDto.PcFeatureConfigDto assemble(BomsProductConfigEntity entity, boolean ignoreSkipCheck) {
        QueryProductConfigRespDto.PcFeatureConfigDto featureConfigDto = new QueryProductConfigRespDto.PcFeatureConfigDto();
        featureConfigDto.setPcPkId(entity.getId());
        featureConfigDto.setPcId(entity.getPcId());
        featureConfigDto.setIgnoreSkipCheck(ignoreSkipCheck);
        return featureConfigDto;
    }

    public static GetPcOptionListRespDto assembleOptionList(BomsProductConfigEntity entity) {
        GetPcOptionListRespDto respDto = new GetPcOptionListRespDto();
        respDto.setPcId(entity.getPcId());
        respDto.setPcName(entity.getName());
        return respDto;
    }

}
