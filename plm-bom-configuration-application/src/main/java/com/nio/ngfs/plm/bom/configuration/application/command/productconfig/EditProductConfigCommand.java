package com.nio.ngfs.plm.bom.configuration.application.command.productconfig;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductConfigOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.event.ProductConfigOptionChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.productconfig.ProductConfigDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.EditProductConfigCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.EditProductConfigRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 编辑Product Config
 *
 * @author xiaozhou.tu
 * @date 2023/8/25
 */
@Component
@RequiredArgsConstructor
public class EditProductConfigCommand extends AbstractLockCommand<EditProductConfigCmd, EditProductConfigRespDto> {

    private final ProductConfigRepository productConfigRepository;
    private final ProductConfigDomainService productConfigDomainService;
    private final ProductConfigOptionRepository productConfigOptionRepository;
    private final ProductContextRepository productContextRepository;
    private final ProductConfigOptionApplicationService productConfigOptionApplicationService;
    private final EventPublisher eventPublisher;

    @Override
    protected String getLockKey(EditProductConfigCmd cmd) {
        return RedisKeyConstant.PRODUCT_CONFIG_EDIT_PRODUCT_CONFIG_LOCK_KEY_PREFIX + cmd.getModel();
    }

    @Override
    protected EditProductConfigRespDto executeWithLock(EditProductConfigCmd cmd) {
        // 改变PC的skipCheck开关
        List<ProductConfigAggr> productConfigAggrList = productConfigDomainService.changePcSkipCheck(LambdaUtil.toKeyValueMap(cmd.getPcList(), EditProductConfigCmd.PcDto::getPcId,
                EditProductConfigCmd.PcDto::isSkipCheck), cmd.getUpdateUser());
        // 查询Product Config勾选
        List<ProductConfigOptionAggr> productConfigOptionAggrList = productConfigOptionRepository.queryByPcIdList(LambdaUtil.map(productConfigAggrList, ProductConfigAggr::getId));
        // 查询Product Context勾选
        List<ProductContextAggr> productContextAggrList = productContextRepository.queryByModelAndModelYearList(cmd.getModel(),
                LambdaUtil.map(productConfigAggrList, ProductConfigAggr::getModelYear, true));
        // 编辑Product Config勾选
        List<ProductConfigOptionAggr> saveProductConfigOptionAggrList = productConfigOptionApplicationService.editPcOptionConfig(cmd.getUpdatePcOptionConfigList(),
                productConfigAggrList, productConfigOptionAggrList);
        // edit时skipCheck校验
        productConfigOptionApplicationService.skipCheckBeforeEdit(productConfigAggrList, productConfigOptionAggrList);
        // Product Context勾选校验
        productConfigOptionApplicationService.checkEditByProductContextSelect(productConfigAggrList, productConfigOptionAggrList, productContextAggrList);
        // 处理初始化勾选完成
        productConfigDomainService.handleCompleteInitSelect(productConfigAggrList, productConfigOptionAggrList);
        // 保存到数据库
        ((EditProductConfigCommand) AopContext.currentProxy()).savePcAndPcOptionConfig(productConfigAggrList, saveProductConfigOptionAggrList);
        // ProductConfig打点变更事件
        eventPublisher.publish(new ProductConfigOptionChangeEvent(productConfigAggrList, saveProductConfigOptionAggrList));
        return new EditProductConfigRespDto();
    }

    @Transactional(rollbackFor = Exception.class)
    public void savePcAndPcOptionConfig(List<ProductConfigAggr> productConfigAggrList, List<ProductConfigOptionAggr> saveProductConfigOptionAggrList) {
        productConfigRepository.batchSave(productConfigAggrList);
        productConfigOptionRepository.batchSave(saveProductConfigOptionAggrList);
    }

}
