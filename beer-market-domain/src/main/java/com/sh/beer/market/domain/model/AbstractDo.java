package com.sh.beer.market.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 抽象领域对象（Domain Object）
 *
 * @author
 * @date 2023/7/3
 */
@Data
@SuperBuilder
@AllArgsConstructor
public abstract class AbstractDo {

    /**
     * 主键ID
     *//*
    protected Long id;

    *//**
     * 创建人
     *//*
    protected String createUser;

    *//**
     * 更新人
     *//*
    protected String updateUser;

    *//**
     * 创建时间
     *//*
    protected Date createTime;

    *//**
     * 更新时间
     *//*
    protected Date updateTime;

    *//**
     * 删除标记
     *//*
    protected Integer delFlag;

    *//**
     * 是否已删除
     *
     * @return true|false
     *//*
    public boolean isDeleted() {
        return Objects.equals(delFlag, CommonConstants.DEL_FLAG);
    }

    *//**
     * 是否未删除
     *
     * @return true|false
     *//*
    public boolean isNotDeleted() {
        return !isDeleted();
    }*/

}
