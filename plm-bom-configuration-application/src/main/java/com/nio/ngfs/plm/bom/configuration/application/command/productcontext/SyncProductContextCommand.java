package com.nio.ngfs.plm.bom.configuration.application.command.productcontext;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractCommand;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductContextFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextFeatureDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextModelFeatureOptionDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextOptionDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.kafka.SyncProductContextFeatureKafkaCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.kafka.SyncProductContextKafkaCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.kafka.SyncProductContextOptionKafkaCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/9/15
 */
@Component
@RequiredArgsConstructor
public class SyncProductContextCommand extends AbstractCommand<SyncProductContextKafkaCmd, Void> {

    private final ProductContextFacade productContextFacade;

    @Override
    protected Void executeCommand(SyncProductContextKafkaCmd kafkaCmd) {
        if (Objects.nonNull(kafkaCmd.getSyncProductContextModelFeatureKafkaCmd())){
            SyncProductContextModelFeatureDto dto = new SyncProductContextModelFeatureDto();
            dto.setMay(kafkaCmd.getSyncProductContextModelFeatureKafkaCmd().getMay());
            dto.setFeatureSeq(kafkaCmd.getSyncProductContextModelFeatureKafkaCmd().getFeatureSeq());
            dto.setFeatureCode(kafkaCmd.getSyncProductContextModelFeatureKafkaCmd().getFeatureCode());
            dto.setModelCodeList(kafkaCmd.getSyncProductContextModelFeatureKafkaCmd().getModelCodeList());
            productContextFacade.syncAddProductContextModelFeatureToEnovia(dto);
        }
        if (Objects.nonNull(kafkaCmd.getSyncProductContextModelFeatureOptionKafkaCmd())){
            SyncProductContextModelFeatureOptionDto dto = new SyncProductContextModelFeatureOptionDto();
            dto.setModel(kafkaCmd.getSyncProductContextModelFeatureOptionKafkaCmd().getModel());
            List<SyncProductContextFeatureDto> featureList = new ArrayList<>();
            kafkaCmd.getSyncProductContextModelFeatureOptionKafkaCmd().getFeature().forEach(feature->{
                SyncProductContextFeatureDto featureDto = new SyncProductContextFeatureDto();
                featureDto.setFeatureCode(feature.getFeatureCode());
                List<SyncProductContextOptionDto> optionList = new ArrayList<>();
                feature.getOption().forEach(option->{
                    SyncProductContextOptionDto optionDto = new SyncProductContextOptionDto();
                    optionDto.setOptionCode(option.getOptionCode());
                    optionList.add(optionDto);
                });
                featureDto.setOption(optionList);
                featureList.add(featureDto);
            });
            dto.setFeature(featureList);
            productContextFacade.syncAddProductContextModelFeatureOptionToEnovia(dto);
        }
        return null;
    }
}
