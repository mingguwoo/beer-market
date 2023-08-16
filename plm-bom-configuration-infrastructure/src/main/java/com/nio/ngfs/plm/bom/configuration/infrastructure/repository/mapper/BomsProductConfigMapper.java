package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 单车PC表 Mapper 接口
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-08-09
 */
public interface BomsProductConfigMapper extends BaseMapper<BomsProductConfigEntity> {

    /**
     * 删除
     *
     * @param entity BomsProductConfigEntity
     */
    void remove(@Param("entity") BomsProductConfigEntity entity);

    /**
     * 根据pcIdPrefix查询最新的PC
     *
     * @param pcIdPrefix pcIdPrefix
     * @return BomsProductConfigEntity
     */
    BomsProductConfigEntity queryLastPcByPcIdPrefix(@Param("pcIdPrefix") String pcIdPrefix);

    /**
     * 根据PC Id列表查询
     *
     * @param pcIdList PC Id列表
     * @return BomsProductConfigEntity列表
     */
    List<BomsProductConfigEntity> queryByPcIdList(@Param("pcIdList") List<String> pcIdList);

}
