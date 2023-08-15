package com.nio.ngfs.plm.bom.configuration.application.query.productcontext.assemble;

import com.nio.bom.share.utils.DateUtils;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductContextEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.response.ProductContextDto;
import org.springframework.beans.BeanUtils;

/**
 * @author bill.wang
 * @date 2023/8/11
 */
public class ProductContextAssembler {

    public static ProductContextDto assemble(BomsProductContextEntity entity){
        ProductContextDto dto = new ProductContextDto();
        BeanUtils.copyProperties(entity,dto);
        dto.setCreateTime(DateUtils.dateTimeStr(entity.getCreateTime()));
        dto.setUpdateTime(DateUtils.dateTimeStr(entity.getUpdateTime()));
        return dto;
    }
}
