package com.nio.ngfs.plm.bom.configuration.domain.facade.dto.response;


import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class CompareOxoFeatureModelRespDto {

    /**
     * 基准Feature
     */
    private OxoListRespDto baseIpFeature;
    /**
     * 被比较的Feature
     */
    private OxoListRespDto compareIpFeature;
    /**
     * key=version+code
     */
    private Map<String, OxoRowsQry> featureMap = new LinkedHashMap<>();
    /**
     * key=version+featureCode+optionCode
     */
    private Map<String, OxoRowsQry> optionMap = new LinkedHashMap<>();
    /**
     * key=version+featureCode+optionCode+salesOptionCode
     */
    private Map<String, OxoEditCmd> oxoMap = new LinkedHashMap<>();
    /**
     * key=version+modelYear+featureCode+optionCode
     */
    private Map<String, OxoHeadQry.RegionInfo> modelYearFeatureOptionMap = new LinkedHashMap<>();
    /**
     * key=version+modelYear+featureCode+optionCode+driveHardOptionCode
     */
    private Map<String, OxoHeadQry.DriveHandInfo> driverOptionInfoMap = new LinkedHashMap<>();
    /**
     * key=version+modelYear+featureCode+optionCode+driveHardOptionCode+salesOptionCode
     */
    private Map<String, OxoHeadQry.SalesVersionInfo> salesOptionInfoMap = new LinkedHashMap<>();

}