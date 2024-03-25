package com.sh.beer.market.infrastructure.repository.dao.impl;


import com.sh.beer.market.domain.model.WherePageRequest;
import com.sh.beer.market.infrastructure.repository.dao.BomsConfigurationRuleDao;
import com.sh.beer.market.infrastructure.repository.entity.BomsConfigurationRuleEntity;
import com.sh.beer.market.infrastructure.repository.mapper.BomsConfigurationRuleMapper;
import org.springframework.stereotype.Repository;

/**
 * @author
 * @date 2023/10/17
 */
@Repository
public class BomsConfigurationRuleDaoImpl extends AbstractDao<BomsConfigurationRuleMapper, BomsConfigurationRuleEntity, WherePageRequest<BomsConfigurationRuleEntity>> implements BomsConfigurationRuleDao {

    /*@Override
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
    }*/

}
