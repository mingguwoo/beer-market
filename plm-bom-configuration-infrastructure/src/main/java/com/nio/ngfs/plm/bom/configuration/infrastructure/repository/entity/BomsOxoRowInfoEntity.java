package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;



/**
 * <p>
 * oxo行信息
 * </p>
 *
 * @author xiaozhou.tu
 * @since 2023-07-11
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_oxo_row_info")
public class BomsOxoRowInfoEntity extends BaseEntity{


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
