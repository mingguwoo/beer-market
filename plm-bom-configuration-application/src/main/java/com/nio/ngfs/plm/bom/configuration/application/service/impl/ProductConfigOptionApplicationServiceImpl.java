package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.google.common.collect.Lists;
import com.nio.bom.share.enums.YesOrNoEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.command.productconfig.context.EditProductConfigContext;
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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaozhou.tu
 * @date 2023/9/4
 */
@Service
@RequiredArgsConstructor
public class ProductConfigOptionApplicationServiceImpl implements ProductConfigOptionApplicationService {

    @Override
    public List<ProductConfigOptionAggr> editPcOptionConfig(List<EditProductConfigCmd.PcOptionConfigDto> updatePcOptionConfigList, List<ProductConfigAggr> productConfigAggrList,
                                                            List<ProductConfigOptionAggr> productConfigOptionAggrList, String updateUser) {
        if (CollectionUtils.isEmpty(updatePcOptionConfigList)) {
            return Lists.newArrayList();
        }
        // 构建PC、Product Config勾选、Product Context勾选的Map
        Map<String, ProductConfigAggr> productConfigAggrMap = LambdaUtil.toKeyMap(productConfigAggrList, ProductConfigAggr::getPcId);
        Map<ProductConfigOptionId, ProductConfigOptionAggr> productConfigOptionAggrMap = LambdaUtil.toKeyMap(productConfigOptionAggrList, ProductConfigOptionAggr::getUniqId);
        return LambdaUtil.map(updatePcOptionConfigList, i -> {
            ProductConfigAggr productConfigAggr = productConfigAggrMap.get(i.getPcId());
            if (productConfigAggr == null) {
                throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_NOT_EXIST);
            }
            ProductConfigOptionAggr existProductConfigOptionAggr = productConfigOptionAggrMap.get(new ProductConfigOptionId(productConfigAggr.getId(), i.getOptionCode()));
            if (existProductConfigOptionAggr == null) {
                // 新增的勾选记录
                ProductConfigOptionAggr newProductConfigOptionAggr = ProductConfigOptionFactory.create(productConfigAggr.getId(), i.getOptionCode(), i.getFeatureCode(),
                        i.isSelect(), updateUser);
                productConfigOptionAggrList.add(newProductConfigOptionAggr);
                return newProductConfigOptionAggr;
            }
            // 编辑Product Config勾选校验
            checkEditPcOptionConfig(i, existProductConfigOptionAggr, productConfigAggr, updateUser);
            return existProductConfigOptionAggr;
        });
    }

    @Override
    public void skipCheckBeforeEdit(List<ProductConfigAggr> productConfigAggrList, List<ProductConfigOptionAggr> productConfigOptionAggrList) {
        // Product Config勾选按PC分组
        Map<Long, List<ProductConfigOptionAggr>> productConfigOptionGroupByPc = LambdaUtil.groupBy(productConfigOptionAggrList, ProductConfigOptionAggr::getPcId);
        // skipCheck关闭状态时，进行Feature/Option校验
        productConfigAggrList.stream().filter(ProductConfigAggr::isSkipCheckClose).forEach(productConfigAggr -> {
            List<ProductConfigOptionAggr> productConfigOptionList = productConfigOptionGroupByPc.get(productConfigAggr.getId());
            // 按Feature分组
            LambdaUtil.groupBy(productConfigOptionList, ProductConfigOptionAggr::getFeatureCode).forEach((featureCode, optionList) -> {
                // 1个Feature下最多只可勾选1个Option（可以不勾选）
                if (optionList.stream().filter(ProductConfigOptionAggr::isSelect).count() > 1) {
                    EditProductConfigContext.addMessage(productConfigAggr.getPcId(),
                            String.format("Please Choose One Option Of Feature %s In PC %s (Skip Check Button Is Closed)!", featureCode, productConfigAggr.getPcId()));
                }
            });
        });
    }

    @Override
    public void checkEditByProductContextSelect(List<ProductConfigAggr> productConfigAggrList, List<ProductConfigOptionAggr> productConfigOptionAggrList, List<ProductContextAggr> productContextAggrList) {
        Map<Long, ProductConfigAggr> productConfigAggrMap = LambdaUtil.toKeyMap(productConfigAggrList, ProductConfigAggr::getId);
        Map<String, ProductContextAggr> productContextAggrMap = LambdaUtil.toKeyMap(productContextAggrList, i -> buildProductContextAggrKey(i.getOptionCode(), i.getModelYear()));
        productConfigOptionAggrList.stream().filter(ProductConfigOptionAggr::isSelect).forEach(productConfigOptionAggr -> {
            ProductConfigAggr productConfigAggr = productConfigAggrMap.get(productConfigOptionAggr.getPcId());
            if (productConfigAggr == null) {
                throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_NOT_EXIST);
            }
            // 排除From BaseVehicle且未完成初始化勾选的打点
            if (productConfigOptionAggr.isFromBaseVehicle() && productConfigAggr.isNotCompleteInitSelect()) {
                return;
            }
            ProductContextAggr productContextAggr = productContextAggrMap.get(buildProductContextAggrKey(productConfigOptionAggr.getOptionCode(),
                    productConfigAggr.getModelYear()));
            if (productContextAggr == null) {
                // Product Config勾选了，Product Context未勾选，报错
                EditProductConfigContext.addMessage(productConfigAggr.getPcId(),
                        String.format("Option %s Is Not Applied In Product Context %s %s, Which Can Not Be Applied In Related PC Either!",
                                productConfigOptionAggr.getOptionCode(), productConfigAggr.getModelCode(), productConfigAggr.getModelYear()));
            }
        });

        Map<Long, List<ProductConfigOptionAggr>> productConfigOptionAggrMap = LambdaUtil.groupBy(productConfigOptionAggrList, ProductConfigOptionAggr::getPcId);
        // 按Model Year分组
        LambdaUtil.groupBy(productContextAggrList, ProductContextAggr::getModelYear)
                .forEach((modelYear, productContextOptionList) -> {
                    // 筛选出勾选的Feature Code
                    Set<String> selectFeatureCodeSet = productContextOptionList.stream().map(ProductContextAggr::getFeatureCode).collect(Collectors.toSet());
                    productConfigAggrList.stream().filter(i -> Objects.equals(i.getModelYear(), modelYear)).forEach(pc -> {
                        // PC Copy From Base Vehicle，且未完成初始化勾选，跳过校验
                        if (pc.isFromBaseVehicle() && pc.isNotCompleteInitSelect()) {
                            return;
                        }
                        List<ProductConfigOptionAggr> productConfigOptionListByPc = productConfigOptionAggrMap.get(pc.getId());
                        // 按Feature Code分组校验
                        LambdaUtil.groupBy(productConfigOptionListByPc, ProductConfigOptionAggr::getFeatureCode).forEach((featureCode, optionList) -> {
                            if (!selectFeatureCodeSet.contains(featureCode)) {
                                return;
                            }
                            // Feature下的Option在PC对应Model/Model Year下的Product Context中有勾选，则在该PC中至少勾选Feature下的其中1个Option
                            if (optionList.stream().noneMatch(ProductConfigOptionAggr::isSelect)) {
                                EditProductConfigContext.addMessage(pc.getPcId(),
                                        String.format("Feature %s Is Applied In Product Context %s, Please Choose At Least One Option Of The Feature In PC %s!",
                                                featureCode, pc.getModelCode() + " " + pc.getModelYear(), pc.getPcId()));
                            }
                        });
                    });
                });
    }

    private String buildProductContextAggrKey(String optionCode, String modelYear) {
        return String.format("%s::%s", optionCode, modelYear);
    }

    /**
     * 编辑Product Config勾选校验
     */
    private void checkEditPcOptionConfig(EditProductConfigCmd.PcOptionConfigDto pcOptionConfigDto, ProductConfigOptionAggr productConfigOptionAggr,
                                         ProductConfigAggr productConfigAggr, String updateUser) {
        // 勾选状态未变更，不处理
        if (pcOptionConfigDto.isSelect() && productConfigOptionAggr.isSelect()) {
            return;
        }
        // 勾选状态未变更，不处理
        if (!pcOptionConfigDto.isSelect() && !productConfigOptionAggr.isSelect()) {
            return;
        }
        // 勾选状态变更，校验是否可编辑
        if (Objects.equals(ProductConfigOptionTypeEnum.FROM_BASE_VEHICLE.getType(), productConfigOptionAggr.getType())
                && Objects.equals(YesOrNoEnum.NO.getCode(), productConfigAggr.getCompleteInitSelect())) {
            // 打点Copy From Base Vehicle，且未完成初始化勾选，校验是否可编辑
            if (!productConfigOptionAggr.isSelectCanEdit()) {
                EditProductConfigContext.addMessage(productConfigAggr.getPcId(),
                        String.format("Option %s In PC %s Can Not Edit, Because PC Is Copy From Base Vehicle And Not Complete Init Select!",
                                productConfigOptionAggr.getOptionCode(), productConfigAggr.getPcId()));
            }
        }
        productConfigOptionAggr.changeSelectStatus(pcOptionConfigDto.isSelect());
        productConfigOptionAggr.setUpdateUser(updateUser);
    }

}
