package com.nio.ngfs.plm.bom.configuration.application.event.productcontext;

import cn.hutool.core.collection.CollectionUtil;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductContextFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event.SyncProductContextEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.ProductContextFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.ProductContextOptionDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.SyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.SyncProductContextModelFeatureOptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
@Component
@RequiredArgsConstructor
public class SyncProductContextEventHandler implements EventHandler<SyncProductContextEvent> {

    private final ProductContextFacade productContextFacade;
    @Override
    @Async("commonThreadPool")
    public void onApplicationEvent(SyncProductContextEvent event) {
        List<SyncProductContextModelFeatureDto> modelFeatureList = new ArrayList<>();
        SyncProductContextModelFeatureOptionDto modelFeatureOption = new SyncProductContextModelFeatureOptionDto();
        buildSyncData(event,modelFeatureList,modelFeatureOption);
        modelFeatureList.forEach(modelFeature->{
            productContextFacade.syncAddProductContextModelFeatureToEnovia(modelFeature);
        });
        productContextFacade.syncAddProductContextModelFeatureOptionToEnovia(modelFeatureOption);
    }


    private void buildSyncData(SyncProductContextEvent event,List<SyncProductContextModelFeatureDto> modelFeatureList,SyncProductContextModelFeatureOptionDto modelFeatureOption) {
        Set<String> existFeature = new HashSet<>();
        Map<String, ProductContextFeatureDto> codeFeatureMap = new HashMap<>();
        List<ProductContextFeatureDto> featureList = new ArrayList<>();
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
