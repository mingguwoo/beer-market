package com.sh.beer.market.infrastructure.facade;


import com.sh.beer.market.domain.facade.FeatureFacade;
import com.sh.beer.market.infrastructure.facade.common.AbstractEnoviaFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/8/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductContextFacadeImpl extends AbstractEnoviaFacade implements FeatureFacade {

    /*private final PlmEnoviaClient plmEnoviaClient;
    private final ConfigurationTo3deWarnSender configurationTo3deWarnSender;

    @Override
    public void syncAddProductContextModelFeatureOptionToEnovia(SyncProductContextModelFeatureOptionDto syncProductContextModelFeatureOptionDto) {

        PlmSyncProductContextModelFeatureOptionDto dto = buildModelFeature(syncProductContextModelFeatureOptionDto);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        log.info("ProductContextFacade syncProductContextModelFeatureOptionToEnovia data={}", gson.toJson(syncProductContextModelFeatureOptionDto));
        invokeEnovia(plmEnoviaClient::syncProductContextModelFeatureOption, dto,"PlmEnoviaClient.syncProductContextModelFeatureOption",(response, e)->
            configurationTo3deWarnSender.sendSyncProductContextModelFeatureOptionWarn(syncProductContextModelFeatureOptionDto, e != null ? e.getMessage() : gson.toJson(response))
        );
    }

    private PlmSyncProductContextModelFeatureOptionDto buildModelFeature(SyncProductContextModelFeatureOptionDto syncProductContextModelFeatureOptionDto) {
        PlmSyncProductContextModelFeatureOptionDto dto = new PlmSyncProductContextModelFeatureOptionDto();
        dto.setModel(syncProductContextModelFeatureOptionDto.getModel());
        List<PlmSyncProductContextFeatureDto> featureList = new ArrayList<>();
        syncProductContextModelFeatureOptionDto.getFeature().forEach(feature->{
            PlmSyncProductContextFeatureDto featureDto = new PlmSyncProductContextFeatureDto();
            featureDto.setFeatureCode(feature.getFeatureCode());
            List<PlmSyncProductContextOptionDto> optionList = new ArrayList<>();
            feature.getOption().forEach(option->{
                PlmSyncProductContextOptionDto optionDto = new PlmSyncProductContextOptionDto();
                optionDto.setOptionCode(option.getOptionCode());
                optionList.add(optionDto);
            });
            featureDto.setOption(optionList);
            featureList.add(featureDto);
        });
        dto.setFeature(featureList);
        return dto;
    }

    @Override
    public void syncAddProductContextModelFeatureToEnovia(SyncProductContextModelFeatureDto modelFeature) {
        PlmSyncProductContextModelFeatureDto dto = new PlmSyncProductContextModelFeatureDto();
        dto.setFeatureCode(modelFeature.getFeatureCode());
        dto.setFeatureSeq(modelFeature.getFeatureSeq());
        dto.setMay(modelFeature.getMay());
        dto.setModelCodeList(modelFeature.getModelCodeList());
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        log.info("ProductContextFacade syncProductContextModelFeatureToEnovia data={}", gson.toJson(modelFeature));
        invokeEnovia(plmEnoviaClient::syncProductContextModelFeature,dto,"PlmEnoviaClient.syncProductContextModelFeature",(response,e)->{
            configurationTo3deWarnSender.sendSyncProductContextFeatureModelWarn(modelFeature,e != null ? e.getMessage() : gson.toJson(response));
        });
    }*/

}
