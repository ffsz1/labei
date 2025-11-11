package com.juxiao.xchat.manager.external.jpush.impl;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonElement;
import com.juxiao.xchat.manager.external.jpush.JpushManager;
import com.juxiao.xchat.manager.external.jpush.conf.JpushConf;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JpushManagerImpl implements JpushManager {
    private static final Logger logger = LoggerFactory.getLogger(JpushManagerImpl.class);
    @Autowired
    private JpushConf jpushConf;


    @Override
    public int sendToRegistrationId(String registrationId, String notificationAlert, String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasParam) {
        int result = 0;
        try {
            JPushClient jPushClient = new JPushClient(jpushConf.getSecret(), jpushConf.getAppKey());
            PushPayload pushPayload = buildPushObjectWithAllPlatform(
                    Platform.all(), Audience.alias(registrationId),
                    notificationAlert, notificationTitle, msgTitle, msgContent, extrasParam);
            logger.info("极光别名推送, registrationId:>{}, content:>{}", registrationId, JSONObject.toJSONString(pushPayload));
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            logger.info("极光别名推送返回内容, registrationId:>{}, respContent:>{}", registrationId, JSONObject.toJSONString(pushResult));
            if (pushResult.getResponseCode() == 200) {
                result = 1;
            }
        } catch (Exception e) {
            logger.error("极光别名推送失败, errMsg:>{}, registrationId:>{}, notificationAlert:>{}, notificationTitle:>{}, msgTitle:>{}, msgContent:>{}, extrasParam:>{}",
                    e.getMessage(), registrationId, notificationAlert, notificationTitle, msgTitle, msgContent, JSONObject.toJSONString(extrasParam), e);
        }
        return result;
    }


    @Override
    public int sendToAllAndroid(String notificationAlert, String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasParam) {
        int result = 0;
        try {
            JPushClient jPushClient = new JPushClient(jpushConf.getSecret(), jpushConf.getAppKey());
            PushPayload pushPayload = buildPushObjectWithAllPlatform(
                    Platform.android(), Audience.all(),
                    notificationAlert, notificationTitle, msgTitle, msgContent, extrasParam);
            logger.info("极光安卓广播推送, content:>{}", JSONObject.toJSONString(pushPayload));
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            logger.info("极光安卓广播推送返回内容, respContent:>{}", JSONObject.toJSONString(pushResult));
            if (pushResult.getResponseCode() == 200) {
                result = 1;
            }
        } catch (Exception e) {
            logger.error("极光安卓广播推送失败, errMsg:>{}, notificationAlert:>{}, notificationTitle:>{}, msgTitle:>{}, msgContent:>{}, extrasParam:>{}",
                    e.getMessage(), notificationAlert, notificationTitle, msgTitle, msgContent, JSONObject.toJSONString(extrasParam), e);
        }
        return result;
    }


    @Override
    public int sendToAllIos(String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasParam) {
        int result = 0;
        try {
            JPushClient jPushClient = new JPushClient(jpushConf.getSecret(), jpushConf.getAppKey());
            PushPayload pushPayload = buildPushObjectWithAllPlatform(
                    Platform.ios(), Audience.all(),
                    "", notificationTitle, msgTitle, msgContent, extrasParam);
            logger.info("极光IOS广播推送, content:>{}", JSONObject.toJSONString(pushPayload));
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            logger.info("极光IOS广播推送返回内容, respContent:>{}", JSONObject.toJSONString(pushResult));
            if (pushResult.getResponseCode() == 200) {
                result = 1;
            }
        } catch (Exception e) {
            logger.error("极光IOS广播推送失败, errMsg:>{}, notificationTitle:>{}, msgTitle:>{}, msgContent:>{}, extrasParam:>{}",
                    e.getMessage(), notificationTitle, msgTitle, msgContent, JSONObject.toJSONString(extrasParam), e);
        }
        return result;
    }

    @Async
    @Override
    public void sendToAll(String notificationAlert, String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasParam) {
        try {
            JPushClient jPushClient = new JPushClient(jpushConf.getSecret(), jpushConf.getAppKey());
            PushPayload pushPayload = buildPushObjectWithAllPlatform(Platform.all(), Audience.all(), notificationAlert, notificationTitle, msgTitle, msgContent, extrasParam);
            logger.info("极光广播推送, content:>{}", JSONObject.toJSONString(pushPayload));
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            logger.info("极光广播推送返回内容, respContent:>{}", JSONObject.toJSONString(pushResult));
        } catch (Exception e) {
            logger.error("极光广播推送失败, notificationAlert:>{}, notificationTitle:>{}, msgTitle:>{}, msgContent:>{}, extrasParam:>{},", notificationAlert, notificationTitle, msgTitle, msgContent, JSONObject.toJSONString(extrasParam), e);
        }
    }


    /**
     * 创建所有的平台的推送消息
     *
     * @param platform
     * @param audience
     * @param notificationAlert
     * @param notificationTitle
     * @param msgTitle
     * @param msgContent
     * @param extrasParam
     * @return
     */
    private PushPayload buildPushObjectWithAllPlatform(Platform platform, Audience audience, String notificationAlert, String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasParam) {
        PushPayload.Builder pushPayloadBuilder = PushPayload.newBuilder();
        Notification.Builder notificationBuilder = Notification.newBuilder();
        pushPayloadBuilder.setPlatform(platform)
                .setAudience(audience);
        //判断平台的类型
        boolean isAndroid = false;
        boolean isIos = false;
        if (platform.isAll()) {
            isAndroid = true;
            isIos = true;
        } else {
            //判断平台的类型
            for (JsonElement element : platform.toJSON().getAsJsonArray()) {
                if ("android".equalsIgnoreCase(element.getAsString())) {
                    isAndroid = true;
                } else if ("ios".equalsIgnoreCase(element.getAsString())) {
                    isIos = true;
                }
            }
        }
        if (isAndroid) {
            //指定当前推送的android通知
            notificationBuilder.addPlatformNotification(AndroidNotification.newBuilder()
                    .setAlert(StringUtils.trimToEmpty(notificationAlert))
                    .setTitle(StringUtils.trimToEmpty(notificationTitle))
                    .addExtras(extrasParam)
                    .build());

        }
        if (isIos) {
            //指定当前推送的iOS通知
            notificationBuilder.addPlatformNotification(IosNotification.newBuilder()
                    .setAlert(StringUtils.trimToEmpty(notificationAlert))
                    .incrBadge(1)
                    .setSound("sound.caf")
                    .addExtras(extrasParam)
                    // .setContentAvailable(true)
                    .build());
        }

        return pushPayloadBuilder
                .setNotification(notificationBuilder.build())
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(true)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(86400)
                        .build())
                .build();
    }

    public static void main(String[] args) {
        try {
            JpushManagerImpl manager = new JpushManagerImpl();
            manager.jpushConf = new JpushConf();
            manager.jpushConf.setAppKey("45e0affd380067ae96b1f7bf");
            manager.jpushConf.setSecret("368723e45991217275f9d02c");

            Map<String, String> extraMap = new HashMap<>(1);
            extraMap.put("roomUid", "503187");
            manager.sendToRegistrationId("500029","情人节都过了，还不来语萌找cp？", "通知消息",
                    "情人节都过了，还不来语萌找cp→", "情人节都过了，还不来语萌找cp→_→", extraMap);
//            manager.sendToAll("sendToAll i am notification_alert", "sendToAll i am notification_title",
//                    "sendToAll i am msg_title", "sendToAll i am msg_content", extraMap);


//            extraMap.clear();
//            extraMap.put("msg", "sendToAllAndroid");
//            manager.sendToAllAndroid("sendToAllAndroid i am notification_alert", "sendToAllAndroid i am notification_title",
//                    "sendToAllAndroid i am msg_title", "sendToAllAndroid i am msg_content", extraMap);
//
//
//            extraMap.clear();
//            extraMap.put("msg", "sendToAllIos");
//            manager.sendToAllIos("sendToAllIos i am notification_alert", "sendToAllIos i am notification_title",
//                    "sendToAllIos i am msg_content", extraMap);
//
//
//            extraMap.clear();
//            extraMap.put("msg", "sendToRegistrationId");


        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("连接错误，稍后尝试" + e);
            }
            e.printStackTrace();
        }
    }

}
