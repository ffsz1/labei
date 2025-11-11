package com.vslk.lbgx.utils;

import android.content.Context;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.popupmenu.PopupMenuItem;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import java.util.List;

/**
 * UIKit自定义消息界面用法展示类
 */
public class SessionHelper {

    private static final int ACTION_HISTORY_QUERY = 0;
    private static final int ACTION_SEARCH_MESSAGE = 1;
    private static final int ACTION_CLEAR_MESSAGE = 2;

    private static SessionCustomization p2pCustomization;
    private static SessionCustomization teamCustomization;
    private static SessionCustomization myP2pCustomization;
    private static List<PopupMenuItem> menuItemList;

    public static void init() {
        // 注册自定义消息附件解析器
//        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new IMCustomAttachParser());

        // 注册各种扩展消息类型的显示ViewHolder
//        registerViewHolders();

        // 设置会话中点击事件响应处理
//        setSessionListener();

        // 注册消息转发过滤器
//        registerMsgForwardFilter();

        // 注册消息撤回过滤器
//        registerMsgRevokeFilter();

        // 注册消息撤回监听器
//        registerMsgRevokeObserver();

//        NimUIKit.setCommonP2PSessionCustomization(getP2pCustomization());

//        NimUIKit.setCommonTeamSessionCustomization(getTeamCustomization());
    }

    public static void startP2PSession(Context context, String account) {
        startP2PSession(context, account, null);
    }

    public static void startP2PSession(Context context, String account, IMMessage anchor) {
        if (!String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()).equals(account)) {
            NimUIKit.startP2PSession(context, account, anchor);
        } else {
//            NimUIKit.startChatting(context, account, SessionTypeEnum.P2P, getMyP2pCustomization(), anchor);
        }
    }

    public static void startTeamSession(Context context, String tid) {
        startTeamSession(context, tid, null);
    }

    public static void startTeamSession(Context context, String tid, IMMessage anchor) {
        NimUIKit.startTeamSession(context, tid, anchor);
    }

    /**
     * 打开私聊界面
     */
    public static void startPrivateChat(Context context, long userId) {
        NimUserInfo nimUserInfo = NimUserInfoCache.getInstance().getUserInfo(userId + "");
        if (nimUserInfo != null) {
            NimUIKit.startP2PSession(context, userId + "");
        } else {
            NimUserInfoCache.getInstance().getUserInfoFromRemote(userId + "", new RequestCallbackWrapper<NimUserInfo>() {
                @Override
                public void onResult(int i, NimUserInfo nimUserInfo, Throwable throwable) {
                    if (i == 200) {
                        NimUIKit.startP2PSession(context, userId + "");
                    } else {
                        SingleToastUtil.showToast("进入私聊失败！");
                    }
                }
            });
        }
    }

    /**
     * 获发送关注消息用户uid
     *
     * @return
     */
    public static long getSendFocusMsgUid() {
        return BasicConfig.INSTANCE.isDebuggable() ? 100711 : 1094274;
    }

    /**
     * 线上大厅官方用户uid
     *
     * @return
     */
    public static long getChatHallOfficialUid() {
        return BasicConfig.INSTANCE.isDebuggable() ? 100712 : 1094804;
    }

}
