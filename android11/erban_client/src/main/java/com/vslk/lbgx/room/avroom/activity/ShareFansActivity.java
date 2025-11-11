package com.vslk.lbgx.room.avroom.activity;


import static com.netease.nimlib.sdk.msg.constant.SessionTypeEnum.P2P;
import static com.tongdaxing.xchat_core.im.custom.bean.CustomAttachment.CUSTOM_MSG_SHARE_FANS;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.base.fragment.BaseListFragment;
import com.vslk.lbgx.room.avroom.adapter.ShareFansAdapter;
import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.im.custom.bean.ShareFansAttachment;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.ArrayList;
import java.util.List;


public class ShareFansActivity extends BaseActivity {

    private TextView moreOption;
    private ShareFansAdapter shareFansAdapter;
    private View buSubmit;
    private int sendSuccessCount = 0;
    private int sendErrorCount = 0;
    private int sendCount = 0;
    private View llMoreSelectBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_fans);
        initTitle();

        BaseListFragment baseListFragment = new BaseListFragment();

        shareFansAdapter = new ShareFansAdapter(new ArrayList<>());
        shareFansAdapter.itemAction = uid -> sendInvitationMsg(uid, true);

        baseListFragment
                .setPageSize(50)
                .setEmptyStr("没有粉丝")
                .setShortUrl(UriProvider.getFansList())
                .setOtherParams(Json.parse("uid:" + CoreManager.getCore(IAuthCore.class).getCurrentUid()))
                .setAdapter(shareFansAdapter)
                .setDataFilter(json -> json.json_ok("data").jlist("fansList"));


        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_base_list, baseListFragment);
        fragmentTransaction.commit();

        buSubmit = findViewById(R.id.bu_submit);
        buSubmit.setOnClickListener(v -> submit());
        llMoreSelectBg = findViewById(R.id.ll_more_select_bg);
    }

    private void submit() {
        if (shareFansAdapter == null) {
            return;
        }
        List<Json> data = shareFansAdapter.getData();
        List<Long> uids = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Json json = data.get(i);
            if (json.boo("select") && shareFansAdapter.sendHistory.num(json.str("uid")) != 1) {
                uids.add(json.num_l("uid"));
            }
        }

        sendCount = uids.size();
        if (sendCount == 0) {
            return;
        }
        sendErrorCount = 0;
        sendSuccessCount = 0;
        for (long l : uids) {
            sendInvitationMsg(l, false);
        }
    }

    private void initTitle() {
        initTitleBar("邀请好友");
        moreOption = new TextView(this);
        moreOption.setTextColor(Color.BLACK);
        moreOption.setText("多选");
        moreOption.setOnClickListener(v -> {
            if (shareFansAdapter != null) {
                boolean b = !shareFansAdapter.isMoreOption();
                shareFansAdapter.setMoreOption(b);
                llMoreSelectBg.setVisibility(b ? View.VISIBLE : View.GONE);
                moreOption.setText(!b ? "多选" : "取消");
            }
        });
        mTitleBar.mRightLayout.addView(moreOption);
    }

    private void sendInvitationMsg(long uid, boolean needToast) {
        IMMessage customMessage = createCustomMessage(uid);
        if (customMessage == null) {
            return;
        }
        InvocationFuture<Void> voidInvocationFuture = NIMClient.getService(MsgService.class).sendMessage(customMessage, false);

        voidInvocationFuture.setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                if (needToast) {
                    toast("发送成功");
                } else {
                    markSendCount(1);
                }
                if (shareFansAdapter != null) {
                    shareFansAdapter.sendHistory.set(uid + "", "1");
                    if (needToast) {
                        shareFansAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailed(int code) {
                if (needToast) {
                    toast("发送失败");
                } else {
                    markSendCount(0);
                }
            }

            @Override
            public void onException(Throwable exception) {
                if (needToast) {
                    toast("发送失败");
                } else {
                    markSendCount(0);
                }
            }
        });
    }


    private void markSendCount(int i) {
        if (i == 0) {
            sendErrorCount++;
        } else {
            sendSuccessCount++;
        }

        if (sendErrorCount + sendSuccessCount >= sendCount) {
            if (sendErrorCount > 0 && sendSuccessCount > 0) {
                toast("已经发送完毕,部分用户因发送太频繁未成功");
            } else if (sendSuccessCount > 0 && sendErrorCount == 0) {
                toast("发送成功");
            } else if (sendSuccessCount == 0) {
                toast("发送失败");
            }

            sendErrorCount = 0;
            sendSuccessCount = 0;
            finish();
        }
    }

    // 新版IM 自定义的私聊消息需要特别处理
    protected IMMessage createCustomMessage(long uid) {
        ShareFansAttachment shareFansAttachment = new ShareFansAttachment(CUSTOM_MSG_SHARE_FANS, CUSTOM_MSG_SHARE_FANS);
        RoomInfo mCurrentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (mCurrentRoomInfo == null) {
            return null;
        }
        Json json = new Json();


        UserInfo cacheUserInfoByUid = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(mCurrentRoomInfo.getUid());
        String avatar = UriProvider.JAVA_WEB_URL +  "/home/images/logo.png";
        if (cacheUserInfoByUid == null) {
            UserInfo cacheLoginUserInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
            if (cacheLoginUserInfo != null) {
                avatar = cacheLoginUserInfo.getAvatar();
            }
        } else {
            avatar = cacheUserInfoByUid.getAvatar();
        }


        String title = mCurrentRoomInfo.getTitle() + "";
        if (title.length() > 10) {
            title = title.substring(0, 10) + "...";
        }

        String titleContent = "我邀请你参加【" + title + "】的房间，快来吧！";
        json.set("title", titleContent);
        json.set("avatar", avatar);
        json.set("uid", mCurrentRoomInfo.getUid());
        json.set("bg", avatar);
        shareFansAttachment.setParams(json + "");
        IMMessage customMessage = MessageBuilder.createCustomMessage(uid + "", P2P, shareFansAttachment);
        customMessage.setContent(json + "");
        return customMessage;
    }
}
