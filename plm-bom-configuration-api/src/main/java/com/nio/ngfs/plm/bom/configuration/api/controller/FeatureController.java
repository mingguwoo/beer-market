package com.nio.ngfs.plm.bom.configuration.api.controller;

import com.nio.ngfs.common.model.BaseResponse;
import com.nio.ngfs.plm.bom.configuration.application.app.feature.AddGroupApp;
import com.nio.ngfs.plm.bom.configuration.application.app.feature.ListFeatureLibraryApp;
import com.nio.ngfs.plm.bom.configuration.sdk.PlmFeatureClient;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupRequest;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ListFeatureLibraryRequest;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.AddGroupResponse;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.FeatureLibraryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@RestController
@Slf4j
@RequestMapping("/feature")
@RequiredArgsConstructor
public class FeatureController implements PlmFeatureClient {

    private final AddGroupApp addGroupApp;
    private final ListFeatureLibraryApp listFeatureLibraryApp;

    @PostMapping("/addGroup")
    @Override
    public BaseResponse<AddGroupResponse> addGroup(AddGroupRequest request) {
        return BaseResponse.success(addGroupApp.doAction(request));
    }

    @PostMapping("/listFeatureLibrary")
    @Override
    public BaseResponse<List<FeatureLibraryDTO>> listFeatureLibrary(ListFeatureLibraryRequest request) {
        return BaseResponse.success(listFeatureLibraryApp.doAction(request));
    }

}
