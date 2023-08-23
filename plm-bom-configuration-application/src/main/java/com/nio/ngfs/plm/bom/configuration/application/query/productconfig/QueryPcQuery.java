package com.nio.ngfs.plm.bom.configuration.application.query.productconfig;

import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.productconfig.assemble.ProductConfigAssembler;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.util.ModelYearComparator;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBaseVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.QueryPcQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.QueryPcRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 查询PC列表
 *
 * @author xiaozhou.tu
 * @date 2023/8/16
 */
@Component
@RequiredArgsConstructor
public class QueryPcQuery extends AbstractQuery<QueryPcQry, List<QueryPcRespDto>> {

    private final BomsProductConfigDao bomsProductConfigDao;
    private final BomsBaseVehicleDao bomsBaseVehicleDao;
    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    protected void validate(QueryPcQry qry) {
        if (qry.getSearch() != null) {
            qry.setSearch(qry.getSearch().trim());
        }
    }

    @Override
    protected List<QueryPcRespDto> executeQuery(QueryPcQry qry) {
        // 搜索PC并排序
        List<BomsProductConfigEntity> pcList = searchAndSortPc(qry);
        // 查询Based On Base Vehicle
        List<BomsBaseVehicleEntity> basedOnBaseVehicleList = bomsBaseVehicleDao.queryByIdList(LambdaUtil.map(pcList, i -> i.getBasedOnBaseVehicleId() > 0,
                BomsProductConfigEntity::getBasedOnBaseVehicleId));
        // 查询Feature Library
        List<BomsFeatureLibraryEntity> baseVehicleOptionList = null;
        if (CollectionUtils.isNotEmpty(basedOnBaseVehicleList)) {
            baseVehicleOptionList = bomsFeatureLibraryDao.queryByParentFeatureCodeListAndType(ConfigConstants.BASE_VEHICLE_FEATURE_CODE_LIST,
                    FeatureTypeEnum.OPTION.getType());
        }
        // 查询Based On PC
        List<BomsProductConfigEntity> basedOnPcList = bomsProductConfigDao.queryByPcIdList(LambdaUtil.map(pcList, i -> StringUtils.isNotBlank(i.getBasedOnPcId()),
                BomsProductConfigEntity::getBasedOnPcId));
        // 构建Response
        return buildResponse(pcList, basedOnBaseVehicleList, baseVehicleOptionList, basedOnPcList);
    }

    /**
     * 模糊搜索
     */
    private List<BomsProductConfigEntity> searchAndSortPc(QueryPcQry qry) {
        // 查询PC列表
        List<BomsProductConfigEntity> pcList = bomsProductConfigDao.queryByModelAndModelYearList(qry.getModel(), qry.getModelYearList());
        return pcList.stream()
                // 模糊搜索
                .filter(i -> StringUtils.isBlank(qry.getSearch()) || (
                        i.getPcId().contains(qry.getSearch()) ||
                                i.getModelCode().contains(qry.getSearch()) ||
                                i.getModelYear().contains(qry.getSearch()) ||
                                i.getName().contains(qry.getSearch())
                ))
                // 按Model Year、创建时间（倒排）排序
                .sorted(Comparator.comparing(BomsProductConfigEntity::getModelYear, ModelYearComparator.INSTANCE)
                        .thenComparing(Comparator.comparing(BomsProductConfigEntity::getCreateTime).reversed()))
                .toList();
    }

    /**
     * 构建Response
     */
    private List<QueryPcRespDto> buildResponse(List<BomsProductConfigEntity> pcList, List<BomsBaseVehicleEntity> basedOnBaseVehicleList,
                                               List<BomsFeatureLibraryEntity> baseVehicleOptionList, List<BomsProductConfigEntity> basedOnPcList) {
        Map<Long, BomsBaseVehicleEntity> basedOnBaseVehicleMap = LambdaUtil.toKeyMap(basedOnBaseVehicleList, BomsBaseVehicleEntity::getId);
        Map<String, BomsFeatureLibraryEntity> baseVehicleOptionMap = LambdaUtil.toKeyMap(baseVehicleOptionList, BomsFeatureLibraryEntity::getFeatureCode);
        Map<String, BomsProductConfigEntity> basedOnPcMap = LambdaUtil.toKeyMap(basedOnPcList, BomsProductConfigEntity::getPcId);
        return LambdaUtil.map(pcList, pc -> {
            BomsFeatureLibraryEntity regionOption = null;
            BomsFeatureLibraryEntity driveHandOption = null;
            BomsFeatureLibraryEntity salesVersionOption = null;
            BomsBaseVehicleEntity basedOnBaseVehicle = basedOnBaseVehicleMap.get(pc.getBasedOnBaseVehicleId());
            if (basedOnBaseVehicle != null) {
                regionOption = baseVehicleOptionMap.get(basedOnBaseVehicle.getRegionOptionCode());
                driveHandOption = baseVehicleOptionMap.get(basedOnBaseVehicle.getDriveHand());
                salesVersionOption = baseVehicleOptionMap.get(basedOnBaseVehicle.getSalesVersion());
                if (regionOption == null || driveHandOption == null || salesVersionOption == null) {
                    throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_BASED_ON_BASE_VEHICLE_OPTION_NOT_EXIST);
                }
            }
            BomsProductConfigEntity basedOnPc = basedOnPcMap.get(pc.getBasedOnPcId());
            return ProductConfigAssembler.assemble(pc, basedOnBaseVehicle, regionOption, driveHandOption, salesVersionOption, basedOnPc);
        });
    }

}
