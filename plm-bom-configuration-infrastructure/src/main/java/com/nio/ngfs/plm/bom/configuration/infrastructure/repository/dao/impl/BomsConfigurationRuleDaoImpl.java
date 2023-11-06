package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsConfigurationRuleMapper;
import org.apache.commons.collections4.CollectionUtils;
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
    public String getMaxRuleNumber() {
        return getBaseMapper().getMaxRuleNumber();
    }

    @Override
    public List<BomsConfigurationRuleEntity> queryByGroupId(Long groupId) {
        LambdaQueryWrapper<BomsConfigurationRuleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsConfigurationRuleEntity::getGroupId, groupId);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }


    @Override
    public List<BomsConfigurationRuleEntity> queryByGroupIdsAndStatus(List<Long> groupIds, String status) {
        LambdaQueryWrapper<BomsConfigurationRuleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsConfigurationRuleEntity::getGroupId, groupIds);
        lambdaQueryWrapper.eq(BomsConfigurationRuleEntity::getStatus, status);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsConfigurationRuleEntity> queryByGroupIdList(List<Long> groupIdList) {
        if (CollectionUtils.isEmpty(groupIdList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<BomsConfigurationRuleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsConfigurationRuleEntity::getGroupId, groupIdList);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsConfigurationRuleEntity> queryByRuleNumbers(List<String> ruleNumbers) {
        if (CollectionUtils.isEmpty(ruleNumbers)) {
            return getBaseMapper().selectList(new LambdaQueryWrapper<>());
        }
        LambdaQueryWrapper<BomsConfigurationRuleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsConfigurationRuleEntity::getRuleNumber, ruleNumbers);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

}
