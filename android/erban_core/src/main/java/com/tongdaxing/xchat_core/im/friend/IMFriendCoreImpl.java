package com.tongdaxing.xchat_core.im.friend;

import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.constant.FriendFieldEnum;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.netease.nimlib.sdk.friend.model.BlackListChangedNotify;
import com.netease.nimlib.sdk.friend.model.FriendChangedNotify;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.xchat_core.im.message.IIMMessageCore;
import com.tongdaxing.xchat_core.praise.IPraiseCore;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;
import java.util.Map;

/**
 * Created by zhouxiangfeng on 2017/5/18.
 */

public class IMFriendCoreImpl extends AbstractBaseCore implements IIMFriendCore {

    public IMFriendCoreImpl() {
        registerFriendsChanged(friendChangedNotifyObserver);
        registerBlackListChanged(blackListChangedNotifyObserver);
    }

    private Observer<FriendChangedNotify> friendChangedNotifyObserver = new Observer<FriendChangedNotify>() {
        @Override
        public void onEvent(FriendChangedNotify friendChangedNotify) {
            List<String> deleteFriends = friendChangedNotify.getDeletedFriends();
            if (deleteFriends != null && deleteFriends.size() > 0) {
                for (int i = 0; i < deleteFriends.size(); i++) {
                    CoreManager.getCore(IIMMessageCore.class).deleteRecentContact(deleteFriends.get(i));
                }
            }
            notifyClients(IIMFriendCoreClient.class, IIMFriendCoreClient.METHOD_ON_FRIEND_LIST_UPDATE, getMyFriends());
        }
    };

    private Observer<BlackListChangedNotify> blackListChangedNotifyObserver = new Observer<BlackListChangedNotify>() {
        @Override
        public void onEvent(BlackListChangedNotify blackListChangedNotify) {

        }
    };

    @Override
    public List<NimUserInfo> getMyFriends() {
        List<NimUserInfo> nimUserInfos = NimUserInfoCache.getInstance().getAllUsersOfMyFriend();
        return nimUserInfos;
    }

    @Override
    public List<String> getBlackList() {
        List<String> accounts = NIMClient.getService(FriendService.class).getBlackList();
        return accounts;
    }

    @Override
    public List<String> getMuteList() {
        List<String> accounts = NIMClient.getService(FriendService.class).getMuteList();
        return accounts;
    }

    @Override
    public void passRequestFriend(String account, boolean isPass) {
        // 通过对方的好友请求
        NIMClient.getService(FriendService.class).ackAddFriendRequest(account, isPass);
    }

    @Override
    public boolean isMyFriend(String uid) {
        return NIMClient.getService(FriendService.class).isMyFriend(uid);
    }

    @Override
    public void addFriend(String uid, String tip) {
        // 发起好友验证请求
        final VerifyType verifyType = VerifyType.VERIFY_REQUEST;
//        String msg = "好友请求附言";
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(uid, verifyType, tip)).setCallback(new RequestCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {

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
    public void deleteFriend(String uid) {
        NIMClient.getService(FriendService.class).deleteFriend(uid).setCallback(new RequestCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
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
    public void requestFriend(String uid) {

    }

    @Override
    public void addToBlackList(final String uid) {
        NIMClient.getService(FriendService.class).addToBlackList(uid).setCallback(new RequestCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                try {
                    long cancelUid = Long.valueOf(uid);
                    CoreManager.getCore(IPraiseCore.class).cancelPraise(cancelUid, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifyClients(IIMFriendCoreClient.class, IIMFriendCoreClient.addBlackListSuccess);
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
    public void removeFromBlackList(String uid) {
        NIMClient.getService(FriendService.class).removeFromBlackList(uid).setCallback(new RequestCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                notifyClients(IIMFriendCoreClient.class, IIMFriendCoreClient.removeBlackListSuccess);
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
    public boolean isUserInBlackList(String uid) {
        boolean black = NIMClient.getService(FriendService.class).isInBlackList(uid);
        return black;
    }

    @Override
    public void updateFriendFields(String tempnick, Map<FriendFieldEnum, Object> map) {
       // 更新备注名
        NIMClient.getService(FriendService.class).updateFriendFields(tempnick, map).setCallback(new RequestCallback<Void>() {

            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    private void registerFriendsChanged(Observer<FriendChangedNotify> friendChangedNotifyObserver) {
        NIMClient.getService(FriendServiceObserve.class).observeFriendChangedNotify(friendChangedNotifyObserver, true);
    }

    private void registerBlackListChanged(Observer<BlackListChangedNotify> blackListChangedNotifyObserver) {
        NIMClient.getService(FriendServiceObserve.class)
                .observeBlackListChangedNotify(blackListChangedNotifyObserver, true);
    }

    @Override
    public void setMessageNotify(String account, final boolean checkState) {
        NIMClient.getService(FriendService.class).setMessageNotify(account, checkState)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        if (checkState) {
//                            Toast.makeText(UserProfileActivity.this, "开启消息提醒", Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(UserProfileActivity.this, "关闭消息提醒", Toast.LENGTH_SHORT).show();
                        }
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
    public boolean isNeedMessageNotify(String account) {
        boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
        return notice;
    }
}
