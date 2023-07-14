package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wangchao.wang
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("boms_oxo_option_package_info")
public class BomsOxoOptionPackageInfoEntity extends BaseEntity {



    private Long rowId;

    private Long baseVehicleId;

    private String packageCode;

    private String description;

    private String brand;

}
