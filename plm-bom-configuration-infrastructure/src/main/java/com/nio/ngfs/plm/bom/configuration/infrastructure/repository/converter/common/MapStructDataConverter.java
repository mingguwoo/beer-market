package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common;

import com.nio.ngfs.common.utils.BeanConvertUtils;
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
        Entity entity = doToEntity(domainObject);
        convertDoToEntityCallback(domainObject, entity);
        return entity;
    }

    /**
     * Entity转换为DomainObject
     *
     * @param entity entity
     * @return domainObject
     */
    default DO convertEntityToDo(Entity entity) {
        DO domainObject = entityToDo(entity);
        convertEntityToDoCallback(entity, domainObject);
        return domainObject;
    }

}
