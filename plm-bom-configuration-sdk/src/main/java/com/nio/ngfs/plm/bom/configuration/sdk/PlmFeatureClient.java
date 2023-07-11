package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.EditGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ListFeatureLibraryQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.AddGroupRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.EditGroupRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.FeatureLibraryDto;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
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
     * Feature库列表
     *
     * @param qry 查询
     * @return 响应
     */
    @PostMapping("/feature/listFeatureLibrary")
    ResultInfo<List<FeatureLibraryDto>> listFeatureLibrary(ListFeatureLibraryQry qry);

}
