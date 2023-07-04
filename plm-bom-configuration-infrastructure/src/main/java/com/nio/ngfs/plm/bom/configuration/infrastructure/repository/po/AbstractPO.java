package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author luke.zhu
 * @date 02/17/2023
 */
@Data
public class AbstractPO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5862727978698906376L;

    @Schema(description = "id", required = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 删除状态
     */
    @Schema(description = "是否删除", required = true)
    @TableLogic(value = "0", delval = "1")
    private Integer delFlag;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createUser;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private String updateUser;

}
