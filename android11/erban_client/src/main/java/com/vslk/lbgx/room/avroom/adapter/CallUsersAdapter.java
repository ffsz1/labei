package com.vslk.lbgx.room.avroom.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;
import com.vslk.lbgx.utils.ImageLoadUtils;

import java.util.List;

public class CallUsersAdapter extends BaseQuickAdapter<MicMemberInfo, BaseViewHolder> {
    private int[] imageMs = new int[]{
            R.mipmap.ic_room_call_m1,
            R.mipmap.ic_room_call_m2,
            R.mipmap.ic_room_call_m3,
            R.mipmap.ic_room_call_m4,
            R.mipmap.ic_room_call_m5,
            R.mipmap.ic_room_call_m6,
            R.mipmap.ic_room_call_m7,
            R.mipmap.ic_room_call_m8,
    };

    private int oldPosition = -1;

    public CallUsersAdapter(@Nullable List<MicMemberInfo> data) {
        super(R.layout.item_call_users, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MicMemberInfo item) {
        ((ImageView) helper.getView(R.id.iv_m)).setImageResource(imageMs[item.getMicPosition() == -1 ? 0 : item.getMicPosition()]);
        ((ImageView) helper.getView(R.id.iv_bg)).setImageResource(item.isSelect() ? R.drawable.bg_room_call_user : R.drawable.bg_room_call_user_off);
        ImageView avatar = helper.getView(R.id.avatar);
        ImageLoadUtils.loadAvatar(mContext, item.getAvatar(), avatar);
        helper.itemView.setOnClickListener(v -> {
            if (oldPosition != helper.getAdapterPosition()) {
                getData().get(helper.getAdapterPosition()).setSelect(true);
                notifyItemChanged(helper.getAdapterPosition());
                if (oldPosition != -1) {
                    getData().get(oldPosition).setSelect(false);
                    notifyItemChanged(oldPosition);
                }
                oldPosition = helper.getAdapterPosition();
                if (listener != null)
                    listener.selectUser(getData().get(helper.getAdapterPosition()));
            }
        });
    }

    public void upOldPosition() {
        if (oldPosition != -1) {
            getData().get(oldPosition).setSelect(false);
            notifyItemChanged(oldPosition);
            oldPosition = -1;
        }
    }

    public void setCallUserListener(ICallUserListener listener) {
        this.listener = listener;
    }

    private ICallUserListener listener;

    public interface ICallUserListener {
        void selectUser(MicMemberInfo info);
    }
}
