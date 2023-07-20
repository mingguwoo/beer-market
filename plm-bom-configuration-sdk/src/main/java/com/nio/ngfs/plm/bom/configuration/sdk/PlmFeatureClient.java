package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@FeignClient(name = "plm-bom-configuration")
public interface PlmFeatureClient {

    /**
     * 新增Group
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/feature/addGroup")
    ResultInfo<AddGroupRespDto> addGroup(AddGroupCmd cmd);

    /**
     * 编辑Group
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/feature/editGroup")
    ResultInfo<EditGroupRespDto> editGroup(EditGroupCmd cmd);

    /**
     * 改变Group状态
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/feature/changeGroupStatus")
    ResultInfo<ChangeGroupStatusRespDto> changeGroupStatus(ChangeGroupStatusCmd cmd);

    /**
     * 新增Feature
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/feature/addFeature")
    ResultInfo<AddFeatureRespDto> addFeature(AddFeatureCmd cmd);

    /**
     * 编辑Feature
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/feature/editFeature")
    ResultInfo<EditFeatureCmdRespDto> editFeature(EditFeatureCmd cmd);

    /**
     * 改变Feature状态
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/feature/changeFeatureStatus")
    ResultInfo<ChangeFeatureStatusRespDto> changeFeatureStatus(ChangeFeatureStatusCmd cmd);

    /**
     * 新增Option
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/feature/addOption")
    ResultInfo<AddOptionRespDto> addOption(AddOptionCmd cmd);

    /**
     * 编辑Option
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/feature/editOption")
    ResultInfo<EditOptionRespDto> editOption(EditOptionCmd cmd);

    /**
     * 改变Option状态
     *
     * @param cmd 命令
     * @return 响应
     */
    @PostMapping("/feature/changeOptionStatus")
    ResultInfo<ChangeOptionStatusRespDto> changeOptionStatus(ChangeOptionStatusCmd cmd);

    /**
     * 查询Logs记录
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/feature/getChangeLogList")
    ResultInfo<List<GetChangeLogListDto>> getChangeLogList(GetChangeLogListQry qry);

    /**
     * 查询Group Code列表
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/feature/getGroupCodeList")
    ResultInfo<List<String>> getGroupCodeList(GetGroupCodeListQry qry);

    /**
     * 查询Feature Library列表
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/feature/queryFeatureLibrary")
    ResultInfo<List<QueryFeatureLibraryDto>> queryFeatureLibrary(QueryFeatureLibraryQry qry);

}
