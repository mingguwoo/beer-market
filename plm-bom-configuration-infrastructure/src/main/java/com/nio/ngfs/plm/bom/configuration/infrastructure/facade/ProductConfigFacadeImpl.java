package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;

import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductConfigFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncAddPcDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncDeletePcDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncUpdatePcDto;
import com.nio.ngfs.plm.bom.configuration.infrastructure.common.warn.ConfigurationTo3deWarnSender;
import com.nio.ngfs.plm.bom.configuration.infrastructure.facade.common.AbstractEnoviaFacade;
import com.nio.ngfs.plm.bom.configuration.remote.PlmEnoviaClient;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.PlmDeletePcDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.PlmModifyPcDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.PlmSyncProductConfigurationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/8/18
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConfigFacadeImpl extends AbstractEnoviaFacade implements ProductConfigFacade {

    private final PlmEnoviaClient plmEnoviaClient;
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

    private PlmSyncProductConfigurationDto buildPlmSyncProductConfigurationDto(SyncAddPcDto dto) {
        PlmSyncProductConfigurationDto syncProductConfigurationDto = new PlmSyncProductConfigurationDto();
        BeanUtils.copyProperties(dto, syncProductConfigurationDto);
        syncProductConfigurationDto.setModelYear(dto.getModel() + " " + dto.getModelYear());
        syncProductConfigurationDto.setRevision(dto.getModelYear());
        syncProductConfigurationDto.setOwner(dto.getCreateUser());
        syncProductConfigurationDto.setBrandName(dto.getBrand());
        return syncProductConfigurationDto;
    }

    private PlmModifyPcDto buildPlmModifyPcDto(SyncUpdatePcDto dto) {
        PlmModifyPcDto modifyPcDto = new PlmModifyPcDto();
        modifyPcDto.setPcId(dto.getPcId());
        modifyPcDto.setName(dto.getName());
        return modifyPcDto;
    }

    private PlmDeletePcDto buildPlmDeletePcDto(SyncDeletePcDto dto) {
        PlmDeletePcDto deletePcDto = new PlmDeletePcDto();
        deletePcDto.setPcId(dto.getPcId());
        return deletePcDto;
    }

}
