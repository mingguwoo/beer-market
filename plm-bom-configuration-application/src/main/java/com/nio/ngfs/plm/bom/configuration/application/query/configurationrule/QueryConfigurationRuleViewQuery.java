package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule;


import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.service.ConfigurationRuleQueryService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsConfigurationRuleGroupDao;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.QueryConfigurationRuleQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.QueryViewQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.QueryConfigurationRuleRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.RuleViewInfoRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class QueryConfigurationRuleViewQuery extends AbstractQuery<QueryViewQry, RuleViewInfoRespDto> {


    private final ConfigurationRuleQueryService configurationRuleQueryService;


    @Override
    protected RuleViewInfoRespDto executeQuery(QueryViewQry qry) {
        return configurationRuleQueryService.queryView(qry.getGroupId());
    }
}
