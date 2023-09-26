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
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.enums.ProductContextFeatureEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.feature.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    public void addProductContext(OxoListRespDto OxoListRespDto, String owner) {
        //预防只有行信息却没有打点的情况，这种情况基本不存在，但以防万一，在此做判断。这种情况下不生成product context，直接返回。
        if (OxoListRespDto.getOxoHeadResps().isEmpty()){
            return;
        }
        String modelCode = OxoListRespDto.getOxoHeadResps().get(CommonConstants.INT_ZERO).getModelCode();
        List<String> modelYearList = modelFacade.getModelYearByModel(modelCode);

        //AF00相关信息初始化
        FeatureAggr featureModelYearAggr = featureDomainService.getAndCheckFeatureAggr(ConfigConstants.FEATURE_CODE_AF00, FeatureTypeEnum.FEATURE);
        Map<String,String> modelYearMap = new HashMap<>();

        //获取已有记录
        List<ProductContextAggr> productContextList =  productContextRepository.queryByModelCode(modelCode);
        List<ProductContextFeatureAggr> productContextFeatureList = productContextFeatureRepository.queryByModelCode(modelCode);

        //记录feature和feature下的所有option
        List<OxoRowsQry> featureList = OxoListRespDto.getOxoRowsResps();
        Map<OxoRowsQry,List<OxoRowsQry>> featureOptionMap = new HashMap<>();
        featureList.forEach(featureRow->featureOptionMap.put(featureRow,featureRow.getOptions()));

        List<ProductContextAggr> addProductContextAggrList = new ArrayList<>();
        List<ProductContextFeatureAggr> addProductContextFeatureAggrList = new ArrayList<>();
        List<ProductContextAggr> removeProductContextAggrList = new ArrayList<>();
        Set<ProductContextAggr> existProductContextSet = new HashSet<>();
        //更新
        //先处理其他的
        ProductContextFeatureFactory.createProductContextFeatureList(productContextFeatureList,featureList,featureOptionMap,modelCode,addProductContextFeatureAggrList,owner);
        ProductContextFactory.createProductContextList(productContextList,featureList,OxoListRespDto,addProductContextAggrList,removeProductContextAggrList,existProductContextSet,owner);
        //单独处理AF00
        ProductContextFeatureFactory.createModelYearProductContextFeature(productContextFeatureList,featureModelYearAggr,modelCode,modelYearMap,addProductContextFeatureAggrList,owner,modelYearList);
        ProductContextFactory.createModelYearProductContext(modelCode,modelYearList,modelYearMap,addProductContextAggrList,removeProductContextAggrList,existProductContextSet,owner);
        //去重
        addProductContextFeatureAggrList = addProductContextFeatureAggrList.stream().distinct().collect(Collectors.toList());
        addProductContextAggrList = addProductContextAggrList.stream().distinct().collect(Collectors.toList());
        removeProductContextAggrList = removeProductContextAggrList.stream().distinct().collect(Collectors.toList());
        //存库
        if (!addProductContextAggrList.isEmpty() || !addProductContextFeatureAggrList.isEmpty() || !removeProductContextAggrList.isEmpty()){
            saveProductContextToDb(addProductContextAggrList,addProductContextFeatureAggrList,removeProductContextAggrList);
            //3de同步
            if (!addProductContextAggrList.isEmpty() || !addProductContextFeatureAggrList.isEmpty()){
                buildSyncData(featureOptionMap,addProductContextAggrList,addProductContextFeatureAggrList);
                //只传feature，不传option类型的行
                addProductContextFeatureAggrList = addProductContextFeatureAggrList.stream().filter(aggr->Objects.equals(aggr.getType(), ProductContextFeatureEnum.FEATURE.getType())).collect(Collectors.toList());
                eventPublisher.publish(new SyncProductContextEvent(addProductContextAggrList,addProductContextFeatureAggrList));
            }
        }
    }

    //对要下发3de的数据进行处理
    private void buildSyncData(Map<OxoRowsQry,List<OxoRowsQry>> featureOptionMap,List<ProductContextAggr> addProductContextAggrList, List<ProductContextFeatureAggr> addProductContextFeatureAggrList){
        Set<String> selectedPoint = addProductContextAggrList.stream().map(aggr->aggr.getOptionCode()).collect(Collectors.toSet());
        Map<String,String> optionFeatureCodeMap = new HashMap<>();
        featureOptionMap.forEach((k,v)->{
            featureOptionMap.get(k).forEach(option->{
                optionFeatureCodeMap.put(option.getFeatureCode(),k.getFeatureCode());
            });
        });
        if (!addProductContextFeatureAggrList.isEmpty()){
            //先处理未打勾的option，需要也加到同步option中去
            addProductContextFeatureAggrList.forEach(aggr->{
                    if (Objects.equals(aggr.getType(),ProductContextFeatureEnum.OPTION.getType()) && !selectedPoint.contains(aggr.getFeatureCode())){
                        ProductContextAggr contextAggr = new ProductContextAggr();
                        contextAggr.setModelCode(aggr.getModelCode());
                        contextAggr.setOptionCode(aggr.getFeatureCode());
                        contextAggr.setFeatureCode(optionFeatureCodeMap.get(aggr.getFeatureCode()));
                        addProductContextAggrList.add(contextAggr);
                    }
            });
        }
    }

    @Override
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
