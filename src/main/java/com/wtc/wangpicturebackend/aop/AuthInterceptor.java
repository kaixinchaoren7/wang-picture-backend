package com.wtc.wangpicturebackend.aop;

import com.wtc.wangpicturebackend.annotation.AuthCheck;
import com.wtc.wangpicturebackend.enums.UserRoleEnum;
import com.wtc.wangpicturebackend.exception.BusinessException;
import com.wtc.wangpicturebackend.exception.ErrorCode;
import com.wtc.wangpicturebackend.model.entity.User;
import com.wtc.wangpicturebackend.service.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthInterceptor {
    @Resource
    private UserService userService;
    /**
     * 执行拦截
     *
     * @param joinPoint 切入点
     * @param authCheck 权限校验注解
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        //获取当前请求的相关属性信息对象（RequestAttributes），这个对象包含了与当前 HTTP 请求相关的各种上下文信息
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        //再调用其 getRequest() 方法获取到 HttpServletRequest 对象，
        // 这个对象是处理 HTTP 请求的核心对象，可用于获取请求中的各种参数、头信息等内容
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        // 如果这个方法不需要权限，放行
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }
        // 以下的代码：方法需要权限，用户必须有权限，才会通过
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR);
        }
        // 要求必须有管理员权限，但用户没有管理员权限，拒绝
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR);
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}
