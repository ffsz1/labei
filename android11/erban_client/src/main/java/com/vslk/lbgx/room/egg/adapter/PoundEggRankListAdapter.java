package com.vslk.lbgx.room.egg.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.bean.UserInfo;

import java.util.Locale;

/**
 * <p>房间消费adapter  </p>
 *
 * @author Administrator
 * @date 2017/11/20
 */
public class PoundEggRankListAdapter extends BaseQuickAdapter<UserInfo, BaseViewHolder> {

    private Drawable mManDrawable, mFemaleDrawable;

    public PoundEggRankListAdapter(Context context) {
        super(R.layout.list_item_room_consume);
        mManDrawable = context.getResources().getDrawable(R.drawable.icon_man);
        mFemaleDrawable = context.getResources().getDrawable(R.drawable.icon_woman);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        baseViewHolder.setImageDrawable(R.id.gender, userInfo.getGender() == 1 ? mManDrawable : mFemaleDrawable);
        baseViewHolder.setText(R.id.coin_text, String.format(Locale.getDefault(), String.valueOf(userInfo.getTol())))
                .setText(R.id.nick, userInfo.getNick());
        baseViewHolder.setTextColor(R.id.nick,mContext.getResources().getColor(R.color.white));
        ImageView avatar = baseViewHolder.getView(R.id.avatar);
        ImageLoadUtils.loadAvatar(mContext, userInfo.getAvatar(), avatar, true);
        TextView numberText = baseViewHolder.getView(R.id.auction_number_text);
//        numberText.setVisibility(View.GONE);
        int position = baseViewHolder.getAdapterPosition();
        numberText.setText(String.valueOf((position + 1)));
        if (position == 0) {
            numberText.setBackgroundResource(R.drawable.num_one);
            numberText.setTextColor(Color.BLACK);
        } else if (position == 1) {
            numberText.setBackgroundResource(R.drawable.num_two);
            numberText.setTextColor(Color.BLACK);
        } else if (position == 2) {
            numberText.setBackgroundResource(R.drawable.num_three);
            numberText.setTextColor(Color.BLACK);
        } else {
            numberText.setTextColor(Color.WHITE);
            numberText.setBackgroundResource(R.color.transparent);
        }

//        LevelView levelView = baseViewHolder.getView(R.id.level_info_room_user_list);
//        if (userInfo.getExperLevel() > 0) {
//            levelView.setVisibility(View.VISIBLE);
//            levelView.setExperLevel(userInfo.getExperLevel());
//        } else {
//            levelView.setVisibility(View.INVISIBLE);
//        }
    }
}
