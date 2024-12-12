package com.wtc.wangpicturebackend.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class deleteRequest implements Serializable {

    /**
     * 删除数据的id
     */
    private Long id;

    private static final long serialVersionUID = 1L;

}
