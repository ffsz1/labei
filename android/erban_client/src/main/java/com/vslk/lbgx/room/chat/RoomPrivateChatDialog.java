package com.vslk.lbgx.room.chat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.vslk.lbgx.im.actions.GiftAction;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nim.uikit.session.fragment.MessageFragment;
import com.netease.nim.uikit.uinfo.UserInfoHelper;
import com.netease.nim.uikit.uinfo.UserInfoObservable;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.util.DisplayUtils;

import java.util.ArrayList;

/**
 * 房间私聊
 */
public class RoomPrivateChatDialog extends BaseDialogFragment implements View.OnClickListener {
    protected String sessionId;
    private MessageFragment messageFragment;
    private SessionCustomization customization;
    private UserInfoObservable.UserInfoObserver uinfoObserver;
    private TextView tvToolbarTitle;
    private ImageView ivCloss;

    public static RoomPrivateChatDialog newInstance(String sessionId) {
        RoomPrivateChatDialog privateChat = new RoomPrivateChatDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Extras.EXTRA_ACCOUNT, sessionId);
        privateChat.setArguments(bundle);
        return privateChat;
    }

    public RoomPrivateChatDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            sessionId = savedInstanceState.getString(Extras.EXTRA_ACCOUNT, sessionId);
        } else {
            if (getArguments() != null)
                sessionId = getArguments().getString(Extras.EXTRA_ACCOUNT, "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        // setup window and width
        View view = inflater.inflate(R.layout.dialog_room_private_chat, window.findViewById(android.R.id.content), false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        setCancelable(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View nav = view.findViewById(R.id.v_replace_nav);
        int navHeight = DisplayUtils.getNavigationBarHeight(getContext());
        if (navHeight > 0){
            LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) nav.getLayoutParams();
            ll.height = navHeight;
            nav.setLayoutParams(ll);
        }
        tvToolbarTitle = view.findViewById(R.id.tv_list_data_title);
        ivCloss = view.findViewById(R.id.iv_close_dialog);
        ivCloss.setOnClickListener(this);
        messageFragment = new MessageFragment();
        Bundle arguments = getArguments();
        customization = new SessionCustomization();
        ArrayList<BaseAction> actions = new ArrayList<>();
        actions.add(new GiftAction());
        customization.actions = actions;
        customization.withSticker = true;
        arguments.putSerializable(Extras.EXTRA_CUSTOMIZATION,customization);
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        messageFragment.setArguments(arguments);
        messageFragment.setContainerId(R.id.msg_fragment_container);
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_room_msg_container, messageFragment).commitAllowingStateLoss();
        requestBuddyInfo();
        registerObservers(true);
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }

    }

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = accounts -> {
                if (accounts.contains(sessionId)) {
                    requestBuddyInfo();
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

    private void requestBuddyInfo() {
        // 显示自己的textview并且居中
        String userTitleName = UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P);
        if (tvToolbarTitle != null)
            tvToolbarTitle.setText(userTitleName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (messageFragment != null) {
            messageFragment.onActivityResult(requestCode, resultCode, data);
        }

        if (customization != null) {
            customization.onActivityResult(getActivity(), requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

}
