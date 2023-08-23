package com.nio.ngfs.plm.bom.configuration.application.query.productconfig;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.oxo.common.OxoQueryUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.productconfig.assemble.ProductConfigAssembler;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.util.ModelYearComparator;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.enums.BaseVehicleMaturityEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.enums.OxoSnapshotEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBaseVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoVersionSnapshotDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoVersionSnapshotEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.GetBasedOnBaseVehicleListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.GetBasedOnBaseVehicleListRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 查询Based On Base Vehicle列表
 *
 * @author xiaozhou.tu
 * @date 2023/8/21
 */
@Component
@RequiredArgsConstructor
public class GetBasedOnBaseVehicleListQuery extends AbstractQuery<GetBasedOnBaseVehicleListQry, List<GetBasedOnBaseVehicleListRespDto>> {

    private final BomsOxoVersionSnapshotDao oxoVersionSnapshotDao;
    private final BomsBaseVehicleDao baseVehicleDao;
    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    protected List<GetBasedOnBaseVehicleListRespDto> executeQuery(GetBasedOnBaseVehicleListQry qry) {
        // 查询当前最新Release OXO
        BomsOxoVersionSnapshotEntity oxoVersionSnapshotEntity = oxoVersionSnapshotDao.queryLastReleaseSnapshotByModel(qry.getModel(), null);
        if (oxoVersionSnapshotEntity == null) {
            return Collections.emptyList();
        }
        List<BomsBaseVehicleEntity> baseVehicleEntityList = Lists.newArrayList();
        if (Objects.equals(OxoSnapshotEnum.FORMAL.getCode(), oxoVersionSnapshotEntity.getType())) {
            // 最新Release OXO版本为Formal版本
            BomsOxoVersionSnapshotEntity informalOxoVersionSnapshotEntity = oxoVersionSnapshotDao.queryLastReleaseSnapshotByModel(qry.getModel(),
                    OxoSnapshotEnum.INFORMAL.getCode());
            baseVehicleEntityList.addAll(getBaseVehicleFromOxoRelease(oxoVersionSnapshotEntity, BaseVehicleMaturityEnum.P));
            baseVehicleEntityList.addAll(getBaseVehicleFromOxoRelease(informalOxoVersionSnapshotEntity, BaseVehicleMaturityEnum.U));
        } else {
            // 最新Release OXO版本为Informal版本
            baseVehicleEntityList.addAll(getBaseVehicleFromOxoRelease(oxoVersionSnapshotEntity, null));
        }
        // 查询Feature Library
        List<BomsFeatureLibraryEntity> baseVehicleOptionList = bomsFeatureLibraryDao.queryByParentFeatureCodeListAndType(ConfigConstants.BASE_VEHICLE_FEATURE_CODE_LIST,
                FeatureTypeEnum.OPTION.getType());
        // 组装结果
        List<GetBasedOnBaseVehicleListRespDto> respDtoList = ProductConfigAssembler.assemble(qry.getModel(), baseVehicleEntityList, baseVehicleOptionList);
        // 按Region、Drive Hand、Sales Version排序
        respDtoList.forEach(respDto -> respDto.setBaseVehicleList(
                respDto.getBaseVehicleList().stream().sorted(Comparator.comparing(GetBasedOnBaseVehicleListRespDto.BasedOnBaseVehicleDto::getRegionCode)
                                .thenComparing(GetBasedOnBaseVehicleListRespDto.BasedOnBaseVehicleDto::getDriveHandCode)
                                .thenComparing(GetBasedOnBaseVehicleListRespDto.BasedOnBaseVehicleDto::getSalesVersionCode))
                        .toList()
        ));
        // 按Model Year排序
        return respDtoList.stream()
                .sorted(Comparator.comparing(GetBasedOnBaseVehicleListRespDto::getModelYear, ModelYearComparator.INSTANCE))
                .toList();
    }

    /**
     * 从OXO Release中获取Base Vehicle
     */
    private List<BomsBaseVehicleEntity> getBaseVehicleFromOxoRelease(BomsOxoVersionSnapshotEntity oxoVersionSnapshotEntity, BaseVehicleMaturityEnum maturityEnum) {
        if (oxoVersionSnapshotEntity == null) {
            return Lists.newArrayList();
        }
        OxoListQry oxoListQry = OxoQueryUtil.resolveSnapShot(oxoVersionSnapshotEntity.getOxoSnapshot());
        List<Long> baseVehicleIdList = OxoQueryUtil.getBaseVehicleIdListFromOxoRelease(oxoListQry);
        List<BomsBaseVehicleEntity> baseVehicleEntityList = baseVehicleDao.queryByIdList(baseVehicleIdList);
        // 过滤maturity
        return baseVehicleEntityList.stream().filter(i -> maturityEnum == null || Objects.equals(maturityEnum.getMaturity(), i.getMaturity())).toList();
    }

}
