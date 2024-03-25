package com.sh.beer.market.application.query.configurationrule;


import com.sh.beer.market.application.query.AbstractQuery;
import com.sh.beer.market.sdk.dto.configurationrule.request.GetGroupAndRuleQry;
import com.sh.beer.market.sdk.dto.configurationrule.response.GetGroupAndRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
@RequiredArgsConstructor
public class QueryConfigurationRuleViewQuery extends AbstractQuery<GetGroupAndRuleQry, GetGroupAndRuleRespDto> {


//    private final ConfigurationRuleQueryService configurationRuleQueryService;


    @Override
    protected GetGroupAndRuleRespDto executeQuery(GetGroupAndRuleQry qry) {
//        return configurationRuleQueryService.queryView(qry.getGroupId());
        return null;
    }
}
