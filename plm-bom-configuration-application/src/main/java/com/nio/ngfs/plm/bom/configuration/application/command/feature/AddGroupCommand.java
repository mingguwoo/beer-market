package com.nio.ngfs.plm.bom.configuration.application.command.feature;

import com.nio.ngfs.plm.bom.configuration.application.command.Command;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.event.GroupAddEvent;
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
    private final FeatureRepository featureRepository;
    private final EventPublisher eventPublisher;

    @Override
    public AddGroupResponse doAction(AddGroupRequest request) {
        // 1、新增操作，使用工厂创建聚合根
        FeatureAggr featureAggr = FeatureFactory.create(request);
        // 2、调用聚合根自己可以完成的操作
        featureAggr.addGroup();
        // 3、领域服务完成的操作
        featureDomainService.checkGroupCodeUnique(featureAggr);
        // 4、Repository保存
        featureRepository.save(featureAggr);
        // 5、发布领域事件
        eventPublisher.publish(new GroupAddEvent());
        return new AddGroupResponse();
    }

}
