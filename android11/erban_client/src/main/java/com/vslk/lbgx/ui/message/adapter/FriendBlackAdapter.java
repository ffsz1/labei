package com.vslk.lbgx.ui.message.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * @author huangmeng1
 * @date 2018/1/18
 */

public class FriendBlackAdapter extends BaseQuickAdapter<NimUserInfo, BaseViewHolder> {

    //声明自定义的监听接口
    private OnRecyclerItemClickListener monItemClickListener;

    //提供set方法供Activity或Fragment调用
    public void setRecyclerItemClickListener(OnRecyclerItemClickListener listener) {
        monItemClickListener = listener;
    }

    public FriendBlackAdapter() {
        super(R.layout.item_rv_friend_black_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, NimUserInfo item) {
        helper.setText(R.id.mt_userName, item.getName())
                .addOnClickListener(R.id.remove);
        ImageLoadUtils.loadCircleImage(mContext, item.getAvatar(), helper.getView(R.id.imageView), R.drawable.ic_default_avatar);

        UserInfo userInfo =
                CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(Long.valueOf(item.getAccount()), true);

        ImageView ivGenger = helper.getView(R.id.iv_gender);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monItemClickListener != null) {

                    monItemClickListener.onItemClick(helper.getLayoutPosition(), item);
                }
            }
        });

        if (userInfo != null) {
//            int experLevel = userInfo.getExperLevel();
//            int charmLevel = userInfo.getCharmLevel();
//
//            LevelView levelView = helper.getView(R.id.level_info);
//            //等级和魅力值
//            if (charmLevel > 0 || experLevel > 0) {
//                levelView.setVisibility(View.VISIBLE);
//            } else {
//                levelView.setVisibility(View.GONE);
//            }
//            levelView.setCharmLevel(charmLevel);
//            levelView.setExperLevel(experLevel);


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

            helper.setText(R.id.tv_user_id, "ID:" + userInfo.getErbanNo());
        } else {
            helper.setVisible(R.id.tv_user_id, false);
            ivGenger.setVisibility(View.INVISIBLE);
        }
    }

    public interface OnRecyclerItemClickListener {
        //RecyclerView的点击事件，将信息回调给view
        void onItemClick(int position, NimUserInfo item);
    }
}
