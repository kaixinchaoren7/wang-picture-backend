package com.wtc.wangpicturebackend.controller;

import com.wtc.wangpicturebackend.annotation.AuthCheck;
import com.wtc.wangpicturebackend.common.BaseResponse;
import com.wtc.wangpicturebackend.common.ResultUtils;
import com.wtc.wangpicturebackend.constant.UserConstant;
import com.wtc.wangpicturebackend.model.dto.picture.PictureUploadRequest;
import com.wtc.wangpicturebackend.model.entity.User;
import com.wtc.wangpicturebackend.model.vo.PictureVO;
import com.wtc.wangpicturebackend.service.PictureService;
import com.wtc.wangpicturebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private UserService userService;

    @Autowired
    private PictureService pictureService;
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
}
