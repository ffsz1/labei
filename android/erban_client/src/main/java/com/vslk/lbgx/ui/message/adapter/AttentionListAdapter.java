package com.vslk.lbgx.ui.message.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.AttentionInfo;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;

/**
 * <p> 关注列表 </p>
 *
 * @author Administrator
 * @date 2017/11/17
 */
public class AttentionListAdapter extends BaseQuickAdapter<AttentionInfo, BaseViewHolder> {
    List<AttentionInfo> attentionInfoList;

    public AttentionListAdapter(List<AttentionInfo> attentionInfoList) {
        super(R.layout.attention_item, attentionInfoList);
        this.attentionInfoList = attentionInfoList;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final AttentionInfo attentionInfo) {
        if (attentionInfo == null) {
            return;
        }
        //隐藏官方账号去找Ta
        DrawableTextView drawableTextView = baseViewHolder.getView(R.id.find_him_layout);
        if (attentionInfo.getUid() == 500001L) {
            drawableTextView.setVisibility(View.GONE);
        } else {
            drawableTextView.setVisibility(View.VISIBLE);
        }

        baseViewHolder.setText(R.id.mt_userName, attentionInfo.getNick())

                .setOnClickListener(R.id.find_him_layout, v -> {
                    if (rylListener != null) {
                        rylListener.findHimListeners(attentionInfo);
                    }
                })
                .setOnClickListener(R.id.rly, v -> {
                    if (rylListener != null) {
                        rylListener.rylListeners(attentionInfo);
                    }
                });
        int gender = attentionInfo.getGender();
        ImageView ivGenger = baseViewHolder.getView(R.id.iv_gender);
        if (gender == 1) {
            ivGenger.setBackground(mContext.getResources().getDrawable(com.netease.nim.uikit.R.drawable.icon_man));
            ivGenger.setVisibility(View.VISIBLE);
        } else if (gender == 2) {
            ivGenger.setBackground(mContext.getResources().getDrawable(com.netease.nim.uikit.R.drawable.icon_woman));
            ivGenger.setVisibility(View.VISIBLE);
        } else {
            ivGenger.setVisibility(View.INVISIBLE);

        }


        ImageView imageView = baseViewHolder.getView(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.start(mContext, attentionInfo.getUid());
            }
        });

        ImageLoadUtils.loadCircleImage(mContext, attentionInfo.getAvatar(), imageView, R.drawable.ic_default_avatar);

        UserInfo userInfo =
                CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(attentionInfo.getUid(), true);

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
        } else {
            baseViewHolder.setVisible(R.id.tv_user_id, false);
        }
    }

    private onClickListener rylListener;

    public interface onClickListener {
        void rylListeners(AttentionInfo attentionInfo);

        void findHimListeners(AttentionInfo attentionInfo);
    }

    public void setRylListener(onClickListener onClickListener) {
        rylListener = onClickListener;
    }
}
