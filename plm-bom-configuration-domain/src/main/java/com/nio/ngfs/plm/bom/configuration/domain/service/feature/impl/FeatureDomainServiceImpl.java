package com.nio.ngfs.plm.bom.configuration.domain.service.feature.impl;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.enums.StatusEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureId;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureGroupCodeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.FeatureStatusChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.BaseVehicleOptionsRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.response.GetBaseVehicleOptionsRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
@Service
@RequiredArgsConstructor
public class FeatureDomainServiceImpl implements FeatureDomainService {

    private final FeatureRepository featureRepository;
    private final EventPublisher eventPublisher;

    @Override
    public FeatureAggr getAndCheckFeatureAggr(String featureCode, FeatureTypeEnum typeEnum) {
        FeatureId featureId = new FeatureId(featureCode, typeEnum);
        FeatureAggr featureAggr = featureRepository.find(featureId);
        if (featureAggr == null) {
            throw new BusinessException(typeEnum == FeatureTypeEnum.GROUP ? ConfigErrorCode.FEATURE_GROUP_NOT_EXISTS :
                    (typeEnum == FeatureTypeEnum.FEATURE ? ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS :
                            ConfigErrorCode.FEATURE_OPTION_NOT_EXISTS));
        }
        return featureAggr;
    }

    @Override
    public void checkGroupCodeUnique(FeatureAggr featureAggr) {
        FeatureAggr existedFeatureAggr = featureRepository.find(featureAggr.getUniqId());
        if (existedFeatureAggr != null) {
            if (Objects.equals(featureAggr.getId(), existedFeatureAggr.getId())) {
                // id相同，代表同一条记录，忽略
                return;
            }
            throw new BusinessException(ConfigErrorCode.FEATURE_GROUP_CODE_REPEAT);
        }
    }

    @Override
    public void changeGroupFeatureOptionStatusByGroup(FeatureAggr featureAggr, FeatureStatusChangeTypeEnum changeTypeEnum, String updateUser) {
        if (changeTypeEnum == FeatureStatusChangeTypeEnum.NO_CHANGE) {
            return;
        }
        if (!featureAggr.isType(FeatureTypeEnum.GROUP)) {
            return;
        }
        // Group的状态由Active变为Inactive
        if (changeTypeEnum == FeatureStatusChangeTypeEnum.ACTIVE_TO_INACTIVE) {
            featureRepository.save(featureAggr);
            return;
        }
        // Group的状态状态由Inactive变为Active
        List<String> featureCodeList = LambdaUtil.map(featureAggr.getChildrenList(), i -> i.getFeatureId().getFeatureCode());
        // 查询Group下面所有的Option列表
        List<FeatureAggr> optionList = featureRepository.queryByParentFeatureCodeListAndType(featureCodeList, FeatureTypeEnum.OPTION.getType());
        // 筛选Group下状态为Inactive的Feature和Option
        List<Long> idList = LambdaUtil.map(optionList, FeatureAggr::isInactive, FeatureAggr::getId);
        idList.addAll(LambdaUtil.map(featureAggr.getChildrenList(), FeatureAggr::isInactive, FeatureAggr::getId));
        idList.add(featureAggr.getId());
        // 批量更新Group/Feature/Option的状态为Active
        featureRepository.batchUpdateStatus(idList, StatusEnum.ACTIVE.getStatus(), updateUser);
        eventPublisher.publish(new FeatureStatusChangeEvent(idList, StatusEnum.INACTIVE, StatusEnum.ACTIVE, updateUser));
    }

    @Override
    public void checkFeatureOptionCodeUnique(FeatureAggr featureAggr) {
        List<FeatureAggr> existedFeatureAggrList = featureRepository.queryByFeatureCode(featureAggr.getFeatureId().getFeatureCode());
        if (CollectionUtils.isEmpty(existedFeatureAggrList)) {
            return;
        }
        existedFeatureAggrList.forEach(existedFeatureAggr -> {
            // Feature和Option的Code不可重复
            if (existedFeatureAggr.isType(FeatureTypeEnum.FEATURE) || existedFeatureAggr.isType(FeatureTypeEnum.OPTION)) {
                throw new BusinessException(featureAggr.isType(FeatureTypeEnum.FEATURE) ?
                        ConfigErrorCode.FEATURE_FEATURE_CODE_REPEAT : ConfigErrorCode.FEATURE_OPTION_CODE_REPEAT);
            }
        });
    }

    @Override
    public void checkDisplayNameUnique(FeatureAggr featureAggr) {
        List<FeatureAggr> existedFeatureAggrList = featureRepository.queryByDisplayNameCatalogAndType(
                featureAggr.getDisplayName(), featureAggr.getCatalog(), featureAggr.getFeatureId().getType());
        if (CollectionUtils.isNotEmpty(existedFeatureAggrList)) {
            existedFeatureAggrList.forEach(existedFeatureAggr -> {
                if (Objects.equals(featureAggr.getId(), existedFeatureAggr.getId())) {
                    // 同一条记录。跳过
                    return;
                }
                throw new BusinessException(ConfigErrorCode.FEATURE_DISPLAY_NAME_REPEAT);
            });
        }
    }

    @Override
    public void changeFeatureGroupCode(FeatureAggr featureAggr, String newGroupCode) {
        newGroupCode = newGroupCode.trim();
        String oldGroupCode = featureAggr.getParentFeatureCode();
        if (Objects.equals(oldGroupCode, newGroupCode)) {
            // Group Code未变更
            return;
        }
        FeatureAggr groupFeatureAggr = getAndCheckFeatureAggr(newGroupCode, FeatureTypeEnum.GROUP);
        if (!groupFeatureAggr.isActive()) {
            throw new BusinessException(ConfigErrorCode.FEATURE_GROUP_IS_NOT_ACTIVE);
        }
        featureAggr.setParentFeatureCode(newGroupCode);
        eventPublisher.publish(new FeatureGroupCodeChangeEvent(featureAggr, oldGroupCode));
    }

    @Override
    public GetBaseVehicleOptionsRespDto sortBaseVehicleOptions(List<FeatureAggr> featureAggrList) {
        GetBaseVehicleOptionsRespDto ans = new GetBaseVehicleOptionsRespDto();
        ans.setRegionOptionCodeList(new ArrayList<>());
        ans.setDriveHandList(new ArrayList<>());
        ans.setSalesVersionList(new ArrayList<>());
        featureAggrList.forEach(aggr->{
            //根据开头两个字母进行分类,只有三种：salesVersion/driveHand/regionOptionCode
            if (aggr.getFeatureId().getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO).equals(ConfigConstants.BASE_VEHICLE_SALES_VERSION_FEATURE.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                BaseVehicleOptionsRespDto option = new BaseVehicleOptionsRespDto();
                option.setOptionCode(aggr.getFeatureId().getFeatureCode());
                option.setDescription(aggr.getDescription());
                option.setChineseName(aggr.getChineseName());
                option.setEnglishName(aggr.getDisplayName());
                ans.getSalesVersionList().add(option);
            }
            else if (aggr.getFeatureId().getFeatureCode().substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO).equals(ConfigConstants.BASE_VEHICLE_DRIVE_HAND_FEATURE.substring(CommonConstants.INT_ZERO,CommonConstants.INT_TWO))){
                BaseVehicleOptionsRespDto option = new BaseVehicleOptionsRespDto();
                option.setOptionCode(aggr.getFeatureId().getFeatureCode());
                option.setDescription(aggr.getDescription());
                option.setChineseName(aggr.getChineseName());
                option.setEnglishName(aggr.getDisplayName());
                ans.getDriveHandList().add(option);
            }
            else {
                BaseVehicleOptionsRespDto option = new BaseVehicleOptionsRespDto();
                option.setOptionCode(aggr.getFeatureId().getFeatureCode());
                option.setDescription(aggr.getDescription());
                option.setChineseName(aggr.getChineseName());
                option.setEnglishName(aggr.getDisplayName());
                ans.getRegionOptionCodeList().add(option);            }
        });
        return ans;
    }

}
