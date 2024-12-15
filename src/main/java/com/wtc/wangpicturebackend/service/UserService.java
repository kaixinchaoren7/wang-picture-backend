package com.wtc.wangpicturebackend.service;

import cn.hutool.http.server.HttpServerRequest;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wtc.wangpicturebackend.model.dto.user.UserQueryRequest;
import com.wtc.wangpicturebackend.model.dto.user.UserRegisterRequest;
import com.wtc.wangpicturebackend.model.entity.User;
import com.wtc.wangpicturebackend.model.vo.LoginUserVO;
import com.wtc.wangpicturebackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpClient;
import java.util.List;

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
     * 获取加密后的密码
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    public String getEncryptPassword(String userPassword);
    /**
     * 用户登陆
     * @param userAccount
     * @param userPassword
     * @return
     */
    LoginUserVO login(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取LoginUserVO
     */
    public LoginUserVO getLoginUserVO(User user);
    /**
     * 获取登陆用户
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取包装类
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    Wrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    List<UserVO> getUserVOList(List<User> records);
}
