package com.wtc.wangpicturebackend.service.ServiceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtc.wangpicturebackend.common.DeleteRequest;
import com.wtc.wangpicturebackend.exception.ErrorCode;
import com.wtc.wangpicturebackend.exception.ThrowUtils;
import com.wtc.wangpicturebackend.manager.FileManager;
import com.wtc.wangpicturebackend.model.dto.file.UploadPictureResult;
import com.wtc.wangpicturebackend.model.dto.picture.PictureQueryRequest;
import com.wtc.wangpicturebackend.model.dto.picture.PictureUploadRequest;
import com.wtc.wangpicturebackend.model.entity.Picture;
import com.wtc.wangpicturebackend.model.entity.User;
import com.wtc.wangpicturebackend.model.vo.PictureVO;
import com.wtc.wangpicturebackend.model.vo.UserVO;
import com.wtc.wangpicturebackend.service.PictureService;
import com.wtc.wangpicturebackend.mapper.PictureMapper;
import com.wtc.wangpicturebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author kaixinchaoren
* @description 针对表【picture(图片)】的数据库操作Service实现
* @createDate 2025-01-27 22:09:26
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService{

    @Autowired
    private  FileManager fileManager;

    @Autowired
    private UserService userService;
    /**
     * 上传图片
     * @param multipartFile
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    @Override
    public PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser) {
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_AUTH_ERROR);
        //用于判断是新增还是更新图片
        Long pictureId = null;
        if (pictureUploadRequest != null) {
            pictureId = pictureUploadRequest.getId();
        }
        if (pictureId != null) {
            boolean exists = this.lambdaQuery()
                    .eq(Picture::getId, pictureId)
                    .exists();
            ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "图片不存在");
        }

        //上传图片，得到信息
        //按照用户 id 划分目录
        String uploadPathPrefix = String.format("public/%s", loginUser.getId());
        UploadPictureResult uploadPictureResult = fileManager.uploadPicture(multipartFile, uploadPathPrefix);
        //构造要入库的图片信息
        Picture picture = new Picture();
        picture.setUrl(uploadPictureResult.getUrl());
        picture.setName(uploadPictureResult.getPicName());
        picture.setPicSize(uploadPictureResult.getPicSize());
        picture.setPicWidth(uploadPictureResult.getPicWidth());
        picture.setPicHeight(uploadPictureResult.getPicHeight());
        picture.setPicScale(uploadPictureResult.getPicScale());
        picture.setPicFormat(uploadPictureResult.getPicFormat());
        picture.setUserId(loginUser.getId());
        //如果 pictureId 不为空，表示更新，否则是新增
        if (pictureId != null) {
        // 如果是更新，需要补充 id 和编辑时间
            picture.setId(pictureId);
            picture.setEditTime(new Date());
        }
        boolean result = this.saveOrUpdate(picture);
        ThrowUtils.throwIf(!result,ErrorCode.OPERATION_ERROR,"图片上传失败");
        return PictureVO.objToVo(picture);
    }

    /**
     * 获取QueryWrapper 分页查询对象
     * @param pictureQueryRequest
     * @return
     */
    public QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest) {
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        if (pictureQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = pictureQueryRequest.getId();
        String name = pictureQueryRequest.getName();
        String introduction = pictureQueryRequest.getIntroduction();
        String category = pictureQueryRequest.getCategory();
        List<String> tags = pictureQueryRequest.getTags();
        Long picSize = pictureQueryRequest.getPicSize();
        Integer picWidth = pictureQueryRequest.getPicWidth();
        Integer picHeight = pictureQueryRequest.getPicHeight();
        Double picScale = pictureQueryRequest.getPicScale();
        String picFormat = pictureQueryRequest.getPicFormat();
        String searchText = pictureQueryRequest.getSearchText();
        Long userId = pictureQueryRequest.getUserId();
        String sortField = pictureQueryRequest.getSortField();
        String sortOrder = pictureQueryRequest.getSortOrder();
        // 从多字段中搜索
        if (StrUtil.isNotBlank(searchText)) {
        // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("name", searchText)
                    .or()
                    .like("introduction", searchText)
            );
        }
        queryWrapper.eq(ObjUtil.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        queryWrapper.like(StrUtil.isNotBlank(introduction), "introduction", introduction); queryWrapper.like(StrUtil.isNotBlank(picFormat), "picFormat", picFormat);
        queryWrapper.eq(StrUtil.isNotBlank(category), "category", category); queryWrapper.eq(ObjUtil.isNotEmpty(picWidth), "picWidth", picWidth);
        queryWrapper.eq(ObjUtil.isNotEmpty(picHeight), "picHeight", picHeight);
        queryWrapper.eq(ObjUtil.isNotEmpty(picSize), "picSize", picSize);
        queryWrapper.eq(ObjUtil.isNotEmpty(picScale), "picScale" , picScale);
        //JSON数组查询
        if(tags != null){
            for(String tag : tags){
                queryWrapper.like("tags","\""+tag+"\"");
            }
        }
        //排序
        queryWrapper.orderBy(StrUtil.isNotBlank(sortField),sortOrder.equals("ascend"),sortField);
        return queryWrapper;
    }

    /**
     * 获取单个图片封装
     * @param picture
     * @param request
     * @return
     */
    @Override
    public PictureVO getPictureVO(Picture picture, HttpServletRequest request) {
        ThrowUtils.throwIf(picture==null,ErrorCode.PARAMS_ERROR);
        PictureVO pictureVO = PictureVO.objToVo(picture);
        Long userId = picture.getUserId();
        User user = userService.getById(userId);
        UserVO userVO=userService.getUserVO(user);
        pictureVO.setUserVO(userVO);
        return pictureVO;
    }

    /**
     * 分页获取多个图片封装
     * @param picturePage
     * @param request
     * @return
     */
    @Override
    public Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request) {
        List<Picture> pictureList = picturePage.getRecords();
        Page<PictureVO> pictureVOPage=new Page<PictureVO>(picturePage.getCurrent(),picturePage.getSize());
        if(CollUtil.isEmpty(pictureList)){
            return pictureVOPage;
        }
        //对象列表=》封装对象列表
        List<PictureVO> pictureVOList=pictureList.stream().map(PictureVO::objToVo).collect(Collectors.toList());
        //TODO 优化：1:先获取需要查询用户的id 再将查到的userVO设置到图片中去

        //2: 填充信息
        pictureVOList.forEach(pictureVO -> {
            Long userId = pictureVO.getUserId();
            User user=userService.getById(userId);
            UserVO userVO=userService.getUserVO(user);
            pictureVO.setUserVO(userVO);
        });
        pictureVOPage.setRecords(pictureVOList);
        return pictureVOPage;
    }

    /**
     * 图片数据校验
     * @param picture
     */
    @Override
    public void validPicture(Picture picture) {
        ThrowUtils.throwIf(picture==null,ErrorCode.PARAMS_ERROR);
        //从对象中取值
        long id = picture.getId();
        String url=picture.getUrl();
        String introduction=picture.getIntroduction();
        ThrowUtils.throwIf(ObjUtil.isEmpty(id),ErrorCode.PARAMS_ERROR,"id 不能为空");
        if(StrUtil.isNotBlank(url)){
            ThrowUtils.throwIf(url.length()>1024,ErrorCode.PARAMS_ERROR,"url太长");
        }
        if(StrUtil.isNotBlank(introduction)){
            ThrowUtils.throwIf(introduction.length()>800,ErrorCode.PARAMS_ERROR,"简介过长");
        }
    }


}




