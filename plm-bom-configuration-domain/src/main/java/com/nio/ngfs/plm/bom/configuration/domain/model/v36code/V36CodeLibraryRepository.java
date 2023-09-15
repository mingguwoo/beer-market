package com.nio.ngfs.plm.bom.configuration.domain.model.v36code;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
public interface V36CodeLibraryRepository extends Repository<V36CodeLibraryAggr, Long> {

    /**
     * 根据Code + ParentId + ChineseName查询
     *
     * @param code        Code
     * @param parentId    ParentId
     * @param chineseName ChineseName
     * @return V36CodeLibraryAggr列表
     */
    List<V36CodeLibraryAggr> queryByCodeParentIdAndChineseName(String code, Long parentId, String chineseName);

    /**
     * 根据ParentId查询
     *
     * @param parentId ParentId
     * @return V36CodeLibraryAggr列表
     */
    List<V36CodeLibraryAggr> queryByParentId(Long parentId);

}
