package com.sh.beer.market.infrastructure.repository.dao.impl;

import com.sh.beer.market.domain.model.WherePageRequest;
import com.sh.beer.market.infrastructure.repository.dao.BomsConfigurationRuleOptionDao;
import com.sh.beer.market.infrastructure.repository.entity.BomsConfigurationRuleOptionEntity;
import com.sh.beer.market.infrastructure.repository.mapper.BomsConfigurationRuleOptionMapper;
import org.springframework.stereotype.Repository;

/**
 * @author
 * @date 2023/10/17
 */
@Repository
public class BomsConfigurationRuleOptionDaoImpl extends AbstractDao<BomsConfigurationRuleOptionMapper, BomsConfigurationRuleOptionEntity, WherePageRequest<BomsConfigurationRuleOptionEntity>> implements BomsConfigurationRuleOptionDao {

    /*@Override
    protected void fuzzyConditions(WherePageRequest<BomsConfigurationRuleOptionEntity> bomsConfigurationRuleOptionEntityWherePageRequest, LambdaQueryWrapper<BomsConfigurationRuleOptionEntity> queryWrapper) {
    }

    @Override
    public List<BomsConfigurationRuleOptionEntity> queryByRuleId(Long ruleId) {
        LambdaQueryWrapper<BomsConfigurationRuleOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsConfigurationRuleOptionEntity::getRuleId, ruleId);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsConfigurationRuleOptionEntity> queryByRuleIdList(List<Long> ruleIdList) {
        if (CollectionUtils.isEmpty(ruleIdList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<BomsConfigurationRuleOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsConfigurationRuleOptionEntity::getRuleId, ruleIdList);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }*/

}
