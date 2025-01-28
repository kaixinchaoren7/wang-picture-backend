package com.wtc.wangpicturebackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

@Data
public class PictureUploadRequest implements Serializable {

    /**
     * 图片id（用于修改）
     */
    private Long id;

    private static final long SERIAL_VERSION_UID = 1L;
}
