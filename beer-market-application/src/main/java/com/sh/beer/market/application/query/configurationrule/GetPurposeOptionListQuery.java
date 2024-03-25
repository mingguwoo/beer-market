package com.sh.beer.market.application.query.configurationrule;


import com.sh.beer.market.application.query.AbstractQuery;
import com.sh.beer.market.sdk.dto.configurationrule.request.GetGroupAndRuleQry;
import com.sh.beer.market.sdk.dto.configurationrule.response.GetGroupAndRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 查询Purpose选项列表
 *
 * @author
 * @date 2023/10/19
 */
@Component
@RequiredArgsConstructor
public class GetPurposeOptionListQuery extends AbstractQuery<GetGroupAndRuleQry, List<GetGroupAndRuleRespDto>> {

    @Override
    protected List<GetGroupAndRuleRespDto> executeQuery(GetGroupAndRuleQry qry) {
        /*return Stream.of(ConfigurationRulePurposeEnum.values()).filter(ConfigurationRulePurposeEnum::isSelectOption).map(i -> {
            GetPurposeOptionListRespDto respDto = new GetPurposeOptionListRespDto();
            respDto.setCode(i.getCode());
            respDto.setPurpose(i.getPurpose());
            return respDto;
        }).toList();*/
        return null;
    }

}
