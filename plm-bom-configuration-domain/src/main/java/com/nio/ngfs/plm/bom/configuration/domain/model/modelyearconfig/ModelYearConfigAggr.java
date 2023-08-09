package com.nio.ngfs.plm.bom.configuration.domain.model.modelyearconfig;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author xiaozhou.tu
 * @date 2023/8/9
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ModelYearConfigAggr extends AbstractDo implements AggrRoot<ModelYearConfigId> {

    /**
     * 唯一标识
     */
    private ModelYearConfigId modelYearConfigId;

    /**
     * OXO是否Release，取值Yes、No
     */
    private String oxoRelease;

    @Override
    public ModelYearConfigId getUniqId() {
        return modelYearConfigId;
    }

    public String getModel() {
        return modelYearConfigId.getModel();
    }

    public String getModelYear() {
        return modelYearConfigId.getModelYear();
    }

    /**
     * OXO发布
     */
    public void oxoReleased() {
        setOxoRelease("Yes");
    }

}
