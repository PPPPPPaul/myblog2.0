package com.lk.controller;

import com.lk.custom.result.PictureResult;
import com.lk.utils.PictureUtil;
import com.lk.utils.QiniuFileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class AdminFileUploadController {
/*    @Value("${IMG_ADDR}")
    private String img_addr;*/
    @RequestMapping("/uploadFile")
    @ResponseBody
    public PictureResult picUpload(MultipartFile file) {
        /*return PictureUtil.uploadFile(file,img_addr);*/
        try {
            return QiniuFileUtil.upload(file.getBytes());
        } catch (IOException e) {
        }
        return null;
        }
    }
