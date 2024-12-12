package com.wtc.wangpicturebackend.common;

import lombok.Data;

/**
 * 分页查询请求类
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int currentPage=1;

    /**
     * 每页页数大小
     */
    private int pageSize=10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 升序/降序
     */
    private String sortOrder="descend";
}
