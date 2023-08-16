package com.nio.ngfs.plm.bom.configuration.application.query.productconfig;

import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.productconfig.assemble.ProductConfigAssembler;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
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
    protected List<QueryPcRespDto> executeQuery(QueryPcQry qry) {
        // 查询PC列表
        List<BomsProductConfigEntity> pcList = bomsProductConfigDao.queryByModelAndModelYearList(qry.getModel(), qry.getModelYearList());
        // 模糊搜索
        pcList = search(pcList, qry.getSearch());
        // 排序，按创建时间倒排
        pcList = pcList.stream().sorted(Comparator.comparing(BomsProductConfigEntity::getCreateTime).reversed()).toList();
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
    private List<BomsProductConfigEntity> search(List<BomsProductConfigEntity> pcList, String search) {
        if (StringUtils.isBlank(search)) {
            return pcList;
        }
        String keyword = search.trim();
        return pcList.stream().filter(i -> i.getPcId().contains(keyword) ||
                        i.getModelCode().contains(keyword) ||
                        i.getModelYear().contains(keyword) ||
                        i.getName().contains(keyword))
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
