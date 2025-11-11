package com.vslk.lbgx.room.gift.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.StateListDrawable;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;

/**
 * Created by chenran on 2017/10/25.
 */

public class GiftAvatarAdapter extends BaseQuickAdapter<MicMemberInfo, BaseViewHolder> {
    private OnCancelAllMicSelectListener onCancelAllMicSelectListener;
    private int selectCount = 0;

    public void setAllSelect(boolean isAllMic) {
        this.selectCount = isAllMic ? getItemCount() : 0;
    }

    public GiftAvatarAdapter() {
        super(R.layout.list_item_gift_avatar);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void convert(BaseViewHolder helper, MicMemberInfo item) {
        CircleImageView avatar = helper.getView(R.id.avatar);
        FrameLayout bgAvatar = helper.getView(R.id.bg_avatar);
        FrameLayout avatarContainer = helper.getView(R.id.avatar_container);
        ImageLoadUtils.loadAvatar(mContext, item.getAvatar(), avatar);
        CheckBox cbSelect = helper.getView(R.id.cb_member_select);
        cbSelect.setText(item.getMicPosition() == -1 ? "房主" : (item.getMicPosition() + 1) + "");
        if (item.getMicPosition() == -1) {
            cbSelect.setBackground(setBoss());
        } else {
            cbSelect.setBackground(setUser());
        }
        if (item.isSelect()) {
            bgAvatar.setBackgroundResource(R.drawable.bg_room_call_user);
            cbSelect.setChecked(true);
        } else {
            bgAvatar.setBackgroundResource(R.drawable.bg_room_call_user_off);
            cbSelect.setChecked(false);
        }
        avatarContainer.setOnClickListener(v -> {
            if (item.isSelect()) {
                item.setSelect(false);
                bgAvatar.setBackgroundResource(R.drawable.bg_room_call_user);
                selectCount--;
            } else {
                bgAvatar.setBackgroundResource(R.drawable.bg_room_call_user_off);
                item.setSelect(true);
                selectCount++;
            }
            if (selectCount == getItemCount()) {//选中之后如果满人数则全选
                if (onCancelAllMicSelectListener != null)
                    onCancelAllMicSelectListener.onChange(true, selectCount);
            } else {
                if (onCancelAllMicSelectListener != null)//选中之前如果满人数则取消全选
                    onCancelAllMicSelectListener.onChange(false, selectCount);
            }
            notifyDataSetChanged();
        });
    }


    private StateListDrawable setBoss() {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{-android.R.attr.state_checked}, mContext.getResources().getDrawable(R.mipmap.ic_send_gift_select_un));
        states.addState(new int[]{android.R.attr.state_checked}, mContext.getResources().getDrawable(R.mipmap.ic_send_gift_select));
        return states;
    }

    private StateListDrawable setUser() {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{-android.R.attr.state_checked}, mContext.getResources().getDrawable(R.mipmap.ic_send_gift_select_user_un));
        states.addState(new int[]{android.R.attr.state_checked}, mContext.getResources().getDrawable(R.mipmap.ic_send_gift_select_user));
        return states;
    }


    public interface OnCancelAllMicSelectListener {
        void onChange(boolean isAllMic, int count);
    }

    public void setOnCancelAllMicSelectListener(OnCancelAllMicSelectListener onCancelAllMicSelectListener) {
        this.onCancelAllMicSelectListener = onCancelAllMicSelectListener;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }
}
