package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response;


import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Data
public class OxoHeadQry implements Dto {

    private String modelCode;

    private String modelYear;

    private List<RegionInfo> regionInfos;

    @Getter
    @Setter
    @ToString
    public static class RegionInfo {

        private String regionCode;

        private String regionName;

        /**
         * 比对时候用到
         * @see com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.CompareChangeTypeEnum
         */
        private String changeType;

        private List<DriveHandInfo> driveHands;
    }

    @Getter
    @Setter
    @ToString
    public static class DriveHandInfo {

         private String driveHandCode;

         private String driveHandName;

         private List<SalesVersionInfo> salesVersionInfos;


        /**
         * 比对时候用到
         * @see com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.CompareChangeTypeEnum
         */
        private String changeType;
    }


    @Getter
    @Setter
    @ToString
    public static class SalesVersionInfo {

         private String salesCode;

         private String salesName;

         private Long headId;

        /**
         * 比对时候用到
         * @see com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.CompareChangeTypeEnum
         */
        private String changeType;
    }
}
