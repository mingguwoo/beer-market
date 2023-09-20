package com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog;

/**
 * @author bill.wang
 * @date 2023/9/20
 */
public class V36CodeLibraryChangeLogFactory {

    public static V36CodeLibraryChangeLogAggr create(Long id, String changeAttribute, String oldValue, String newValue) {
        V36CodeLibraryChangeLogAggr aggr = new V36CodeLibraryChangeLogAggr();
        aggr.setCodeId(id);
        aggr.setChangeAttribute(changeAttribute);
        aggr.setOldValue(oldValue);
        aggr.setNewValue(newValue);
        return aggr;
    }

}
