package com.nio.ngfs.plm.bom.configuration.persistence.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ngfs codegen
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class BasePO {

    private Date createTime;
    private Date updateTime;
    private String createUser;
    private String updateUser;
    private Boolean deleted;
}
