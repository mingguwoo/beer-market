package com.nio.ngfs.plm.bom.configuration.application.app.feature;

import com.nio.ngfs.plm.bom.configuration.application.app.Application;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.dos.AddGroupDO;
import com.nio.ngfs.plm.bom.configuration.domain.service.FeatureDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.AddGroupRequest;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.AddGroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Component
@RequiredArgsConstructor
public class AddGroupApp implements Application<AddGroupRequest, AddGroupResponse> {

    private final FeatureDomainService featureDomainService;

    @Override
    public AddGroupResponse doAction(AddGroupRequest request) {
        featureDomainService.addGroup(buildAddGroupDO(request));
        return new AddGroupResponse();
    }

    private AddGroupDO buildAddGroupDO(AddGroupRequest request) {
        return AddGroupDO.builder()
                .featureCode(request.getGroupCode())
                .displayName(request.getDisplayName())
                .chineseName(request.getChineseName())
                .description(request.getDescription())
                .build();
    }

}
