package com.nio.ngfs.plm.bom.configuration.domain.model.oxo;

import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.RuleCheckEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OxoRowInfoAggr extends AbstractDo {

    /**
     * 车型
     */
    private String modelCode;

    /**
     * 配置code
     */
    private String featureCode;

    /**
     * 固定值Y/N 定义Rule发布时软校验
     */
    private String ruleCheck;

    /**
     * 评论
     */
    private String comments;

    /**
     * 是否头节点 1 featureCode 2optionCode
     */
    private Integer isHead;

    /**
     * 排序id
     */
    private Integer sort;

    /**
     * 软删除 1删除 0未删除
     */
    private Integer sortDelete;






}
