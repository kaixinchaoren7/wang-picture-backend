package com.wtc.wangpicturebackend.exception;


import com.wtc.wangpicturebackend.common.BaseResponse;
import com.wtc.wangpicturebackend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e){
        return ResultUtils.error(e.getCode(),e.getMessage());
    }
}
