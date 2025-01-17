package com.sh.beer.market.infrastructure.facade;


import com.sh.beer.market.domain.facade.FeatureFacade;
import com.sh.beer.market.infrastructure.facade.common.AbstractEnoviaFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author
 * @date 2023/8/18
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConfigFacadeImpl extends AbstractEnoviaFacade implements FeatureFacade {

    /*private final PlmEnoviaClient plmEnoviaClient;
    private final ConfigurationTo3deWarnSender configurationTo3deWarnSender;

    @Override
    public void syncAddPcToEnovia(SyncAddPcDto dto) {
        log.info("ProductConfigFacade syncAddPcToEnovia data={}", GsonUtils.toJson(dto));
        PlmSyncProductConfigurationDto syncDto = buildPlmSyncProductConfigurationDto(dto);
        invokeEnovia(plmEnoviaClient::syncProductConfiguration, syncDto, "PlmEnoviaClient.syncProductConfiguration", (response, e) ->
                configurationTo3deWarnSender.sendAddPcWarn(dto, syncDto, e != null ? e.getMessage() : GsonUtils.toJson(response))
        );
    }

    @Override
    public void syncUpdatePcToEnovia(SyncUpdatePcDto dto) {
        log.info("ProductConfigFacade syncUpdatePcToEnovia data={}", GsonUtils.toJson(dto));
        PlmModifyPcDto syncDto = buildPlmModifyPcDto(dto);
        invokeEnovia(plmEnoviaClient::modifyPc, syncDto, "PlmEnoviaClient.modifyPc", (response, e) ->
                configurationTo3deWarnSender.sendUpdatePcWarn(dto, syncDto, e != null ? e.getMessage() : GsonUtils.toJson(response))
        );
    }

    @Override
    public void syncDeletePcToEnovia(SyncDeletePcDto dto) {
        log.info("ProductConfigFacade syncDeletePcToEnovia data={}", GsonUtils.toJson(dto));
        PlmDeletePcDto syncDto = buildPlmDeletePcDto(dto);
        invokeEnovia(plmEnoviaClient::deletePc, syncDto, "PlmEnoviaClient.deletePc", (response, e) ->
                configurationTo3deWarnSender.sendDeletePcWarn(dto, syncDto, e != null ? e.getMessage() : GsonUtils.toJson(response))
        );
    }

    @Override
    public void syncSelectPcOptionToEnovia(SyncSelectPcOptionDto dto) {
        log.info("ProductConfigFacade syncSelectPcOptionToEnovia data={}", GsonUtils.toJson(dto));
        PlmConnectPcFeatureAndOptionDto syncDto = buildPlmConnectPcFeatureAndOptionDto(dto);
        invokeEnovia(plmEnoviaClient::connectConfigurationFeatureAndOption, Lists.newArrayList(syncDto), "PlmEnoviaClient.connectConfigurationFeatureAndOption", (response, e) ->
                configurationTo3deWarnSender.sendSelectPcOptionWarn(syncDto, e != null ? e.getMessage() : GsonUtils.toJson(response))
        );
    }

    @Override
    public void syncUnselectPcOptionToEnovia(SyncUnselectPcOptionDto dto) {
        log.info("ProductConfigFacade syncUnselectPcOptionToEnovia data={}", GsonUtils.toJson(dto));
        PlmDisconnectPcFeatureAndOptionDto syncDto = buildPlmDisconnectPcFeatureAndOptionDto(dto);
        invokeEnovia(plmEnoviaClient::disconnectConfigurationFeatureAndOption, Lists.newArrayList(syncDto), "PlmEnoviaClient.disconnectConfigurationFeatureAndOption", (response, e) ->
                configurationTo3deWarnSender.sendUnselectPcOptionWarn(syncDto, e != null ? e.getMessage() : GsonUtils.toJson(response))
        );
    }

    private PlmSyncProductConfigurationDto buildPlmSyncProductConfigurationDto(SyncAddPcDto dto) {
        PlmSyncProductConfigurationDto syncProductConfigurationDto = new PlmSyncProductConfigurationDto();
        BeanUtils.copyProperties(dto, syncProductConfigurationDto.getParamMap());
        syncProductConfigurationDto.getParamMap().setModelYear(dto.getModel() + " " + dto.getModelYear());
        syncProductConfigurationDto.getParamMap().setRevision(dto.getModelYear());
        syncProductConfigurationDto.getParamMap().setOwner(dto.getCreateUser());
        syncProductConfigurationDto.getParamMap().setBrandName(dto.getBrand());
        return syncProductConfigurationDto;
    }

    private PlmModifyPcDto buildPlmModifyPcDto(SyncUpdatePcDto dto) {
        PlmModifyPcDto modifyPcDto = new PlmModifyPcDto();
        modifyPcDto.getParamMap().setPcId(dto.getPcId());
        modifyPcDto.getParamMap().setName(dto.getName());
        return modifyPcDto;
    }

    private PlmDeletePcDto buildPlmDeletePcDto(SyncDeletePcDto dto) {
        PlmDeletePcDto deletePcDto = new PlmDeletePcDto();
        deletePcDto.getParamMap().setPcId(dto.getPcId());
        return deletePcDto;
    }

    private PlmConnectPcFeatureAndOptionDto buildPlmConnectPcFeatureAndOptionDto(SyncSelectPcOptionDto dto) {
        PlmConnectPcFeatureAndOptionDto connectPcFeatureAndOptionDto = new PlmConnectPcFeatureAndOptionDto();
        connectPcFeatureAndOptionDto.setPcId(dto.getPcId());
        connectPcFeatureAndOptionDto.setOptionCode(dto.getOptionCode());
        connectPcFeatureAndOptionDto.setFeatureCode(dto.getFeatureCode());
        return connectPcFeatureAndOptionDto;
    }

    private PlmDisconnectPcFeatureAndOptionDto buildPlmDisconnectPcFeatureAndOptionDto(SyncUnselectPcOptionDto dto) {
        PlmDisconnectPcFeatureAndOptionDto disconnectPcFeatureAndOptionDto = new PlmDisconnectPcFeatureAndOptionDto();
        disconnectPcFeatureAndOptionDto.setPcId(dto.getPcId());
        disconnectPcFeatureAndOptionDto.setOptionCode(dto.getOptionCode());
        return disconnectPcFeatureAndOptionDto;
    }*/

}
