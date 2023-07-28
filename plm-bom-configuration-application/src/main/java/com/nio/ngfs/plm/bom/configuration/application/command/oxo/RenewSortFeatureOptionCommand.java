package com.nio.ngfs.plm.bom.configuration.application.command.oxo;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.OxoFeatureOptionDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.RenewSortFeatureOptionCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.RenewSortFeatureOptionRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 重排序Feature Option
 *
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Component
@RequiredArgsConstructor
public class RenewSortFeatureOptionCommand extends AbstractCommand<RenewSortFeatureOptionCmd, RenewSortFeatureOptionRespDto> {

    private final OxoFeatureOptionDomainService oxoFeatureOptionDomainService;
    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;
    private final OxoFeatureOptionApplicationService oxoFeatureOptionApplicationService;

    @Override
    protected RenewSortFeatureOptionRespDto executeCommand(RenewSortFeatureOptionCmd cmd) {
        // 查询同一排序分组的Feature/Option
        List<OxoFeatureOptionAggr> oxoFeatureOptionAggrList = oxoFeatureOptionApplicationService.querySameSortGroupFeatureOption(cmd.getModelCode(), cmd.getTargetFeatureCode());
        // 重新排序Feature/Option
        oxoFeatureOptionDomainService.renewSortFeatureOption(oxoFeatureOptionAggrList, cmd.getTargetFeatureCode(), cmd.getMoveFeatureCodeList());
        // 批量保存
        oxoFeatureOptionRepository.batchSave(oxoFeatureOptionAggrList);
        return new RenewSortFeatureOptionRespDto();
    }

}
