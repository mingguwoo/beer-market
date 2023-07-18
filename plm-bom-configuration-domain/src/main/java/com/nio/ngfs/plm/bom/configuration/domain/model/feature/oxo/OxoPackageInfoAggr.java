package com.nio.ngfs.plm.bom.configuration.domain.model.feature.oxo;


import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangchao.wang
 */
@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OxoPackageInfoAggr extends AbstractDo {



    private Long rowId;

    private Long baseVehicleId;

    private String packageCode;

    private String description;

    private String brand;










}
