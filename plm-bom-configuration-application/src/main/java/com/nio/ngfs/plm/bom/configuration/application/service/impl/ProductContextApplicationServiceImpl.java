package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductContextApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureFactory;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    private final ProductContextRepository productContextRepository;
    private final ProductContextFactory productContextFactory;
    private final ProductContextFeatureFactory productContextFeatureFactory;
    @Override
    public void addProductContext(OxoListQry oxoListQry) {

        //判断是否已有记录
        List<ProductContextAggr> productContextAggrs =  productContextRepository.queryByModelCode(oxoListQry.getOxoHeadResps().get(CommonConstants.INT_ZERO).getModelCode());
        //记录feature和feature下的所有option
        List<OxoRowsQry> featureList = oxoListQry.getOxoRowsResps();
        Map<OxoRowsQry,List<OxoRowsQry>> featureOptionMap = new HashMap<>();
        oxoListQry.getOxoRowsResps().forEach(featureRow->featureOptionMap.put(featureRow,featureRow.getOptions()));
        //没有该model的product context，直接新增
        if (productContextAggrs.isEmpty()){
            //先处理其他的
            List<ProductContextFeatureAggr> productContextFeatureAggrList =  productContextFeatureFactory.createProductContextFeatureList(featureList,featureOptionMap,oxoListQry);
            List<ProductContextAggr> productContextAggrList = productContextFactory.createProductContextListFromOxo(featureList,featureOptionMap,oxoListQry);
        }
    }
}
