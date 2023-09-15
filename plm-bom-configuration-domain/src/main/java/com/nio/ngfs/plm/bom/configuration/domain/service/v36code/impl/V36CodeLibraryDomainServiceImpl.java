package com.nio.ngfs.plm.bom.configuration.domain.service.v36code.impl;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.v36code.V36CodeLibraryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Service
@RequiredArgsConstructor
public class V36CodeLibraryDomainServiceImpl implements V36CodeLibraryDomainService {

    private final V36CodeLibraryRepository v36CodeLibraryRepository;

    @Override
    public void checkCodeAndParentAndChineseNameUnique(V36CodeLibraryAggr aggr) {
        V36CodeLibraryAggr existAggr = v36CodeLibraryRepository.queryByCodeParentIdAndChineseName(aggr.getCode(), aggr.getParentId(), aggr.getChineseName());
        if (existAggr != null && !Objects.equals(existAggr.getId(), aggr.getId())) {
            throw new BusinessException(aggr.isDigit() ? ConfigErrorCode.V36_CODE_DIGIT_CHINESE_NAME_REPEAT :
                    ConfigErrorCode.V36_CODE_DIGIT_OPTION_CHINESE_NAME_REPEAT);
        }
    }

    @Override
    public void checkDigitCodeOverlap(V36CodeLibraryAggr aggr) {
        List<V36CodeLibraryAggr> digitAggrList = v36CodeLibraryRepository.queryByParentId(ConfigConstants.V36_CODE_DIGIT_PARENT_CODE_ID);
        digitAggrList.forEach(digitAggr -> digitAggr.checkDigitCodeOverlap(aggr));
    }

}
