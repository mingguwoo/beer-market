package com.nio.ngfs.plm.bom.configuration.sdk;

import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupRequest;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ListFeatureLibraryRequest;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.AddGroupResponse;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.FeatureLibraryDTO;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
public interface PlmFeatureClient {

    /**
     * 添加Group
     *
     * @param request 请求
     * @return 响应
     */
    ResultInfo<AddGroupResponse> addGroup(AddGroupRequest request);

    /**
     * Feature库列表
     *
     * @param request 请求
     * @return 响应
     */
    ResultInfo<List<FeatureLibraryDTO>> listFeatureLibrary(ListFeatureLibraryRequest request);

}
