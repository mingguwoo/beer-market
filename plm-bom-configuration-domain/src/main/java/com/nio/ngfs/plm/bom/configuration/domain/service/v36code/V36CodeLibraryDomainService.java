package com.nio.ngfs.plm.bom.configuration.domain.service.v36code;

import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
public interface V36CodeLibraryDomainService {

    /**
     * 获取并校验聚合根
     *
     * @param id id
     * @return 聚合根
     */
    V36CodeLibraryAggr getAndCheckAggr(Long id);

    /**
     * 校验Code + Parent Code + ChineseName是否唯一
     *
     * @param aggr 聚合根
     */
    void checkCodeAndParentAndChineseNameUnique(V36CodeLibraryAggr aggr);

    /**
     * 校验Digit Code是否重叠
     *
     * @param aggr 聚合根
     */
    void checkDigitCodeOverlap(V36CodeLibraryAggr aggr);

    /**
     * Digit下面是否包含相同的Option
     *
     * @param aggr 聚合根
     * @return true|false
     */
    boolean isDigitHasSameOption(V36CodeLibraryAggr aggr);

}
