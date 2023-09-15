package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsV36CodeLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsV36CodeLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsV36CodeLibraryMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Repository
public class BomsV36CodeLibraryDaoImpl extends AbstractDao<BomsV36CodeLibraryMapper, BomsV36CodeLibraryEntity, WherePageRequest<BomsV36CodeLibraryEntity>> implements BomsV36CodeLibraryDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsV36CodeLibraryEntity> bomsV36CodeLibraryEntityWherePageRequest, LambdaQueryWrapper<BomsV36CodeLibraryEntity> queryWrapper) {
    }

    @Override
    public List<BomsV36CodeLibraryEntity> queryByCodeParentIdAndChineseName(String code, Long parentId, String chineseName) {
        LambdaQueryWrapper<BomsV36CodeLibraryEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsV36CodeLibraryEntity::getCode, code);
        lambdaQueryWrapper.eq(BomsV36CodeLibraryEntity::getParentId, parentId);
        if (StringUtils.isNotBlank(chineseName)) {
            lambdaQueryWrapper.eq(BomsV36CodeLibraryEntity::getChineseName, chineseName);
        }
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsV36CodeLibraryEntity> queryByParentId(Long parentId) {
        LambdaQueryWrapper<BomsV36CodeLibraryEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsV36CodeLibraryEntity::getParentId, parentId);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

}
