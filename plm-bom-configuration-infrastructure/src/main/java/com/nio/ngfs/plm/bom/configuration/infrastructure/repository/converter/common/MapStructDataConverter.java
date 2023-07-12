package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common;

import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BaseEntity;

/**
 * @author xiaozhou.tu
 * @date 2023/7/5
 */
public interface MapStructDataConverter<DO extends AbstractDo, Entity extends BaseEntity> extends DataConverter<DO, Entity> {

    /**
     * DomainObject转换为Entity
     *
     * @param domainObject domainObject
     * @return Entity
     */
    Entity doToEntity(DO domainObject);

    /**
     * Entity转换为DomainObject
     *
     * @param entity entity
     * @return domainObject
     */
    DO entityToDo(Entity entity);


    /**
     * DomainObject转换为Entity
     *
     * @param domainObject domainObject
     * @return entity
     */
    default Entity convertDoToEntity(DO domainObject) {
        if (domainObject == null) {
            return null;
        }
        Entity entity = doToEntity(domainObject);
        convertDoToEntityCallback(domainObject, entity);
        // createTime和updateTime设为null
        entity.setCreateTime(null);
        entity.setUpdateTime(null);
        return entity;
    }

    /**
     * Entity转换为DomainObject
     *
     * @param entity entity
     * @return domainObject
     */
    default DO convertEntityToDo(Entity entity) {
        if (entity == null) {
            return null;
        }
        DO domainObject = entityToDo(entity);
        convertEntityToDoCallback(entity, domainObject);
        return domainObject;
    }

}
