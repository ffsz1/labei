package com.vslk.lbgx.room.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * Created by chenran on 2017/8/3.
 */

public class AuctionFinishDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private long auctionUid, beAuctionUid;
    private ImageView close;
    private TextView userNick, voiceActorNick;
    private CircleImageView userAvatar, voiceActorAvatar;

    public AuctionFinishDialog(Context context, boolean cancelable,
                               DialogInterface.OnCancelListener cancelListener, long auctionUid, long beAuctionUid) {
        super(context, cancelable, cancelListener);
        // Auto-generated constructor stub
        this.context = context;
        this.auctionUid = auctionUid;
        this.beAuctionUid = beAuctionUid;
    }

    public AuctionFinishDialog(Context context, long auctionUid, long beAuctionUid) {
        super(context, R.style.BottomSelectDialog);
        // Auto-generated constructor stub
        this.context = context;
        this.auctionUid = auctionUid;
        this.beAuctionUid = beAuctionUid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoreManager.addClient(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_auction_finish);

        close = (ImageView) findViewById(R.id.close_btn);
        userAvatar = (CircleImageView) findViewById(R.id.user_avatar);
        voiceActorAvatar = (CircleImageView) findViewById(R.id.voice_actor_avatar);
        userNick = (TextView) findViewById(R.id.user_nick);
        voiceActorNick = (TextView) findViewById(R.id.voice_actor_nick);

        close.setOnClickListener(this);
        userAvatar.setOnClickListener(this);
        voiceActorAvatar.setOnClickListener(this);

        initView();
        setDialogShowAttributes(context, this);
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfo(UserInfo info) {
        if (info.getUid() == auctionUid || info.getUid() == beAuctionUid) {
            initView();
        }
    }

    private void initView() {
        UserInfo auctionInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(auctionUid);
        UserInfo beAuctionInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(beAuctionUid);

        if (auctionInfo != null) {
            userNick.setText(auctionInfo.getNick());
            ImageLoadUtils.loadAvatar(getContext(), auctionInfo.getAvatar(), userAvatar);
        }

        if (beAuctionInfo != null) {
            voiceActorNick.setText(beAuctionInfo.getNick());
            ImageLoadUtils.loadAvatar(getContext(), beAuctionInfo.getAvatar(), voiceActorAvatar);
        }
    }

    public static void setDialogShowAttributes(Context context ,Dialog dialog){
        WindowManager winManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);//获取窗口管理�?
        int mScreenWidth = winManager.getDefaultDisplay().getWidth();//获取屏幕宽度,将此宽度设为要显示的Dialog窗口的宽�?
        Window mWindow = dialog.getWindow();//获取Dialog窗口
        mWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams Params = mWindow.getAttributes();
        Params.width = WindowManager.LayoutParams.MATCH_PARENT;
        Params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindow.setAttributes(Params);
        mWindow.setGravity(Gravity.CENTER);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CoreManager.removeClient(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close_btn:
                dismiss();
                break;
            case R.id.user_avatar:
                dismiss();
                UserInfoActivity.start((BaseActivity)context, auctionUid);
                break;
            case R.id.voice_actor_avatar:
                dismiss();
                UserInfoActivity.start((BaseActivity)context, beAuctionUid);
                break;
        }
    }
}
