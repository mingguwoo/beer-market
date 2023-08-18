package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;

import com.nio.bom.share.utils.GsonUtils;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.facade.FeatureFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.FeatureOptionSyncReqDto;
import com.nio.ngfs.plm.bom.configuration.infrastructure.common.warn.ConfigurationTo3deWarnSender;
import com.nio.ngfs.plm.bom.configuration.infrastructure.facade.common.AbstractEnoviaFacade;
import com.nio.ngfs.plm.bom.configuration.remote.PlmEnoviaClient;
import com.nio.ngfs.plm.bom.configuration.remote.dto.feature.PlmFeatureOptionSyncDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/7/19
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FeatureFacadeImpl extends AbstractEnoviaFacade implements FeatureFacade {

    private final PlmEnoviaClient plmEnoviaClient;
    private final ConfigurationTo3deWarnSender configurationTo3deWarnSender;

    @Override
    public void syncFeatureOption(FeatureOptionSyncReqDto reqDto) {
        log.info("FeatureFacade syncFeatureOption data={}", GsonUtils.toJson(reqDto));
        PlmFeatureOptionSyncDto.Feature feature = new PlmFeatureOptionSyncDto.Feature();
        feature.setFeatureCode(reqDto.getFeature().getFeatureCode());
        feature.setFeatureFamily(reqDto.getFeature().getGroupCode());
        feature.setFeatureName(reqDto.getFeature().getDisplayName());
        feature.setChineseName(reqDto.getFeature().getChineseName());
        feature.setDescription(reqDto.getFeature().getDescription());
        feature.setSelectionType(reqDto.getFeature().getSelectionType());
        feature.setCatalogue(reqDto.getFeature().getCatalog());
        feature.setType(reqDto.getFeature().getType() == FeatureOptionSyncReqDto.FeatureSyncType.ADD ? ConfigConstants.FEATURE_OPTION_SYNC_ADD : (
                reqDto.getFeature().getType() == FeatureOptionSyncReqDto.FeatureSyncType.UPDATE ? ConfigConstants.FEATURE_OPTION_SYNC_CHANGE_OLD_DATA : null
        ));
        PlmFeatureOptionSyncDto syncDto = new PlmFeatureOptionSyncDto();
        syncDto.setOwner(reqDto.getUpdateUser());
        syncDto.setFeature(feature);
        syncDto.setOptionList(LambdaUtil.map(reqDto.getFeature().getOptionList(), optionSyncDto -> {
            PlmFeatureOptionSyncDto.Option option = new PlmFeatureOptionSyncDto.Option();
            option.setOptionCode(optionSyncDto.getOptionCode());
            option.setOptionName(optionSyncDto.getDisplayName());
            option.setChineseName(optionSyncDto.getChineseName());
            option.setDescription(optionSyncDto.getDescription());
            option.setOptionSeq(0);
            option.setType(optionSyncDto.getType() == FeatureOptionSyncReqDto.FeatureSyncType.ADD ? ConfigConstants.FEATURE_OPTION_SYNC_ADD :
                    ConfigConstants.FEATURE_OPTION_SYNC_UPDATE);
            return option;
        }));
        invokeEnovia(plmEnoviaClient::syncFeatureOption, syncDto, "PlmEnoviaClient.syncFeatureOption", (response, e) ->
                configurationTo3deWarnSender.sendSyncFeatureOptionWarn(syncDto, e != null ? e.getMessage() : GsonUtils.toJson(response))
        );
    }

}
