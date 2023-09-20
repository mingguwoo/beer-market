package com.nio.ngfs.plm.bom.configuration.domain.model.v36codelibrarychangelog;

/**
 * @author bill.wang
 * @date 2023/9/20
 */
public class V36CodeLibraryChangeLogFactory {

    public static V36CodeLibraryChangeLogAggr create(Long id, String changeAttribute, String oldValue, String newValue,
                                                     String updateUser) {
        V36CodeLibraryChangeLogAggr aggr = new V36CodeLibraryChangeLogAggr();
        aggr.setCodeId(id);
        aggr.setChangeAttribute(changeAttribute);
        aggr.setOldValue(oldValue);
        aggr.setNewValue(newValue);
        aggr.setCreateUser(updateUser);
        aggr.setUpdateUser(updateUser);
        return aggr;
    }

}
