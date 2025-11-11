package com.tongdaxing.xchat_core.file;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zhouxiangfeng on 2017/5/16.
 */

public class FileCoreImpl extends AbstractBaseCore implements IFileCore {

    private UploadManager uploadManager;
    // 拉贝星球的七牛地址
    public static final String accessUrl = "http://pic.haijiaoxingqiu.cn/";
    public static final String accessKey = "F8V---cUo1AK3M0hTQA1HZQYjwKrzUytpx7RRjnO";
    public static final String secretKey = "EybTYBJb4tscjEG2FAlSEEpHU6-OIx1TFUD-YoYu";
    public static final String picprocessing = "?imageslim";
    // 拉贝星球的七牛bucket
    public static final String bucket = "tianyalive";


    public FileCoreImpl() {
        uploadManager = new UploadManager();
    }


    @Override
    public void upload(File file) {
        try {
            // 1 构造上传策略
            JSONObject _json = new JSONObject();
            long _deadline = System.currentTimeMillis() / 1000 + 3600;
            _json.put("deadline", _deadline);// 有效时间为一个小时
            _json.put("scope", bucket);
            String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json
                    .toString().getBytes());
            byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, secretKey);
            String _encodedSign = UrlSafeBase64.encodeToString(_sign);
            String _uploadToken = accessKey + ':' + _encodedSign + ':' + _encodedPutPolicy;
            UploadManager uploadManager = new UploadManager();
            uploadManager.put(file, null, _uploadToken, new UpCompletionHandler() {

                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            try {
                                String imgName = response.getString("key");
                                notifyClients(IFileCoreClient.class, IFileCoreClient.METHOD_ON_UPLOAD,
                                        accessUrl + imgName + picprocessing);
                            } catch (Exception e) {
                                e.printStackTrace();
                                notifyClients(IFileCoreClient.class, IFileCoreClient.METHOD_ON_UPLOAD_FAITH);
                            }

                        }
                    }, null);

        } catch (Exception e) {
            e.printStackTrace();
            notifyClients(IFileCoreClient.class, IFileCoreClient.METHOD_ON_UPLOAD_FAITH);
        }
    }

    @Override
    public void uploadPhoto(File file) {
        try {
            // 1 构造上传策略
            JSONObject _json = new JSONObject();
            long _deadline = System.currentTimeMillis() / 1000 + 3600;
            _json.put("deadline", _deadline);// 有效时间为一个小时
            _json.put("scope", bucket);
            String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json
                    .toString().getBytes());
            byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, secretKey);
            String _encodedSign = UrlSafeBase64.encodeToString(_sign);
            String _uploadToken = accessKey + ':' + _encodedSign + ':'
                    + _encodedPutPolicy;
            UploadManager uploadManager = new UploadManager();
            uploadManager.put(file, null, _uploadToken,
                    (key, info, response) -> {
                        if (response != null) {
                            try {
                                String imgName = response.getString("key");
                                notifyClients(IFileCoreClient.class, IFileCoreClient.METHOD_ON_UPLOAD_PHOTO,
                                        accessUrl + imgName + picprocessing);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                notifyClients(IFileCoreClient.class, IFileCoreClient.METHOD_ON_UPLOAD_PHOTO_FAITH);
                            }
                        }

                    }, null);

        } catch (Exception e) {
            e.printStackTrace();
            notifyClients(IFileCoreClient.class, IFileCoreClient.METHOD_ON_UPLOAD_PHOTO_FAITH);
        }
    }

    @Override
    public void download() {

    }

    /**
     * 使用 HMAC-SHA1 签名方法对encryptText进行签名
     *
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     */
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
            throws Exception {
        byte[] data = encryptKey.getBytes("UTF-8");
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance("HmacSHA1");
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        byte[] text = encryptText.getBytes("UTF-8");
        // 完成 Mac 操作
        return mac.doFinal(text);
    }
}
