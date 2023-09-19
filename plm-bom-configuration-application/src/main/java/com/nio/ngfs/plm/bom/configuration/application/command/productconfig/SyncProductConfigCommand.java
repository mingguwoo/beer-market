package com.nio.ngfs.plm.bom.configuration.application.command.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractCommand;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductConfigFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncSelectPcOptionDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncUnselectPcOptionDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.kafka.SyncProductConfigOptionKafkaCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Component
@RequiredArgsConstructor
public class SyncProductConfigCommand extends AbstractCommand<SyncProductConfigOptionKafkaCmd, Void> {

    private final ProductConfigFacade productConfigFacade;

    @Override
    protected Void executeCommand(SyncProductConfigOptionKafkaCmd cmd) {
        if (cmd.isSelect()) {
            // 同步ProductConfig勾选到3DE
            SyncSelectPcOptionDto syncDto = new SyncSelectPcOptionDto();
            syncDto.setPcId(cmd.getPcId());
            syncDto.setOptionCode(cmd.getOptionCode());
            syncDto.setFeatureCode(cmd.getFeatureCode());
            productConfigFacade.syncSelectPcOptionToEnovia(syncDto);
        } else {
            // 同步ProductConfig取消勾选到3DE
            SyncUnselectPcOptionDto syncDto = new SyncUnselectPcOptionDto();
            syncDto.setPcId(cmd.getPcId());
            syncDto.setOptionCode(cmd.getOptionCode());
            syncDto.setFeatureCode(cmd.getFeatureCode());
            productConfigFacade.syncUnselectPcOptionToEnovia(syncDto);
        }
        return null;
    }

}
