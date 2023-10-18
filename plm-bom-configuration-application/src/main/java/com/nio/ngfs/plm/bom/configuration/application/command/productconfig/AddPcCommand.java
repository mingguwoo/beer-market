package com.nio.ngfs.plm.bom.configuration.application.command.productconfig;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.ProductConfigApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ModelFacade;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.event.PcAddEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionId;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.event.ProductConfigOptionChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.productconfig.ProductConfigDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.AddPcCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.AddPcRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final EventPublisher eventPublisher;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected String getLockKey(AddPcCmd cmd) {
        return String.format(RedisKeyConstant.PRODUCT_CONFIG_ADD_PC_LOCK_KEY_FORMAT, cmd.getModel(), cmd.getModelYear());
    }

    @Override
    protected AddPcRespDto executeWithLock(AddPcCmd cmd) {
        redisTemplate.opsForValue().set("name", "huangshengda");
        redisTemplate.opsForValue().get("name");
        redisTemplate.opsForList().leftPush("nameList", "huangshengda");
        redisTemplate.opsForList().rightPop("nameList");
        redisTemplate.opsForSet().add("nameSet", "huangshengda");
        redisTemplate.opsForSet().add("nameSet", "tuxiaozhou");
        redisTemplate.opsForSet().members("nameSet");
        redisTemplate.opsForZSet().add("nameRank", "huangshengda", 1.0D);
        redisTemplate.opsForZSet().add("nameRank", "tuxiaozhou", 1.0D);
        redisTemplate.opsForZSet().rank("nameRank", "tuxiaozhou");
        redisTemplate.opsForHash().put("user", "name", "huangshengda");
        redisTemplate.opsForHash().put("user", "sex", "man");
        redisTemplate.opsForHash().get("user", "name");
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
        // 发布PC新增事件
        eventPublisher.publish(new PcAddEvent(productConfigAggr));
        // 发布ProductConfig打点变更事件
        publishProductConfigOptionChangeEvent(productConfigAggr, basedOnPcOptionAggrList, basedOnBaseVehicleOptionAggrList);
        return new AddPcRespDto();
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveProductConfigAndProductConfigOption(ProductConfigAggr productConfigAggr, List<ProductConfigOptionAggr> basedOnPcOptionAggrList,
                                                        List<ProductConfigOptionAggr> basedOnBaseVehicleOptionAggrList) {
        productConfigRepository.save(productConfigAggr);
        if (CollectionUtils.isNotEmpty(basedOnPcOptionAggrList)) {
            // 补充pcId
            basedOnPcOptionAggrList.forEach(aggr -> aggr.setProductConfigOptionId(new ProductConfigOptionId(productConfigAggr.getId(), aggr.getOptionCode())));
            productConfigOptionRepository.batchSave(basedOnPcOptionAggrList);
        }
        if (CollectionUtils.isNotEmpty(basedOnBaseVehicleOptionAggrList)) {
            // 补充pcId
            basedOnBaseVehicleOptionAggrList.forEach(aggr -> aggr.setProductConfigOptionId(new ProductConfigOptionId(productConfigAggr.getId(), aggr.getOptionCode())));
            productConfigOptionRepository.batchSave(basedOnBaseVehicleOptionAggrList);
        }
    }

    private void publishProductConfigOptionChangeEvent(ProductConfigAggr productConfigAggr, List<ProductConfigOptionAggr> basedOnPcOptionAggrList,
                                                       List<ProductConfigOptionAggr> basedOnBaseVehicleOptionAggrList) {
        if (CollectionUtils.isNotEmpty(basedOnPcOptionAggrList)) {
            // 新增PC时只同步勾选的打点
            eventPublisher.publish(new ProductConfigOptionChangeEvent(Lists.newArrayList(productConfigAggr), basedOnPcOptionAggrList
                    .stream().filter(ProductConfigOptionAggr::isSelect).toList()));
        }
        if (CollectionUtils.isNotEmpty(basedOnBaseVehicleOptionAggrList)) {
            // 新增PC时只同步勾选的打点
            eventPublisher.publish(new ProductConfigOptionChangeEvent(Lists.newArrayList(productConfigAggr), basedOnBaseVehicleOptionAggrList
                    .stream().filter(ProductConfigOptionAggr::isSelect).toList()));
        }
    }

}
