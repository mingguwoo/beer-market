package com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MatrixRule查询参数
 * @author wangchao.wang
 */

@Getter
@Setter
@NoArgsConstructor
public class MatrixRuleQueryDto {


    public MatrixRuleQueryDto(String name, String category, String abscissa, String ordinate, String programName) {
        this.name = name;
        this.category = category;
        this.abscissa = abscissa;
        this.ordinate = ordinate;
        this.programName = programName;
    }

    /**
     * Matrix Rule 的名称
     */
    private String name;

    /**
     * 可选：matrix，table
     */
    private String category;

    /**
     * x坐标
     */
    private String abscissa;

    /**
     * y坐标
     */
    private String ordinate;

    /**
     * 标定调用方是谁：系统+模块+方法（由调用方自己拼接传过来）
     */
    private String programName;

}
