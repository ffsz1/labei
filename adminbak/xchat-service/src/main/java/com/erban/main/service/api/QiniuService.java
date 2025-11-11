package com.erban.main.service.api;

import com.erban.main.config.QiniuConfig;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.xchat.common.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Service
public class QiniuService {

    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    // TODO 访问URL需要修改成正式的域名
//    public static final String accessUrl = PropertyUtil.getProperty("qiniu.access_url");
//    public static final String accessKey = PropertyUtil.getProperty("qiniu.access_key");
//    public static final String secretKey = PropertyUtil.getProperty("qiniu.secret_key");
//    public static final String bucket = PropertyUtil.getProperty("qiniu.bucket");     //

    private UploadManager uploadManager;
    private Auth auth;

    @Autowired
    QiniuConfig qiniuConfig;

    @PostConstruct
    public void init() throws Exception {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
    }

    /**
     *  构建成可访问的文件地址
     *
     * @param fileName
     * @return
     */
    public String mergeUrl(String fileName) {
        return qiniuConfig.getAccessUrl() + fileName;
    }

    /**
     *  构建成可访问的文件地址，并加上瘦身的参数
     *
     * @param fileName
     * @return
     */
    public String mergeUrlAndSlim(String fileName) {
        return qiniuConfig.getAccessUrl() + fileName + "?imageslim";
    }

    /**
     * 下载网络上的文件，并上传到七牛
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String uploadByUrl(String url) throws Exception {
        return uploadByUrl(url, null);
    }

    /**
     * 下载网络上的文件，并上传到七牛
     *
     * @param url      网络URL
     * @param fileName 指定保存的文件名
     * @return
     * @throws Exception
     */
    public String uploadByUrl(String url, String fileName) throws Exception {
        InputStream is = FileUtils.downloadFileInputStream(url);
        return uploadByStream(is,fileName);
    }

    /**
     * 上传到七牛对象存储，以文件内容的hash值作为文件名
     *
     * @param is
     * @return 返回上传后的文件名
     * @throws Exception
     */
    public String uploadByStream(InputStream is) throws Exception {
        return uploadByStream(is, null);
    }

    /**
     *  上传到七牛对象存储，指定上传后的文件名
     *
     * @param is        数据流
     * @param fileName 上传后的文件名，默认不指定的情况下，以文件内容的hash值作为文件名
     * @return 返回上传后的文件名
     * @throws Exception
     */
    public String uploadByStream(InputStream is,String fileName) throws Exception {
        try {
            // 获取上传的凭证
            String upToken = auth.uploadToken(qiniuConfig.getBucket());
            Response response = uploadManager.put(is, fileName, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            logger.error("uploadByStream error, response is: " + ex.response, ex);
        }
        return null;
    }

    /**
     * 获取本地的文件，上传到七牛
     *
     * @param localPath
     * @return
     */
    public String uploadByLocal(String localPath) {
        return uploadByLocal(localPath, null);
    }

    /**
     * 获取本地的文件，上传到七牛
     *
     * @param localPath 本地文件名
     * @param fileName  保存到七牛的文件名
     * @return 返回上传后的文件名
     */
    public String uploadByLocal(String localPath, String fileName) {
        try {
            // 获取上传的凭证
            String upToken = auth.uploadToken(qiniuConfig.getBucket());
            Response response = uploadManager.put(localPath, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            logger.error("uploadByStream error, response is: " + ex.response, ex);
        }
        return null;

    }
    public static void main(String[] args) throws Exception {
//        QiniuService qiniuService = new QiniuService();
////        String path =  qiniuService.uploadByUrl("https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV85NDg4NTUyMDZfMTUwOTMzOTY1NzI0MF9kNTg0NGMxMS1lNzNiLTQ2MzktODViYi0xYmYxYTJjMjE1YzU=", "save_my_name.jpg");
////        System.out.println("path: "+path);
////
////        path =  qiniuService.uploadByUrl("https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV85NDg4NTUyMDZfMTUwOTMzOTY1NzI0MF9kNTg0NGMxMS1lNzNiLTQ2MzktODViYi0xYmYxYTJjMjE1YzU=");
////        System.out.println("path: "+path);

        Configuration cfg = new Configuration(Zone.zone1());                //zong1() 代表华北地区
        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = "77QyTeGDcCesh5He9vr0A23O5QRCXaC_-IUGxAlt";      //AccessKey的值
        String secretKey = "-012u5i-MYZ6FseMSSACrnZoJMlEjUoVZgWnVMzu";      //SecretKey的值
        String bucket = "yingtao";                                          //存储空间名
        String localFilePath = "/Users/chris/Desktop/icon/xinshouzhiyin@3x.png";     //上传图片路径

        String key = "xinshouzhiyin.png";                                               //在七牛云中图片的命名
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

    }
}
