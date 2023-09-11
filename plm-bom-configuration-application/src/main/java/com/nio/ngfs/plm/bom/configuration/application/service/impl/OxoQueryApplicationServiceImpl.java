package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.query.oxo.assemble.OxoInfoAssembler;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoCompareApplicationService;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoQueryApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.service.basevehicle.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoCompareQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author wangchao.wang
 */
@Service
@RequiredArgsConstructor
public class OxoQueryApplicationServiceImpl implements OxoQueryApplicationService {

    private final BaseVehicleDomainService baseVehicleDomainService;


    private final OxoVersionSnapshotDomainService versionSnapshotDomainService;


    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;


    private final OxoOptionPackageRepository oxoOptionPackageRepository;


    private final OxoCompareApplicationService oxoCompareDomainService;


    /**
     * 对比版本
     *
     * @param oxoCompareQry
     * @return
     */
    @Override
    public OxoListRespDto compareVersion(OxoCompareQry oxoCompareQry) {


        String modelCode = oxoCompareQry.getModelCode();

        String baseVersion = oxoCompareQry.getBaseVersion();

        String compareVersion = oxoCompareQry.getCompareVersion();

        if (StringUtils.equals(baseVersion, compareVersion)) {
            throw new BusinessException(ConfigErrorCode.BASIC_VERSION_ERROR);
        }

        //查询oxo  info 基准数据
        OxoListRespDto baseVersionQry = findQryByVersion(modelCode, baseVersion);

        //查询对比数据
        OxoListRespDto compareVersionQry = findQryByVersion(modelCode, baseVersion);

        return oxoCompareDomainService.compareVersion(baseVersionQry, compareVersionQry, oxoCompareQry.isShowDiff());
    }


    public OxoListRespDto findQryByVersion(String modelCode, String version){
        OxoListRespDto compareVersionQry = queryOxoInfoByModelCode(modelCode, version, false, Lists.newArrayList());

        if (CollectionUtils.isEmpty(compareVersionQry.getOxoHeadResps()) || CollectionUtils.isEmpty(compareVersionQry.getOxoHeadResps())) {
            throw new BusinessException(MessageFormat.format(ConfigErrorCode.VERSION_ERROR.getMessage(), version));
        }

        return compareVersionQry;
    }

    @Override
    public OxoListRespDto queryOxoInfoByModelCode(String modelCode, String version, Boolean isMaturity, List<String> roleNames) {
        OxoListRespDto qry = new OxoListRespDto();

        // 快照版本查询
        if (StringUtils.isNotBlank(version) && !StringUtils.equals(version, ConfigConstants.WORKING)) {

            OxoVersionSnapshotAggr oxoVersionSnapshot =
                    versionSnapshotDomainService.queryOxoInfoByModelAndVersion(modelCode, version);

            if (Objects.nonNull(oxoVersionSnapshot)) {
                //return JSONObject.parseObject(oxoVersionSnapshot.getOxoSnapshot(), OxoListRespDto.class);
                return JSONObject.parseObject(JSONArray.parse(oxoVersionSnapshot.getOxoSnapshot()).toString(), OxoListRespDto.class);

            }

            // 查询working版本
        } else {
            // 查询行数据
            List<OxoFeatureOptionAggr> oxoFeatureOptions = oxoFeatureOptionRepository.queryFeatureListsByModel(modelCode, roleNames);

            //查询表头信息
            List<OxoHeadQry> oxoLists = baseVehicleDomainService.queryByModel(modelCode, isMaturity);

            qry.setOxoHeadResps(oxoLists);

            if (CollectionUtils.isEmpty(oxoFeatureOptions)) {
                return qry;
            }

            //获取所有的行节点
            List<Long> rowIds = oxoFeatureOptions.stream().map(OxoFeatureOptionAggr::getId).distinct().toList();

            // 获取打点信息
            List<OxoOptionPackageAggr> entities =
                    oxoOptionPackageRepository.queryByBaseVehicleIds(rowIds);


            /**
             * 系统默认排序
             * 1.Feature的Catalog属性（Engineering在前，Sales在后）
             * 2.Group排序（按首数字正排，00在前，依次往后排）
             * 3.Feature排序（按首字母正排）+Renew Sort按钮调整的Feature排序
             * 4.Feature下的Option排序（按首字母正排）+Renew Sort按钮调整的Option排序
             *
             * 自定义：
             * 基于某个Catalog（Engineering或Sales）,在同一Group下，可对Feature进行重新排序
             * 可对某个Feature下的Option进行重新排序
             */
            Map<String, List<OxoFeatureOptionAggr>> oxoInfoDoMaps =
                    oxoFeatureOptions.stream().filter(x -> StringUtils.equals(x.getType(), FeatureTypeEnum.FEATURE.getType()))
                            .sorted(Comparator.comparing(OxoFeatureOptionAggr::getCatalog).thenComparing(OxoFeatureOptionAggr::getParentFeatureCode)
                                    .thenComparing(OxoFeatureOptionAggr::getSort).thenComparing(OxoFeatureOptionAggr::getFeatureCode))
                            .collect(Collectors.groupingBy(OxoFeatureOptionAggr::getParentFeatureCode, LinkedHashMap::new, Collectors.toList()));

            qry.setOxoHeadResps(oxoLists);

            List<OxoRowsQry> rowsQryList = Lists.newArrayList();
            oxoInfoDoMaps.forEach((k, features) -> {
                List<OxoFeatureOptionAggr> oxoInfoDoList = features.stream().sorted().sorted(Comparator.comparing(OxoFeatureOptionAggr::getSort)
                        .thenComparing(OxoFeatureOptionAggr::getFeatureCode)).toList();

                oxoInfoDoList.forEach(x -> {
                    //获取
                    OxoRowsQry oxoRowsQry = OxoInfoAssembler.assembleOxoQry(x, k);

                    List<OxoFeatureOptionAggr> options = oxoFeatureOptions.stream().filter(y ->
                                    StringUtils.equals(y.getParentFeatureCode(), x.getFeatureCode()) &&
                                            StringUtils.equals(y.getType(), FeatureTypeEnum.OPTION.getType())).
                            sorted(Comparator.comparing(OxoFeatureOptionAggr::getSort).thenComparing(OxoFeatureOptionAggr::getFeatureCode)).toList();

                    List<OxoRowsQry> optionQrys = Lists.newArrayList();

                    if (CollectionUtils.isNotEmpty(options)) {
                        options.forEach(option -> {
                            OxoRowsQry optionQry = OxoInfoAssembler.assembleOxoQry(option, k);

                            if (CollectionUtils.isNotEmpty(entities)) {
                                optionQry.setPackInfos(OxoInfoAssembler.buildOxoEditCmd(entities, option, oxoLists));
                            }
                            optionQrys.add(optionQry);
                        });
                        oxoRowsQry.setOptions(optionQrys);
                        rowsQryList.add(oxoRowsQry);
                    }
                });

            });

            qry.setOxoRowsResps(rowsQryList.stream().sorted(Comparator.comparing(OxoRowsQry::getCatalog).thenComparing(OxoRowsQry::getGroup)
                    .thenComparing(OxoRowsQry::getSort).thenComparing(OxoRowsQry::getFeatureCode)).collect(Collectors.toList()));
        }
        return qry;
    }
}
