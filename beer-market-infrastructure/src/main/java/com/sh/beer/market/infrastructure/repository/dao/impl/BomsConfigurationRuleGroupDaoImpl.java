package com.sh.beer.market.infrastructure.repository.dao.impl;

import com.sh.beer.market.domain.model.WherePageRequest;
import com.sh.beer.market.infrastructure.repository.dao.BomsConfigurationRuleGroupDao;
import com.sh.beer.market.infrastructure.repository.entity.BomsConfigurationRuleGroupEntity;
import com.sh.beer.market.infrastructure.repository.mapper.BomsConfigurationRuleGroupMapper;
import org.springframework.stereotype.Repository;

/**
 * @author
 * @date 2023/10/17
 */
@Repository
public class BomsConfigurationRuleGroupDaoImpl extends AbstractDao<BomsConfigurationRuleGroupMapper, BomsConfigurationRuleGroupEntity, WherePageRequest<BomsConfigurationRuleGroupEntity>> implements BomsConfigurationRuleGroupDao {

    /*@Override
    protected void fuzzyConditions(WherePageRequest<BomsConfigurationRuleGroupEntity> bomsConfigurationRuleGroupEntityWherePageRequest, LambdaQueryWrapper<BomsConfigurationRuleGroupEntity> queryWrapper) {
    }

    @Override
    public List<BomsConfigurationRuleGroupEntity> queryByDefinedBy(List<String> definedBy) {
        LambdaQueryWrapper<BomsConfigurationRuleGroupEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsConfigurationRuleGroupEntity::getDefinedBy,definedBy);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }*/
}
