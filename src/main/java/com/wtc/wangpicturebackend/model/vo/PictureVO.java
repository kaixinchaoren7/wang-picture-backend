package com.wtc.wangpicturebackend.model.vo;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wtc.wangpicturebackend.model.entity.Picture;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PictureVO {

    /**
     * id
     */
    private Long id;

    /**
     * 图片 url
     */
    private String url;

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

    /**
     * 图片体积
     */
    private Long picSize;

    /**
     * 图片宽度
     */
    private Integer picWidth;

    /**
     * 图片高度
     */
    private Integer picHeight;

    /**
     * 图片宽高比例
     */
    private Double picScale;

    /**
     * 图片格式
     */
    private String picFormat;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 封装类转对象
     */
    public static Picture voToObj(PictureVO pictureVO) {
        if(pictureVO != null) {
            Picture picture = new Picture();
            BeanUtil.copyProperties(pictureVO, picture);
            picture.setTags(JSONUtil.toJsonStr(pictureVO.getTags()));
            return picture;
        }
        else return null;
    }

    /**
     * 对象转封装类
     */
    public static PictureVO objToVo(Picture picture) {
        if(picture==null) return null;
        PictureVO pictureVO = new PictureVO();
        BeanUtil.copyProperties(picture, pictureVO);
        pictureVO.setTags(JSONUtil.toList(picture.getTags(), String.class));
        return pictureVO;
    }
}
