package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;

import cn.hutool.core.collection.CollectionUtil;
import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductContextFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event.SyncProductContextEvent;
import com.nio.ngfs.plm.bom.configuration.infrastructure.common.warn.ConfigurationTo3deWarnSender;
import com.nio.ngfs.plm.bom.configuration.infrastructure.facade.common.AbstractEnoviaFacade;
import com.nio.ngfs.plm.bom.configuration.remote.PlmEnoviaClient;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.*;
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
    public void syncAddProductContextToEnovia(SyncProductContextEvent event) {
        List<PlmSyncProductContextModelFeatureDto> productContextModelFeatureDtoList = new ArrayList<>();
        PlmSyncProductContextModelFeatureOptionDto productContextModelFeatureOptionDtoList = new PlmSyncProductContextModelFeatureOptionDto();
        buildSyncData(event,productContextModelFeatureDtoList,productContextModelFeatureOptionDtoList);
        productContextModelFeatureDtoList.forEach(modelFeature->{
            syncAddProductContextModelFeatureToEnovia(modelFeature);
        });
        syncAddProductContextModelFeatureOptionToEnovia(productContextModelFeatureOptionDtoList);
    }

    private void syncAddProductContextModelFeatureOptionToEnovia(PlmSyncProductContextModelFeatureOptionDto modelFeatureOption) {
        invokeEnovia(plmEnoviaClient::syncProductContextModelFeatureOption,modelFeatureOption,"PlmEnoviaClient.syncProductContextModelFeatureOption",(response,e)->
            configurationTo3deWarnSender.sendSyncProductContextModelFeatureOptionWarn(modelFeatureOption, e != null ? e.getMessage() : GsonUtils.toJson(response))
        );
    }

    private void syncAddProductContextModelFeatureToEnovia(PlmSyncProductContextModelFeatureDto modelFeature) {
        log.info("ProductContextFacade syncProductContextModelFeatureToEnovia data={}", GsonUtils.toJson(modelFeature));
        invokeEnovia(plmEnoviaClient::syncProductContextModelFeature,modelFeature,"PlmEnoviaClient.syncProductContextModelFeature",(response,e)->{
            configurationTo3deWarnSender.sendSyncProductContextFeatureModelWarn(modelFeature,e != null ? e.getMessage() : GsonUtils.toJson(response));
        });
    }

    public void buildSyncData(SyncProductContextEvent event, List<PlmSyncProductContextModelFeatureDto> modelFeatureList, PlmSyncProductContextModelFeatureOptionDto modelFeatureOption) {
        Set<String> existFeature = new HashSet<>();
        Map<String, PlmProductContextFeatureDto> codeFeatureMap = new HashMap<>();
        List<PlmProductContextFeatureDto> featureList = new ArrayList<>();
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
                    PlmProductContextFeatureDto featureDto = new PlmProductContextFeatureDto();
                    List<PlmProductContextOptionDto> optionList = new ArrayList<>();
                    PlmProductContextOptionDto optionDto = new PlmProductContextOptionDto();
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
                    PlmProductContextOptionDto dto = new PlmProductContextOptionDto();
                    dto.setOptionCode(aggr.getOptionCode());
                    codeFeatureMap.get(aggr.getFeatureCode()).getOption().add(dto);
                }
            });
        }
    }

}
