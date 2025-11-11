package com.vslk.lbgx.room.avroom.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.ui.widget.LevelView;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.queue.bean.RoomConsumeInfo;

import java.util.Locale;

/**
 * <p>房间消费adapter  </p>
 *
 * @author Administrator
 * @date 2017/11/20
 */
public class RoomConsumeListAdapter extends BaseQuickAdapter<RoomConsumeInfo, BaseViewHolder> {

    //private Drawable mManDrawable, mFemaleDrawable;
    private int whiteColor, blackColor, firstColor, secondColor, thirdColor, defaultColor;
    private String contributionValueFormatText;
    public int rankType = 1;

    public RoomConsumeListAdapter(Context context) {
        super(R.layout.list_item_room_consume);
        //mManDrawable = context.getResources().getDrawable(R.drawable.icon_mic_male);
        //mFemaleDrawable = context.getResources().getDrawable(R.drawable.icon_mic_female);
        defaultColor = ContextCompat.getColor(context, R.color.transparent);
        whiteColor = ContextCompat.getColor(context, R.color.white);
        blackColor = ContextCompat.getColor(context, R.color.color_1A1A1A);
        firstColor = ContextCompat.getColor(context, R.color.color_1A1A1A);
        secondColor = ContextCompat.getColor(context, R.color.color_1A1A1A);
        thirdColor = ContextCompat.getColor(context, R.color.color_1A1A1A);
        contributionValueFormatText = "%s";
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RoomConsumeInfo roomConsumeInfo) {

        if (roomConsumeInfo == null) {
            return;
        }

//        int layoutPosition = baseViewHolder.getLayoutPosition();
//        Log.i(TAG, "convert: " + roomConsumeInfo);
        baseViewHolder.setText(R.id.nick, roomConsumeInfo.getNick())
                .setGone(R.id.gender, true)
                /*.setImageDrawable(R.id.gender, roomConsumeInfo.getGender() == 1 ? mManDrawable : mFemaleDrawable)*/
                .setText(R.id.coin_text, String.format(Locale.getDefault(),
                        rankType == 0 ? contributionValueFormatText : "%s", String.valueOf(roomConsumeInfo.getSumGold())))
                .setImageResource(R.id.iv_coin_img, rankType == 1 ?
                        R.drawable.icon_gold :
                        R.drawable.icon_charme_love);
        TextView nick = baseViewHolder.getView(R.id.nick);
        ImageView avatar = baseViewHolder.getView(R.id.avatar);
        ImageLoadUtils.loadAvatar(mContext, roomConsumeInfo.getAvatar(), avatar, true);

        TextView numberText = baseViewHolder.getView(R.id.auction_number_text);
        LevelView levelView = baseViewHolder.getView(R.id.level_info_room_user_list);
        if (rankType == 1) {
            levelView.setExperLevel(roomConsumeInfo.getExperLevel());
            levelView.setCharmLevel(0);
        } else {
            levelView.setCharmLevel(roomConsumeInfo.getCharmLevel());
            levelView.setExperLevel(0);
        }
//


        int position = baseViewHolder.getLayoutPosition();

        numberText.setText(String.valueOf((position + 4)));

        if (position <= 2) {
//            numberText.setTextColor(defaultColor);
            if (position == 0) {
//                nick.setTextColor(firstColor);
//                numberText.setBackgroundResource(R.drawable.list_number_background_first);
            } else if (position == 1) {
//                nick.setTextColor(secondColor);
//                numberText.setBackgroundResource(R.drawable.list_number_background_second);
            } else {
//                nick.setTextColor(thirdColor);
//                numberText.setBackgroundResource(R.drawable.list_number_background_third);
            }
        } else {
//            numberText.setTextColor(blackColor);
//            numberText.setBackgroundColor(whiteColor);
//            numberText.setText(String.valueOf((position + 1)));
        }
    }
}
