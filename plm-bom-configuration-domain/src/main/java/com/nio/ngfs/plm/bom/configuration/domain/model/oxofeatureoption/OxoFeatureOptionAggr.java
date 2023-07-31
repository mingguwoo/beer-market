package com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.enums.OxoFeatureOptionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OxoFeatureOptionAggr extends AbstractDo implements AggrRoot<Long>, Comparable<OxoFeatureOptionAggr> {

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
    private Integer sortDelete;

    private String parentFeatureCode;


    private String displayName;

    private String chineseName;

    private String catalog;

    /**
     * 子节点列表
     */
    private transient List<OxoFeatureOptionAggr> children;

    @Override
    public Long getUniqId() {
        return id;
    }

    @Override
    public int compareTo(OxoFeatureOptionAggr other) {
        Comparator<OxoFeatureOptionAggr> c1 = Comparator.comparing(OxoFeatureOptionAggr::getSort);
        Comparator<OxoFeatureOptionAggr> c2 = Comparator.comparing(OxoFeatureOptionAggr::getFeatureCode);
        return c1.thenComparing(c2).compare(this, other);
    }

    /**
     * 重新排序
     */
    public void renewSort(int sort) {
        setSort(sort);
    }

    /**
     * 是否可以删除
     */
    public boolean canDelete() {
        return Objects.equals(CommonConstants.NOT_DEL_FLAG, delFlag) &&
                Objects.equals(CommonConstants.NOT_DEL_FLAG, sortDelete);
    }

    /**
     * 是否Feature
     */
    public boolean isFeature() {
        return Objects.equals(type, OxoFeatureOptionTypeEnum.FEATURE.getType());
    }

    /**
     * 是否Option
     */
    public boolean isOption() {
        return Objects.equals(type, OxoFeatureOptionTypeEnum.OPTION.getType());
    }

    /**
     * 物理删除
     */
    public void physicalDelete() {
        setDelFlag(CommonConstants.DEL_FLAG);
        if (isFeature() && CollectionUtils.isNotEmpty(children)) {
            children.forEach(OxoFeatureOptionAggr::physicalDelete);
        }
    }

    /**
     * 软删除
     */
    public void softDelete() {
        setSortDelete(CommonConstants.DEL_FLAG);
        if (isFeature() && CollectionUtils.isNotEmpty(children)) {
            children.forEach(OxoFeatureOptionAggr::softDelete);
        }
    }

}
