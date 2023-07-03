package com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject;

import lombok.Builder;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Data
@Builder
public class AddGroupDO {

    private String featureCode;

    private String displayName;

    private String chineseName;

    private String description;

    private String createUser;

}
