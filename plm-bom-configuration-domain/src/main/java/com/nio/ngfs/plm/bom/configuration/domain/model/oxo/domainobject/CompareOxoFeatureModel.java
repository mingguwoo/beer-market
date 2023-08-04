package com.nio.ngfs.plm.bom.configuration.domain.model.oxo.domainobject;


import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CompareOxoFeatureModel {

    /**
     * 基准Feature
     */
    private OxoListQry baseIpFeature;
    /**
     * 被比较的Feature
     */
    private OxoListQry compareIpFeature;
    /**
     * key=version+code
     */
    private Map<String, OxoRowsQry> featureMap = new HashMap<>();
    /**
     * key=version+featureCode+optionCode
     */
    private Map<String, OxoRowsQry> optionMap = new HashMap<>();
    /**
     * key=version+featureCode+optionCode+salesOptionCode
     */
    private Map<String, OxoEditCmd> oxoMap = new HashMap<>();
    /**
     * key=version+modelYear+featureCode+optionCode
     */
    private Map<String, OxoHeadQry.RegionInfo> modelYearFeatureOptionMap = new HashMap<>();
    /**
     * key=version+modelYear+featureCode+optionCode+driveHardOptionCode
     */
    private Map<String, OxoHeadQry.DriveHandInfo> driverOptionInfoMap = new HashMap<>();
    /**
     * key=version+modelYear+featureCode+optionCode+driveHardOptionCode+salesOptionCode
     */
    private Map<String, OxoHeadQry.SalesVersionInfo> salesOptionInfoMap = new HashMap<>();

}
