//package com.nio.ngfs.plm.bom.configuration.application.query.oxo;
//
//import com.google.common.collect.Lists;
//import com.nio.ngfs.plm.bom.configuration.application.query.Query;
//import com.nio.ngfs.plm.bom.configuration.application.query.oxo.assemble.OxoInfoAssembler;
//import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
//import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.domainobject.OxoInfoDo;
//import com.nio.ngfs.plm.bom.configuration.domain.service.BaseVehicleDomainService;
//import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoOptionPackageInfoDao;
//import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoRowInfoDao;
//import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageInfoEntity;
//import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoBaseCmd;
//import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
//import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
//import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//
///**
// * @author wangchao.wang
// */
//@Component
//@RequiredArgsConstructor
//public class OxoInfoQuery implements Query<OxoBaseCmd, OxoListQry> {
//
////
//    private final BomsOxoRowInfoDao bomsOxoRowInfoDao;
//
//
//    private final BomsOxoOptionPackageInfoDao bomsOxoOptionPackageInfoDao;
//
//
//    private final BaseVehicleDomainService baseVehicleDomainService;
//
//
//    @Override
//    public OxoListQry execute(OxoBaseCmd oxoListQry) {
//
//        String modelCode = oxoListQry.getModelCode();
//
//
//        // 查询行数据
//        List<OxoInfoDo> oxoInfoDos = bomsOxoRowInfoDao.queryFeatureListsByModel(modelCode);
//
//        //获取所有的行节点
//        List<Long> rowIds = oxoInfoDos.stream().map(OxoInfoDo::getId).distinct().toList();
//
//
//        // 获取打点信息
//        List<BomsOxoOptionPackageInfoEntity> entities =
//                bomsOxoOptionPackageInfoDao.queryOxoOptionPackageByHeadIds(rowIds);
//
//
//        //查询表头信息
//        List<OxoHeadQry> oxoLists = baseVehicleDomainService.queryByModel(modelCode);
//
//
//        /**
//         * 系统默认排序
//         * 1.Feature的Catalog属性（Engineering在前，Sales在后）
//         * 2.Group排序（按首数字正排，00在前，依次往后排）
//         * 3.Feature排序（按首字母正排）+Renew Sort按钮调整的Feature排序
//         * 4.Feature下的Option排序（按首字母正排）+Renew Sort按钮调整的Option排序
//         *
//         * 自定义：
//         * 基于某个Catalog（Engineering或Sales）,在同一Group下，可对Feature进行重新排序
//         * 可对某个Feature下的Option进行重新排序
//         */
//        Map<String, List<OxoInfoDo>> oxoInfoDoMaps =
//                oxoInfoDos.stream().filter(x -> StringUtils.equals(x.getType(), FeatureTypeEnum.FEATURE.getType()))
//                        .sorted(Comparator.comparing(OxoInfoDo::getCatalog).thenComparing(OxoInfoDo::getParentFeatureCode))
//                        .collect(Collectors.groupingBy(OxoInfoDo::getParentFeatureCode));
//
//
//        OxoListQry qry=new OxoListQry();
//        qry.setOxoHeadResps(oxoLists);
//
//
//        List<OxoRowsQry> rowsQries=Lists.newArrayList();
//        oxoInfoDoMaps.forEach((k, features) -> {
//            List<OxoInfoDo> oxoInfoDoList = features.stream().sorted().sorted(Comparator.comparing(OxoInfoDo::getSort)
//                    .thenComparing(OxoInfoDo::getFeatureCode)).toList();
//
//            oxoInfoDoList.forEach(x -> {
//                //获取
//                OxoRowsQry oxoRowsQry = OxoInfoAssembler.assembleOxoQry(x, k);
//
//                List<OxoInfoDo> options = oxoInfoDos.stream().filter(y ->
//                                StringUtils.equals(y.getParentFeatureCode(), x.getFeatureCode()) &&
//                                        StringUtils.equals(y.getType(), FeatureTypeEnum.OPTION.getType())).
//                        sorted(Comparator.comparing(OxoInfoDo::getSort).thenComparing(OxoInfoDo::getFeatureCode)).toList();
//
//                List<OxoRowsQry> optionQrys = Lists.newArrayList();
//
//
//                if (CollectionUtils.isNotEmpty(options)) {
//                    options.forEach(option -> {
//                        OxoRowsQry optionQry = OxoInfoAssembler.assembleOxoQry(option, k);
//
//                        if (CollectionUtils.isNotEmpty(entities)) {
//                            optionQry.setPackInfos(OxoInfoAssembler.buildOxoEditCmd(entities,option,oxoLists));
//                        }
//                        optionQrys.add(optionQry);
//                    });
//                    oxoRowsQry.setOptions(optionQrys);
//                }
//                rowsQries.add(oxoRowsQry);
//            });
//        });
//
//        qry.setOxoRowsResps(rowsQries);
//        return qry;
//    }
//}
