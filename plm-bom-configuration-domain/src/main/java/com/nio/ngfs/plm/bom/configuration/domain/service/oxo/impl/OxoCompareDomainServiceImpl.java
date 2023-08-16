package com.nio.ngfs.plm.bom.configuration.domain.service.oxo.impl;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.GZIPUtils;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.domainobject.CompareOxoFeatureModel;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums.CompareChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoCompareDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoTemplateRequestCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangchao.wang
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OxoCompareDomainServiceImpl implements OxoCompareDomainService {


    /**
     * 高低版本对比
     *
     * @param baseQry    低版本
     * @param compareQry 高版本
     * @param showDiff   只展示不一样的结果
     * @return
     */
    @Override
    public OxoListQry compareVersion(OxoListQry baseQry, OxoListQry compareQry, boolean showDiff) {


        CompareOxoFeatureModel compareIpFeatureModel = buildCompareModel(Lists.newArrayList(baseQry, compareQry), Lists.newArrayList(
                ConfigConstants.BASE_VERSION, ConfigConstants.LOW_VERSION));
        compareIpFeatureModel.setBaseIpFeature(baseQry);
        compareIpFeatureModel.setCompareIpFeature(compareQry);

        wrapBaseIpFeatureChangeType(compareIpFeatureModel, baseQry, true);
        wrapBaseIpFeatureChangeType(compareIpFeatureModel, compareQry, false);
        //comPareOxoFeatureInfoRequest.setShowDiff(true);
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
    private OxoListQry filterDiffData(OxoListQry oxoListQry) {
        if (oxoListQry == null) {
            return oxoListQry;
        }
        OxoListQry diffOxoFeatureEntity = new OxoListQry();
        filterDiffFeature(diffOxoFeatureEntity, oxoListQry.getOxoRowsResps());
        diffOxoFeatureEntity.setOxoHeadResps(oxoListQry.getOxoHeadResps());
        return diffOxoFeatureEntity;
    }


    /**
     * 过滤Feature
     *
     * @param oxoListQry
     * @param oxoRowsQries
     */
    private void filterDiffFeature(OxoListQry oxoListQry, List<OxoRowsQry> oxoRowsQries) {
        if (CollectionUtils.isEmpty(oxoRowsQries)) {
            return;
        }
        List<OxoRowsQry> ipdList = Lists.newArrayList();
        ipdList.addAll(oxoRowsQries);
        oxoListQry.setOxoRowsResps(ipdList);
        //处理Feature
        oxoRowsQries.forEach(feature -> {
            //无变化的跳过
            if (StringUtils.isEmpty(feature.getChangeType()) && checkModify(feature)) {
                ipdList.remove(feature);
                return;
            }
            //ADD或者DELETE下面节点属性都是一样
            if (StringUtils.isNotBlank(feature.getChangeType()) && !feature.getChangeType().equals(CompareChangeTypeEnum.MODIFY.getName())) {
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
            feature.setOptions(optionList);
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
     * @param oxoListQry
     * @param baseVersion
     */
    private void wrapBaseIpFeatureChangeType(CompareOxoFeatureModel compareOxoFeatureModel, OxoListQry oxoListQry, boolean baseVersion) {
        //待比较待版本名称
        String versionName = baseVersion ? ConfigConstants.LOW_VERSION : ConfigConstants.BASE_VERSION;
        wrapFeatureOptionChangeType(compareOxoFeatureModel, oxoListQry, baseVersion, versionName);
        wrapHeaderChangeType(compareOxoFeatureModel, oxoListQry, baseVersion, versionName);
    }


    /**
     * 设置headerChangeType
     *
     * @param compareOxoFeatureModel
     * @param oxoListQry
     * @param baseVersion
     * @param versionName
     */
    private void wrapHeaderChangeType(CompareOxoFeatureModel compareOxoFeatureModel,
                                      OxoListQry oxoListQry, boolean baseVersion, String versionName) {
        Map<String, OxoHeadQry.RegionInfo> modelYearFeatureOptionMap = compareOxoFeatureModel.getModelYearFeatureOptionMap();
        Map<String, OxoHeadQry.DriveHandInfo> driverOptionInfoMap = compareOxoFeatureModel.getDriverOptionInfoMap();
        Map<String, OxoHeadQry.SalesVersionInfo> salesOptionInfoMap = compareOxoFeatureModel.getSalesOptionInfoMap();

        oxoListQry.getOxoHeadResps().forEach(modelYear -> {
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
//                    //基准添加删除的modelYearFeature
//                    List<ModelYearFeatureOption> modelYearFeatureOptionList =
//                            compareOxoFeatureModel.getBaseIpFeature().getOxoHeadResps()
//                    modelYear.add(modelYearFeatureOption);
//                    compareOxoFeatureModel.getBaseIpFeature().getOxoRowsResps().put(k, modelYearFeatureOptionList);
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
     * @param oxoListQry
     * @param baseVersion
     * @param versionName
     */
    private void wrapFeatureOptionChangeType(CompareOxoFeatureModel compareOxoFeatureModel,
                                             OxoListQry oxoListQry, boolean baseVersion, String versionName) {
        Map<String, OxoRowsQry> featureMap = compareOxoFeatureModel.getFeatureMap();
        Map<String, OxoRowsQry> optionMap = compareOxoFeatureModel.getOptionMap();
        Map<String, OxoEditCmd> oxoMap = compareOxoFeatureModel.getOxoMap();
        // Feature/option列，只需要高版本和低版本比较,不需要低版本和高版本比较
        if (!baseVersion) {
            return;
        }
        oxoListQry.getOxoRowsResps().forEach(feature -> {
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
                    feature.setChangeType(CompareChangeTypeEnum.MODIFY.getName());
                    return;
                }
                //option高低版本都存在
                option.getPackInfos().forEach(oxo -> {
                    //比较所有低oxo打点
                    String oxoKey = String.format("%s:%s:%s:%s:%s:%s:%s",
                            versionName, oxo.getModelCode(), oxo.getModelYear(),feature.getFeatureCode(),
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
//                long count=option.getOptionOxoConfigration().stream().filter(s -> s.getPackageCode().equals(Constant.UN_AVAILABLE)
//                        && oxoMap.containsKey(String.format("%s:%s:%s:%s:%s:%s", versionName, feature.getFeatureCode(), option.getOptionCode(),
//                        s.getRegionOptionCode(), s.getDriveOptionCode(), s.getSalesOptionCode()))).count();

                long count = option.getPackInfos().stream().filter(s -> compareOxoFeatureModel.getSalesOptionInfoMap().containsKey(
                        String.format("%s:%s:%s:%s:%s:%s:%s", versionName, s.getModelCode(),
                        s.getModelYear(), "AD00", s.getRegionCode(), s.getDriveHandCode(), s.getSalesCode()))).count();


                long unAvailableCount = option.getPackInfos().stream().filter(s -> compareOxoFeatureModel.getSalesOptionInfoMap().containsKey(String.format("%s:%s:%s:%s:%s:%s:%s", versionName,
                        s.getModelCode(),s.getModelYear(), "AD00",
                        s.getRegionCode(), s.getDriveHandCode(), s.getSalesCode())) && s.getPackageCode().equals(ConfigConstants.UN_AVAILABLE)).count();

                //2、check option所有的oxo是不是Unavailable，如果都是Unavailable，则设置option为Delete
                log.info("option:{},delete count:{},unAvailableCount:{},changeType:{}", option.getFeatureCode(), count, unAvailableCount, option.getChangeType());
                if (unAvailableCount == count && StringUtils.equals(option.getChangeType(), CompareChangeTypeEnum.MODIFY.getName()) && count > 0) {
                    option.setChangeType(CompareChangeTypeEnum.DELETE.getName());
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


    private CompareOxoFeatureModel buildCompareModel(List<OxoListQry> qryList, List<String> versionList) {

        CompareOxoFeatureModel compareOxoFeatureModel = new CompareOxoFeatureModel();
        Map<String, OxoRowsQry> featureMap = compareOxoFeatureModel.getFeatureMap();
        Map<String, OxoRowsQry> optionMap = compareOxoFeatureModel.getOptionMap();
        Map<String, OxoEditCmd> oxoMap = compareOxoFeatureModel.getOxoMap();
        Map<String, OxoHeadQry.RegionInfo> modelYearFeatureOptionMap = compareOxoFeatureModel.getModelYearFeatureOptionMap();
        Map<String, OxoHeadQry.DriveHandInfo> driverOptionInfoMap = compareOxoFeatureModel.getDriverOptionInfoMap();
        Map<String, OxoHeadQry.SalesVersionInfo> salesOptionInfoMap = compareOxoFeatureModel.getSalesOptionInfoMap();


        for (int i = 0; i < qryList.size(); i++) {
            String versionName = versionList.get(i);
            OxoListQry ipFeatureEntity = qryList.get(i);

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
                        oxoMap.put(String.format("%s:%s:%s:%s:%s:%s:%s", versionName,
                                oxo.getModelCode(), oxo.getModelYear(), feature.getFeatureCode(),
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



//    public void sendCompareOxoEmails(OxoSnapshotRequest request, OxoVersionSnapshot oldSnapshot, IpFeatureEntity newEntity, OxoVersionSnapshot newSnapshot) {
//
//        List<OxoTemplateRequestCmd.HeadTitle> headTitles = new LinkedList<>();
//        List<OxoTemplateRequestCmd.OxoInfo> oxoInfos = new LinkedList<>();
//        List<OxoTemplateRequestCmd.RegionOptionCode> regionOptionCodes = new LinkedList<>();
//        List<OxoTemplateRequestCmd.DriveHandOptionCode> driveHandOptionCodes = new LinkedList<>();
//        List<OxoTemplateRequestCmd.SalesOptionName> salesOptionNames = new LinkedList<>();
//
//        if(Objects.nonNull(oldSnapshot)) {
//            IpFeatureEntity oldEntity = JsonHelper.parseToObject(GZIPUtils.uncompress(oldSnapshot.getOxoSnapshot()), IpFeatureEntity.class);
//
//
//            ComPareOxoFeatureInfoRequest featureInfoRequest = new ComPareOxoFeatureInfoRequest();
//            featureInfoRequest.setShowDiff(true);
//
//            IpFeatureEntity ipFeatureEntity = compareVersion(newEntity, oldEntity, featureInfoRequest, true);
//
//            log.info("ipFeatureEntity entity;{}", JsonHelper.parseToJson(ipFeatureEntity));
//
//
//            String oldVersion = oldSnapshot.getVersion();
//            String newVersion = newSnapshot.getVersion();
//
//
//            Map<String, List<ModelYearFeatureOption>> compareInfos = ipFeatureEntity.getHeadInfo();
//
//            if (MapUtils.isEmpty(compareInfos) || compareInfos.size() == 0) {
//                compareInfos = newEntity.getHeadInfo();
//            }
//
//
//            int titleSize = 0;
//            for (Map.Entry<String, List<ModelYearFeatureOption>> entry : compareInfos.entrySet()) {
//                String title = entry.getKey() + " " + oldVersion + " vs " + entry.getKey() + " " + newVersion;
//
//                List<ModelYearFeatureOption> featureOptions = entry.getValue();
//
//                int headTitleSize = 0;
//                for (ModelYearFeatureOption region : featureOptions) {
//                    OxoTemplateRequest.RegionOptionCode code = new OxoTemplateRequest.RegionOptionCode();
//                    code.setRegionOptionCode(region.getOptionEnName());
//                    code.setColor("#e0ffff");//蓝色
//                    List<ModelYearFeatureOption.DriverOptionInfo> driverOptionInfos = region.getDriverOptionInfos();
//
//                    int regionCodeSize = 0;
//                    for (ModelYearFeatureOption.DriverOptionInfo driverOptionInfo : driverOptionInfos) {
//                        OxoTemplateRequest.DriveHandOptionCode driveOptionCode = new OxoTemplateRequest.DriveHandOptionCode();
//                        driveOptionCode.setDriveHandOptionCode(StringUtils.isNotBlank(driverOptionInfo.getDriveHardOptionName()) ?
//                                driverOptionInfo.getDriveHardOptionName() : driverOptionInfo.getDriveHardOptionCode());
//                        regionCodeSize += driverOptionInfo.getSalesVersionList().size();
//                        headTitleSize += driverOptionInfo.getSalesVersionList().size();
//                        titleSize += driverOptionInfo.getSalesVersionList().size();
//                        driveOptionCode.setDriveHandOptionSize(String.valueOf(driverOptionInfo.getSalesVersionList().size()));
//                        driveOptionCode.setColor("#e0ffff");//蓝色
//                        driveHandOptionCodes.add(driveOptionCode);
//
//                        for (ModelYearFeatureOption.SalesOptionInfo salesOptionInfo : driverOptionInfo.getSalesVersionList()) {
//                            OxoTemplateRequest.SalesOptionName salesOptionName = new OxoTemplateRequest.SalesOptionName();
//
//                            if (StringUtils.equals(ComapreChangeTypeEnum.ADD.getName(), salesOptionInfo.getChangeType()) ||
//                                    StringUtils.equals(ComapreChangeTypeEnum.DELETE.getName(), salesOptionInfo.getChangeType())) {
//                                salesOptionName.setSalesOptionName(salesOptionInfo.getChangeType() + " " + salesOptionInfo.getChineseName());
//                            } else {
//                                salesOptionName.setSalesOptionName(salesOptionInfo.getChineseName());
//                            }
//
//                            if (StringUtils.equals(ComapreChangeTypeEnum.ADD.getName(), salesOptionInfo.getChangeType())) {
//                                salesOptionName.setColor("#228b22"); //绿
//                            } else if (StringUtils.equals(ComapreChangeTypeEnum.DELETE.getName(), salesOptionInfo.getChangeType())) {
//                                salesOptionName.setColor("#f08080"); //红
//                            }
////                        else{
////                            salesOptionName.setColor("#fffaf0"); //白
////                        }
//                            salesOptionNames.add(salesOptionName);
//                        }
//                    }
//
//                    code.setRegionCodeSize(String.valueOf(regionCodeSize));
//                    regionOptionCodes.add(code);
//                }
//
//                OxoTemplateRequest.HeadTitle headTitle = new OxoTemplateRequest.HeadTitle();
//                headTitle.setHeadTitle(title);
//                headTitle.setHeadTitleSize(String.valueOf(headTitleSize));
//                headTitles.add(headTitle);
//            }
//
//            List<IpdOXOOutput> ipdOXOOutputs = convertPackageOption(compareInfos);
//
//            List<Feature> features = ipFeatureEntity.getIpdList();
//
//            int finalTitleSize = titleSize;
//            features.forEach(feature -> {
//                List<IpdOXOOutput> ipdOXOOutputList = new ArrayList<>();
//                feature.getChildren().forEach(x -> {
//                    ipdOXOOutputList.addAll(x.getOptionOxoConfigration());
//                });
//
//                if (CollectionUtils.isEmpty(ipdOXOOutputList)) {
//                    return;
//                }
//
//                OxoTemplateRequest.OxoInfo oxoInfo = new OxoTemplateRequest.OxoInfo();
//                oxoInfo.setName(feature.getChineseName());
//                oxoInfo.setChangeType(feature.getChangeType());
//                oxoInfo.setFeatureCode(feature.getFeatureCode());
//                oxoInfo.setDisPlayName(feature.getName());
//                oxoInfo.setLibrary(feature.getLibrary());
//
//                oxoInfo.setPackageOptions(convertPackageOptionAll(finalTitleSize, null, null));
//                oxoInfo.setColor("#e0ffff"); //蓝色
//                oxoInfos.add(oxoInfo);
//                feature.getChildren().forEach(y -> {
//                    if (CollectionUtils.isEmpty(y.getOptionOxoConfigration())) {
//                        return;
//                    }
//                    OxoTemplateRequest.OxoInfo childrenOxo = new OxoTemplateRequest.OxoInfo();
//                    childrenOxo.setLibrary(y.getLibrary());
//                    childrenOxo.setName(y.getChineseName());
//                    childrenOxo.setDisPlayName(y.getOptionName());
//                    childrenOxo.setFeatureCode(y.getOptionCode());
//                    childrenOxo.setPackageOptions(convertPackageOptionAll(finalTitleSize, y, ipdOXOOutputs));
//                    if (childrenOxo.getPackageOptions().stream().allMatch(x -> StringUtils.isBlank(x.getPackageOption()))) {
//                        childrenOxo.setChangeType(null);
//                    }
//                    else if(childrenOxo.getPackageOptions().stream().noneMatch(x -> StringUtils.isNotBlank(x.getPackageOption())
//                            && x.getPackageOption().contains(">"))
//                            && StringUtils.equals(y.getChangeType(), ComapreChangeTypeEnum.MODIFY.getName())){
//                        childrenOxo.setChangeType(null);
//                    }
//                    else {
//                        childrenOxo.setChangeType(StringUtils.equals(y.getChangeType(), ComapreChangeTypeEnum.DEL.getName()) ? null : y.getChangeType());
//                    }
//
//                    //childrenOxo.setColor("#fffaf0"); //白色
//                    oxoInfos.add(childrenOxo);
//                });
//            });
//        }
//
//
//        OxoTemplateRequest templateRequest = new OxoTemplateRequest();
//        templateRequest.setRegionOptionCodes(regionOptionCodes);
//        templateRequest.setChangeContent(newSnapshot.getChangeContent());
//        if(StringUtils.equals(newSnapshot.getBrand(), BrandEnum.NIO_CONSTANT)) {
//            templateRequest.setUrl(oxoEmailChangeLogUrl + request.getModelCode());
//        }else {
//            templateRequest.setUrl(oxoEmailAlpsChangeLogUrl + request.getModelCode());
//        }
//
//        templateRequest.setHeadTitles(headTitles);
//        templateRequest.setSalesOptionNames(salesOptionNames);
//        templateRequest.setDriveHandOptionCodes(driveHandOptionCodes);
//        templateRequest.setTemplates(oxoInfos);
//        templateRequest.setOxoTitle(newSnapshot.getTitle() + "_" + newSnapshot.getVersion());
//        buildAndSendEmail(templateRequest, getEmails(request));
//    }
//
//
//    public List<String> getEmails(OxoSnapshotRequest request){
//
//        //获取邮件收件人
//        Map<String, String> map = queryMatrixRuleValuesByAbscissaOrOrdinate(new MatrixRuleQueryParam
//                (BaseCommonConstant.OXO_EMAIL_GROUP, "matrix", null, "oxo.email", "PLM.EBOM.PartNumRequest"));
//
//        String[] emailGroups = request.getSetGroup().split(",");
//
//        List<String> emails = new LinkedList<>();
//
//        for (String group : emailGroups) {
//            emails.addAll(Arrays.asList(map.get(group).split("\n")));
//        }
//
//        return emails.stream().distinct().collect(Collectors.toList());
//    }
//
//    private void buildAndSendEmail(OxoTemplateRequest templateRequest, List<String> users) {
//        List<Map> maps = new ArrayList<>();
//
//        for (String user : users) {
//            Map<String, Object> emailMap = new HashMap<>(4);
//            emailMap.put("templateNo", oxoEmailTemplateNo);
//            emailMap.put("variables", templateRequest);
//            emailMap.put("receiverEmail", user + "@nio.com");
//            emailMap.put("subject", "");
//            maps.add(emailMap);
//        }
//
//        Map params = new HashMap();
//        params.put("mailList", maps);
//        try {
//            String param = JsonHelper.parseToJson(params);
//            log.info("buildAnd request:{}", param);
//            productConfigurationFeign.sendBatchEmails(param);
//        } catch (Exception e) {
//            log.error("sendBatchEmails failed", e);
//        }
//    }
//
//
//    public List<OxoTemplateRequest.PackageOption> convertPackageOptionAll(int titleSize, Option option, List<IpdOXOOutput> ipdOXOOutputs) {
//
//        List<OxoTemplateRequest.PackageOption> options = new ArrayList<>();
//
//        if (Objects.isNull(option) || CollectionUtils.isEmpty(option.getOptionOxoConfigration())) {
//            for (int i = 0; i < titleSize; i++) {
//                OxoTemplateRequest.PackageOption packageOption = new OxoTemplateRequest.PackageOption();
//                if (Objects.isNull(option)) {
//                    packageOption.setColor("#e0ffff");  //蓝色
//                }
////                }else{
////                    packageOption.setColor("#fffaf0");  //白色
////                }
//                packageOption.setPackSize(titleSize);
//                options.add(packageOption);
//            }
//            return options;
//        }
//
//        ipdOXOOutputs.forEach(x -> {
//
//            List<IpdOXOOutput> ipdOXOOutputs1 = option.getOptionOxoConfigration().stream().filter(y -> StringUtils.equals(y.getModelYear(), x.getModelYear()) &&
//                    StringUtils.equals(y.getSalesOptionCode(), x.getSalesOptionCode()) && StringUtils.equals(y.getDriveOptionCode(), x.getDriveOptionCode())
//                    && StringUtils.equals(y.getRegionOptionCode(), x.getRegionOptionCode()) &&
//                    !StringUtils.equals(y.getChangeType(),ComapreChangeTypeEnum.DELETE.getName())).collect(Collectors.toList());
//
//            //delete 列需要删除
//            if (ipdOXOOutputs1.size() > 0 && !StringUtils.equals(x.getChangeType(),ComapreChangeTypeEnum.DELETE.getName())) {
//                IpdOXOOutput ipdOXOOutput = ipdOXOOutputs1.get(0);
//                //todo
//                OxoTemplateRequest.PackageOption packageOption = new OxoTemplateRequest.PackageOption();
//                if (Objects.nonNull(ipdOXOOutput.getCompareIpdOXOOutput())) {
//                    packageOption.setPackageOption(convertPackageNo(ipdOXOOutput.getCompareIpdOXOOutput().getPackageNo()) + " > " + convertPackageNo(ipdOXOOutput.getPackageNo()));
//                } else {
//                    packageOption.setPackageOption(convertPackageNo(ipdOXOOutput.getPackageNo()));
//                }
//                packageOption.setPackSize(titleSize);
//                if (!StringUtils.equals(ComapreChangeTypeEnum.NO_CHANGE.getName(), ipdOXOOutput.getChangeType())&& !StringUtils.equals(ComapreChangeTypeEnum.DEL.getName(), ipdOXOOutput.getChangeType())) {
//                    packageOption.setColor("#f0e68c");  //黄色
//                }
//                options.add(packageOption);
//
//            } else {
//                OxoTemplateRequest.PackageOption packageOption = new OxoTemplateRequest.PackageOption();
//                //packageOption.setColor("#fffaf0");  //白色
//                packageOption.setPackSize(titleSize);
//                options.add(packageOption);
//            }
//        });
//
//        return options;
//
//
//    }
}
