package com.vslk.lbgx.room.avroom.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;

/**
 * Created by huangmeng1 on 2018/1/18.
 */

public class RoomBlackAdapter extends BaseQuickAdapter<IMChatRoomMember, BaseViewHolder> {
    public RoomBlackAdapter(int layoutResId) {
        super(layoutResId);
    }

    private RoomBlackDelete roomBlackDelete;

    public void setRoomBlackDelete(RoomBlackDelete roomBlackDelete) {
        this.roomBlackDelete = roomBlackDelete;
    }

    @Override
    protected void convert(BaseViewHolder helper, IMChatRoomMember item) {
        ImageLoadUtils.loadCircleImage(mContext, item.getAvatar(), helper.getView(R.id.imageView), R.drawable.ic_default_avatar);
        helper.setText(R.id.tv_userName, item.getNick());
        helper.getView(R.id.ll_black).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (roomBlackDelete != null)
                    roomBlackDelete.onDeleteClick(item);
                return true;
            }
        });
        helper.getView(R.id.remove_opration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomBlackDelete != null)
                    roomBlackDelete.onDeleteClick(item);
            }
        });
    }

    public interface RoomBlackDelete {
        void onDeleteClick(IMChatRoomMember chatRoomMember);
    }
}
