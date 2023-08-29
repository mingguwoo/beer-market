package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.comparator;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextFeatureRowDto;

import java.util.Comparator;
import java.util.Map;

/**
 * @author bill.wang
 * @date 2023/8/29
 */
public class ProductContextFeatureRowComparator implements Comparator<ProductContextFeatureRowDto> {

    Map<String,String> groupRecordMap;

    public ProductContextFeatureRowComparator(Map<String,String> groupRecordMap){
        this.groupRecordMap = groupRecordMap;
    }

    @Override
    public int compare(ProductContextFeatureRowDto o1, ProductContextFeatureRowDto o2) {
        //先比较catalog
        if (o1.getCatalog().equals(o2.getCatalog())){
            //再比较group
            if (groupRecordMap.get(o1.getFeatureCode()).equals(groupRecordMap.get(o2.getFeatureCode()))){
                return o1.getFeatureCode().compareTo(o2.getFeatureCode());
            }
            else{
                return groupRecordMap.get(o1.getFeatureCode()).compareTo(groupRecordMap.get(o2.getFeatureCode()));
            }
        }
        return (o1.getCatalog().compareTo(o2.getCatalog()));
    }
}
