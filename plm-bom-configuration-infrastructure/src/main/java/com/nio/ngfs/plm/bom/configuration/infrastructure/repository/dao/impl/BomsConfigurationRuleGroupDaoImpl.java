package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleGroupDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleGroupEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsConfigurationRuleGroupMapper;
import org.springframework.stereotype.Repository;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Repository
public class BomsConfigurationRuleGroupDaoImpl extends AbstractDao<BomsConfigurationRuleGroupMapper, BomsConfigurationRuleGroupEntity, WherePageRequest<BomsConfigurationRuleGroupEntity>> implements BomsConfigurationRuleGroupDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsConfigurationRuleGroupEntity> bomsConfigurationRuleGroupEntityWherePageRequest, LambdaQueryWrapper<BomsConfigurationRuleGroupEntity> queryWrapper) {
    }

}
