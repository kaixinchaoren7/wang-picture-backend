package com.wtc.wangpicturebackend.controller;

import cn.hutool.http.server.HttpServerResponse;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.wtc.wangpicturebackend.annotation.AuthCheck;
import com.wtc.wangpicturebackend.common.BaseResponse;
import com.wtc.wangpicturebackend.common.ResultUtils;
import com.wtc.wangpicturebackend.constant.UserConstant;
import com.wtc.wangpicturebackend.exception.BusinessException;
import com.wtc.wangpicturebackend.exception.ErrorCode;
import com.wtc.wangpicturebackend.manager.CosManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private CosManager cosManager;

    /**
     * 测试文件上传
     * @param multipartFile
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/test/upload")
    public BaseResponse<String> testUpload(@RequestPart("file") MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String filepath=String.format("/test/%s", fileName);
        File file=null;
        try {
            //上传文件
            //1:创建一个临时文件，文件名由 filepath 确定，第二个参数为文件后缀，这里设置为 null
            file=File.createTempFile(filepath,null);
            //将上传文件内容转移到临时文件
            multipartFile.transferTo(file);
            //将文件进行上传
            cosManager.putObject(filepath,file);
            //返回可访问地址
            return ResultUtils.success(filepath);
        } catch (IOException e) {
            log.error("file upload error,filepath= ", filepath,e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"上传失败");
        }finally {
            if(file!=null){
                //删除临时文件
                boolean delete=file.delete();
                if(!delete){
                    log.error("file delete error,filepath= ", filepath);

                }
            }
        }
    }

    /**
     * 测试文件下载
     * @param filepath
     * @param response
     */
    //思路：核心流程是根据路径获取到 COS 文件对象，
    // 然后将文件对象转换为文件流，并写入到 Servlet 的 Response对象中。
    // 注意要设置文件下载专属的响应头
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/test/download")
    public void testDownloadFile(String filepath, HttpServletResponse response) throws IOException {
        COSObjectInputStream cosObjectInputStream = null;
        COSObject cosObject=cosManager.getObject(filepath);
        cosObjectInputStream=cosObject.getObjectContent();
        try {
            byte[] byteArray = IOUtils.toByteArray(cosObjectInputStream);
            //设置响应头
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition","attachment;filename="+filepath);
            //写入响应
            response.getOutputStream().write(byteArray);
            response.getOutputStream().flush();
        } catch (IOException e) {
            log.error("download file error,filepath= ", filepath,e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"下载失败");
        }finally {
            if(cosObject!=null){
                cosObjectInputStream.close();
            }
        }
    }

}
