package com.wtc.wangpicturebackend.model.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 8735650154179439661L;
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
