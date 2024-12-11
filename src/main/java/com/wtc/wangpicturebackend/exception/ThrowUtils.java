package com.wtc.wangpicturebackend.exception;


/**
 * 抛出异常工具类
 */
public class ThrowUtils {

    /**
     * 条件满足则抛异常
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(Boolean condition, RuntimeException runtimeException) {
        if(condition){
            throw runtimeException;
        }
    }

    /**
     * 条件满足抛异常
     * @param condition
     * @param errorCode
     */
    public static void throwIf(Boolean condition,ErrorCode errorCode) {
//        if(condition){
//            throw new RuntimeException(errorCode.getMessage());
//        }
        throwIf(condition,new BusinessException(errorCode));
    }

    /**
     * 条件满足抛异常
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(Boolean condition,ErrorCode errorCode,String message) {
        throwIf(condition,new BusinessException(errorCode,message));
    }
}
