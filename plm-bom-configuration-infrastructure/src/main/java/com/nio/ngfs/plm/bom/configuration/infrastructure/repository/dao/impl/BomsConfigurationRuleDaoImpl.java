package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsConfigurationRuleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Repository
public class BomsConfigurationRuleDaoImpl extends AbstractDao<BomsConfigurationRuleMapper, BomsConfigurationRuleEntity, WherePageRequest<BomsConfigurationRuleEntity>> implements BomsConfigurationRuleDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsConfigurationRuleEntity> bomsConfigurationRuleEntityWherePageRequest, LambdaQueryWrapper<BomsConfigurationRuleEntity> queryWrapper) {
    }

    @Override
    public List<BomsConfigurationRuleEntity> queryByRuleNumber(String ruleNumber) {
        LambdaQueryWrapper<BomsConfigurationRuleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsConfigurationRuleEntity::getRuleNumber,ruleNumber);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }
}
