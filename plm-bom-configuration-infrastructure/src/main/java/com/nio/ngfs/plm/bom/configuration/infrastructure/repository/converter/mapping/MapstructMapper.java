package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.mapping;

/**
 * @author xiaozhou.tu
 * @date 2023/7/18
 */
public interface MapstructMapper<DO, Entity> {

    /**
     * DO转Entity
     *
     * @param domainObject domainObject
     * @return entity
     */
    Entity convertDoToEntity(DO domainObject);

    /**
     * Entity转DO
     *
     * @param entity entity
     * @return domainObject
     */
    DO convertEntityToDo(Entity entity);

}
