package com.nio.ngfs.plm.bom.configuration.application.event.productcontext;

import cn.hutool.core.collection.CollectionUtil;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event.SyncProductContextEvent;
import com.nio.ngfs.plm.bom.configuration.infrastructure.kafka.KafkaSender;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.kafka.*;
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

    private final KafkaSender kafkaSender;

    @Override
    @Async("eventExecutor")
    public void onApplicationEvent(SyncProductContextEvent event) {

        if(CollectionUtils.isEmpty(event.getProductContextAggrlist()) && CollectionUtils.isEmpty(event.getProductContextFeatureAggrList())){
            return;
        }
        List<SyncProductContextModelFeatureKafkaCmd> productContextModelFeatureDtoList = new ArrayList<>();
        SyncProductContextModelFeatureOptionKafkaCmd syncProductContextModelFeatureOptionKafkaCmd = new SyncProductContextModelFeatureOptionKafkaCmd();
        buildData(event,productContextModelFeatureDtoList, syncProductContextModelFeatureOptionKafkaCmd);
        try{
            if (CollectionUtils.isNotEmpty(productContextModelFeatureDtoList)){
                productContextModelFeatureDtoList.forEach(modelFeature->{
                    SyncProductContextKafkaCmd kafkaCmd = new SyncProductContextKafkaCmd();
                    kafkaCmd.setSyncProductContextModelFeatureKafkaCmd(modelFeature);
                    kafkaSender.sendSyncProductContextFeature(kafkaCmd);
                });
            }
        } catch (Exception e) {
            log.error("Kafka sendSyncProductContext error", e);
        }
        if (CollectionUtils.isNotEmpty(syncProductContextModelFeatureOptionKafkaCmd.getFeature())){
            SyncProductContextKafkaCmd kafkaCmd = new SyncProductContextKafkaCmd();
            kafkaCmd.setSyncProductContextModelFeatureOptionKafkaCmd(syncProductContextModelFeatureOptionKafkaCmd);
            kafkaSender.sendSyncProductContextFeatureOption(kafkaCmd);
        }
    }

    public void buildData(SyncProductContextEvent event, List<SyncProductContextModelFeatureKafkaCmd> modelFeatureList, SyncProductContextModelFeatureOptionKafkaCmd kafkaCmd) {
        Set<String> existFeature = new HashSet<>();
        Map<String, SyncProductContextFeatureKafkaCmd> codeFeatureMap = new HashMap<>();
        List<SyncProductContextFeatureKafkaCmd> featureList = new ArrayList<>();
        //去重用，防止model year不同但code相同从而重复添加的情况
        Set<String> optionSet = new HashSet<>();
        kafkaCmd.setFeature(featureList);
        if (!event.getProductContextAggrlist().isEmpty()){
            kafkaCmd.setModel(event.getProductContextAggrlist().get(0).getModelCode());
        }
        else{
            kafkaCmd.setModel(event.getProductContextFeatureAggrList().get(0).getModelCode());
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
                //如果没有该feature，需要新建feature记录和featureOption中的optionList
                if (!existFeature.contains(aggr.getFeatureCode())){
                    existFeature.add(aggr.getFeatureCode());
                    //新建打点中的Feature记录
                    SyncProductContextFeatureKafkaCmd featureDto = new SyncProductContextFeatureKafkaCmd();
                    List<SyncProductContextOptionKafkaCmd> optionList = new ArrayList<>();
                    SyncProductContextOptionKafkaCmd optionDto = new SyncProductContextOptionKafkaCmd();
                    optionDto.setOptionCode(aggr.getOptionCode());
                    optionList.add(optionDto);
                    featureDto.setOption(optionList);
                    featureDto.setFeatureCode(aggr.getFeatureCode());
                    codeFeatureMap.put(aggr.getFeatureCode(),featureDto);
                    kafkaCmd.getFeature().add(featureDto);
                    existFeature.add(aggr.getFeatureCode());
                    optionSet.add(aggr.getOptionCode());
                }
                else{
                    //option记录
                    if (!optionSet.contains(aggr.getOptionCode())){
                        SyncProductContextOptionKafkaCmd dto = new SyncProductContextOptionKafkaCmd();
                        dto.setOptionCode(aggr.getOptionCode());
                        codeFeatureMap.get(aggr.getFeatureCode()).getOption().add(dto);
                        optionSet.add(aggr.getOptionCode());
                    }
                }
            });
        }
    }
}
