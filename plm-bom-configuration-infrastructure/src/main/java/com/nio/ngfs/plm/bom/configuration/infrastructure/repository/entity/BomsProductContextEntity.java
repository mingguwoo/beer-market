package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author bill.wang
 * @date 2023/8/9
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_product_context")
public class BomsProductContextEntity extends BaseEntity{

    private String modelCode;

    private String modelYear;

    private String optionCode;

    private String featureCode;

}
