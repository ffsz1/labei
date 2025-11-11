package com.juxiao.xchat.manager.external.netease.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.juxiao.xchat.manager.external.netease.ImageCheckManager;
import com.juxiao.xchat.manager.external.netease.params.ImageCheckParams;
import com.juxiao.xchat.manager.external.netease.ret.CheckImageRet;
import com.juxiao.xchat.manager.external.netease.ret.ImageCheckRet;
import com.juxiao.xchat.manager.external.netease.utils.HttpClient4Utils;
import com.juxiao.xchat.manager.external.netease.utils.SignatureUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author chris
 * @date 2019-07-14
 */
@Slf4j
@Service
public class ImageCheckManagerImpl implements ImageCheckManager {

    private final static String API_URL = "https://as.dun.163yun.com/v3/image/check";

    /** 产品密钥ID，产品标识 */
    private final static String SECRETID = "98f17a07b933ffc0228652e2cd4b76cf";
    /** 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
    private final static String SECRETKEY = "da10e48acae9ebcbcbc550fb74c51d6f";


    /** 实例化HttpClient，发送http请求使用，可根据需要自行调参 */
    private static HttpClient httpClient = HttpClient4Utils.createHttpClient(100, 20, 2000, 2000, 2000);


    /**
     * 图片反垃圾检测
     *
     * @param imageCheckParams imageCheckParams
     * @return CheckImageRet
     * @throws Exception Exception
     */
    @Override
    public CheckImageRet checkImage(ImageCheckParams imageCheckParams) throws Exception {
        Map<String, String> params = new HashMap<String, String>(16);
        // 1.设置公共参数
        params.put("secretId", SECRETID);
        params.put("businessId", imageCheckParams.getBusinessId());
        params.put("version", "v3.2");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(new Random().nextInt()));

        // 2.设置私有参数
        JsonArray jsonArray = new JsonArray();
        // 传图片url进行检测，name结构产品自行设计，用于唯一定位该图片数据
        for(String url : imageCheckParams.getImages()){
            JsonObject image = new JsonObject();
            image.addProperty("name", url);
            image.addProperty("type", 1);
            image.addProperty("data", url);
            jsonArray.add(image);
        }
        params.put("images", jsonArray.toString());
        // 3.生成签名信息
        String signature = SignatureUtils.genSignature(SECRETKEY, params);
        params.put("signature", signature);

        // 4.发送HTTP请求
        String response = HttpClient4Utils.sendPost(httpClient, API_URL, params, Consts.UTF_8);
        // 5.解析接口返回值
        JsonObject resultObject = new JsonParser().parse(response).getAsJsonObject();
        int code = resultObject.get("code").getAsInt();
        String msg = resultObject.get("msg").getAsString();
        CheckImageRet checkImageRet = new CheckImageRet();
        checkImageRet.setCode(code);
        checkImageRet.setMsg(msg);
        if (code == 200) {
            JsonArray resultArray = resultObject.getAsJsonArray("result");
            for (JsonElement jsonElement : resultArray) {
                JsonObject jObject = jsonElement.getAsJsonObject();
                String name = jObject.get("name").getAsString();
                int status = jObject.get("status").getAsInt();
                String taskId = jObject.get("taskId").getAsString();
                ImageCheckRet imageCheckRet = new ImageCheckRet();
                imageCheckRet.setName(name);
                imageCheckRet.setStatus(status);
                imageCheckRet.setTaskId(taskId);
                checkImageRet.setImageCheckRet(imageCheckRet);
                JsonArray labelArray = jObject.get("labels").getAsJsonArray();
                int maxLevel = -1;
                // 产品需根据自身需求，自行解析处理，本示例只是简单判断分类级别
                for (JsonElement labelElement : labelArray) {
                    JsonObject lObject = labelElement.getAsJsonObject();
                    int level = lObject.get("level").getAsInt();
                    maxLevel = level > maxLevel ? level : maxLevel;
                }
                switch (maxLevel) {
                    case 0:
                        checkImageRet.setLevel(maxLevel);
                        checkImageRet.setResult("正常");
                        break;
                    case 1:
                        checkImageRet.setLevel(maxLevel);
                        checkImageRet.setResult("嫌疑");
                        break;
                    case 2:
                        checkImageRet.setLevel(maxLevel);
                        checkImageRet.setResult("确定");
                        break;
                    default:
                        break;
                }
            }
        }
        return checkImageRet;
    }
}
