package com.lk.utils;

import com.google.gson.Gson;
import com.lk.custom.result.PictureResult;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import java.io.IOException;

public class QiniuFileUtil {
    private static final String ACCESS_KEY = "1O7XzNwaCCmmsdQQE_ZJ_iWWRO7HN56fLj8gqZRn";
    private static final String SECRET_KEY = "7-lByEVlP9m6aclo6Y7nLQkDwgRinAp3Nf_Hg00E";

    private static final String bucketname = "blogstorage";
    private static final Auth auth = Auth.create(ACCESS_KEY,SECRET_KEY);
    public static PictureResult upload(byte[] data) throws IOException {
        //输出的图片结果对象
        PictureResult res = new PictureResult();
        if (data!=null) {
            //生成唯一图片名称
            String fileName = RandomUtil.getRandomFileName();
            //七牛云上传管理中心
            UploadManager uploadManager = new UploadManager(new Configuration(Zone.zone0()));
            try {
                //上传过程，返回结果
                Response response = uploadManager.put(data, fileName, auth.uploadToken(bucketname));
                //解析返回结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                res.setError(0);
                res.setUrl("http://pjys3bcui.bkt.clouddn.com/" + putRet.key);
            } catch (QiniuException e) {
                /*Response r = e.response;*/
                res.setError(1);
                res.setMessage("上传失败!");
 /*           try {
                System.err.println(r.bodyString());
            }catch (QiniuException q){}*/
            }
        }else {
            res.setError(1);
            res.setMessage("没有图片");
        }
        return res;
    }
}
