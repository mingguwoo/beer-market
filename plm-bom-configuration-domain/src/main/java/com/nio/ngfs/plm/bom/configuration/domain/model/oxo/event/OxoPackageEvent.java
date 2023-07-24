package com.nio.ngfs.plm.bom.configuration.domain.model.oxo.event;


import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author wangchao.wang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OxoPackageEvent extends DomainEvent {




    private Long rowId;


    private Long baseVehicleId;


    private String packageCode;


    private String description;

    private String brand;
}
