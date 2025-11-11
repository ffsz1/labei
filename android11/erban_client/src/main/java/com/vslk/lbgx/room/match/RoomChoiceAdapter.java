package com.vslk.lbgx.room.match;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.bean.RoomChoiceBean;

public class RoomChoiceAdapter extends BaseQuickAdapter<RoomChoiceBean, BaseViewHolder> {
    private int selectPosition = -1;

    public RoomChoiceAdapter(int layoutResId) {
        super(layoutResId);
    }

    private int[] bgSelectedImg = {
            R.drawable.ic_match_choice_ball_1,
            R.drawable.ic_match_choice_ball_2,
            R.drawable.ic_match_choice_ball_3,
            R.drawable.ic_match_choice_ball_4,
            R.drawable.ic_match_choice_ball_5,
            R.drawable.ic_match_choice_ball_6,
            R.drawable.ic_match_choice_ball_7,
            R.drawable.ic_match_choice_ball_8,
            R.drawable.ic_match_choice_ball_9
    };

    @Override
    protected void convert(BaseViewHolder helper, RoomChoiceBean item) {
        ImageView rbChoice = helper.getView(R.id.rb_room_choice);
        int selectedIndex = item.getPosition() - 1;
        if (item.isSelect() && selectedIndex >= 0 && selectedIndex < bgSelectedImg.length) {
            rbChoice.setBackgroundResource(bgSelectedImg[selectedIndex]);
        } else {
            rbChoice.setBackgroundResource(item.getResId());
        }
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public void changeState(int position) {
        if (getData().get(position).isRandom()) {//随机
            int select = RoomMatchUtil.getRandomNum(1, 9);
            if (select == position)
                return;
            changePositionState(select, true);
        } else {//
            changePositionState(position, false);
        }
    }

    private void changePositionState(int position, boolean random) {
        if (selectPosition == -1) {
            getData().get(position).setSelect(true);
            notifyDataSetChanged();
            selectPosition = position;
        } else {
            if (selectPosition != position) {
                getData().get(position).setSelect(true);
                getData().get(selectPosition).setSelect(false);
                notifyDataSetChanged();
                selectPosition = position;
            } else {//相同位置
                if (!random) {
                    getData().get(selectPosition).setSelect(false);
                    notifyDataSetChanged();
                    selectPosition = -1;
                }
                //随机到相同位置不做处理
            }
        }
    }

}
