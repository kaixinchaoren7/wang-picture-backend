package com.wtc.wangpicturebackend.controller;

import com.wtc.wangpicturebackend.common.BaseResponse;
import com.wtc.wangpicturebackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class testController {

    @GetMapping("/test")
    public BaseResponse<String> test() {
        return ResultUtils.success("okok");
    }
}
