package com.nio.ngfs.plm.bom.configuration.application.command.v36code;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.v36code.V36CodeLibraryDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.AddDigitCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response.AddDigitRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Component
@RequiredArgsConstructor
public class AddDigitCommand extends AbstractLockCommand<AddDigitCmd, AddDigitRespDto> {

    private final V36CodeLibraryRepository v36CodeLibraryRepository;
    private final V36CodeLibraryDomainService v36CodeLibraryDomainService;

    @Override
    protected String getLockKey(AddDigitCmd cmd) {
        return RedisKeyConstant.V36_CODE_DIGIT_ADD_LOCK_KEY_PREFIX + cmd.getCode();
    }

    @Override
    protected AddDigitRespDto executeWithLock(AddDigitCmd cmd) {
        // 构建聚合更
        V36CodeLibraryAggr aggr = V36CodeLibraryFactory.createDigit(cmd);
        // 新增Digit
        aggr.addDigit();
        // 校验Digit Code + Chinese Name是否唯一
        v36CodeLibraryDomainService.checkCodeAndParentAndChineseNameUnique(aggr);
        // 校验Digit Code是否重叠
        v36CodeLibraryDomainService.checkDigitCodeOverlap(aggr);
        // 保持到数据库
        v36CodeLibraryRepository.save(aggr);
        return new AddDigitRespDto();
    }

}
