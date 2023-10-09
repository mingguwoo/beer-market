package com.nio.ngfs.plm.bom.configuration.application.command.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractCommand;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductConfigFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncAddPcDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncSelectPcOptionDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncUnselectPcOptionDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka.SyncProductConfigKafkaCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka.SyncProductConfigKafkaDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka.SyncProductConfigOptionKafkaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Component
@RequiredArgsConstructor
public class SyncProductConfigCommand extends AbstractCommand<SyncProductConfigKafkaCmd, Void> {

    private final ProductConfigFacade productConfigFacade;

    @Override
    protected Void executeCommand(SyncProductConfigKafkaCmd cmd) {
        // 处理PC同步
        if (cmd.getSyncProductConfigKafkaDto() != null) {
            SyncProductConfigKafkaDto syncProductConfigKafkaDto = cmd.getSyncProductConfigKafkaDto();
            SyncAddPcDto syncAddPcDto = new SyncAddPcDto();
            BeanUtils.copyProperties(syncProductConfigKafkaDto, syncAddPcDto);
            productConfigFacade.syncAddPcToEnovia(syncAddPcDto);
        }
        // 处理ProductConfig打点同步
        if (cmd.getSyncProductConfigOptionKafkaDto() != null) {
            SyncProductConfigOptionKafkaDto syncProductConfigOptionKafkaDto = cmd.getSyncProductConfigOptionKafkaDto();
            if (syncProductConfigOptionKafkaDto.isSelect()) {
                // 同步ProductConfig勾选到3DE
                SyncSelectPcOptionDto syncDto = new SyncSelectPcOptionDto();
                syncDto.setPcId(syncProductConfigOptionKafkaDto.getPcId());
                syncDto.setOptionCode(syncProductConfigOptionKafkaDto.getOptionCode());
                syncDto.setFeatureCode(syncProductConfigOptionKafkaDto.getFeatureCode());
                productConfigFacade.syncSelectPcOptionToEnovia(syncDto);
            } else {
                // 同步ProductConfig取消勾选到3DE
                SyncUnselectPcOptionDto syncDto = new SyncUnselectPcOptionDto();
                syncDto.setPcId(syncProductConfigOptionKafkaDto.getPcId());
                syncDto.setOptionCode(syncProductConfigOptionKafkaDto.getOptionCode());
                syncDto.setFeatureCode(syncProductConfigOptionKafkaDto.getFeatureCode());
                productConfigFacade.syncUnselectPcOptionToEnovia(syncDto);
            }
        }
        return null;
    }

}
