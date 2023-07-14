package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;

import lombok.Data;

/**
 * @author wangchao.wang
 */
@Data
public class OxoListCmd extends OxoBaseCmd {

    /**
     * 类型 为空查询所有 包括Marketing，Engineering，isShowDelete
     */
      private String type;
}
