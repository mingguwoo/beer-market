package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface BomsFeatureLibraryMapper extends BaseMapper<BomsFeatureLibraryEntity> {


    /**
     * 查询 没有选中oxo行的 feature_library
     * @param modelCode
     * @return
     */
    List<BomsFeatureLibraryEntity> queryFeatureOptionLists(@Param("modelCode") String modelCode);
}
