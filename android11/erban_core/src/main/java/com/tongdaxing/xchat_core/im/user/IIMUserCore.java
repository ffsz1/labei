package com.tongdaxing.xchat_core.im.user;

import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import java.util.List;
import java.util.Map;

/**
 * Created by zhouxiangfeng on 2017/5/21.
 */

public interface IIMUserCore extends IBaseCore {
    List<NimUserInfo> getUserInfoList(List<String> accounts);

    NimUserInfo getUserInfo(String account);

    List<NimUserInfo> getAllUserInfo();

    void requestUserInfoList(List<String> accounts);

    /**
     * SDK对部分字段进行格式校验：

     邮箱：必须为合法邮箱
     手机号：必须为合法手机号 如13588888888、+(86)-13055555555
     生日：必须为"yyyy-MM-dd"格式
     更新头像可选方案：

     先将头像图片上传至网易云通信云存储上（见 NosService ) ，上传成功后可以得到 url 。
     更新个人资料的头像字段，保存 url 。
     此外，开发者也可以自行存储头像，仅将 url 更新到个人资料上。
     * @param fields
     */
    void updateUserInfo(Map<UserInfoFieldEnum, Object> fields);

    /**
     * 监听用户资料变更
     用户资料除自己之外，不保证其他用户资料实时更新。其他用户数据更新时机为：

     调用 fetchUserInfo 方法刷新用户
     收到此用户发来消息（如果消息发送者有资料变更，SDK 会负责更新并通知）
     程序再次启动，此时会同步好友信息
     由于用户资料变更需要跨进程异步调用，开发者最好能在第三方 APP 中做好用户资料缓存，查询用户资料时都从本地缓存中访问。在用户资料有变化时，SDK 会告诉注册的观察者，此时，第三方 APP 可更新缓存，并刷新界面。 代码示例如下：
     * @param userInfoUpdateObserver
     * @param register
     */
    void registerUserInfoChanged(Observer<List<NimUserInfo>> userInfoUpdateObserver, boolean register);

}
