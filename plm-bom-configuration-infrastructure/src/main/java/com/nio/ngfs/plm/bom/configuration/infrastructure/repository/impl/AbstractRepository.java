package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nio.ngfs.common.model.page.PageRequestUtils;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.common.constants.Constants;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BaseEntity;
import io.micrometer.core.lang.Nullable;
import org.apache.ibatis.builder.MapperBuilderAssistant;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 持久层抽象类
 *
 * @author luke.zhu
 * @date 02/20/2023
 */
public abstract class AbstractRepository<MAPPER extends BaseMapper<ENTITY>, ENTITY extends BaseEntity, WHERE extends WherePageRequest<ENTITY>> extends ServiceImpl<MAPPER, ENTITY> {

    static {
        // 注意：下面一行必须加来将BaseDomain基类信息注入mybatis
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), BaseEntity.class);
    }

    /**
     * 软删除对象
     *
     * @param entity        待删除对象
     * @param updateWrapper updateWrapper
     * @return String 删除结果
     */
    public String delete(ENTITY entity, LambdaUpdateWrapper<ENTITY> updateWrapper) {
        if (Objects.isNull(entity)) {
            updateWrapper.set(ENTITY::getDelFlag, Constants.DEL_FLAG);
        } else {
            entity.setDelFlag(Constants.DEL_FLAG);
        }
        int result = getBaseMapper().update(entity, updateWrapper);
        return result == Constants.DEL_FLAG ? "删除成功!" : "删除失败!!!";
    }

    /**
     * 获取所有对象不分页，并按照id排序
     *
     * @param entity 条件实体, 如果是null，则说明不需要过滤条件
     */
    public List<ENTITY> queryAll(@Nullable ENTITY entity, Supplier<WHERE> wherePageRequest) {
        LambdaQueryWrapper<ENTITY> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(entity)) {
            WHERE where = wherePageRequest.get();
            where.setWhere(entity);
            // 模糊查询
            fuzzyConditions(where, queryWrapper);
        }
        queryWrapper.eq(ENTITY::getDelFlag, Constants.NOT_DEL_FLAG);
        queryWrapper.orderByDesc(ENTITY::getId);
        return getBaseMapper().selectList(queryWrapper);
    }

    /**
     * 分页获取对象，并按照id排序
     */
    public Page<ENTITY> queryPage(WHERE whereRequest) {
        // 校验分页
        PageRequestUtils.checkWherePageRequest(whereRequest);
        Page<ENTITY> itemPage = new Page<>(whereRequest.getPageNum(), whereRequest.getPageSize());
        LambdaQueryWrapper<ENTITY> queryWrapper = new LambdaQueryWrapper<>();
        // 模糊查询
        fuzzyConditions(whereRequest, queryWrapper);
        queryWrapper.eq(ENTITY::getDelFlag, Constants.NOT_DEL_FLAG);
        queryWrapper.orderByDesc(ENTITY::getId);
        return this.page(itemPage, queryWrapper);
    }

    /**
     * 查询条件，实现此方法来实现不同业务的精确/模糊查询，但需要注意要使用索引字段
     *
     * @param where        待查询条件
     * @param queryWrapper 查询对象wrapper
     */
    protected abstract void fuzzyConditions(WHERE where, LambdaQueryWrapper<ENTITY> queryWrapper);

}
