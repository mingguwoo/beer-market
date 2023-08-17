package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * @author wangchao.wang
 */
@Data
public class OxoEditCmd extends OxoBaseCmd{

     @NotNull(message = "headId不能为空")
     private Long headId;

     private Long rowId;

     private String packageCode;

     private String description="";


     private String regionCode;

     private String driveHandCode;

     private String salesCode;


     private String modelCode;


     private String modelYear;



     private OxoEditCmd compareOxoEdit;


     /**
      * 比对时候用到
      * @see com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.CompareChangeTypeEnum
      */
     private String changeType;
}


