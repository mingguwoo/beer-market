package com.nio.ngfs.plm.bom.configuration.application.command.v36code;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.V36CodeLibraryApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.v36code.V36CodeLibraryDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.EditOptionCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.EditOptionRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 编辑Option
 *
 * @author xiaozhou.tu
 * @date 2023/9/18
 */
@Component
@RequiredArgsConstructor
public class EditV36OptionCommand extends AbstractLockCommand<EditOptionCmd, EditOptionRespDto> {

    private final V36CodeLibraryRepository v36CodeLibraryRepository;
    private final V36CodeLibraryDomainService v36CodeLibraryDomainService;
    private final V36CodeLibraryApplicationService v36CodeLibraryApplicationService;

    @Override
    protected String getLockKey(EditOptionCmd cmd) {
        V36CodeLibraryAggr aggr = v36CodeLibraryDomainService.getAndCheckAggr(cmd.getId());
        return RedisKeyConstant.V36_CODE_OPTION_LOCK_KEY_PREFIX + aggr.getParentCode();
    }

    @Override
    protected EditOptionRespDto executeWithLock(EditOptionCmd cmd) {
        // 查询聚合根
        V36CodeLibraryAggr aggr = v36CodeLibraryDomainService.getAndCheckAggr(cmd.getId());
        // 编辑Option
        aggr.editOption(cmd);
        // 校验Digit Code + Option Code + Chinese Name是否唯一
        v36CodeLibraryDomainService.checkParentCodeCodeChineseNameUnique(aggr);
        // V36 Code ID是否已应用于Release版本的V36中
        if (!cmd.isConfirm() && v36CodeLibraryApplicationService.isV36CodeIdInReleasedV36(aggr)) {
            return new EditOptionRespDto("This Option Code Is Already Applied In Release V36, Confirm To Update It?");
        }
        // 保存到数据库
        v36CodeLibraryRepository.save(aggr);
        return new EditOptionRespDto();
    }

}
