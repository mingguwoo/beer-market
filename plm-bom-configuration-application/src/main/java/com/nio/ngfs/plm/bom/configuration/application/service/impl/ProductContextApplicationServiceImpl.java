package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductContextApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ModelFacade;
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
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ProductContextRepository productContextRepository;
    private final ProductContextFeatureRepository productContextFeatureRepository;
    private final ModelFacade modelFacade;
    private final EventPublisher eventPublisher;
    @Override
    public void addProductContext(String oxoSnapShot) {
        OxoListQry oxoListQry = oxoVersionSnapshotDomainService.resolveSnapShot(oxoSnapShot);
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

        //由于只增不减，因此可以通过判断size是否变化来判断是否有新增数据，是否需要存库。
        int originalProductContextSize = productContextList.size();
        int originalProductContextFeatureSize = productContextFeatureList.size();

        List<ProductContextAggr> addProductContextAggrList = new ArrayList<>();
        List<ProductContextFeatureAggr> addProductContextFeatureAggrList = new ArrayList<>();
        //更新
        //先处理其他的
        ProductContextFeatureFactory.createProductContextFeatureList(productContextFeatureList,featureList,featureOptionMap,modelCode,addProductContextFeatureAggrList);
        ProductContextFactory.createProductContextList(productContextList,featureList,oxoListQry,addProductContextAggrList);
        //单独处理AF00
        ProductContextFeatureFactory.createModelYearProductContextFeature(productContextFeatureList,featureModelYearAggr,modelCode,modelYearMap,addProductContextFeatureAggrList);
        ProductContextFactory.createModelYearProductContext(productContextList,modelCode,modelYearList,modelYearMap,addProductContextAggrList);
        //去重
        addProductContextFeatureAggrList = addProductContextFeatureAggrList.stream().distinct().toList();
        addProductContextAggrList = addProductContextAggrList.stream().distinct().toList();
        //存库
        if (!addProductContextAggrList.isEmpty() || !addProductContextFeatureAggrList.isEmpty() ){
            saveProductContextToDb(addProductContextAggrList,addProductContextFeatureAggrList);
            //3de同步
            eventPublisher.publish(new SyncProductContextEvent(productContextList));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveProductContextToDb(List<ProductContextAggr> productContextList, List<ProductContextFeatureAggr> productContextFeatureList) {
        productContextRepository.saveOrUpdateBatch(productContextList);
        productContextFeatureRepository.saveOrUpdateBatch(productContextFeatureList);
    }

}
