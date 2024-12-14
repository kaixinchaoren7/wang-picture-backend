package com.wtc.wangpicturebackend.controller;

import com.wtc.wangpicturebackend.common.BaseResponse;
import com.wtc.wangpicturebackend.common.ResultUtils;
import com.wtc.wangpicturebackend.exception.ErrorCode;
import com.wtc.wangpicturebackend.exception.ThrowUtils;
import com.wtc.wangpicturebackend.model.dto.user.UserLoginRequest;
import com.wtc.wangpicturebackend.model.dto.user.UserRegisterRequest;
import com.wtc.wangpicturebackend.model.entity.User;
import com.wtc.wangpicturebackend.model.vo.LoginUserVO;
import com.wtc.wangpicturebackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@RequestMapping("/user")
public class UserController {

    @Resource
    private  UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest==null, ErrorCode.PARAMS_ERROR);
        String userAccount=userRegisterRequest.getUserAccount();
        String userPassword=userRegisterRequest.getPassWord();
        String checkPassword=userRegisterRequest.getCheckPassword();
        long result=userService.userRegister(userAccount,userPassword,checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登陆
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest==null, ErrorCode.PARAMS_ERROR);
        String userAccount=userLoginRequest.getUserAccount();
        String userPassword=userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO=userService.login(userAccount,userPassword,request);
        return ResultUtils.success(loginUserVO);
    }
    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
       ThrowUtils.throwIf(request==null, ErrorCode.PARAMS_ERROR);
       boolean result = userService.userLogout(request);
       return ResultUtils.success(result);
    }

    /**
     * 获取登陆用户
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getUser(HttpServletRequest request) {
        ThrowUtils.throwIf(request==null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }
}
