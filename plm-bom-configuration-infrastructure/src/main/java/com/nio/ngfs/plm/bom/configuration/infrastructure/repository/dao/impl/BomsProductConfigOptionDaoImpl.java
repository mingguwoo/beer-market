package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.common.model.page.WherePageRequest;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper.BomsProductConfigOptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Slf4j
@Repository
public class BomsProductConfigOptionDaoImpl extends AbstractDao<BomsProductConfigOptionMapper, BomsProductConfigOptionEntity, WherePageRequest<BomsProductConfigOptionEntity>> implements BomsProductConfigOptionDao {

    @Override
    protected void fuzzyConditions(WherePageRequest<BomsProductConfigOptionEntity> bomsProductConfigOptionEntityWherePageRequest, LambdaQueryWrapper<BomsProductConfigOptionEntity> queryWrapper) {
    }

    @Override
    public BomsProductConfigOptionEntity getByPcIdAndOptionCode(String pcId, String optionCode) {
        LambdaQueryWrapper<BomsProductConfigOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigOptionEntity::getPcId, pcId);
        lambdaQueryWrapper.eq(BomsProductConfigOptionEntity::getOptionCode, optionCode);
        return getBaseMapper().selectOne(lambdaQueryWrapper);
    }

    @Override
    public List<BomsProductConfigOptionEntity> queryByPcId(String pcId) {
        LambdaQueryWrapper<BomsProductConfigOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BomsProductConfigOptionEntity::getPcId, pcId);
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

    @Override
    public List<BomsProductConfigOptionEntity> queryByPcIdList(List<String> pcIdList, String canSelect) {
        if (CollectionUtils.isEmpty(pcIdList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<BomsProductConfigOptionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BomsProductConfigOptionEntity::getPcId, pcIdList);

        if (StringUtils.isNotBlank(canSelect)) {
            lambdaQueryWrapper.eq(BomsProductConfigOptionEntity::getSelectStatus, canSelect);
        }
        return getBaseMapper().selectList(lambdaQueryWrapper);
    }

}
