package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;

import cn.hutool.core.collection.CollectionUtil;
import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductContextFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event.SyncProductContextEvent;
import com.nio.ngfs.plm.bom.configuration.infrastructure.common.warn.ConfigurationTo3deWarnSender;
import com.nio.ngfs.plm.bom.configuration.infrastructure.facade.common.AbstractEnoviaFacade;
import com.nio.ngfs.plm.bom.configuration.remote.PlmEnoviaClient;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.ProductContextFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.ProductContextOptionDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.SyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.SyncProductContextModelFeatureOptionDto;
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
        List<SyncProductContextModelFeatureDto> modelFeatureList = new ArrayList<>();
        SyncProductContextModelFeatureOptionDto modelFeatureOption = new SyncProductContextModelFeatureOptionDto();
        buildSyncData(event,modelFeatureList,modelFeatureOption);

        modelFeatureList.forEach(modelFeature->{
            log.info("ProductContextFacade syncProductContextModelFeatureToEnovia data={}", GsonUtils.toJson(modelFeature));
            invokeEnovia(plmEnoviaClient::syncProductContextModelFeature,modelFeature,"PlmEnoviaClient.syncProductContextModelFeature",(response,e)->{
                configurationTo3deWarnSender.sendSyncProductContextFeatureModelWarn(modelFeature,e != null ? e.getMessage() : GsonUtils.toJson(response));
            });
        });
        log.info("ProductContextFacade syncProductContextFeatureOptionToEnovia data={]" , GsonUtils.toJson(modelFeatureOption));
        invokeEnovia(plmEnoviaClient::syncProductContextModelFeatureOption,modelFeatureOption,"PlmEnoviaClient.syncProductContextModelFeatureOption",(response,e)->{
            configurationTo3deWarnSender.sendSyncProductContextModelFeatureOptionWarn(modelFeatureOption, e != null ? e.getMessage() : GsonUtils.toJson(response));
        });

    }

    private void buildSyncData(SyncProductContextEvent event,List<SyncProductContextModelFeatureDto> modelFeatureList,SyncProductContextModelFeatureOptionDto modelFeatureOption) {
        Set<String> existFeature = new HashSet<>();
        Map<String, ProductContextFeatureDto> codeFeatureMap = new HashMap<>();
        List<ProductContextFeatureDto> featureList = new ArrayList<>();
        modelFeatureOption.setFeature(featureList);
        if (event.getProductContextAggrlist().isEmpty()){
            modelFeatureOption.setModel(event.getProductContextFeatureAggrList().get(0).getModelCode());
        }
        modelFeatureOption.setModel(event.getProductContextAggrlist().get(0).getModelCode());
        if (!event.getProductContextFeatureAggrList().isEmpty()){
            event.getProductContextFeatureAggrList().forEach(aggr->{
                //新建Feature行
                SyncProductContextModelFeatureDto dto = new SyncProductContextModelFeatureDto();
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
                    ProductContextFeatureDto featureDto = new ProductContextFeatureDto();
                    List<ProductContextOptionDto> optionList = new ArrayList<>();
                    ProductContextOptionDto optionDto = new ProductContextOptionDto();
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
                    ProductContextOptionDto dto = new ProductContextOptionDto();
                    dto.setOptionCode(aggr.getOptionCode());
                    codeFeatureMap.get(aggr.getFeatureCode()).getOption().add(dto);
                }
            });
        }
    }

}
