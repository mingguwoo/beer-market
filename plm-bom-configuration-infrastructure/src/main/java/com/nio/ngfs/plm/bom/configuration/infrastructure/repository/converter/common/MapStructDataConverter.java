package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common;

import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping.MapstructMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BaseEntity;

/**
 * @author xiaozhou.tu
 * @date 2023/7/5
 */
public interface MapStructDataConverter<DO extends AbstractDo, Entity extends BaseEntity> extends DataConverter<DO, Entity> {

    /**
     * 获取Mapper
     *
     * @return mapper
     */
    MapstructMapper<DO, Entity> getMapper();

    /**
     * DomainObject转换为Entity
     *
     * @param domainObject domainObject
     * @return Entity
     */
    default Entity doToEntity(DO domainObject) {
        return getMapper().convertDoToEntity(domainObject);
    }

    /**
     * Entity转换为DomainObject
     *
     * @param entity entity
     * @return domainObject
     */
    default DO entityToDo(Entity entity) {
        return getMapper().convertEntityToDo(entity);
    }

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
        // createTime和updateTime设为null
        entity.setCreateTime(null);
        entity.setUpdateTime(null);
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
        if (entity == null) {
            return null;
        }
        DO domainObject = entityToDo(entity);
        convertEntityToDoCallback(entity, domainObject);
        return domainObject;
    }

}
