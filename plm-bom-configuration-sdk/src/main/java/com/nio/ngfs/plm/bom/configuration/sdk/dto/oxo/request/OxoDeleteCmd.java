package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;


import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Getter
@Setter
@ToString
public class OxoDeleteCmd extends OxoBaseCmd{




     @NotEmpty(message = "必须选择删除项！！！")
     private List<Long> rowIds;




}
