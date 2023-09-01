package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.FeignInvokeUtils;
import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductContextApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ModelFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductContextFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.common.warn.ConfigurationTo3deWarnSender;
import com.nio.ngfs.plm.bom.configuration.remote.PlmEnoviaClient;
import com.nio.ngfs.plm.bom.configuration.remote.dto.common.PlmEnoviaResult;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.ProductContextFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.ProductContextOptionDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.SyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.SyncProductContextModelFeatureOptionDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductContextApplicationServiceImpl implements ProductContextApplicationService {

    private final FeatureDomainService featureDomainService;
    private final ProductContextRepository productContextRepository;
    private final ProductContextFeatureRepository productContextFeatureRepository;
    private final ModelFacade modelFacade;
    private final ProductContextFacade productContextFacade;

    @Override
    public void addProductContext(OxoListQry oxoListQry, String owner) {
        String modelCode = oxoListQry.getOxoHeadResps().get(CommonConstants.INT_ZERO).getModelCode();
        List<String> modelYearList = modelFacade.getModelYearByModel(modelCode);

        //AF00相关信息初始化
        FeatureAggr featureModelYearAggr = featureDomainService.getAndCheckFeatureAggr(ConfigConstants.FEATURE_CODE_AF00, FeatureTypeEnum.FEATURE);
        Map<String,String> modelYearMap = new HashMap<>();

        //获取已有记录
        List<ProductContextAggr> productContextList =  productContextRepository.queryByModelCode(modelCode);
        List<ProductContextFeatureAggr> productContextFeatureList = productContextFeatureRepository.queryByModelCode(modelCode);

        //记录feature和feature下的所有option
        List<OxoRowsQry> featureList = oxoListQry.getOxoRowsResps();
        Map<OxoRowsQry,List<OxoRowsQry>> featureOptionMap = new HashMap<>();
        featureList.forEach(featureRow->featureOptionMap.put(featureRow,featureRow.getOptions()));

        List<ProductContextAggr> addProductContextAggrList = new ArrayList<>();
        List<ProductContextFeatureAggr> addProductContextFeatureAggrList = new ArrayList<>();
        List<ProductContextAggr> removeProductContextAggrList = new ArrayList<>();
        Set<ProductContextAggr> existProductContextSet = new HashSet<>();
        //更新
        //先处理其他的
        ProductContextFeatureFactory.createProductContextFeatureList(productContextFeatureList,featureList,featureOptionMap,modelCode,addProductContextFeatureAggrList,owner);
        ProductContextFactory.createProductContextList(productContextList,featureList,oxoListQry,addProductContextAggrList,removeProductContextAggrList,existProductContextSet,owner);
        //单独处理AF00
        ProductContextFeatureFactory.createModelYearProductContextFeature(productContextFeatureList,featureModelYearAggr,modelCode,modelYearMap,addProductContextFeatureAggrList,owner);
        ProductContextFactory.createModelYearProductContext(modelCode,modelYearList,modelYearMap,addProductContextAggrList,removeProductContextAggrList,existProductContextSet,owner);
        //去重
        addProductContextFeatureAggrList = addProductContextFeatureAggrList.stream().distinct().toList();
        addProductContextAggrList = addProductContextAggrList.stream().distinct().toList();
        removeProductContextAggrList = removeProductContextAggrList.stream().distinct().toList();
        //存库
        if (!addProductContextAggrList.isEmpty() || !addProductContextFeatureAggrList.isEmpty() || !removeProductContextAggrList.isEmpty()){
            saveProductContextToDb(addProductContextAggrList,addProductContextFeatureAggrList,removeProductContextAggrList);
            //3de同步
            if (!addProductContextAggrList.isEmpty() || !addProductContextFeatureAggrList.isEmpty()){
                syncTo3de(addProductContextAggrList,addProductContextFeatureAggrList);
            }
        }
    }

    private void syncTo3de(List<ProductContextAggr> addProductContextAggrList,List<ProductContextFeatureAggr> addProductContextFeatureAggrList){
        List<SyncProductContextModelFeatureDto> modelFeatureList = new ArrayList<>();
        SyncProductContextModelFeatureOptionDto modelFeatureOption = new SyncProductContextModelFeatureOptionDto();
        buildSyncData(addProductContextAggrList,addProductContextFeatureAggrList,modelFeatureList,modelFeatureOption);
        modelFeatureList.forEach(modelFeature-> {
            productContextFacade.syncAddProductContextModelFeatureToEnovia(modelFeature);
        });
        productContextFacade.syncAddProductContextModelFeatureOptionToEnovia(modelFeatureOption);

    }
    @Transactional(rollbackFor = Exception.class)
    public void saveProductContextToDb(List<ProductContextAggr> productContextList, List<ProductContextFeatureAggr> productContextFeatureList,List<ProductContextAggr> removeProductContextAggrList) {
        if (!productContextList.isEmpty()){
            productContextRepository.addOrUpdateBatch(productContextList);
        }
        if (!productContextFeatureList.isEmpty()){
            productContextFeatureRepository.saveBatch(productContextFeatureList);
        }
        if (!removeProductContextAggrList.isEmpty()){
            productContextRepository.removeBatchByIds(removeProductContextAggrList);
        }
    }

    private void buildSyncData(List<ProductContextAggr> addProductContextAggrList,List<ProductContextFeatureAggr> addProductContextFeatureAggrList,List<SyncProductContextModelFeatureDto> modelFeatureList,SyncProductContextModelFeatureOptionDto modelFeatureOption) {
        Set<String> existFeature = new HashSet<>();
        Map<String, ProductContextFeatureDto> codeFeatureMap = new HashMap<>();
        List<ProductContextFeatureDto> featureList = new ArrayList<>();
        modelFeatureOption.setFeature(featureList);
        if (!addProductContextAggrList.isEmpty()){
            modelFeatureOption.setModel(addProductContextAggrList.get(0).getModelCode());
        }
        else{
            modelFeatureOption.setModel(addProductContextFeatureAggrList.get(0).getModelCode());
        }
        if (!addProductContextFeatureAggrList.isEmpty()){
            addProductContextFeatureAggrList.forEach(aggr->{
                //新建Feature行
                SyncProductContextModelFeatureDto dto = new SyncProductContextModelFeatureDto();
                dto.setFeatureCode(aggr.getFeatureCode());
                List<String> modelCodeList = CollectionUtil.newArrayList(aggr.getModelCode());
                dto.setModelCodeList(modelCodeList);
                modelFeatureList.add(dto);
            });
        }
        if (!addProductContextAggrList.isEmpty()){
            addProductContextAggrList.forEach(aggr->{
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
