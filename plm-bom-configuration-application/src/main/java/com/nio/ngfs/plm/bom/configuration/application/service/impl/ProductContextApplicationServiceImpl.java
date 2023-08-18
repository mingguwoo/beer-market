package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductContextApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ModelFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@Service
@RequiredArgsConstructor
public class ProductContextApplicationServiceImpl implements ProductContextApplicationService {

    private final OxoVersionSnapshotDomainService oxoVersionSnapshotDomainService;
    private final FeatureDomainService featureDomainService;
    private final FeatureRepository featureRepository;
    private final ProductContextRepository productContextRepository;
    private final ProductContextFeatureRepository productContextFeatureRepository;
    private final ModelFacade modelFacade;

    @Override
    public void addProductContext(String oxoSnapShot) {
        OxoListQry oxoListQry = oxoVersionSnapshotDomainService.resolveSnapShot(oxoSnapShot);
        String modelCode = oxoListQry.getOxoHeadResps().get(CommonConstants.INT_ZERO).getModelCode();
        List<String> modelYearList = modelFacade.getModelYearByModel(modelCode);
        List<ProductContextFeatureAggr> productContextFeatureAggrList = new ArrayList<>();
        List<ProductContextAggr> productContextAggrList = new ArrayList<>();

        //AF00相关信息初始化
        FeatureAggr featureModelYearAggr = featureDomainService.getAndCheckFeatureAggr(ConfigConstants.FEATURE_CODE_AF00, FeatureTypeEnum.FEATURE);
        Map<String,String> modelYearMap = new HashMap<>();

        //获取已有记录已有记录
        List<ProductContextAggr> oldProductContextList =  productContextRepository.queryByModelCode(modelCode);
        List<ProductContextFeatureAggr> oldProductContextFeatureList = productContextFeatureRepository.queryByModelCode(modelCode);

        //记录feature和feature下的所有option
        List<OxoRowsQry> featureList = oxoListQry.getOxoRowsResps();
        Map<OxoRowsQry,List<OxoRowsQry>> featureOptionMap = new HashMap<>();
        oxoListQry.getOxoRowsResps().forEach(featureRow->featureOptionMap.put(featureRow,featureRow.getOptions()));

        //获取groupCode与groupId的对应关系
        Map<String,Long> featureGroupMap = new HashMap<>();
        List<FeatureAggr> groupList = featureRepository.getGroupList();
        groupList.forEach(group->featureGroupMap.put(group.getFeatureId().getFeatureCode(),group.getId()));

        //没有该model的product context，直接新增
        if (oldProductContextList.isEmpty()){
            //先处理其他的
            ProductContextFeatureFactory.createProductContextFeatureList(productContextFeatureAggrList,featureList,featureOptionMap,modelCode,featureGroupMap);
            ProductContextFactory.createProductContextList(productContextAggrList,featureList,oxoListQry);
            //单独处理AF00
            ProductContextFeatureFactory.createModelYearProductContextFeature(productContextFeatureAggrList,featureModelYearAggr,modelCode,modelYearMap);
            ProductContextFactory.createModelYearProductContext(productContextAggrList,modelCode,modelYearList,modelYearMap);
        }
        //有该model的product context，检查更新
        else{
            List<String> addedModelYearList= new ArrayList<>();
            //先处理其他的
            ProductContextFeatureFactory.createAddedProductContextFeatureList(productContextFeatureAggrList,oldProductContextFeatureList,featureList,featureOptionMap,modelCode,featureGroupMap);
            ProductContextFactory.createAddedProductContextList(productContextAggrList,oldProductContextList,featureList,oxoListQry);
            //单独处理AF00
            ProductContextFeatureFactory.createAddedModelYearProductContextFeature(productContextFeatureAggrList,featureModelYearAggr,modelCode,modelYearMap,oldProductContextFeatureList,addedModelYearList);
            ProductContextFactory.createAddedModelYearProductContextFeature(productContextAggrList,oldProductContextList,modelCode,modelYearList,modelYearMap);
        }
        productContextRepository.batchSave(productContextAggrList);
        productContextFeatureRepository.batchSave(productContextFeatureAggrList);

    }
}
