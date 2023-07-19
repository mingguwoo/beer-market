package com.nio.ngfs.plm.bom.configuration.domain.facade;

/**
 * @author xiaozhou.tu
 * @date 2023/7/19
 */
public interface FeatureFacade {

    /**
     * Group在3DE Group Library中是否存在
     *
     * @param groupCode Group Code
     * @return true|false
     */
    boolean isGroupExistedInGroupLibrary(String groupCode);

}
