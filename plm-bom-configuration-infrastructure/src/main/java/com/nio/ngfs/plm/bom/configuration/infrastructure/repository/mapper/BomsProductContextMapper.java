package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/9
 */
public interface BomsProductContextMapper extends BaseMapper<BomsProductContextEntity> {

    /**
     * 批量保存或恢复delFlag
     * @param entityList
     */
    void addOrUpdateBatch(@Param("entityList") List<BomsProductContextEntity> entityList);

    /**
     * 查询所有的Context打点（包括逻辑删除的）
     *
     * @return BomsProductContextEntity列表
     */
    List<BomsProductContextEntity> queryAllIncludeDelete();

}
