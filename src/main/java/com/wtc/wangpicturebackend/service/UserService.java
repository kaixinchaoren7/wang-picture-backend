package com.wtc.wangpicturebackend.service;

import cn.hutool.http.server.HttpServerRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wtc.wangpicturebackend.model.dto.user.UserRegisterRequest;
import com.wtc.wangpicturebackend.model.entity.User;
import com.wtc.wangpicturebackend.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpClient;

public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登陆
     * @param userAccount
     * @param userPassword
     * @return
     */
    LoginUserVO login(String userAccount, String userPassword, HttpServletRequest request);
}
