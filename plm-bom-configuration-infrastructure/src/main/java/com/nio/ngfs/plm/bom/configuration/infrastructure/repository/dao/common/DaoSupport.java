package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BaseEntity;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public static <E extends BaseEntity> void saveOrUpdate(IService<E> dao, E entity, Consumer<E> callback) {
        if (entity.getId() != null) {
            dao.updateById(entity);
        } else {
            dao.save(entity);
        }
        if (callback != null) {
            callback.accept(entity);
        }
    }

    /**
     * 批量新增或更新
     *
     * @param dao        dao
     * @param entityList entityList
     * @param <E>        E
     */
    public static <E extends BaseEntity> void batchSaveOrUpdate(IService<E> dao, List<E> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }
        List<E> saveList = LambdaUtil.map(entityList, entity -> entity.getId() == null, Function.identity());
        List<E> updateList = LambdaUtil.map(entityList, entity -> entity.getId() != null, Function.identity());
        if (CollectionUtils.isNotEmpty(saveList)) {
            dao.saveBatch(saveList);
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            dao.updateBatchById(updateList);
        }
    }

}
