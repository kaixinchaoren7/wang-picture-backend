package com.wtc.wangpicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wtc.wangpicturebackend.common.DeleteRequest;
import com.wtc.wangpicturebackend.model.dto.picture.PictureQueryRequest;
import com.wtc.wangpicturebackend.model.dto.picture.PictureReviewRequest;
import com.wtc.wangpicturebackend.model.dto.picture.PictureUploadRequest;
import com.wtc.wangpicturebackend.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wtc.wangpicturebackend.model.entity.User;
import com.wtc.wangpicturebackend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author kaixinchaoren
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-01-27 22:09:26
*/
public interface PictureService extends IService<Picture> {

    /**
     * 上传图片
     * @param multipartFile
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser);


    /**
     * 获取查询QueryWrapper
     * @param pictureQueryRequest
     * @return
     */
    public QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    /**
     * 获取单个图片封装
     * @param picture
     * @param request
     * @return
     */
    public PictureVO getPictureVO(Picture picture,HttpServletRequest request);

    /**
     * 分页获取多个图片封装
     * @param picturePage
     * @param request
     * @return
     */
    public Page<PictureVO> getPictureVOPage(Page<Picture> picturePage,HttpServletRequest request);

    /**
     * 图片数据校验
     * @param picture
     */
    public void validPicture(Picture picture);

    /**
     * 图片审核
     * @param pictureReviewRequest
     * @param loginUser
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest,User loginUser);

    /**
     * 补充审核参数 通用 （图片上传，用户编辑，管理员更新都需要设置审核状态）
     * 根据用户的角色给图片对象填充审核字段的值
     * @param picture
     * @param loginUser
     */
    public void fillReviewParams(Picture picture,User loginUser);

}
