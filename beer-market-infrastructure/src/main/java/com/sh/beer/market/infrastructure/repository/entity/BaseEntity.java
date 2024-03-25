package com.sh.beer.market.infrastructure.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author
 * @date 02/17/2023
 */
@Data
@SuperBuilder
@AllArgsConstructor
public class BaseEntity implements Serializable {

    /*@Serial
    private static final long serialVersionUID = 5862727978698906376L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    *//**
     * 删除状态
     *//*
    @TableLogic(value = "0", delval = "1")
    private Integer delFlag;

    *//**
     * 创建时间
     *//*
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    *//**
     * 更新时间
     *//*
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    *//**
     * 创建人
     *//*
    private String createUser;

    *//**
     * 更新人
     *//*
    private String updateUser;*/

}
