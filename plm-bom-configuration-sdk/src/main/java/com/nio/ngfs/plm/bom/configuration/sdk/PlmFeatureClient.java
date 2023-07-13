package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ChangeGroupStatusCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.EditGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ListFeatureLibraryQry;
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
    ResultInfo<EditFeatureCmdRespDto> editFeature(EditFeatureCmd cmd);

    /**
     * Feature库列表
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/feature/listFeatureLibrary")
    ResultInfo<List<FeatureLibraryDto>> listFeatureLibrary(ListFeatureLibraryQry qry);

    @PostMapping("/feature/addOption")
    ResultInfo<AddOptionRespDto> addOption(AddOptionCmd cmd);

}
