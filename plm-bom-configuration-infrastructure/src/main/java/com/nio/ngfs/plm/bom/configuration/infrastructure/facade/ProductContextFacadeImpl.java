package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;

import cn.hutool.core.collection.CollectionUtil;
import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductContextFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextModelFeatureOptionDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event.SyncProductContextEvent;
import com.nio.ngfs.plm.bom.configuration.infrastructure.common.warn.ConfigurationTo3deWarnSender;
import com.nio.ngfs.plm.bom.configuration.infrastructure.facade.common.AbstractEnoviaFacade;
import com.nio.ngfs.plm.bom.configuration.remote.PlmEnoviaClient;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.PlmSyncProductContextFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.PlmSyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.PlmSyncProductContextModelFeatureOptionDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.PlmSyncProductContextOptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductContextFacadeImpl extends AbstractEnoviaFacade implements ProductContextFacade {

    private final PlmEnoviaClient plmEnoviaClient;
    private final ConfigurationTo3deWarnSender configurationTo3deWarnSender;

    @Override
    public void syncAddProductContextModelFeatureOptionToEnovia(SyncProductContextModelFeatureOptionDto syncProductContextModelFeatureOptionDto) {

        PlmSyncProductContextModelFeatureOptionDto dto = buildModelFeature(syncProductContextModelFeatureOptionDto);
        invokeEnovia(plmEnoviaClient::syncProductContextModelFeatureOption, dto,"PlmEnoviaClient.syncProductContextModelFeatureOption",(response, e)->
            configurationTo3deWarnSender.sendSyncProductContextModelFeatureOptionWarn(syncProductContextModelFeatureOptionDto, e != null ? e.getMessage() : GsonUtils.toJson(response))
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
    public void syncAddProductContextModelFeatureToEnovia(SyncProductContextModelFeatureDto modelFeature) {
        PlmSyncProductContextModelFeatureDto dto = new PlmSyncProductContextModelFeatureDto();
        dto.setFeatureCode(modelFeature.getFeatureCode());
        dto.setFeatureSeq(modelFeature.getFeatureSeq());
        dto.setMay(modelFeature.getMay());
        dto.setModelCodeList(modelFeature.getModelCodeList());
        log.info("ProductContextFacade syncProductContextModelFeatureToEnovia data={}", GsonUtils.toJson(modelFeature));
        invokeEnovia(plmEnoviaClient::syncProductContextModelFeature,dto,"PlmEnoviaClient.syncProductContextModelFeature",(response,e)->{
            configurationTo3deWarnSender.sendSyncProductContextFeatureModelWarn(modelFeature,e != null ? e.getMessage() : GsonUtils.toJson(response));
        });
    }

    public void buildSyncData(SyncProductContextEvent event, List<PlmSyncProductContextModelFeatureDto> modelFeatureList, PlmSyncProductContextModelFeatureOptionDto modelFeatureOption) {
        Set<String> existFeature = new HashSet<>();
        Map<String, PlmSyncProductContextFeatureDto> codeFeatureMap = new HashMap<>();
        List<PlmSyncProductContextFeatureDto> featureList = new ArrayList<>();
        modelFeatureOption.setFeature(featureList);
        if (!event.getProductContextAggrlist().isEmpty()){
            modelFeatureOption.setModel(event.getProductContextAggrlist().get(0).getModelCode());
        }
        else{
            modelFeatureOption.setModel(event.getProductContextAggrlist().get(0).getModelCode());
        }
        if (!event.getProductContextFeatureAggrList().isEmpty()){
            event.getProductContextFeatureAggrList().forEach(aggr->{
                //新建Feature行
                PlmSyncProductContextModelFeatureDto dto = new PlmSyncProductContextModelFeatureDto();
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
                    PlmSyncProductContextFeatureDto featureDto = new PlmSyncProductContextFeatureDto();
                    List<PlmSyncProductContextOptionDto> optionList = new ArrayList<>();
                    PlmSyncProductContextOptionDto optionDto = new PlmSyncProductContextOptionDto();
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
                    PlmSyncProductContextOptionDto dto = new PlmSyncProductContextOptionDto();
                    dto.setOptionCode(aggr.getOptionCode());
                    codeFeatureMap.get(aggr.getFeatureCode()).getOption().add(dto);
                }
            });
        }
    }

}
