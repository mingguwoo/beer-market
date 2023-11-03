package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleGroupEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public interface BomsConfigurationRuleGroupDao extends IService<BomsConfigurationRuleGroupEntity> {

    List<BomsConfigurationRuleGroupEntity> queryByDefinedBy(String definedBy);
}
