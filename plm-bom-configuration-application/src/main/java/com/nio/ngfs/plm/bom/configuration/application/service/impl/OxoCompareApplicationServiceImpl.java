package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.nio.bom.share.enums.BrandEnum;
import com.nio.bom.share.utils.GZIPUtils;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoCompareApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.facade.EmailFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.MatrixRuleFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.EmailParamDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.MatrixRuleQueryDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.OxoBasicVehicleDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.response.CompareOxoFeatureModelRespDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.basevehicle.BaseVehicleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.CompareChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums.OxoOptionPackageTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoTemplateRequestCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangchao.wang
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OxoCompareApplicationServiceImpl implements OxoCompareApplicationService {


    @Value("${oxo.email.changeLog.url}")
    private String oxoEmailChangeLogUrl;


    private final EmailFacade emailFacade;

    private final MatrixRuleFacade matrixRuleFacade;

    @Value("${oxo.email.templateNo}")
    private String oxoEmailTemplateNo;

    /**
     * 高低版本对比
     *
     * @param baseQry    低版本
     * @param compareQry 高版本
     * @param showDiff   只展示不一样的结果
     * @return
     */
    @Override
    public OxoListRespDto compareVersion(OxoListRespDto baseQry, OxoListRespDto compareQry, boolean showDiff) {


        CompareOxoFeatureModelRespDto compareIpFeatureModel = buildCompareModel(Lists.newArrayList(baseQry, compareQry), Lists.newArrayList(
                ConfigConstants.BASE_VERSION, ConfigConstants.LOW_VERSION));
        compareIpFeatureModel.setBaseIpFeature(baseQry);
        compareIpFeatureModel.setCompareIpFeature(compareQry);

        wrapBaseIpFeatureChangeType(compareIpFeatureModel, baseQry, true);
        wrapBaseIpFeatureChangeType(compareIpFeatureModel, compareQry, false);
        if (showDiff) {
            return filterDiffData(compareIpFeatureModel.getBaseIpFeature());
        }
        return compareIpFeatureModel.getBaseIpFeature();
    }


    /**
     * 过滤新老版本不一样的数据
     * 过滤changeFlag为空数据
     * 1、对于基础车型列
     * 1.1 先处理salesVersion,再处理DriverOptionInfo，再处理ModelYearFeatureOption
     * 2、对于Feature /Option行
     * 2.1 先处理IpdOXOOutput，在处理option,在处理Feature
     *
     * @return IpFeatureEntity
     */
    private OxoListRespDto filterDiffData(OxoListRespDto OxoListRespDto) {
        if (OxoListRespDto == null) {
            return OxoListRespDto;
        }
        OxoListRespDto diffOxoFeatureEntity = new OxoListRespDto();
        filterDiffFeature(diffOxoFeatureEntity, OxoListRespDto.getOxoRowsResps().stream().sorted(Comparator.comparing(OxoRowsQry::getCatalog).thenComparing(OxoRowsQry::getGroup)
                .thenComparing(OxoRowsQry::getSort).thenComparing(OxoRowsQry::getFeatureCode)).toList());
        diffOxoFeatureEntity.setOxoHeadResps(OxoListRespDto.getOxoHeadResps());
        return diffOxoFeatureEntity;
    }


    /**
     * 过滤Feature
     *
     * @param OxoListRespDto
     * @param oxoRowsQries
     */
    private void filterDiffFeature(OxoListRespDto OxoListRespDto, List<OxoRowsQry> oxoRowsQries) {
        if (CollectionUtils.isEmpty(oxoRowsQries)) {
            return;
        }
        List<OxoRowsQry> ipdList = Lists.newArrayList();
        ipdList.addAll(oxoRowsQries);
        OxoListRespDto.setOxoRowsResps(ipdList);
        //处理Feature
        oxoRowsQries.forEach(feature -> {
            //无变化的跳过
            if (StringUtils.isEmpty(feature.getChangeType()) && checkModify(feature)) {
                ipdList.remove(feature);
                return;
            }
            //ADD或者DELETE下面节点属性都是一样
            if (!StringUtils.equals(CompareChangeTypeEnum.MODIFY.getName(),feature.getChangeType()) &&
                    feature.getOptions().stream().allMatch(x-> StringUtils.isBlank(x.getChangeType()) ||
                            StringUtils.equals(x.getChangeType(),CompareChangeTypeEnum.NO_CHANGE.getName()))){
                return;
            }
            //处理option
            List<OxoRowsQry> optionList = new ArrayList<>(feature.getOptions());
            feature.getOptions().forEach(option -> {
                if (CollectionUtils.isEmpty(option.getPackInfos()) &&
                        (StringUtils.equals(option.getChangeType(), CompareChangeTypeEnum.DELETE.getName()) ||
                                StringUtils.equals(option.getChangeType(), CompareChangeTypeEnum.MODIFY.getName()))) {
                    option.setChangeType(null);
                }
            });
            feature.setOptions(optionList.stream().sorted(Comparator.comparing(OxoRowsQry::getSort).thenComparing(OxoRowsQry::getFeatureCode)).toList());
        });
    }


    public Boolean checkModify(OxoRowsQry feature) {
        if (CollectionUtils.isEmpty(feature.getOptions())) {
            return true;
        }
        for (OxoRowsQry f : feature.getOptions()) {
            if (StringUtils.isNotBlank(f.getChangeType())) {
                return false;
            }
        }
        return true;
    }

    /**
     * wrapBaseIpFeatureChangeType
     *
     * @param compareOxoFeatureModel
     * @param OxoListRespDto
     * @param baseVersion
     */
    private void wrapBaseIpFeatureChangeType(CompareOxoFeatureModelRespDto compareOxoFeatureModel, OxoListRespDto OxoListRespDto, boolean baseVersion) {
        //待比较待版本名称
        String versionName = baseVersion ? ConfigConstants.LOW_VERSION : ConfigConstants.BASE_VERSION;
        wrapFeatureOptionChangeType(compareOxoFeatureModel, OxoListRespDto, baseVersion, versionName);
        wrapHeaderChangeType(compareOxoFeatureModel, OxoListRespDto, baseVersion, versionName);
    }


    /**
     * 设置headerChangeType
     *
     * @param compareOxoFeatureModel
     * @param OxoListRespDto
     * @param baseVersion
     * @param versionName
     */
    private void wrapHeaderChangeType(CompareOxoFeatureModelRespDto compareOxoFeatureModel,
                                      OxoListRespDto OxoListRespDto, boolean baseVersion, String versionName) {
        Map<String, OxoHeadQry.RegionInfo> modelYearFeatureOptionMap = compareOxoFeatureModel.getModelYearFeatureOptionMap();
        Map<String, OxoHeadQry.DriveHandInfo> driverOptionInfoMap = compareOxoFeatureModel.getDriverOptionInfoMap();
        Map<String, OxoHeadQry.SalesVersionInfo> salesOptionInfoMap = compareOxoFeatureModel.getSalesOptionInfoMap();

        OxoListRespDto.getOxoHeadResps().forEach(modelYear -> {
            modelYear.getRegionInfos().forEach(regionInfo -> {
                String modelYearFeatureOptionKey = String.format("%s:%s:%s:%s", versionName, modelYear.getModelCode(), modelYear.getModelYear(), regionInfo.getRegionCode());
                //不包含，子级直接继承
                if (!modelYearFeatureOptionMap.containsKey(modelYearFeatureOptionKey)) {
                    //高版本与低版本比较(高版本有，低版本没有，则为添加)
                    if (baseVersion) {
                        regionInfo.setChangeType(CompareChangeTypeEnum.ADD.getName());
                        setRegionChildrenChangeType(regionInfo.getDriveHands(), CompareChangeTypeEnum.ADD.getName());
                        return;
                    }
                    //低版本和高版本比较(高版本没有，低版本有，则为删除)
                    regionInfo.setChangeType(CompareChangeTypeEnum.DELETE.getName());
                    //todo
//                    //基准添加删除的modelYearFeature
//                    List<OxoHeadQry> oxoHeads = Lists.newArrayList();
//                    oxoHeads.addAll(compareOxoFeatureModel.getBaseIpFeature().getOxoHeadResps());
//                    oxoHeads.add(modelYear);
//                    modelYear.add(modelYearFeatureOption);
//                    compareOxoFeatureModel.getBaseIpFeature().getOxoRowsResps().put(k, modelYearFeatureOptionList);

                    //  compareOxoFeatureModel.getBaseIpFeature().setOxoHeadResps(oxoHeads);

                    List<OxoHeadQry> oxoHeadQries = compareOxoFeatureModel.getBaseIpFeature().getOxoHeadResps();
                    List<OxoHeadQry> oxoHeadQryList =Lists.newArrayList(oxoHeadQries);
                    oxoHeadQryList.add(modelYear);
                    compareOxoFeatureModel.getBaseIpFeature().setOxoHeadResps(oxoHeadQryList);
                    //子级直接继承
                    setRegionChildrenChangeType(regionInfo.getDriveHands(), CompareChangeTypeEnum.DELETE.getName());
                    return;
                }
                //driverOption
                regionInfo.getDriveHands().forEach(driver -> {
                    String driveKey = String.format("%s:%s:%s:%s:%s", versionName, modelYear.getModelCode(), modelYear.getModelYear(),
                            regionInfo.getRegionCode(), driver.getDriveHandCode());
                    if (!driverOptionInfoMap.containsKey(driveKey)) {
                        //高版本与低版本比较(高版本有，低版本没有，则为添加)
                        if (baseVersion) {
                            driver.setChangeType(CompareChangeTypeEnum.ADD.getName());
                            setDriverChildrenChangeType(driver.getSalesVersionInfos(), CompareChangeTypeEnum.ADD.getName());
                            return;
                        }
                        //低版本和高版本比较(高版本没有，低版本有，则为删除)
                        driver.setChangeType(CompareChangeTypeEnum.DELETE.getName());
                        //基准添加删除的modelYearFeature
                        modelYearFeatureOptionMap.get(modelYearFeatureOptionKey).getDriveHands().add(driver);
                        //子级直接继承
                        setDriverChildrenChangeType(driver.getSalesVersionInfos(), CompareChangeTypeEnum.DELETE.getName());
                        return;
                    }
                    //SalesVersion
                    driver.getSalesVersionInfos().forEach(sales -> {
                        String salesKey = String.format("%s:%s:%s:%s:%s:%s", versionName, modelYear.getModelCode(), modelYear.getModelYear(), regionInfo.getRegionCode(), driver.getDriveHandCode(), sales.getSalesCode());
                        if (!salesOptionInfoMap.containsKey(salesKey)) {
                            if (baseVersion) {
                                sales.setChangeType(CompareChangeTypeEnum.ADD.getName());
                                return;
                            }
                            sales.setChangeType(CompareChangeTypeEnum.DELETE.getName());
                            //基准添加删除的sales
                            driverOptionInfoMap.get(driveKey).getSalesVersionInfos().add(sales);
                        }
                    });
                });
            });
        });
    }


    /**
     * 设置 setModelYearFeatureChildrenChangeType
     *
     * @param driverOptions
     * @param changeTypeName
     */
    private void setRegionChildrenChangeType(List<OxoHeadQry.DriveHandInfo> driverOptions, String changeTypeName) {
        if (CollectionUtils.isEmpty(driverOptions)) {
            return;
        }
        driverOptions.forEach(driver -> {
            driver.setChangeType(changeTypeName);
            setDriverChildrenChangeType(driver.getSalesVersionInfos(), changeTypeName);
        });
    }


    /**
     * 设置salesChangeType
     *
     * @param salesVersionList salesVersionList
     * @param changeTypeName   changeTypeName
     */
    private void setDriverChildrenChangeType(List<OxoHeadQry.SalesVersionInfo> salesVersionList, String changeTypeName) {
        if (CollectionUtils.isEmpty(salesVersionList)) {
            return;
        }
        salesVersionList.forEach(sale -> {
            sale.setChangeType(changeTypeName);
        });
    }

    /**
     * 设置FeatureOptionChangeType
     *
     * @param compareOxoFeatureModel
     * @param OxoListRespDto
     * @param baseVersion
     * @param versionName
     */
    private void wrapFeatureOptionChangeType(CompareOxoFeatureModelRespDto compareOxoFeatureModel,
                                             OxoListRespDto OxoListRespDto, boolean baseVersion, String versionName) {
        Map<String, OxoRowsQry> featureMap = compareOxoFeatureModel.getFeatureMap();
        Map<String, OxoRowsQry> optionMap = compareOxoFeatureModel.getOptionMap();
        Map<String, OxoEditCmd> oxoMap = compareOxoFeatureModel.getOxoMap();
        // Feature/option列，只需要高版本和低版本比较,不需要低版本和高版本比较
        if (!baseVersion) {
            return;
        }
        OxoListRespDto.getOxoRowsResps().forEach(feature -> {
            String ipFeatureEntityKey = String.format("%s:%s", versionName, feature.getFeatureCode());
            if (!featureMap.containsKey(ipFeatureEntityKey)) {
                //feature高版本和低版本比较(高版本有，低版本没有，则新增)
                feature.setChangeType(CompareChangeTypeEnum.ADD.getName());
                //子级直接继承
                setFeatureChildrenChangeType(feature.getOptions(), CompareChangeTypeEnum.ADD.getName());
                return;
            }

            //Feature高低版本都存在,如果option存在add或者modify,则feature为modify
            feature.getOptions().forEach(option -> {
                String compareChildKey = String.format("%s:%s:%s", versionName, feature.getFeatureCode(), option.getFeatureCode());
                //option不存在
                if (!optionMap.containsKey(compareChildKey)) {
                    //option高版本和低版本比较(高版本有，低版本没有，则新增)
                    option.setChangeType(CompareChangeTypeEnum.ADD.getName());
                    //feature.setChangeType(CompareChangeTypeEnum.MODIFY.getName());
                    return;
                }
                //option高低版本都存在
                option.getPackInfos().forEach(oxo -> {
                    //比较所有低oxo打点
                    String oxoKey = String.format("%s:%s:%s:%s:%s:%s:%s:%s",
                            versionName, oxo.getModelCode(), oxo.getModelYear(), feature.getFeatureCode(), option.getFeatureCode(),
                            oxo.getRegionCode(), oxo.getDriveHandCode(), oxo.getSalesCode());
                    OxoEditCmd compareOxo = oxoMap.get(oxoKey);
                    if (oxoMap.containsKey(oxoKey) && !oxoMap.get(oxoKey).getPackageCode().equals(oxo.getPackageCode())) {
                        if (Objects.nonNull(compareOxo)) {
                            oxo.setCompareOxoEdit(compareOxo);
                        }
                        option.setChangeType(CompareChangeTypeEnum.MODIFY.getName());
                    } else if (!oxoMap.containsKey(oxoKey) && (Objects.nonNull(oxo.getCompareOxoEdit()) || Objects.nonNull(compareOxo))) {
                        option.setChangeType(CompareChangeTypeEnum.MODIFY.getName());
                    } else if (!oxoMap.containsKey(oxoKey) && (Objects.isNull(oxo.getCompareOxoEdit()) && Objects.isNull(compareOxo))
                            && StringUtils.isBlank(option.getChangeType())) {
                        option.setChangeType(CompareChangeTypeEnum.DEL.getName());
                    } else if (oxoMap.containsKey(oxoKey) && oxoMap.get(oxoKey).getPackageCode().equals(oxo.getPackageCode())) {
                        oxo.setChangeType(CompareChangeTypeEnum.NO_CHANGE.getName());
                    }
                });

                //2、check option所有的oxo是不是Unavailable，如果都是Unavailable，则设置option为Delete
                if (StringUtils.equals(option.getChangeType(), CompareChangeTypeEnum.MODIFY.getName())) {
                    long count = option.getPackInfos().stream().filter(s -> compareOxoFeatureModel.getSalesOptionInfoMap().containsKey(
                            String.format("%s:%s:%s:%s:%s:%s", versionName, s.getModelCode(),
                                    s.getModelYear(), s.getRegionCode(), s.getDriveHandCode(), s.getSalesCode()))).count();
                    long unAvailableCount = option.getPackInfos().stream().filter(s -> compareOxoFeatureModel.getSalesOptionInfoMap().containsKey(String.format("%s:%s:%s:%s:%s:%s", versionName,
                            s.getModelCode(), s.getModelYear(), s.getRegionCode(), s.getDriveHandCode(), s.getSalesCode())) && s.getPackageCode().equals(ConfigConstants.UN_AVAILABLE)).count();

                    log.info("option:{},delete count:{},unAvailableCount:{},changeType:{}", option.getFeatureCode(), count, unAvailableCount, option.getChangeType());

                    if (unAvailableCount == count && count > 0) {
                        option.setChangeType(CompareChangeTypeEnum.DELETE.getName());
                    }

                    // 系统判断Option行在所有基础车型下的赋值从全部为“-”改成部分不为“-”，则Change Type为Add
                    if (!option.getPackInfos().stream().allMatch(x -> StringUtils.equals(ConfigConstants.UN_AVAILABLE, x.getPackageCode()))) {
                        List<String> packageCodes = Lists.newArrayList();
                        option.getPackInfos().forEach(packageCode -> {
                            if (Objects.nonNull(packageCode.getCompareOxoEdit())) {
                                packageCodes.add(packageCode.getCompareOxoEdit().getPackageCode());
                            } else {
                                packageCodes.add(packageCode.getPackageCode());
                            }
                        });
                        if (packageCodes.stream().allMatch(x -> StringUtils.equals(x, ConfigConstants.UN_AVAILABLE))) {
                            option.setChangeType(CompareChangeTypeEnum.ADD.getName());
                        }
                    }

                }
            });
        });
    }


    /**
     * 如果父级别是add或者delete,则子级直接继承
     * 设置FeatureChildrenChangeType
     *
     * @param children children
     * @param typeName typeName
     */
    private void setFeatureChildrenChangeType(List<OxoRowsQry> children, String typeName) {
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        children.forEach(child -> {
            child.setChangeType(typeName);
        });
    }


    private CompareOxoFeatureModelRespDto buildCompareModel(List<OxoListRespDto> qryList, List<String> versionList) {

        CompareOxoFeatureModelRespDto compareOxoFeatureModel = new CompareOxoFeatureModelRespDto();
        Map<String, OxoRowsQry> featureMap = compareOxoFeatureModel.getFeatureMap();
        Map<String, OxoRowsQry> optionMap = compareOxoFeatureModel.getOptionMap();
        Map<String, OxoEditCmd> oxoMap = compareOxoFeatureModel.getOxoMap();
        Map<String, OxoHeadQry.RegionInfo> modelYearFeatureOptionMap = compareOxoFeatureModel.getModelYearFeatureOptionMap();
        Map<String, OxoHeadQry.DriveHandInfo> driverOptionInfoMap = compareOxoFeatureModel.getDriverOptionInfoMap();
        Map<String, OxoHeadQry.SalesVersionInfo> salesOptionInfoMap = compareOxoFeatureModel.getSalesOptionInfoMap();


        for (int i = 0; i < qryList.size(); i++) {
            String versionName = versionList.get(i);
            OxoListRespDto ipFeatureEntity = qryList.get(i);

            ipFeatureEntity.getOxoRowsResps().forEach(feature -> {
                //ipFeature
                featureMap.put(String.format("%s:%s", versionName, feature.getFeatureCode()), feature);
                if (CollectionUtils.isEmpty(feature.getOptions())) {
                    return;
                }
                //option
                feature.getOptions().forEach(child -> {
                    optionMap.put(String.format("%s:%s:%s", versionName, feature.getFeatureCode(), child.getFeatureCode()), child);
                    if (CollectionUtils.isEmpty(child.getPackInfos())) {
                        return;
                    }
                    //OxoConfigration
                    child.getPackInfos().forEach(oxo -> {
                        oxoMap.put(String.format("%s:%s:%s:%s:%s:%s:%s:%s", versionName,
                                oxo.getModelCode(), oxo.getModelYear(), feature.getFeatureCode(), child.getFeatureCode(),
                                oxo.getRegionCode(), oxo.getDriveHandCode(), oxo.getSalesCode()), oxo);
                    });
                });
            });

            ipFeatureEntity.getOxoHeadResps().forEach(oxoHeadQry -> {

                oxoHeadQry.getRegionInfos().forEach(regionInfo -> {

                    //modelYearFeature
                    String key = String.format("%s:%s:%s:%s", versionName, oxoHeadQry.getModelCode(), oxoHeadQry.getModelYear(), regionInfo.getRegionCode());
                    modelYearFeatureOptionMap.put(key, regionInfo);
                    if (CollectionUtils.isEmpty(regionInfo.getDriveHands())) {
                        return;
                    }
                    //driverOption
                    regionInfo.getDriveHands().forEach(driveHandInfo -> {
                        String driveKey = String.format("%s:%s:%s:%s:%s", versionName, oxoHeadQry.getModelCode(), oxoHeadQry.getModelYear(),
                                regionInfo.getRegionCode(), driveHandInfo.getDriveHandCode());
                        driverOptionInfoMap.put(driveKey, driveHandInfo);

                        if (CollectionUtils.isEmpty(driveHandInfo.getSalesVersionInfos())) {
                            return;
                        }
                        //SalesVersion
                        driveHandInfo.getSalesVersionInfos().forEach(salesVersionInfo -> {
                            String salesKey = String.format("%s:%s:%s:%s:%s:%s", versionName, oxoHeadQry.getModelCode(), oxoHeadQry.getModelYear(),
                                    regionInfo.getRegionCode(), driveHandInfo.getDriveHandCode(), salesVersionInfo.getSalesCode());
                            salesOptionInfoMap.put(salesKey, salesVersionInfo);
                        });
                    });
                });

            });
        }
        return compareOxoFeatureModel;


    }


    @Override
    public void sendCompareEmail(OxoListRespDto compareOxoListQry, OxoVersionSnapshotAggr oxoVersionSnapshot) {

        List<OxoTemplateRequestCmd.HeadTitle> headTitles = new LinkedList<>();
        List<OxoTemplateRequestCmd.OxoInfo> oxoInfos = new LinkedList<>();
        List<OxoTemplateRequestCmd.RegionOptionCode> regionOptionCodes = new LinkedList<>();
        List<OxoTemplateRequestCmd.DriveHandOptionCode> driveHandOptionCodes = new LinkedList<>();
        List<OxoTemplateRequestCmd.SalesOptionName> salesOptionNames = new LinkedList<>();

        String userName = oxoVersionSnapshot.getCreateUser();

        //新老版本
        String oldVersion = oxoVersionSnapshot.getPreVersion();
        String newVersion = oxoVersionSnapshot.getVersion();
        String modelCode = oxoVersionSnapshot.getModelCode();

        //最新oxo版本
        OxoListRespDto newOxoListOry = JSONObject.parseObject(JSONArray.parse(GZIPUtils.uncompress(oxoVersionSnapshot.getOxoSnapshot())).toString(), OxoListRespDto.class);

        List<OxoHeadQry> compareInfos = compareOxoListQry.getOxoHeadResps();


        if (CollectionUtils.isEmpty(compareInfos)) {
            compareInfos = newOxoListOry.getOxoHeadResps();
        }


        int titleSize = 0;
        for (OxoHeadQry compareInfo : compareInfos) {

            String title = compareInfo.getModelCode() + " " + compareInfo.getModelYear() + " " + oldVersion + " vs " + compareInfo.getModelCode() + " " + compareInfo.getModelYear() + " " + newVersion;

            int headTitleSize = 0;
            for (OxoHeadQry.RegionInfo regionInfo : compareInfo.getRegionInfos()) {
                OxoTemplateRequestCmd.RegionOptionCode code = new OxoTemplateRequestCmd.RegionOptionCode();
                code.setRegionOptionCode(regionInfo.getRegionName());
                code.setColor("#e0ffff");//蓝色

                int regionCodeSize = 0;
                for (OxoHeadQry.DriveHandInfo driveHandInfo : regionInfo.getDriveHands()) {
                    OxoTemplateRequestCmd.DriveHandOptionCode driveOptionCode = new OxoTemplateRequestCmd.DriveHandOptionCode();
                    driveOptionCode.setDriveHandOptionCode(driveHandInfo.getDriveHandName());
                    regionCodeSize += driveHandInfo.getSalesVersionInfos().size();
                    headTitleSize += driveHandInfo.getSalesVersionInfos().size();
                    titleSize += driveHandInfo.getSalesVersionInfos().size();
                    driveOptionCode.setDriveHandOptionSize(String.valueOf(driveHandInfo.getSalesVersionInfos().size()));
                    driveOptionCode.setColor("#e0ffff");//蓝色
                    driveHandOptionCodes.add(driveOptionCode);

                    for (OxoHeadQry.SalesVersionInfo salesOptionInfo : driveHandInfo.getSalesVersionInfos()) {
                        OxoTemplateRequestCmd.SalesOptionName salesOptionName = new OxoTemplateRequestCmd.SalesOptionName();

                        if (StringUtils.equals(CompareChangeTypeEnum.ADD.getName(), salesOptionInfo.getChangeType()) ||
                                StringUtils.equals(CompareChangeTypeEnum.DELETE.getName(), salesOptionInfo.getChangeType())) {
                            salesOptionName.setSalesOptionName(salesOptionInfo.getChangeType() + " " + salesOptionInfo.getSalesName());
                        } else {
                            salesOptionName.setSalesOptionName(salesOptionInfo.getSalesName());
                        }

                        if (StringUtils.equals(CompareChangeTypeEnum.ADD.getName(), salesOptionInfo.getChangeType())) {
                            salesOptionName.setColor("#228b22"); //绿
                        } else if (StringUtils.equals(CompareChangeTypeEnum.DELETE.getName(), salesOptionInfo.getChangeType())) {
                            salesOptionName.setColor("#f08080"); //红
                        }
                        salesOptionNames.add(salesOptionName);
                    }
                }

                code.setRegionCodeSize(String.valueOf(regionCodeSize));
                regionOptionCodes.add(code);
            }
            OxoTemplateRequestCmd.HeadTitle headTitle = new OxoTemplateRequestCmd.HeadTitle();
            headTitle.setHeadTitle(title);
            headTitle.setHeadTitleSize(String.valueOf(headTitleSize));
            headTitles.add(headTitle);
        }


        List<OxoRowsQry> features = compareOxoListQry.getOxoRowsResps();

        List<OxoBasicVehicleDto> oxoBasicVehicleDtos =
                BaseVehicleFactory.buildOxoBasicVehicles(compareInfos, null, null);

        int finalTitleSize = titleSize;
        features.forEach(feature -> {

            OxoTemplateRequestCmd.OxoInfo oxoInfo = new OxoTemplateRequestCmd.OxoInfo();
            oxoInfo.setName(feature.getChineseName());
            oxoInfo.setChangeType(feature.getChangeType());
            oxoInfo.setFeatureCode(feature.getFeatureCode());
            oxoInfo.setDisPlayName(feature.getDisplayName());
            oxoInfo.setLibrary(feature.getGroup());

            oxoInfo.setPackageOptions(convertPackageOptionAll(finalTitleSize, null, null));
            oxoInfo.setColor("#e0ffff"); //蓝色
            oxoInfos.add(oxoInfo);
            feature.getOptions().forEach(y -> {
                OxoTemplateRequestCmd.OxoInfo childrenOxo = new OxoTemplateRequestCmd.OxoInfo();
                childrenOxo.setLibrary(y.getGroup());
                childrenOxo.setName(y.getChineseName());
                childrenOxo.setDisPlayName(y.getDisplayName());
                childrenOxo.setFeatureCode(y.getFeatureCode());
                childrenOxo.setPackageOptions(convertPackageOptionAll(finalTitleSize, y, oxoBasicVehicleDtos));
                if (childrenOxo.getPackageOptions().stream().allMatch(x -> StringUtils.isBlank(x.getPackageOption()))) {
                    childrenOxo.setChangeType(null);
                } else if (childrenOxo.getPackageOptions().stream().noneMatch(x -> StringUtils.isNotBlank(x.getPackageOption())
                        && x.getPackageOption().contains(">"))
                        && StringUtils.equals(y.getChangeType(), CompareChangeTypeEnum.MODIFY.getName())) {
                    childrenOxo.setChangeType(null);
                } else {
                    childrenOxo.setChangeType(StringUtils.equals(y.getChangeType(), CompareChangeTypeEnum.DEL.getName()) ? null : y.getChangeType());
                }

                //childrenOxo.setColor("#fffaf0"); //白色
                oxoInfos.add(childrenOxo);
            });
        });

        OxoTemplateRequestCmd templateRequest = new OxoTemplateRequestCmd();
        templateRequest.setRegionOptionCodes(regionOptionCodes);
        templateRequest.setChangeContent(oxoVersionSnapshot.getChangeContent().replaceAll("\n","<br/>"));
        templateRequest.setUrl(oxoEmailChangeLogUrl.replace("nio", oxoVersionSnapshot.getBrand().toLowerCase()) + modelCode);


        templateRequest.setHeadTitles(headTitles);
        templateRequest.setSalesOptionNames(salesOptionNames);
        templateRequest.setDriveHandOptionCodes(driveHandOptionCodes);
        templateRequest.setTemplates(oxoInfos);
        templateRequest.setOxoTitle(oxoVersionSnapshot.getTitle() + "_" + oxoVersionSnapshot.getVersion());
        buildAndSendEmail(templateRequest, getEmails(oxoVersionSnapshot), userName);
    }


    /**
     * 获取收件人
     *
     * @param oxoVersionSnapshot
     * @return
     */
    public List<String> getEmails(OxoVersionSnapshotAggr oxoVersionSnapshot) {

        String name = ConfigConstants.OXO_EMAIL_GROUP;
        if (StringUtils.equalsAnyIgnoreCase(oxoVersionSnapshot.getBrand(), BrandEnum.ALPS.name())) {
            name = ConfigConstants.OXO_EMAIL_GROUP_ALPS;
        } else if (StringUtils.equalsAnyIgnoreCase(oxoVersionSnapshot.getBrand(), BrandEnum.FY.name())) {
            name = ConfigConstants.OXO_EMAIL_GROUP_FY;
        }

        Map<String, String> map = matrixRuleFacade.queryMatrixRuleValuesByAbscissaOrOrdinate(new MatrixRuleQueryDto
                (name, "matrix", null, "oxo.email", "PLM.EBOM.PartNumRequest"));

        String[] emailGroups = oxoVersionSnapshot.getEmailGroup().split(",");

        List<String> emails = new LinkedList<>();

        for (String group : emailGroups) {
            emails.addAll(Arrays.asList(map.get(group).split("\n")));
        }

        return emails.stream().distinct().collect(Collectors.toList());
    }

    private void buildAndSendEmail(OxoTemplateRequestCmd templateRequest, List<String> users, String userName) {
        for (String user : users) {
            EmailParamDto emailParamDto = new EmailParamDto();
            emailParamDto.setTemplateNo(oxoEmailTemplateNo);
            if (!StringUtils.contains(user, "@nio.com")) {
                emailParamDto.setReceiverEmail(user + "@nio.com");
            } else {
                emailParamDto.setReceiverEmail(user);
            }
            Map<String, Object> maps = JSON.parseObject(JSONObject.toJSONString(templateRequest), Map.class);
            maps.put("PLM_Send_Address", userName+ "@nio.com");
            emailParamDto.setVariables(maps);
            emailFacade.sendEmail(emailParamDto);
        }
    }

    public List<OxoTemplateRequestCmd.PackageOption> convertPackageOptionAll(int titleSize, OxoRowsQry option,
                                                                             List<OxoBasicVehicleDto> oxoBasicVehicleDtos) {

        List<OxoTemplateRequestCmd.PackageOption> options = new ArrayList<>();


        // 设置 feature
        if (Objects.isNull(option) || CollectionUtils.isEmpty(option.getPackInfos())) {
            for (int i = 0; i < titleSize; i++) {
                OxoTemplateRequestCmd.PackageOption packageOption = new OxoTemplateRequestCmd.PackageOption();
                if (Objects.isNull(option)) {
                    packageOption.setColor("#e0ffff");  //蓝色
                }
                packageOption.setPackSize(titleSize);
                options.add(packageOption);
            }
            return options;
        }

        for (OxoBasicVehicleDto oxoBasicVehicleDto : oxoBasicVehicleDtos) {

            OxoEditCmd oxoEditCmd =
                    option.getPackInfos().stream().filter(y -> Objects.equals(y.getHeadId(), oxoBasicVehicleDto.getId())).findFirst().orElse(null);

            OxoTemplateRequestCmd.PackageOption packageOption = new OxoTemplateRequestCmd.PackageOption();
            packageOption.setPackSize(titleSize);

            if (Objects.isNull(oxoEditCmd)) {
                options.add(packageOption);
                continue;
            }

            if (StringUtils.equals(oxoBasicVehicleDto.getChangeType(), CompareChangeTypeEnum.DELETE.getName())) {
                options.add(packageOption);
                continue;
            }


            if (Objects.nonNull(oxoEditCmd.getCompareOxoEdit())) {
                packageOption.setPackageOption(OxoOptionPackageTypeEnum.getByType(oxoEditCmd.getCompareOxoEdit().getPackageCode()).getCode() +
                        " > " + OxoOptionPackageTypeEnum.getByType(oxoEditCmd.getPackageCode()).getCode());
                packageOption.setColor("#f0e68c");
            } else {
                packageOption.setPackageOption(OxoOptionPackageTypeEnum.getByType(oxoEditCmd.getPackageCode()).getCode());
            }

            options.add(packageOption);
        }

        return options;


    }
}
