package com.nio.ngfs.plm.bom.configuration.domain.model.productconfig;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.enums.YesOrNoEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.util.RegexUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.EditPcCmd;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfigAggr extends AbstractDo implements AggrRoot<String> {

    /**
     * 单车PC ID
     */
    private String pcId;

    /**
     * Model
     */
    private String modelCode;

    /**
     * Model Year
     */
    private String modelYear;

    /**
     * 单车PC Name
     */
    private String name;

    /**
     * 单车PC Marketing Name
     */
    private String marketingName;

    /**
     * 单车PC描述
     */
    private String description;

    /**
     * Based On Base Vehicle主键ID
     */
    private Long basedOnBaseVehicleId;

    /**
     * Based On Base Vehicle所在的OXO发布版本id
     */
    private Long oxoVersionSnapshotId;

    /**
     * Based On Base Vehicle是否完成初始化勾选，0：否，1：是
     */
    private Integer completeInitSelect;

    /**
     * Based On PC ID
     */
    private String basedOnPcId;

    /**
     * Model的品牌
     */
    private String brand;

    /**
     * Skip Check开关，取值Open、Close
     */
    private String skipCheck;

    @Override
    public String getUniqId() {
        return pcId;
    }

    /**
     * 新增PC
     */
    public void add() {
        checkName(name);
        // Based On Base Vehicle和Based On PC只能二选一
        if (basedOnBaseVehicleId != null && StringUtils.isNotBlank(basedOnPcId)) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_BASED_ON_ONLY_SELECT_ONE);
        }
        // 选择了Based On Base Vehicle，oxoVersionSnapshotId不可为空
        if (basedOnBaseVehicleId != null && oxoVersionSnapshotId == null) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_OXO_VERSION_SNAPSHOT_ID_IS_NULL);
        }
        // 设置completeInitSelect
        if (basedOnBaseVehicleId != null) {
            // Copy From Base Vehicle，默认未完成初始化勾选
            setCompleteInitSelect(YesOrNoEnum.NO.getCode());
        } else {
            // 其它情况，默认完成初始化勾选
            setCompleteInitSelect(YesOrNoEnum.YES.getCode());
        }
        // 设置Skip Check开关，默认关闭
        setSkipCheck(CommonConstants.CLOSE);
    }

    /**
     * 编辑PC
     */
    public void edit(EditPcCmd cmd) {
        checkName(cmd.getName());
        setName(cmd.getName());
        setUpdateUser(cmd.getUpdateUser());
    }

    /**
     * 删除PC
     */
    public void delete(String updateUser) {
        setDelFlag(CommonConstants.DEL_FLAG);
        setUpdateUser(updateUser);
    }

    /**
     * 改变Skip Check
     */
    public void changeSkipCheck(boolean skipCheck, String updateUser) {
        setSkipCheck(skipCheck ? CommonConstants.OPEN : CommonConstants.CLOSE);
        setUpdateUser(updateUser);
    }

    /**
     * 校验Name
     */
    private void checkName(String name) {
        if (name.contains(StringUtils.SPACE)) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_NAME_HAS_SPACE);
        }
        if (!RegexUtil.isMatchPcName(name)) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_NAME_FORMAT_ERROR);
        }
    }

}
