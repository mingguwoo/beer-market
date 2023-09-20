package com.nio.ngfs.plm.bom.configuration.application.command.v36code;

import com.nio.ngfs.plm.bom.configuration.application.command.v36code.common.AbstractV36CodeCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.V36CodeLibraryApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.common.V36CodeLibraryAggrThreadLocal;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.event.V36CodeLibraryAttributeChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.v36code.V36CodeLibraryDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.EditDigitCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.EditDigitRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 编辑Digit
 *
 * @author xiaozhou.tu
 * @date 2023/9/18
 */
@Component
@RequiredArgsConstructor
public class EditV36DigitCommand extends AbstractV36CodeCommand<EditDigitCmd, EditDigitRespDto> {

    private final V36CodeLibraryRepository v36CodeLibraryRepository;
    private final V36CodeLibraryDomainService v36CodeLibraryDomainService;
    private final V36CodeLibraryApplicationService v36CodeLibraryApplicationService;
    private final EventPublisher eventPublisher;

    @Override
    protected String getLockKey(EditDigitCmd cmd) {
        V36CodeLibraryAggr aggr = v36CodeLibraryDomainService.getAndCheckAggr(cmd.getId());
        return RedisKeyConstant.V36_CODE_DIGIT_LOCK_KEY_PREFIX + aggr.getCode();
    }

    @Override
    protected EditDigitRespDto executeWithLock(EditDigitCmd cmd) {
        // 查询集合根
        V36CodeLibraryAggr aggr = v36CodeLibraryDomainService.getAndCheckAggr(cmd.getId());
        // 编辑Digit
        aggr.editDigit(cmd);
        // 校验Digit Code + Chinese Name是否唯一
        v36CodeLibraryDomainService.checkParentCodeCodeChineseNameUnique(aggr);
        // 校验Sales Feature List是否有效
        v36CodeLibraryApplicationService.checkSalesFeatureList(aggr);
        // V36 Code ID是否已应用于Release版本的V36中
        if (!cmd.isConfirm() && v36CodeLibraryApplicationService.isV36CodeIdInReleasedV36(aggr)) {
            return new EditDigitRespDto("This Digit Is Already Applied In Release V36, Confirm To Update It?");
        }
        // 保存到数据库
        v36CodeLibraryRepository.save(aggr);
        // 发布属性变更事件
        eventPublisher.publish(new V36CodeLibraryAttributeChangeEvent(V36CodeLibraryAggrThreadLocal.get(aggr.getId()), aggr));
        return new EditDigitRespDto();
    }

}
