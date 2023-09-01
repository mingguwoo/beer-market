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

import java.util.Collections;
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
    private Integer sort=0;


    /**
     * 软删除 1删除 0未删除
     */
    private Integer softDelete=0;

    private String parentFeatureCode;


    private String displayName;

    private String chineseName;

    private String catalog;

    /**
     * 父节点（type=Option时，父节点为Feature）
     */
    private transient OxoFeatureOptionAggr parent;

    /**
     * 子节点列表（type=Feature时，子节点列表为Feature下的Option列表）
     */
    private transient List<OxoFeatureOptionAggr> children = Collections.emptyList();

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
                Objects.equals(CommonConstants.NOT_DEL_FLAG, softDelete);
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
     * 软删除
     */
    public void softDelete() {
        setSoftDelete(CommonConstants.DEL_FLAG);
        if (isFeature() && CollectionUtils.isNotEmpty(children)) {
            children.forEach(OxoFeatureOptionAggr::softDelete);
        }
    }

    /**
     * 是否软删除
     */
    public boolean isSoftDelete() {
        return Objects.equals(softDelete, CommonConstants.DEL_FLAG);
    }

}
