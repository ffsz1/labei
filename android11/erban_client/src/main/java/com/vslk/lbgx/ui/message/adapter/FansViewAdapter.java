package com.vslk.lbgx.ui.message.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.im.friend.IIMFriendCore;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.FansInfo;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;

/**
 * @author chenran
 * @date 2017/10/2
 */

public class FansViewAdapter extends BaseQuickAdapter<FansInfo, BaseViewHolder> {
    private Context mContext;

    public FansViewAdapter(List<FansInfo> fansInfoList, Context mContext) {
        super(R.layout.fans_list_item, fansInfoList);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final FansInfo fansInfo) {
        if (fansInfo == null) {
            return;
        }
        baseViewHolder.setText(R.id.mt_userName, fansInfo.getNick())
                .setVisible(R.id.view_line, baseViewHolder.getLayoutPosition() != getItemCount() - 1)
                .addOnClickListener(R.id.rly)
                .addOnClickListener(R.id.attention_img);

        DrawableTextView attention = baseViewHolder.getView(R.id.attention_img);
        boolean state = CoreManager.getCore(IIMFriendCore.class).isMyFriend(String.valueOf(fansInfo.getUid()));
        attention.setText(state ? "互相关注" : "关注");
        attention.setTextColor(state ? mContext.getResources().getColor(R.color.color_grey_999999) :
                mContext.getResources().getColor(R.color.white));

        attention.setBackground(state ? mContext.getResources().getDrawable(R.drawable.bg_msg_guanzhu_on) :
                mContext.getResources().getDrawable(R.drawable.bg_msg_guanzhu_off));

        attention.setSelected(state);
        fansInfo.setMyFriend(state);
        ImageView avatar = baseViewHolder.getView(R.id.imageView);
        ImageLoadUtils.loadCircleImage(mContext, fansInfo.getAvatar(), avatar, R.drawable.ic_default_avatar);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.start(mContext, fansInfo.getUid());
            }
        });


        UserInfo userInfo =
                CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(fansInfo.getUid(), true);

        ImageView ivGenger = baseViewHolder.getView(R.id.iv_gender);

        if (userInfo != null) {

//            int experLevel = userInfo.getExperLevel();
//            int charmLevel = userInfo.getCharmLevel();
//
//            LevelView levelView = baseViewHolder.getView(R.id.level_info);
//            //等级和魅力值
//            if (charmLevel > 0 || experLevel > 0) {
//                levelView.setVisibility(View.VISIBLE);
//            } else {
//                levelView.setVisibility(View.GONE);
//            }
//
//            levelView.setCharmLevel(charmLevel);
//
//            levelView.setExperLevel(experLevel);

            baseViewHolder.setText(R.id.tv_user_id, "ID:" + userInfo.getErbanNo());

            int gender = userInfo.getGender();

            if (gender == 1) {
                ivGenger.setBackground(mContext.getResources().getDrawable(com.netease.nim.uikit.R.drawable.icon_man));
                ivGenger.setVisibility(View.VISIBLE);
            } else if (gender == 2) {
                ivGenger.setBackground(mContext.getResources().getDrawable(com.netease.nim.uikit.R.drawable.icon_woman));
                ivGenger.setVisibility(View.VISIBLE);
            } else {
                ivGenger.setVisibility(View.INVISIBLE);

            }


        } else {
            baseViewHolder.setVisible(R.id.tv_user_id,
                    false);
            ivGenger.setVisibility(View.INVISIBLE);
        }


    }
}
