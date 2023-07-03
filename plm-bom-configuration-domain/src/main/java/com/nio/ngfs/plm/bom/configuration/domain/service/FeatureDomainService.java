package com.nio.ngfs.plm.bom.configuration.domain.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.AddGroupDO;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.EditGroupDO;

/**
 * Feature领域服务员
 *
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface FeatureDomainService {

    /**
     * 新增Group
     *
     * @param addGroupDO addGroupDO
     */
    void addGroup(AddGroupDO addGroupDO);

    /**
     * 编辑Group
     *
     * @param editGroupDO editGroupDO
     */
    void editGroup(EditGroupDO editGroupDO);

}
