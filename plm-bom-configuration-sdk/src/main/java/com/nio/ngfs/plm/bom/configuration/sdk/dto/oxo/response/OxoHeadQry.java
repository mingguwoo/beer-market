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

    private String modelYear;

    private String modelYearCode;

    private List<RegionInfo> regionInfos;

    @Getter
    @Setter
    @ToString
    public static class RegionInfo {

        private String regionCode;

        private String regionName;

        private List<DriveHandInfo> driveHands;
    }

    @Getter
    @Setter
    @ToString
    public static class DriveHandInfo {

         private String driveHandCode;

         private String driveHandName;

         private List<SalesVersionInfo> salesVersionInfos;
    }


    @Getter
    @Setter
    @ToString
    public static class SalesVersionInfo {

         private String salesCode;

         private String salesName;

         private Long headId;
    }
}
