package com.nio.ngfs.plm.bom.configuration.application.command.v36code;

import com.nio.ngfs.plm.bom.configuration.application.command.v36code.common.AbstractV36CodeCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.v36code.V36CodeLibraryDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.AddOptionCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.AddOptionRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 新增Option
 *
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Component
@RequiredArgsConstructor
public class AddV36OptionCommand extends AbstractV36CodeCommand<AddOptionCmd, AddOptionRespDto> {

    private final V36CodeLibraryRepository v36CodeLibraryRepository;
    private final V36CodeLibraryDomainService v36CodeLibraryDomainService;

    @Override
    protected String getLockKey(AddOptionCmd cmd) {
        V36CodeLibraryAggr parentAggr = v36CodeLibraryDomainService.getAndCheckAggr(cmd.getParentId());
        return RedisKeyConstant.V36_CODE_OPTION_LOCK_KEY_PREFIX + parentAggr.getCode();
    }

    @Override
    protected AddOptionRespDto executeWithLock(AddOptionCmd cmd) {
        V36CodeLibraryAggr parentAggr = v36CodeLibraryDomainService.getAndCheckAggr(cmd.getParentId());
        // 构建聚合根
        V36CodeLibraryAggr aggr = V36CodeLibraryFactory.createOption(cmd, parentAggr);
        // 新增Option
        aggr.addOption();
        // 校验Digit + Option是否存在
        if (v36CodeLibraryDomainService.isDigitHasSameOption(aggr)) {
            // 第一次提交
            if (!cmd.isConfirm()) {
                return new AddOptionRespDto("This Option Code Is Already Existed In The Digit, Confirm To Add It?");
            }
            // 校验Digit Code + Option Code + Chinese Name是否唯一
            v36CodeLibraryDomainService.checkParentCodeCodeChineseNameUnique(aggr);
        } else {
            // 校验Digit下Option的ChineseName和DisplayName是否重复
            v36CodeLibraryDomainService.checkChineseNameAndDisplayNameRepeatByDigit(aggr);
        }
        // 保持到数据库
        v36CodeLibraryRepository.save(aggr);
        return new AddOptionRespDto(aggr.getId());
    }

}
