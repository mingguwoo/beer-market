package com.nio.ngfs.plm.bom.configuration.domain.model.productconfig;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.util.RegexUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
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
    private String model;

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
     * Base Vehicle所在的OXO打包版本id
     */
    private Long oxoVersionSnapshotId;

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
        checkName();
        if (basedOnBaseVehicleId == null && StringUtils.isNotBlank(basedOnPcId)) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_BASED_ON_ONLY_SELECT_ONE);
        }
        if (basedOnBaseVehicleId == null && oxoVersionSnapshotId == null) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_OXO_VERSION_SNAPSHOT_ID_IS_NULL);
        }
        setSkipCheck(CommonConstants.CLOSE);
    }

    /**
     * 校验Name
     */
    private void checkName() {
        if (name.contains(StringUtils.SPACE)) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_NAME_HAS_SPACE);
        }
        if (!RegexUtil.isMatchPcName(name)) {
            throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_NAME_FORMAT_ERROR);
        }
    }

}
