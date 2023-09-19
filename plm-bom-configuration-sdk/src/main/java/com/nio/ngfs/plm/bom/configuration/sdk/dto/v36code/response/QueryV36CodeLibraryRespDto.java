package com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/18
 */
@Data
@AllArgsConstructor
public class QueryV36CodeLibraryRespDto implements Dto {

    private List<V36CodeLibraryDigitDto> digitList;
}
