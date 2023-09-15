package com.nio.ngfs.plm.bom.configuration.application.command.v36code;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
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
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Component
@RequiredArgsConstructor
public class AddV36OptionCommand extends AbstractLockCommand<AddOptionCmd, AddOptionRespDto> {

    private final V36CodeLibraryRepository v36CodeLibraryRepository;
    private final V36CodeLibraryDomainService v36CodeLibraryDomainService;

    @Override
    protected String getLockKey(AddOptionCmd cmd) {
        return RedisKeyConstant.V36_CODE_OPTION_ADD_LOCK_KEY_PREFIX + cmd.getParentId();
    }

    @Override
    protected AddOptionRespDto executeWithLock(AddOptionCmd cmd) {
        V36CodeLibraryAggr parentAggr = v36CodeLibraryDomainService.getAndCheckAggr(cmd.getParentId());
        // 构建聚合根
        V36CodeLibraryAggr aggr = V36CodeLibraryFactory.createOption(cmd, parentAggr);
        // 新增Option
        aggr.addOption();
        // 第一次提交校验Digit + Option是否存在
        if (!cmd.isConfirm() && v36CodeLibraryDomainService.isDigitHasSameOption(aggr)) {
            return new AddOptionRespDto("This Option Code Is Already Existed In The Digit, Confirm To Add It?");
        }
        // 校验Digit Code + Option Code + Chinese Name是否唯一
        v36CodeLibraryDomainService.checkCodeAndParentAndChineseNameUnique(aggr);
        // 保持到数据库
        v36CodeLibraryRepository.save(aggr);
        return new AddOptionRespDto();
    }

}
