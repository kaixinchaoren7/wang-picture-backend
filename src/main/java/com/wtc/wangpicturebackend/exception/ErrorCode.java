package com.wtc.wangpicturebackend.exception;

import lombok.Getter;

/**
 * 错误异常码类
 */
@Getter
public enum ErrorCode {

    SUCCESS(0,"ok"),
    NOT_LOGIN_ERROR(40100,"用户未登录"),
    NOT_AUTH_ERROR(40101,"无权限"),
    PARAMS_ERROR(40000,"请求参数错误"),
    FORBIDDEN_ERROR(40003,"禁止访问"),
    NOT_FOUND_ERROR(40400,"请求资源不存在"),
    SYSTEM_ERROR(50000,"服务器内部出现问题"),
    OPERATION_ERROR(50001,"操作失败");


    //错误码
    public final int code;

    //错误消息
    public final String message;

    ErrorCode(int code,String message) {
        this.code = code;
        this.message=message;
    }
}
