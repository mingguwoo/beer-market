package com.nio.ngfs.plm.bom.configuration.domain.model.v36code;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class V36CodeLibraryAggr extends AbstractDo implements AggrRoot<Long> {

    /**
     * Code
     */
    private String code;

    /**
     * Parent Code Id
     */
    private Long parentId;

    /**
     * 类型，Digit/Option
     */
    private String type;

    /**
     * 英文描述
     */
    private String displayName;

    /**
     * 中文描述
     */
    private String chineseName;

    /**
     * Sales Feature列表，逗号分隔
     */
    private String salesFeatureList;

    /**
     * 备注说明
     */
    private String remark;

    @Override
    public Long getUniqId() {
        return id;
    }

}
