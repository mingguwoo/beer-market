package com.nio.ngfs.plm.bom.configuration.application.query.productconfig.assemble;

import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.DateUtils;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.util.ModelYearComparator;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsModelYearConfigEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetBasedOnBaseVehicleListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetBasedOnPcListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetModelListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.QueryPcRespDto;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
public class ProductConfigAssembler {

    public static GetModelListRespDto assemble(String model, List<BomsModelYearConfigEntity> entityList) {
        GetModelListRespDto respDto = new GetModelListRespDto();
        respDto.setModel(model);
        respDto.setModelYearList(entityList.stream().map(BomsModelYearConfigEntity::getModelYear)
                .sorted(ModelYearComparator.INSTANCE).toList());
        return respDto;
    }

    public static GetBasedOnPcListRespDto assemble(BomsProductConfigEntity entity) {
        GetBasedOnPcListRespDto respDto = new GetBasedOnPcListRespDto();
        respDto.setPcId(entity.getPcId());
        respDto.setPcName(entity.getName());
        return respDto;
    }

    public static QueryPcRespDto assemble(BomsProductConfigEntity pc, BomsBaseVehicleEntity basedOnBaseVehicle,
                                          BomsFeatureLibraryEntity regionOption, BomsFeatureLibraryEntity driveHandOption,
                                          BomsFeatureLibraryEntity salesVersionOption, BomsProductConfigEntity basedOnPc) {
        QueryPcRespDto respDto = new QueryPcRespDto();
        BeanUtils.copyProperties(pc, respDto);
        respDto.setModel(pc.getModelCode());
        respDto.setCreateTime(DateUtils.dateTimeStr(pc.getCreateTime()));
        respDto.setUpdateTime(DateUtils.dateTimeStr(pc.getUpdateTime()));
        if (basedOnBaseVehicle != null) {
            respDto.setBasedOnBaseVehicle(QueryPcRespDto.BasedOnBaseVehicleDto.builder()
                    .baseVehicleId(basedOnBaseVehicle.getBaseVehicleId())
                    .regionCode(regionOption.getFeatureCode())
                    .regionCn(regionOption.getChineseName())
                    .driveHandCode(driveHandOption.getFeatureCode())
                    .driveHandCn(driveHandOption.getChineseName())
                    .salesVersionCode(salesVersionOption.getFeatureCode())
                    .salesVersionCn(salesVersionOption.getChineseName())
                    .build());
        }
        if (basedOnPc != null) {
            respDto.setBasedOnPcId(basedOnPc.getPcId());
            respDto.setBasedOnPcName(basedOnPc.getName());
        }
        return respDto;
    }

    public static List<GetBasedOnBaseVehicleListRespDto> assemble(String modelCode, List<BomsBaseVehicleEntity> baseVehicleEntityList,
                                                                  List<BomsFeatureLibraryEntity> baseVehicleOptionList,
                                                                  Map<Long, Long> baseVehicleOxoVersionSnapshotIdMap) {
        Map<String, BomsFeatureLibraryEntity> baseVehicleOptionMap = LambdaUtil.toKeyMap(baseVehicleOptionList, BomsFeatureLibraryEntity::getFeatureCode);
        Map<String, List<BomsBaseVehicleEntity>> baseVehicleEntityGroup = LambdaUtil.groupBy(baseVehicleEntityList, BomsBaseVehicleEntity::getModelYear);
        return LambdaUtil.map(baseVehicleEntityGroup.entrySet(), entry -> {
            GetBasedOnBaseVehicleListRespDto respDto = new GetBasedOnBaseVehicleListRespDto();
            respDto.setModel(modelCode);
            respDto.setModelYear(entry.getKey());
            respDto.setBaseVehicleList(entry.getValue().stream().map(baseVehicleEntity -> {
                GetBasedOnBaseVehicleListRespDto.BasedOnBaseVehicleDto baseVehicleDto = new GetBasedOnBaseVehicleListRespDto.BasedOnBaseVehicleDto();
                baseVehicleDto.setId(baseVehicleEntity.getId());
                baseVehicleDto.setBaseVehicleId(baseVehicleEntity.getBaseVehicleId());
                baseVehicleDto.setRegionCode(baseVehicleEntity.getRegionOptionCode());
                baseVehicleDto.setRegionCn(getBomsFeatureLibraryEntity(baseVehicleOptionMap, baseVehicleEntity.getRegionOptionCode()).getChineseName());
                baseVehicleDto.setDriveHandCode(baseVehicleEntity.getDriveHand());
                baseVehicleDto.setDriveHandCn(getBomsFeatureLibraryEntity(baseVehicleOptionMap, baseVehicleEntity.getDriveHand()).getChineseName());
                baseVehicleDto.setSalesVersionCode(baseVehicleEntity.getSalesVersion());
                baseVehicleDto.setSalesVersionCn(getBomsFeatureLibraryEntity(baseVehicleOptionMap, baseVehicleEntity.getSalesVersion()).getChineseName());
                baseVehicleDto.setOxoVersionSnapshotId(baseVehicleOxoVersionSnapshotIdMap.get(baseVehicleEntity.getId()));
                return baseVehicleDto;
            }).toList());
            return respDto;
        });
    }

    private static BomsFeatureLibraryEntity getBomsFeatureLibraryEntity(Map<String, BomsFeatureLibraryEntity> baseVehicleOptionMap, String optionCode) {
        BomsFeatureLibraryEntity bomsFeatureLibraryEntity = baseVehicleOptionMap.get(optionCode);
        if (bomsFeatureLibraryEntity == null) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_BASED_ON_BASE_VEHICLE_OPTION_NOT_EXIST);
        }
        return bomsFeatureLibraryEntity;
    }

}
