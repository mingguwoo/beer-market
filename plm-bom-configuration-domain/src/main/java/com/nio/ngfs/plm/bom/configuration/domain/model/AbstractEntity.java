package com.nio.ngfs.plm.bom.configuration.domain.model;

import com.nio.ngfs.plm.bom.configuration.common.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.Objects;

/**
 * 抽象实体
 *
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity {

    /**
     * 删除标记
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否已删除
     *
     * @return true|false
     */
    public boolean isDeleted() {
        return Objects.equals(delFlag, Constants.DEL_FLAG);
    }

}
