package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * BOM中台V36CodeLibrary属性变更记录表
 * </p>
 * @author bill.wang
 * @date 2023/9/20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_v36_code_library_change_log")
public class BomsV36CodeLibraryChangeLogEntity extends BaseEntity{

    /**
     * Code Id
     */
    private Long codeId;

    /**
     * 变更属性
     */
    private String changeAttribute;

    /**
     * 变更属性的旧值
     */
    private String oldValue;

    /**
     * 变更属性的新值
     */
    private String newValue;

    /**
     * 变更类型，Auto/Hand
     */
    private String type;
}
