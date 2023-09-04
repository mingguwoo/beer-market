package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.nio.bom.share.enums.YesOrNoEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductConfigOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionId;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.enums.ProductConfigOptionTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.EditProductConfigCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/9/4
 */
@Service
@RequiredArgsConstructor
public class ProductConfigOptionApplicationServiceImpl implements ProductConfigOptionApplicationService {

    @Override
    public List<ProductConfigOptionAggr> editPcOptionConfig(List<EditProductConfigCmd.PcOptionConfigDto> updatePcOptionConfigList, List<ProductConfigAggr> productConfigAggrList,
                                                            List<ProductConfigOptionAggr> productConfigOptionAggrList, List<ProductContextAggr> productContextAggrList) {
        Map<String, ProductConfigAggr> productConfigAggrMap = LambdaUtil.toKeyMap(productConfigAggrList, ProductConfigAggr::getPcId);
        Map<String, ProductContextAggr> productContextAggrMap = LambdaUtil.toKeyMap(productContextAggrList, i -> buildProductContextAggrKey(i.getOptionCode(), i.getModelYear()));
        Map<ProductConfigOptionId, ProductConfigOptionAggr> productConfigOptionAggrMap = LambdaUtil.toKeyMap(productConfigOptionAggrList, ProductConfigOptionAggr::getUniqId);
        return LambdaUtil.map(updatePcOptionConfigList, i -> {
            ProductConfigOptionAggr existProductConfigOptionAggr = productConfigOptionAggrMap.get(new ProductConfigOptionId(i.getPcId(), i.getOptionCode()));
            if (existProductConfigOptionAggr == null) {
                // 新增的勾选记录
                ProductConfigOptionAggr newProductConfigOptionAggr = ProductConfigOptionFactory.create(i.getPcId(), i.getOptionCode(), i.getFeatureCode());
                productConfigOptionAggrList.add(newProductConfigOptionAggr);
                return newProductConfigOptionAggr;
            }
            ProductConfigAggr productConfigAggr = productConfigAggrMap.get(existProductConfigOptionAggr.getPcId());
            checkEditPcOptionConfig(i, existProductConfigOptionAggr, productConfigAggr,
                    productContextAggrMap.get(buildProductContextAggrKey(existProductConfigOptionAggr.getOptionCode(), productConfigAggr.getModelYear())));
            return existProductConfigOptionAggr;
        });
    }

    @Override
    public void skipCheckBeforeEdit(List<ProductConfigAggr> productConfigAggrList, List<ProductConfigOptionAggr> productConfigOptionAggrList) {
        Map<String, List<ProductConfigOptionAggr>> productConfigOptionGroupByPc = LambdaUtil.groupBy(productConfigOptionAggrList, ProductConfigOptionAggr::getPcId);
        // skipCheck关闭状态
        productConfigAggrList.stream().filter(ProductConfigAggr::isSkipCheckClose).forEach(productConfigAggr -> {
            List<ProductConfigOptionAggr> productConfigOptionList = productConfigOptionGroupByPc.get(productConfigAggr.getPcId());
            // 按Feature分组
            LambdaUtil.groupBy(productConfigOptionList, ProductConfigOptionAggr::getFeatureCode).forEach((featureCode, optionList) -> {
                // 1个Feature下只可勾选1个Option
                if (optionList.stream().filter(ProductConfigOptionAggr::isSelect).count() > 1) {
                    throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_SKIP_CHECK_CLOSE_ERROR);
                }
            });
        });
    }

    private String buildProductContextAggrKey(String optionCode, String modelYear) {
        return String.format("%s::%s", optionCode, modelYear);
    }

    /**
     * 编辑Product Config勾选校验
     */
    private void checkEditPcOptionConfig(EditProductConfigCmd.PcOptionConfigDto pcOptionConfigDto, ProductConfigOptionAggr productConfigOptionAggr, ProductConfigAggr productConfigAggr,
                                         ProductContextAggr productContextAggr) {
        // 勾选状态未变更，不处理
        if (pcOptionConfigDto.isSelect() && productConfigOptionAggr.isSelect()) {
            return;
        }
        // 勾选状态未变更，不处理
        if (!pcOptionConfigDto.isSelect() && !productConfigOptionAggr.isSelect()) {
            return;
        }
        // 勾选状态变更，是否可编辑校验
        if (Objects.equals(ProductConfigOptionTypeEnum.FROM_BASE_VEHICLE.getType(), productConfigOptionAggr.getType())
                && Objects.equals(YesOrNoEnum.NO.getCode(), productConfigAggr.getCompleteInitSelect())) {
            // 打点Copy From Base Vehicle，且未完成初始化勾选，校验是否可编辑
            if (!productConfigOptionAggr.isSelectCanEdit()) {
                throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_OPTION_CAN_NOT_EDIT.getCode(),
                        String.format("Option %s In PC %s Can Not Edit!", productConfigOptionAggr.getOptionCode(), productConfigOptionAggr.getPcId()));
            }
        }
        // 状态改为勾选，校验是否可勾选
        if (pcOptionConfigDto.isSelect() && productContextAggr == null) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_OPTION_CAN_NOT_SELECT.getCode(),
                    String.format("Option %s Is Not Applied In Product Context %s %s, Which Can Not Be Applied In Related PC Either!",
                            productConfigOptionAggr.getOptionCode(), productConfigAggr.getModelCode(), productConfigAggr.getModelYear()));
        }
        productConfigOptionAggr.changeSelectStatus(pcOptionConfigDto.isSelect());
    }

}
