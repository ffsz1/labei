package com.juxiao.xchat.manager.external.qiniu.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.manager.external.qiniu.QiniuManager;
import com.juxiao.xchat.manager.external.qiniu.conf.QiniuConf;
import com.juxiao.xchat.manager.external.qiniu.dto.QiNiuDTO;
import com.qiniu.common.Constants;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Service
public class QiniuManagerImpl implements QiniuManager {
    private static final Logger logger = LoggerFactory.getLogger(QiniuManagerImpl.class);

    private UploadManager uploadManager;
    private Auth auth;

    @Autowired
    private QiniuConf qiniuConf;

    @PostConstruct
    public void init() throws Exception {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        auth = Auth.create(qiniuConf.getAccessKey(), qiniuConf.getSecretKey());
    }

    /**
     * 构建成可访问的文件地址
     *
     * @param fileName
     * @return
     */
    @Override
    public String mergeUrl(String fileName) {
        return qiniuConf.getAccessUrl() + fileName;
    }

    /**
     * 构建成可访问的文件地址，并加上瘦身的参数
     *
     * @param fileName
     * @return
     */
    @Override
    public String mergeUrlAndSlim(String fileName) {
        return qiniuConf.getAccessUrl() + fileName + "?imageslim";
    }

    /**
     * 下载网络上的文件，并上传到七牛
     *
     * @param url
     * @return
     * @throws Exception
     */
    @Override
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
    @Override
    public String uploadByUrl(String url, String fileName) throws Exception {
        URL fileUrl = new URL(url);
        URLConnection con = fileUrl.openConnection();
        con.setConnectTimeout(3 * 1000);
        InputStream is = con.getInputStream();
        return uploadByStream(is, fileName);
    }

    /**
     * 上传到七牛对象存储，以文件内容的hash值作为文件名
     *
     * @param is
     * @return 返回上传后的文件名
     * @throws Exception
     */
    @Override
    public String uploadByStream(InputStream is) throws Exception {
        return uploadByStream(is, null);
    }

    /**
     * 上传到七牛对象存储，指定上传后的文件名
     *
     * @param is       数据流
     * @param fileName 上传后的文件名，默认不指定的情况下，以文件内容的hash值作为文件名
     * @return 返回上传后的文件名
     * @throws Exception
     */
    @Override
    public String uploadByStream(InputStream is, String fileName) throws Exception {
        try {
            // 获取上传的凭证
            String upToken = auth.uploadToken(qiniuConf.getBucket());
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
    @Override
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
    @Override
    public String uploadByLocal(String localPath, String fileName) {
        try {
            // 获取上传的凭证
            String upToken = auth.uploadToken(qiniuConf.getBucket());
            Response response = uploadManager.put(localPath, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            logger.error("uploadByStream error, response is: " + ex.response, ex);
        }
        return null;

    }

    /**
     * 七牛图片审核
     *
     * @param uri
     * @return
     */
    @Override
    public boolean imageCensor(String uri) {
        Client client = new Client();
        String s = "{\"data\":{\"uri\": \""+uri+"\"},\"params\":{\"scenes\": [\"pulp\",\"terror\",\"politician\"],\"detail" + "\":true}}";
        byte[] body = s.getBytes(Constants.UTF_8);
        String url = String.format("%s%s", "http://ai.qiniuapi.com", "/v3/image/censor");
        StringMap headers = auth.authorizationV2(url, "POST", body, Client.JsonMime);
        try {
            Response response = client.post(url, body, headers, Client.JsonMime);
            QiNiuDTO qiNiuDTO = new Gson().fromJson(response.bodyString(), QiNiuDTO.class);
            if (qiNiuDTO.getCode() == 200) {
                if ("pass".equals(qiNiuDTO.getResult().get("suggestion").toString())) {
                    return true;
                } else {
                    logger.warn("[ 七牛图片审核不通过 --> imageCensor ],uri:{},data:{}", uri, qiNiuDTO);
                }
            } else {
                logger.warn("[ 七牛图片审核请求失败 --> imageCensor ],uri:{},data:{}", uri, qiNiuDTO);
            }
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return false;
    }
}
