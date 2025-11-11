package com.tongdaxing.xchat_core.im.user;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;

import java.util.List;
import java.util.Map;

/**
 * Created by zhouxiangfeng on 2017/5/21.
 */

public class IMUserCoreImpl extends AbstractBaseCore implements IIMUserCore {

    @Override
    public List<NimUserInfo> getUserInfoList(List<String> accounts) {
        return NIMClient.getService(UserService.class).getUserInfoList(accounts);
    }

    @Override
    public NimUserInfo getUserInfo(String account) {
        return NIMClient.getService(UserService.class).getUserInfo(account);
    }

    @Override
    public List<NimUserInfo> getAllUserInfo() {
        return NIMClient.getService(UserService.class).getAllUserInfo();
    }

    @Override
    public void requestUserInfoList(List<String> accounts) {
        NIMClient.getService(UserService.class).fetchUserInfo(accounts)
                .setCallback(new RequestCallback<List<NimUserInfo>>() {
                    @Override
                    public void onSuccess(List<NimUserInfo> userInfos) {
                        //:转换成UserInfo对象存入本地

                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }


    @Override
    public void updateUserInfo(Map<UserInfoFieldEnum, Object> fields) {
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int i, Void aVoid, Throwable throwable) {

                    }
                });
    }

    @Override
    public void registerUserInfoChanged(Observer<List<NimUserInfo>> userInfoUpdateObserver, boolean register) {
// 注册/注销观察者
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(userInfoUpdateObserver, register);
//// 用户资料变更观察者
    }


}
