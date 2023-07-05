package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.converter.common;

import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BaseEntity;

/**
 * @author xiaozhou.tu
 * @date 2023/7/5
 */
public interface BeanCopyDataConverter<DO extends AbstractDo, Entity extends BaseEntity> extends DataConverter<DO, Entity> {

    /**
     * 创建新的DomainObject
     *
     * @return domainObject
     */
    DO newDo();

    /**
     * 创建新的Entity
     *
     * @return entity
     */
    Entity newEntity();

    /**
     * DomainObject转换为Entity
     *
     * @param domainObject domainObject
     * @return entity
     */
    default Entity convertDoToEntity(DO domainObject) {
        return BeanConvertUtils.convertTo(domainObject, this::newEntity, this::convertDoToEntityCallback);
    }

    /**
     * Entity转换为DomainObject
     *
     * @param entity entity
     * @return domainObject
     */
    default DO convertEntityToDo(Entity entity) {
        return BeanConvertUtils.convertTo(entity, this::newDo, this::convertEntityToDoCallback);
    }

}
