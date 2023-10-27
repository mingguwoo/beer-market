package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleGroupDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleGroupEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleOptionEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.GetGroupAndRuleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.GetGroupAndRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 获取Group和Rule详情
 *
 * @author xiaozhou.tu
 * @date 2023/10/27
 */
@Component
@RequiredArgsConstructor
public class GetGroupAndRuleQuery extends AbstractQuery<GetGroupAndRuleQry, GetGroupAndRuleRespDto> {

    private final BomsConfigurationRuleDao bomsConfigurationRuleDao;
    private final BomsConfigurationRuleGroupDao bomsConfigurationRuleGroupDao;
    private final BomsConfigurationRuleOptionDao bomsConfigurationRuleOptionDao;

    @Override
    protected GetGroupAndRuleRespDto executeQuery(GetGroupAndRuleQry qry) {
        BomsConfigurationRuleGroupEntity ruleGroupEntity = bomsConfigurationRuleGroupDao.getById(qry.getGroupId());
        List<BomsConfigurationRuleEntity> ruleEntityList = bomsConfigurationRuleDao.queryByGroupId(ruleGroupEntity.getId());
        List<BomsConfigurationRuleOptionEntity> ruleOptionEntityList = bomsConfigurationRuleOptionDao.queryByRuleIdList(LambdaUtil.map(ruleEntityList,
                BomsConfigurationRuleEntity::getId));
        return new GetGroupAndRuleRespDto();
    }

}
