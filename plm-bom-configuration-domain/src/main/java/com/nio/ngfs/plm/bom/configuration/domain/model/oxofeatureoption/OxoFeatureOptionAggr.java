package com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OxoFeatureOptionAggr extends AbstractDo implements AggrRoot<Long> {

    /**
     * 车型
     */
    private String modelCode;

    /**
     * 配置code
     */
    private String featureCode;

    private String ruleCheck;

    /**
     * 类型，Feature、Option
     */
    private String type;

    /**
     * 评论
     */
    private String comment;

    /**
     * 排序id
     */
    private Integer sort;

    /**
     * 软删除 1删除 0未删除
     */
    private Short softDelete;

    @Override
    public Long getUniqId() {
        return id;
    }

}
