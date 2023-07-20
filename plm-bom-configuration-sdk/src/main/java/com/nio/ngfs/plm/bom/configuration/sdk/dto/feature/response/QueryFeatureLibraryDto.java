package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Data
public class QueryFeatureLibraryDto implements Dto {

    private String featureCode;

    private String type;

    private String displayName;

    private String chineseName;

    private String description;

    private String group;

    private String catalog;

    private String requestor;

    private String status;

    private String createUser;

    private String updateUser;

    private String createTime;

    private String updateTime;

    private List<QueryFeatureLibraryDto> children;

}
