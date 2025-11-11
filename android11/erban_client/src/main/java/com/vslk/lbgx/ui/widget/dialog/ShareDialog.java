package com.vslk.lbgx.ui.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.vslk.lbgx.room.avroom.activity.ShareFansActivity;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * @author xiaoyu
 * @date 2017/12/13
 */

public class ShareDialog extends BottomSheetDialog implements View.OnClickListener {
    private Context context;
    private String textTitle = "分享至";
    private int gravity = Gravity.START;
    //    private TextView mTitle;
    private TextView tvName;
    private TextView tvWeixin;
    private TextView tvWeixinpy;
    private TextView tvQq;
    private TextView tvQqZone;
    private LinearLayout llInvite;
    private TextView tvInvite;//新增邀请好友
    private TextView tvCancel;
    private static final String TAG = "ShareDialog";
    private OnShareDialogItemClick onShareDialogItemClick;
    private boolean hasInvite = false;//是否有邀请好友 默认无

    public ShareDialog(Context context) {
        super(context, R.style.ErbanBottomSheetDialog);
        this.context = context;
    }

    public void setOnShareDialogItemClick(OnShareDialogItemClick onShareDialogItemClick) {
        this.onShareDialogItemClick = onShareDialogItemClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        setCanceledOnTouchOutside(true);
//        mTitle = findViewById(R.id.title);
        tvName = (TextView) findViewById(R.id.tv_title);
        tvWeixin = (TextView) findViewById(R.id.tv_weixin);
        tvWeixinpy = (TextView) findViewById(R.id.tv_weixinpy);
        tvQq = (TextView) findViewById(R.id.tv_qq);
        tvQqZone = (TextView) findViewById(R.id.tv_qq_zone);
        llInvite = findViewById(R.id.ll_share2);
        tvInvite = findViewById(R.id.tv_invite_friend);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);

        tvWeixin.setOnClickListener(this);
        tvWeixinpy.setOnClickListener(this);
        tvQq.setOnClickListener(this);
        tvQqZone.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvInvite.setOnClickListener(this);
//        mTitle.setText(textTitle);
//        mTitle.setGravity(gravity);
        if (hasInvite) {
            llInvite.setVisibility(View.VISIBLE);
        }
        FrameLayout bottomSheet = (FrameLayout) findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(false);
            BottomSheetBehavior.from(bottomSheet).setPeekHeight(ConvertUtils.dp2px(310));
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }

    public void setName(String name) {
        if (!StringUtils.isEmpty(name)) {
            tvName.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_weixin:
                if (onShareDialogItemClick != null) {
                    onShareDialogItemClick.onSharePlatformClick(ShareSDK.getPlatform(Wechat.NAME));
                }
                dismiss();
                break;
            case R.id.tv_weixinpy:
                if (onShareDialogItemClick != null) {
                    onShareDialogItemClick.onSharePlatformClick(ShareSDK.getPlatform(WechatMoments.NAME));
                }
                dismiss();
                break;
            case R.id.tv_qq:
                if (onShareDialogItemClick != null) {
                    onShareDialogItemClick.onSharePlatformClick(ShareSDK.getPlatform(QQ.NAME));
                }
                dismiss();
                break;
            case R.id.tv_qq_zone:
                if (onShareDialogItemClick != null) {
                    onShareDialogItemClick.onSharePlatformClick(ShareSDK.getPlatform(QZone.NAME));
                }
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_invite_friend:
                //
                if (context == null)
                    return;
                context.startActivity(new Intent(context, ShareFansActivity.class));
                dismiss();
                break;
            default:
                break;
        }
    }

    public boolean isHasInvite() {
        return hasInvite;
    }

    public void setHasInvite(boolean hasInvite) {
        this.hasInvite = hasInvite;
    }

    public interface OnShareDialogItemClick {
        void onSharePlatformClick(Platform platform);
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
