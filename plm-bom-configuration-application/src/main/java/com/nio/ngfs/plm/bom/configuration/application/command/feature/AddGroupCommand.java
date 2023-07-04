package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.Command;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject.AddGroupDO;
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
public class AddGroupCommand implements Command<AddGroupRequest, AddGroupResponse> {

    private final FeatureDomainService featureDomainService;

    @Override
    public AddGroupResponse doAction(AddGroupRequest request) {
        // 1、基本的请求参数校验直接在Request对象上使用Validation注解
        // 2、特殊场景下的参数校验
        // 3、请求参数转换为DO
        // 4、领域服务编排，不处理业务逻辑
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
