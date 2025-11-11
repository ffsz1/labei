package com.vslk.lbgx.ui.message.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tongdaxing.erban.databinding.ListItemFriendBinding;
import com.vslk.lbgx.base.bindadapter.BaseAdapter;
import com.vslk.lbgx.base.bindadapter.BindingViewHolder;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.vslk.lbgx.utils.ImageLoadUtils;

/**
 * Created by chenran on 2017/10/3.
 */

public class FriendListAdapter extends BaseAdapter<NimUserInfo> {

    public FriendListAdapter(int layoutResId, int brid) {
        super(layoutResId, brid);
    }

    @Override
    protected void convert(BindingViewHolder helper, NimUserInfo item) {
        super.convert(helper, item);
        ListItemFriendBinding binding = (ListItemFriendBinding) helper.getBinding();

        binding.imageView.setOnClickListener(view -> {
            if ("Constants.OFFICIAL".equals(item.getAccount())) {
                return;
            }
            UserInfoActivity.start(mContext, JavaUtil.str2long(item.getAccount()));
        });


        int gender = item.getGenderEnum().getValue();
        ImageView ivGenger = helper.getView(R.id.iv_gender);
        if (gender == 1) {
            ivGenger.setVisibility(View.VISIBLE);
            ivGenger.setBackground(mContext.getResources().getDrawable(com.netease.nim.uikit.R.drawable.icon_man));
        } else if (gender == 2) {
            ivGenger.setVisibility(View.VISIBLE);
            ivGenger.setBackground(mContext.getResources().getDrawable(com.netease.nim.uikit.R.drawable.icon_woman));
        } else {
            ivGenger.setVisibility(View.INVISIBLE);
        }

        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(Long.valueOf(item.getAccount()),
                true);

        if (userInfo != null) {

            int experLevel = userInfo.getExperLevel();
            int charmLevel = userInfo.getCharmLevel();

            //等级和魅力值
            if (charmLevel > 0 || experLevel > 0) {
                binding.levelInfo.setVisibility(View.VISIBLE);
            } else {
                binding.levelInfo.setVisibility(View.GONE);
            }

            binding.levelInfo.setCharmLevel(charmLevel);

            binding.levelInfo.setExperLevel(experLevel);
            if (TextUtils.isEmpty(userInfo.getSignture())) {
                helper.setVisible(R.id.tv_user_id, false);
            } else {
                helper.setVisible(R.id.tv_user_id, true);
                helper.setText(R.id.tv_user_id, userInfo.getSignture());
            }

        } else {
            binding.tvUserId.setVisibility(View.INVISIBLE);
        }
//-------------------无用户UID和房间，进入房间功能未实现-----------------------
//        ImageView imgLiving = helper.getView(R.id.img_living_anim);
//        LinearLayout linLiving = helper.getView(R.id.lin_living);
//        imgLiving.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
//            @Override
//            public void onViewAttachedToWindow(View v) {
//                setPlayAnim(true, linLiving, imgLiving);
//            }
//
//            @Override
//            public void onViewDetachedFromWindow(View v) {
//
//            }
//        });

    }

    private void setPlayAnim(boolean roomState, LinearLayout linLiving, ImageView img) {
        if (roomState) {//在线或者正在播放语音时显示动画
            linLiving.setVisibility(View.VISIBLE);
            img.setVisibility(View.VISIBLE);
            AnimationDrawable animationDrawable = (AnimationDrawable) img.getBackground();
            if (!animationDrawable.isRunning()) {
                animationDrawable.start();
            }
        } else {
            linLiving.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
            img.setAnimation(null);
        }
    }
}
