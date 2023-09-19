package com.nio.ngfs.plm.bom.configuration.application.command.productcontext;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractCommand;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductContextFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.kafka.SyncProductContextModelFeatureKafkaCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/9/15
 */
@Component
@RequiredArgsConstructor
public class SyncProductContextCommand extends AbstractCommand<SyncProductContextModelFeatureKafkaCmd, Void> {

    private final ProductContextFacade productContextFacade;

    @Override
    protected Void executeCommand(SyncProductContextModelFeatureKafkaCmd kafkaCmd) {
        SyncProductContextModelFeatureDto dto = new SyncProductContextModelFeatureDto();
        dto.setMay(kafkaCmd.getMay());
        dto.setFeatureSeq(kafkaCmd.getFeatureSeq());
        dto.setFeatureCode(kafkaCmd.getFeatureCode());
        dto.setModelCodeList(kafkaCmd.getModelCodeList());
        productContextFacade.syncAddProductContextModelFeatureToEnovia(dto);
        return null;
    }
}
