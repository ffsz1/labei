package com.netease.nim.uikit.session.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.OnlineStateChangeListener;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.cache.FriendDataCache;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.model.ToolBarOptions;
import com.netease.nim.uikit.session.NimUser;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nim.uikit.session.fragment.MessageFragment;
import com.netease.nim.uikit.uinfo.UserInfoHelper;
import com.netease.nim.uikit.uinfo.UserInfoObservable;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * 点对点聊天界面
 * <p/>
 *
 * @author huangjun
 * @date 2015/2/1
 */
public class P2PMessageActivity extends BaseMessageActivity implements MessageFragment.OnFragmentInteractionListener {

    private boolean isResume = false;
    private TextView mTitle;
    //TextView tvToolbarTitle;

    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        setStatusBar();
        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
        displayOnlineState();
        registerObservers(true);
        registerOnlineStateChangeListener(true);
        findViewById(R.id.iv_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NimUser nimUser = new NimUser();
                nimUser.setSessionId(sessionId);
                EventBus.getDefault().post(nimUser);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            registerObservers(false);
            registerOnlineStateChangeListener(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    private void requestBuddyInfo() {
        // 显示自己的textview并且居中
        mTitle = getToolBar().findViewById(R.id.tv_toolbar_title);
        String userTitleName = UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P);
        if (StringUtils.isNotEmpty(userTitleName) && !userTitleName.equals(sessionId)) {
            mTitle.setText(userTitleName);
            return;
        }
        NimUserInfoCache.getInstance().getUserInfoFromRemote(sessionId, new RequestCallback<NimUserInfo>() {

            @Override
            public void onSuccess(NimUserInfo param) {
                mTitle.setText(param.getName());
                //tvToolbarTitle.setText(param.getName());
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
        //tvToolbarTitle.setText(userTitleName);
        //tvToolbarTitle.setVisibility(View.VISIBLE);
        Log.e("requestBuddyInfo", "requestBuddyInfo: " + userTitleName);
    }


    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
    }

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {

        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            mTitle.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            mTitle.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            mTitle.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            mTitle.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    private UserInfoObservable.UserInfoObserver uinfoObserver;

    OnlineStateChangeListener onlineStateChangeListener = new OnlineStateChangeListener() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            // 更新 toolbar
            if (accounts.contains(sessionId)) {
                // 按照交互来展示
                displayOnlineState();
            }
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKit.enableOnlineState()) {
            return;
        }
        if (register) {
            NimUIKit.addOnlineStateChangeListeners(onlineStateChangeListener);
        } else {
            NimUIKit.removeOnlineStateChangeListeners(onlineStateChangeListener);
        }
    }

    private void displayOnlineState() {
        if (!NimUIKit.enableOnlineState()) {
            return;
        }
        String detailContent = NimUIKit.getOnlineStateContentProvider().getDetailDisplay(sessionId);
        setSubTitle(detailContent);
    }

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }

        UserInfoHelper.registerObserver(uinfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            UserInfoHelper.unregisterObserver(uinfoObserver);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {

            //: 2018/4/12
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }

        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                Toast.makeText(P2PMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
            }
//            else {
//                Toast.makeText(P2PMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected MessageFragment fragment() {
        MessageFragment fragment = new MessageFragment();
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        }
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new ToolBarOptions();
        setToolBar(R.id.toolbar, options.navigateId);
        if (options.titleId != 0) {
            mTitle.setText(options.titleId);
        }
        if (!TextUtils.isEmpty(options.titleString)) {
            mTitle.setText(options.titleString);
        }
    }

    @Override
    public void onPermission() {
        AlertDialog dialog = new AlertDialog.Builder(P2PMessageActivity.this)
                .setMessage("当前应用缺少必要权限,请点击设置跳转权限页面")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", P2PMessageActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        P2PMessageActivity.this.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_BASE_APPLICATION);
        dialog.show();
        //放在show()之后，不然有些属性是没有效果的，比如height和width
        Window dialogWindow = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 设置宽度
        p.width = (int) (d.getWidth() * 0.95); // 宽度设置为屏幕的0.95
        p.gravity = Gravity.CENTER;//设置位置
        //p.alpha = 0.8f;//设置透明度
        dialogWindow.setAttributes(p);
    }
}
