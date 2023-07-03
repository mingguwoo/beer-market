package com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject;

import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Data
public class EditGroupDO {

    private Long id;

    private String featureCode;

    private String displayName;

    private String chineseName;

    private String description;

    private String status;

    private String updateUser;

}
