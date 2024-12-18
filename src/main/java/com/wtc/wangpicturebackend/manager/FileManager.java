package com.wtc.wangpicturebackend.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.qcloud.cos.model.ciModel.persistence.OriginalInfo;
import com.wtc.wangpicturebackend.config.CosClientConfig;
import com.wtc.wangpicturebackend.exception.BusinessException;
import com.wtc.wangpicturebackend.exception.ErrorCode;
import com.wtc.wangpicturebackend.exception.ThrowUtils;
import com.wtc.wangpicturebackend.model.dto.file.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Slf4j
/**
 * 1. CosManager 的功能
 * CosManager 类的主要功能是与腾讯云 COS 交互，提供了基本的文件上传和下载功能。它的代码是直接与 COS 的 SDK 对接的。具体功能包括：
 *
 * 上传文件 (putObject)：将文件上传到 COS。
 * 上传图片（带图片处理） (putPictureObject)：在上传文件的同时，对图片进行一些额外的处理（例如，获取图片的基本信息如宽高、格式等）。
 * 下载文件 (getObject)：从 COS 下载文件。
 * 2. FileManager 的功能
 * FileManager 类在上传文件和图片时，提供了更高层次的业务逻辑处理，它的功能不仅仅是与 COS 交互，还包括：
 *
 * 文件校验：确保上传的文件符合要求（大小、类型等）。
 * 上传过程管理：将文件从 MultipartFile 转换为本地文件（通常是临时文件），然后上传到 COS。
 * 图片信息提取：上传图片后，获取图片的宽高、格式等信息，进行处理和封装。
 * 临时文件管理：上传结束后，清理临时文件。
 * 异常处理：处理上传过程中的异常，提供详细的错误信息。
 * 3. 两者的角色和分工
 * CosManager 类属于 底层 代码，它主要负责与 COS 进行交互。它提供了一个简单的接口，直接调用腾讯云的 API 来上传和下载文件。
 * FileManager 类属于 业务层 代码，它提供了更复杂的文件处理逻辑。它会调用 CosManager 来完成文件上传的操作，但在此基础上，它还会进行文件的校验、处理上传路径、生成文件名、获取图片的相关信息等操作。
 * 4. 为何有重复的功能？
 * 表面上看，CosManager 和 FileManager 都有文件上传的功能，可能会让人感觉有重复。但这两者的功能并不是完全重复，而是有不同的职责和层级：
 *
 * CosManager 的职责 是与 COS 存储进行交互，完成上传、下载等基础操作，它并不关心上传的文件是什么类型、是否需要特殊处理。
 * FileManager 的职责 是处理文件上传的业务逻辑，比如文件的校验、文件名的生成、临时文件的管理、获取图片的元信息等，它依赖 CosManager 来完成实际的文件上传。
 * 从设计角度来看，CosManager 是一个工具类，提供与 COS 的交互功能，而 FileManager 是业务层代码，将具体的业务需求（如校验文件类型、生成文件路径、获取图片信息等）与 COS 的操作结合起来。
 *
 * 5. 实际情况的使用
 * CosManager 会被 FileManager 调用。通常在业务逻辑中，FileManager 处理文件的上传过程，包括文件验证、路径构建等，并将处理后的文件通过 CosManager 上传到腾讯云 COS。
 * 举个例子：当你上传图片时，FileManager 会先验证文件类型和大小，然后通过 CosManager 将文件上传到 COS 并返回上传的 URL。CosManager 只是负责与 COS 进行交互，FileManager 则负责更复杂的业务逻辑。
 */
@Service
public class FileManager {
    @Resource
    private CosClientConfig cosClientConfig;
    @Resource
    private CosManager cosManager;
    /**
     * 上传图片
     *
     * @param multipartFile    文件
     * @param uploadPathPrefix 上传路径前缀
     * @return
     */
    public UploadPictureResult uploadPicture(MultipartFile multipartFile, String uploadPathPrefix) {
        // 校验图片
        validPicture(multipartFile);
        // 图片上传地址
        String uuid = RandomUtil.randomString(16);
        String originalFilename = multipartFile.getOriginalFilename();
        // 自己拼接文件上传路径，而不是使用原始文件名称，可以增强安全性
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid,
                FileUtil.getSuffix(originalFilename));
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFilename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(uploadPath, null);
            multipartFile.transferTo(file);
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            // 获取图片信息对象
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            // 计算宽高
            int picWidth = imageInfo.getWidth();
            int picHeight = imageInfo.getHeight();
            double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
            // 封装返回结果
            UploadPictureResult uploadPictureResult = new UploadPictureResult();
            uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);
            uploadPictureResult.setPicName(FileUtil.mainName(originalFilename));
            uploadPictureResult.setPicSize(FileUtil.size(file));
            uploadPictureResult.setPicWidth(picWidth);
            uploadPictureResult.setPicHeight(picHeight);
            uploadPictureResult.setPicScale(picScale);
            uploadPictureResult.setPicFormat(imageInfo.getFormat());
            // 返回可访问的地址
            return uploadPictureResult;
        } catch (Exception e) {
            log.error("图片上传到对象存储失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            // 临时文件清理
            this.deleteTempFile(file);
        }
    }
    /**
     * 校验文件
     *
     * @param multipartFile
     */
    private void validPicture(MultipartFile multipartFile) {
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.PARAMS_ERROR, "文件不能为空");
        // 1. 校验文件大小
        long fileSize = multipartFile.getSize();
        final long ONE_M = 1024 * 1024;
        ThrowUtils.throwIf(fileSize > 2 * ONE_M, ErrorCode.PARAMS_ERROR, "文件大小不能超过 2MB");
        // 2. 校验文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        // 允许上传的文件后缀列表（或者集合）
        final List<String> ALLOW_FORMAT_LIST = Arrays.asList("jpeg", "png", "jpg", "webp");
        ThrowUtils.throwIf(!ALLOW_FORMAT_LIST.contains(fileSuffix), ErrorCode.PARAMS_ERROR, "文件类型错误");
    }
    /**
     * 清理临时文件
     *
     * @param file
     */
    public void deleteTempFile(File file) {
        if (file == null) {
            return;
        }
        // 删除临时文件
        boolean deleteResult = file.delete();
        if (!deleteResult) {
            log.error("file delete error, filepath = {}", file.getAbsolutePath());
        }
    }
}