package com.wtc.wangpicturebackend.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wtc.wangpicturebackend.annotation.AuthCheck;
import com.wtc.wangpicturebackend.common.BaseResponse;
import com.wtc.wangpicturebackend.common.DeleteRequest;
import com.wtc.wangpicturebackend.common.ResultUtils;
import com.wtc.wangpicturebackend.constant.UserConstant;
import com.wtc.wangpicturebackend.enums.UserRoleEnum;
import com.wtc.wangpicturebackend.exception.BusinessException;
import com.wtc.wangpicturebackend.exception.ErrorCode;
import com.wtc.wangpicturebackend.exception.ThrowUtils;
import com.wtc.wangpicturebackend.manager.FileManager;
import com.wtc.wangpicturebackend.model.dto.picture.PictureQueryRequest;
import com.wtc.wangpicturebackend.model.dto.picture.PictureUpdateRequest;
import com.wtc.wangpicturebackend.model.dto.picture.PictureUploadRequest;
import com.wtc.wangpicturebackend.model.entity.Picture;
import com.wtc.wangpicturebackend.model.entity.User;
import com.wtc.wangpicturebackend.model.vo.PictureVO;
import com.wtc.wangpicturebackend.service.PictureService;
import com.wtc.wangpicturebackend.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private UserService userService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private FileManager fileManager;
    /**
     * 上传图片（可重新上传）
     * @param multipartFile
     * @param pictureUploadRequest
     * @param request
     * @return
     */
    @PostMapping("/upload")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<PictureVO> uploadPicture(@RequestPart("file") MultipartFile multipartFile,
                                                 PictureUploadRequest pictureUploadRequest,
                                                 HttpServletRequest request) {
        User loginUser=userService.getLoginUser(request);
        PictureVO pictureVO=pictureService.uploadPicture(multipartFile,pictureUploadRequest,loginUser);
        return ResultUtils.success(pictureVO);
    }


    /**
     * 删除图片
     * @param deleteRequest
     * @return
     */
    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deletePicture(@RequestBody DeleteRequest deleteRequest,HttpServletRequest request) {
        ThrowUtils.throwIf(deleteRequest==null || deleteRequest.getId()==null, ErrorCode.PARAMS_ERROR);
        //判断图片是否存在
        Picture picture=pictureService.getById(deleteRequest.getId());
        ThrowUtils.throwIf(picture==null, ErrorCode.PARAMS_ERROR,"图片不存在");
        //只有创建用户 或者 管理员才可以删除图片
        User loginUser = userService.getLoginUser(request);
        if(!deleteRequest.getId().equals(loginUser.getId()) && userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR);
        }
        boolean result=pictureService.removeById(picture);
        ThrowUtils.throwIf(result,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    /**
     * 更新图片
     * @param pictureUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updatePicture(@RequestBody PictureUpdateRequest pictureUpdateRequest){
        ThrowUtils.throwIf(pictureUpdateRequest==null || pictureUpdateRequest.getId()<=0, ErrorCode.PARAMS_ERROR);
        Picture picture=new Picture();
        BeanUtils.copyProperties(pictureUpdateRequest,picture);
        //数据校验
        pictureService.validPicture(picture);
        picture.setTags(JSONUtil.toJsonStr(pictureUpdateRequest.getTags()));
        //判断是否存在
        long id=pictureUpdateRequest.getId();
        Picture oldPicture=pictureService.getById(id);
        ThrowUtils.throwIf(oldPicture==null, ErrorCode.PARAMS_ERROR);
        //更新数据
        boolean result=pictureService.updateById(picture);
        ThrowUtils.throwIf(!result,ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 获取图片 仅管理员
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Picture> getPictureById(@RequestParam("id") long id,HttpServletRequest request){
        User loginUser=userService.getLoginUser(request);
        if(!loginUser.getUserRole().equals(UserRoleEnum.ADMIN)){
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR);
        }
        Picture picture=pictureService.getById(id);
        ThrowUtils.throwIf(picture==null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(picture);
    }

    /**
     * 获取图片封装类
     */
    @GetMapping("/get/vo")
    public BaseResponse<PictureVO> getPictureVOById(@RequestParam("id") long id,HttpServletRequest request){
        ThrowUtils.throwIf(id<=0, ErrorCode.PARAMS_ERROR);
        Picture picture=pictureService.getById(id);
        ThrowUtils.throwIf(picture==null, ErrorCode.PARAMS_ERROR);
        PictureVO pictureVO=pictureService.getPictureVO(picture,request);
        return ResultUtils.success(pictureVO);
    }

    /**
     * 分页获取图片(仅管理员）
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Picture>> listPicturePage(@RequestBody PictureQueryRequest pictureQueryRequest,HttpServletRequest request){
        ThrowUtils.throwIf(pictureQueryRequest==null || pictureQueryRequest.getId()<=0, ErrorCode.PARAMS_ERROR);
        long current = pictureQueryRequest.getCurrentPage();
        long size = pictureQueryRequest.getPageSize();
        QueryWrapper queryWrapper=pictureService.getQueryWrapper(pictureQueryRequest);
        Page<Picture> page=userService.page(new Page<>(current,size),queryWrapper);
        return ResultUtils.success(page);
    }


}
