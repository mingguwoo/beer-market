package com.sh.beer.market.domain.facade.dto.response;


import lombok.Data;

@Data
public class CompareOxoFeatureModelRespDto {

    /**
     * 基准Feature
     *//*
    private OxoListRespDto baseIpFeature;
    *//**
     * 被比较的Feature
     *//*
    private OxoListRespDto compareIpFeature;
    *//**
     * key=version+code
     *//*
    private Map<String, OxoRowsQry> featureMap = new LinkedHashMap<>();
    *//**
     * key=version+featureCode+optionCode
     *//*
    private Map<String, OxoRowsQry> optionMap = new LinkedHashMap<>();
    *//**
     * key=version+featureCode+optionCode+salesOptionCode
     *//*
    private Map<String, OxoEditCmd> oxoMap = new LinkedHashMap<>();
    *//**
     * key=version+modelYear+featureCode+optionCode
     *//*
    private Map<String, OxoHeadQry.RegionInfo> modelYearFeatureOptionMap = new LinkedHashMap<>();
    *//**
     * key=version+modelYear+featureCode+optionCode+driveHardOptionCode
     *//*
    private Map<String, OxoHeadQry.DriveHandInfo> driverOptionInfoMap = new LinkedHashMap<>();
    *//**
     * key=version+modelYear+featureCode+optionCode+driveHardOptionCode+salesOptionCode
     *//*
    private Map<String, OxoHeadQry.SalesVersionInfo> salesOptionInfoMap = new LinkedHashMap<>();
*/
}
