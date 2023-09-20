package com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;

/**
 * @author bill.wang
 * @date 2023/9/20
 */
public class V36CodeLibraryChangeLogAggr extends AbstractDo implements AggrRoot<Long> {

    private Long codeId;

    private String changeAttribute;

    private String oldValue;

    private String newValue;

    private String type;

    @Override
    public Long getUniqId() {
        return getId();
    }
}
