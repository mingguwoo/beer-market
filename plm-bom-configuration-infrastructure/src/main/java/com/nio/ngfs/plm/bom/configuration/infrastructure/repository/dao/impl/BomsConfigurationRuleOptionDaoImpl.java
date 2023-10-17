package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsConfigurationRuleOptionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Repository
public class BomsConfigurationRuleOptionDaoImpl extends AbstractDao<BomsConfigurationRuleOptionMapper, BomsConfigurationRuleOptionEntity, WherePageRequest<BomsConfigurationRuleOptionEntity>> implements BomsConfigurationRuleOptionDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsConfigurationRuleOptionEntity> bomsConfigurationRuleOptionEntityWherePageRequest, LambdaQueryWrapper<BomsConfigurationRuleOptionEntity> queryWrapper) {
    }

    @Override
    public List<BomsConfigurationRuleOptionEntity> queryByRuleId(Long ruleId) {
        LambdaQueryWrapper<BomsConfigurationRuleOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsConfigurationRuleOptionEntity::getRuleId, ruleId);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

}
