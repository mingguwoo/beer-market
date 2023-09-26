package com.nio.ngfs.plm.bom.configuration.application.event.productcontext;

import cn.hutool.core.collection.CollectionUtil;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductContextFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextFeatureDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextModelFeatureOptionDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextOptionDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event.SyncProductContextEvent;
import com.nio.ngfs.plm.bom.configuration.infrastructure.kafka.KafkaSender;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.kafka.SyncProductContextModelFeatureKafkaCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SyncProductContextEventHandler implements EventHandler<SyncProductContextEvent> {

    private final ProductContextFacade productContextFacade;
    private final KafkaSender kafkaSender;

    @Override
    @Async("eventExecutor")
    public void onApplicationEvent(SyncProductContextEvent event) {

        if(CollectionUtils.isEmpty(event.getProductContextAggrlist()) && CollectionUtils.isEmpty(event.getProductContextFeatureAggrList())){
            return;
        }
        List<SyncProductContextModelFeatureKafkaCmd> productContextModelFeatureDtoList = new ArrayList<>();
        SyncProductContextModelFeatureOptionDto syncProductContextModelFeatureOptionDto = new SyncProductContextModelFeatureOptionDto();
        buildData(event,productContextModelFeatureDtoList, syncProductContextModelFeatureOptionDto);
        try{
            if (CollectionUtils.isNotEmpty(productContextModelFeatureDtoList)){
                productContextModelFeatureDtoList.forEach(modelFeature->{
                    kafkaSender.sendSyncProductContextFeature(modelFeature);
                });
            }
        } catch (Exception e) {
            log.error("Kafka sendSyncProductContext error", e);
        }
        if (CollectionUtils.isNotEmpty(syncProductContextModelFeatureOptionDto.getFeature())){
            productContextFacade.syncAddProductContextModelFeatureOptionToEnovia(syncProductContextModelFeatureOptionDto);
        }
    }

    public void buildData(SyncProductContextEvent event, List<SyncProductContextModelFeatureKafkaCmd> modelFeatureList, SyncProductContextModelFeatureOptionDto modelFeatureOption) {
        Set<String> existFeature = new HashSet<>();
        Map<String, SyncProductContextFeatureDto> codeFeatureMap = new HashMap<>();
        List<SyncProductContextFeatureDto> featureList = new ArrayList<>();
        modelFeatureOption.setFeature(featureList);
        if (!event.getProductContextAggrlist().isEmpty()){
            modelFeatureOption.setModel(event.getProductContextAggrlist().get(0).getModelCode());
        }
        else{
            modelFeatureOption.setModel(event.getProductContextFeatureAggrList().get(0).getModelCode());
        }
        if (!event.getProductContextFeatureAggrList().isEmpty()){
            event.getProductContextFeatureAggrList().forEach(aggr->{
                //新建Feature行
                SyncProductContextModelFeatureKafkaCmd dto = new SyncProductContextModelFeatureKafkaCmd();
                dto.setFeatureCode(aggr.getFeatureCode());
                List<String> modelCodeList = CollectionUtil.newArrayList(aggr.getModelCode());
                dto.setModelCodeList(modelCodeList);
                modelFeatureList.add(dto);
            });
        }
        if (!event.getProductContextAggrlist().isEmpty()){
            event.getProductContextAggrlist().forEach(aggr->{
                //如果没有该feature，需要新建feature记录和featureOption中的featureList
                if (!existFeature.contains(aggr.getFeatureCode())){
                    existFeature.add(aggr.getFeatureCode());
                    //新建打点中的Feature记录
                    SyncProductContextFeatureDto featureDto = new SyncProductContextFeatureDto();
                    List<SyncProductContextOptionDto> optionList = new ArrayList<>();
                    SyncProductContextOptionDto optionDto = new SyncProductContextOptionDto();
                    optionDto.setOptionCode(aggr.getOptionCode());
                    optionList.add(optionDto);
                    featureDto.setOption(optionList);
                    featureDto.setFeatureCode(aggr.getFeatureCode());
                    codeFeatureMap.put(aggr.getFeatureCode(),featureDto);
                    modelFeatureOption.getFeature().add(featureDto);
                    existFeature.add(aggr.getFeatureCode());

                }
                else{
                    //option记录
                    SyncProductContextOptionDto dto = new SyncProductContextOptionDto();
                    dto.setOptionCode(aggr.getOptionCode());
                    codeFeatureMap.get(aggr.getFeatureCode()).getOption().add(dto);
                }
            });
        }
    }
}
