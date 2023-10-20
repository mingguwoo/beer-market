package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.common;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common.DataConverter;
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

    /**
     * 批量新增或更新，并回填主键id
     */
    public static <E extends BaseEntity, D extends AbstractDo> void batchSaveOrUpdate(IService<E> dao, DataConverter<D, E> converter, List<D> aggrList) {
        if (CollectionUtils.isEmpty(aggrList)) {
            return;
        }
        List<D> saveList = LambdaUtil.map(aggrList, aggr -> aggr.getId() == null, Function.identity());
        List<D> updateList = LambdaUtil.map(aggrList, aggr -> aggr.getId() != null, Function.identity());
        if (CollectionUtils.isNotEmpty(saveList)) {
            List<E> entityList = converter.convertDoListToEntityList(saveList);
            dao.saveBatch(entityList);
            // 回填主键id
            for (int i = 0; i < entityList.size(); i++) {
                saveList.get(i).setId(entityList.get(i).getId());
            }
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            dao.updateBatchById(converter.convertDoListToEntityList(updateList));
        }
    }

}
