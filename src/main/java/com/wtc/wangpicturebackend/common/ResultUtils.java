package com.wtc.wangpicturebackend.common;

import com.wtc.wangpicturebackend.exception.ErrorCode;

public class ResultUtils<T> {
    /**
     * 成功
     * @param data
     * @return
     */
    public static<T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0, data,"ok");
    }

    /**
     * 失败
     * @param code
     * @param message
     * @return
     */
    public static BaseResponse error(int code,String message){
        return new BaseResponse<>(code, null, message);
    }
    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * @param errorCode
     * @param message
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String message){
        return new BaseResponse<>(errorCode.getCode(),null,message);
    }
}
