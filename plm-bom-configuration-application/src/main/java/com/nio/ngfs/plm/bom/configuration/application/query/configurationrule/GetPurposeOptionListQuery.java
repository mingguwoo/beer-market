package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule;

import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRulePurposeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.GetPurposeOptionListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.GetPurposeOptionListRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

/**
 * 查询Purpose选项列表
 *
 * @author xiaozhou.tu
 * @date 2023/10/19
 */
@Component
@RequiredArgsConstructor
public class GetPurposeOptionListQuery extends AbstractQuery<GetPurposeOptionListQry, List<GetPurposeOptionListRespDto>> {

    @Override
    protected List<GetPurposeOptionListRespDto> executeQuery(GetPurposeOptionListQry qry) {
        return Stream.of(ConfigurationRulePurposeEnum.values()).filter(ConfigurationRulePurposeEnum::isSelectOption).map(i -> {
            GetPurposeOptionListRespDto respDto = new GetPurposeOptionListRespDto();
            respDto.setCode(i.getCode());
            respDto.setPurpose(i.getPurpose());
            return respDto;
        }).toList();
    }

}
