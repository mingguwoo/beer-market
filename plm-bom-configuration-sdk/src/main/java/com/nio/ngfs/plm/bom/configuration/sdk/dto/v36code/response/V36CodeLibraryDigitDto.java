package com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response;

import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/18
 */
@Data
public class V36CodeLibraryDigitDto {

    private String code;

    private String displayName;

    private String chineseName;

    private String salesFeatureList;

    private String remark;

    private String createUser;

    private String createTime;

    private String updateUser;

    private String updateTime;

    private List<V36CodeLibraryOptionDto> optionList;
}
