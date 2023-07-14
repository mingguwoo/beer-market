package com.nio.ngfs.plm.bom.configuration.sdk.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页参数
 * @param <T>
 */
@Getter
@Setter
@AllArgsConstructor
public class PageData<T> {

	/**当前所在页数*/
    private int currentPage;

    /**每页的条数*/
    private int pageSize;

    /**一共多少条*/
    private long totalItems;
    
    /**查询集合*/
    private List<T> dataList;
}
