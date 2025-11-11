package com.juxiao.xchat.manager.external.jpush;

import java.util.Map;

/**
 * 极光推送管理接口
 */
public interface JpushManager {
    /**
     * 推送给设备标识参数的用户，推送消息调用该方法
     *
     * @param registrationId    设备标识
     * @param notificationAlert 通知内容
     * @param notificationTitle 通知内容标题
     * @param msgTitle          消息内容标题
     * @param msgContent        消息内容
     * @param extrasParam       扩展字段
     * @return 0推送失败，1推送成功
     */
    int sendToRegistrationId(String registrationId, String notificationAlert, String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasParam);

    /**
     * 推送给所有的安卓端
     *
     * @param notificationAlert
     * @param notificationTitle
     * @param msgTitle
     * @param msgContent
     * @param extrasParam
     * @return
     */
    int sendToAllAndroid(String notificationAlert, String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasParam);

    /**
     * 推送给所有的ios端
     *
     * @param notificationTitle
     * @param msgTitle
     * @param msgContent
     * @param extrasParam
     * @return
     */
    int sendToAllIos(String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasParam);

    /**
     * 推送所有用户
     *
     * @param notificationAlert
     * @param notificationTitle 大标题题
     * @param msgTitle 小标题
     * @param msgContent
     * @param extrasParam
     * @return
     */
    void sendToAll(String notificationAlert, String notificationTitle, String msgTitle, String msgContent, Map<String, String> extrasParam);

}
