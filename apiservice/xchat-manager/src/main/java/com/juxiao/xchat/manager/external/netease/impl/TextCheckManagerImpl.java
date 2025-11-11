package com.juxiao.xchat.manager.external.netease.impl;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.juxiao.xchat.manager.external.netease.TextCheckManager;
import com.juxiao.xchat.manager.external.netease.params.TextCheckParams;
import com.juxiao.xchat.manager.external.netease.ret.CheckRet;
import com.juxiao.xchat.manager.external.netease.ret.TextCheckResult;
import com.juxiao.xchat.manager.external.netease.utils.HttpClient4Utils;
import com.juxiao.xchat.manager.external.netease.utils.SignatureUtils;
import com.juxiao.xchat.manager.external.netease.vo.CheckDetailsVO;
import com.juxiao.xchat.manager.external.netease.vo.CheckLabelsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 易盾反垃圾云服务文本在线检测
 * @author chris
 * @date 2019-07-14
 */
@Slf4j
@Service
public class TextCheckManagerImpl implements TextCheckManager {


    private final static String API_URL = "https://as.dun.163yun.com/v3/text/check";

    /** 产品密钥ID，产品标识 */
    private final static String SECRETID = "98f17a07b933ffc0228652e2cd4b76cf";
    /** 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
    private final static String SECRETKEY = "da10e48acae9ebcbcbc550fb74c51d6f";


    /** 实例化HttpClient，发送http请求使用，可根据需要自行调参 */
    private static HttpClient httpClient = HttpClient4Utils.createHttpClient(100, 20, 2000, 2000, 2000);

    /**
     * 在线检测文本内容
     *
     * @param textCheckParams textCheckParams
     * @return TextCheckRet
     */
    @Override
    public CheckRet checkText(TextCheckParams textCheckParams) throws Exception{
        long startTime = System.currentTimeMillis();
        Map<String, String> params = new HashMap<String, String>(16);
        // 1.设置公共参数
        params.put("secretId", SECRETID);
        params.put("businessId", textCheckParams.getBusinessId());
        params.put("version", "v3.1");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(new Random().nextInt()));
        // 2.设置私有参数
        params.put("dataId", textCheckParams.getDataId());
        params.put("content", textCheckParams.getContent());
        params.put("ip", textCheckParams.getIp());
        params.put("deviceId", textCheckParams.getDeviceId());
        params.put("deviceType", textCheckParams.getDeviceType().toString());
        // 3.生成签名信息
        String signature = SignatureUtils.genSignature(SECRETKEY, params);
        params.put("signature", signature);
        //4.发送HTTP请求，这里使用的是HttpClient工具包
        String response = HttpClient4Utils.sendPost(httpClient, API_URL, params, Consts.UTF_8);
        log.info("[易盾] 反垃圾云服务文本在线检测执行耗时:{}/ms",System.currentTimeMillis() - startTime);
        JsonObject jObject = new JsonParser().parse(response).getAsJsonObject();
        int code = jObject.get("code").getAsInt();
        String msg = jObject.get("msg").getAsString();
        CheckRet checkRet = new CheckRet();
        checkRet.setCode(code);
        checkRet.setMsg(msg);
        if(code == 200){
            JsonObject resultObject = jObject.getAsJsonObject("result");
            int action = resultObject.get("action").getAsInt();
            String taskId = resultObject.get("taskId").getAsString();
            JsonArray labelArray = resultObject.getAsJsonArray("labels");
            TextCheckResult textCheckResult = new TextCheckResult();
            for (JsonElement labelElement : labelArray) {
                JsonObject record = labelElement.getAsJsonObject();
                int label = record.get("label").getAsInt();
                int level = record.get("level").getAsInt();
                JsonObject detailsObject = record.getAsJsonObject("details");
                CheckLabelsVO checkLabelsVO = new CheckLabelsVO();
                checkLabelsVO.setLabel(label);
                checkLabelsVO.setLevel(level);
                textCheckResult.setLabels(checkLabelsVO);
                JsonArray hintArray = detailsObject.getAsJsonArray("hint");
                List<CheckDetailsVO> checkDetails = Lists.newArrayList();
                for (JsonElement hintElement : hintArray) {
                    CheckDetailsVO checkDetailsVO = new CheckDetailsVO();
                    checkDetailsVO.setInfo(hintElement.getAsString());
                    checkDetails.add(checkDetailsVO);
                }
                checkLabelsVO.setDetails(checkDetails);
            }
            textCheckResult.setAction(action);
            textCheckResult.setTaskId(taskId);

            checkRet.setTextCheckResult(textCheckResult);
        }
        return checkRet;
    }
}
