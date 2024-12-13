package com.wtc.wangpicturebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wtc.wangpicturebackend.model.dto.user.UserRegisterRequest;
import com.wtc.wangpicturebackend.model.entity.User;

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
}
