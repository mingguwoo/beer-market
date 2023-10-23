package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleEntity;

/**
 * <p>
 * 配置管理-Rule表 Mapper 接口
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-10-17
 */
public interface BomsConfigurationRuleMapper extends BaseMapper<BomsConfigurationRuleEntity> {

    /**
     * 获取最大的Rule Number
     *
     * @return 最大的Rule Number
     */
    String getMaxRuleNumber();

}
