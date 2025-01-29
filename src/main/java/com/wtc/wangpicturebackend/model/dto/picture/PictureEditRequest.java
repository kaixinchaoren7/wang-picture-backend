package com.wtc.wangpicturebackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 图片修改请求类（一般给普通用户使用）
 */
@Data
public class PictureEditRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签（JSON 数组）
     */
    private List<String> tags;

    private static final long SerialVersionUID = 1L;
}
