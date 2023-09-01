package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.nio.bom.share.constants.CommonConstants;
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
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event.SyncProductContextEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureDomainService;
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
    private final EventPublisher eventPublisher;

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
                eventPublisher.publish(new SyncProductContextEvent(addProductContextAggrList,addProductContextFeatureAggrList));
            }
        }
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

}
