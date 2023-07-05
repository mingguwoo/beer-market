package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BaseEntity;

/**
 * @author xiaozhou.tu
 * @date 2023/7/5
 */
public class DaoSupport {

    /**
     * 根据ID是否为空判断新增或更新
     *
     * @param dao    dao
     * @param entity entity
     * @param <E>    E
     */
    public static <E extends BaseEntity> void saveOrUpdate(IService<E> dao, E entity) {
        if (entity.getId() != null) {
            dao.updateById(entity);
        } else {
            dao.save(entity);
        }
    }

}
