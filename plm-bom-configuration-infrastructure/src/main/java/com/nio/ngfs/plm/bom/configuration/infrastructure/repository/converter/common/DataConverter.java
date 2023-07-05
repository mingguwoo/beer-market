package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common;

import com.nio.ngfs.plm.bom.configuration.common.util.ConverterUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BaseEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/4
 */
public interface DataConverter<DO extends AbstractDo, Entity extends BaseEntity> {

    /**
     * DomainObject转换为Entity
     *
     * @param domainObject domainObject
     * @return entity
     */
    Entity convertDoToEntity(DO domainObject);

    /**
     * DomainObject转换为Entity回调
     *
     * @param domainObject domainObject
     * @param entity       entity
     */
    default void convertDoToEntityCallback(DO domainObject, Entity entity) {
    }

    /**
     * Entity转换为DomainObject
     *
     * @param entity entity
     * @return domainObject
     */
    DO convertEntityToDo(Entity entity);

    /**
     * Entity转换为DomainObject回调
     *
     * @param entity       entity
     * @param domainObject domainObject
     */
    default void convertEntityToDoCallback(Entity entity, DO domainObject) {
    }

    /**
     * DomainObject列表转换为Entity列表
     *
     * @param domainObjectList domainObject列表
     * @return Entity列表
     */
    default List<Entity> convertDoListToEntityList(List<DO> domainObjectList) {
        return ConverterUtil.convertList(domainObjectList, this::convertDoToEntity);
    }

    /**
     * Entity列表转换为DomainObject列表
     *
     * @param entityList Entity列表
     * @return domainObject列表
     */
    default List<DO> convertEntityListToDoList(List<Entity> entityList) {
        return ConverterUtil.convertList(entityList, this::convertEntityToDo);
    }

}
