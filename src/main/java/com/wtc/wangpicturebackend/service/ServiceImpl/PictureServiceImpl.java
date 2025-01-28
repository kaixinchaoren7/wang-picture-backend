package com.wtc.wangpicturebackend.service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtc.wangpicturebackend.model.entity.Picture;
import com.wtc.wangpicturebackend.service.PictureService;
import com.wtc.wangpicturebackend.mapper.PictureMapper;
import org.springframework.stereotype.Service;

/**
* @author kaixinchaoren
* @description 针对表【picture(图片)】的数据库操作Service实现
* @createDate 2025-01-27 22:09:26
*/
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService{

}




