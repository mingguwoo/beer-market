package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
public interface BomsV36CodeLibraryDao extends IService<BomsV36CodeLibraryEntity> {

    /**
     * 根据Code + ParentId + ChineseName查询
     *
     * @param parentCode  ParentCode
     * @param code        Code
     * @param chineseName ChineseName
     * @return BomsV36CodeLibraryEntity列表
     */
    List<BomsV36CodeLibraryEntity> queryByParentCodeCodeAndChineseName(String parentCode, String code, String chineseName);

    /**
     * 根据ParentId查询
     *
     * @param parentId ParentId
     * @return BomsV36CodeLibraryEntity列表
     */
    List<BomsV36CodeLibraryEntity> queryByParentId(Long parentId);

    /**
     * 根据sales feature和name查询
     * @param salesFeature
     * @param name
     * @return
     */
    List<BomsV36CodeLibraryEntity> queryBySalesFeatureAndName(String salesFeature,String name);
}
