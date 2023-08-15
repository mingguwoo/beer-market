package com.nio.ngfs.plm.bom.configuration.application.command.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductConfigApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ModelFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.productconfig.ProductConfigDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.AddPcCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.AddPcRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 新增PC
 *
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Component
@RequiredArgsConstructor
public class AddPcCommand extends AbstractLockCommand<AddPcCmd, AddPcRespDto> {

    private final ProductConfigRepository productConfigRepository;
    private final ProductConfigOptionRepository productConfigOptionRepository;
    private final ProductConfigDomainService productConfigDomainService;
    private final ProductConfigApplicationService productConfigApplicationService;
    private final ModelFacade modelFacade;

    @Override
    protected String getLockKey(AddPcCmd cmd) {
        return String.format(RedisKeyConstant.PRODUCT_CONFIG_ADD_PC_LOCK_KEY_FORMAT, cmd.getModel(), cmd.getModelYear());
    }

    @Override
    protected AddPcRespDto executeWithLock(AddPcCmd cmd) {
        // 获取车型品牌
        String brand = modelFacade.getBrandByModel(cmd.getModel());
        // 生成PC Id
        String pcId = productConfigDomainService.generatePcId(cmd.getModel(), cmd.getModelYear());
        // 创建聚合根
        ProductConfigAggr productConfigAggr = ProductConfigFactory.create(cmd, pcId, brand);
        productConfigAggr.add();
        // 校验PC Name是否唯一
        productConfigDomainService.checkPcNameUnique(productConfigAggr);
        // copy PC的Option Code勾选
        List<ProductConfigOptionAggr> basedOnPcOptionAggrList = productConfigApplicationService.copyProductConfigOptionByPc(productConfigAggr);
        // copy Base Vehicle的Option Code勾选
        List<ProductConfigOptionAggr> basedOnBaseVehicleOptionAggrList = productConfigApplicationService.copyProductConfigOptionByBaseVehicle(productConfigAggr);
        // 保存到数据库
        ((AddPcCommand) AopContext.currentProxy()).saveProductConfigAndProductConfigOption(productConfigAggr, basedOnPcOptionAggrList, basedOnBaseVehicleOptionAggrList);
        return new AddPcRespDto();
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveProductConfigAndProductConfigOption(ProductConfigAggr productConfigAggr, List<ProductConfigOptionAggr> basedOnPcOptionAggrList,
                                                        List<ProductConfigOptionAggr> basedOnBaseVehicleOptionAggrList) {
        productConfigRepository.save(productConfigAggr);
        productConfigOptionRepository.batchSave(basedOnPcOptionAggrList);
        productConfigOptionRepository.batchSave(basedOnBaseVehicleOptionAggrList);
    }

}
