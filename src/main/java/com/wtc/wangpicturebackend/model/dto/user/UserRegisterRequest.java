package com.wtc.wangpicturebackend.model.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    /**
     * 账号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String passWord;
    /**
     * 确认密码
     */
    private String checkPassword;

}
