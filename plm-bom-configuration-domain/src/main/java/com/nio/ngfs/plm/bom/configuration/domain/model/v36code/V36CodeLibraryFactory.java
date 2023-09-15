package com.nio.ngfs.plm.bom.configuration.domain.model.v36code;

import com.google.common.base.Joiner;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.AddDigitCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request.AddOptionCmd;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
public class V36CodeLibraryFactory {

    public static V36CodeLibraryAggr createDigit(AddDigitCmd cmd) {
        return V36CodeLibraryAggr.builder()
                .code(cmd.getCode().trim())
                .displayName(cmd.getDisplayName().trim())
                .chineseName(cmd.getChineseName().trim())
                .salesFeatureList(Joiner.on(",").join(cmd.getSalesFeatureCodeList()))
                .remark(cmd.getRemark())
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .build();
    }

    public static V36CodeLibraryAggr createOption(AddOptionCmd cmd, V36CodeLibraryAggr parentAggr) {
        return V36CodeLibraryAggr.builder()
                .code(cmd.getCode().trim().toUpperCase())
                .parentId(cmd.getParentId())
                .displayName(cmd.getDisplayName().trim())
                .chineseName(cmd.getChineseName().trim())
                .remark(cmd.getRemark())
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .parent(parentAggr)
                .build();
    }

}
