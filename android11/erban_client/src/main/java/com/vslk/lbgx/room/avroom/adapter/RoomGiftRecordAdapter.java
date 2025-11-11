package com.vslk.lbgx.room.avroom.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.GiftReceiveInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.gift.MultiGiftReceiveInfo;
import com.tongdaxing.xchat_core.im.custom.bean.GiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.MultiGiftAttachment;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 * @date 2018/3/23
 */

public class RoomGiftRecordAdapter extends RecyclerView.Adapter<RoomGiftRecordAdapter.GiftRecordViewHolder> {

    private List<ChatRoomMessage> datas = new ArrayList<>();
    private Context mContext;
    private OnGiftRecordListener onGiftRecordListener;

    public RoomGiftRecordAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public GiftRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_gift_record, parent, false);
        GiftRecordViewHolder holder = new GiftRecordViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(GiftRecordViewHolder holder, int position) {
        ChatRoomMessage message = datas.get(position);
        IMCustomAttachment attachment = (IMCustomAttachment) message.getAttachment();
        String nick = "";
        String targetNick = "全麦";
        String num = "0";
        String name = "";
        if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT) {
            GiftAttachment giftAttachment = (GiftAttachment) attachment;
            GiftReceiveInfo giftRecieveInfo = giftAttachment.getGiftRecieveInfo();
            if (giftRecieveInfo == null)
                return;
//            holder.tvTime.setText(TimeUtils.getPostTimeString(mContext, giftRecieveInfo.getGiftSendTime(), true, false));
            holder.tvTime.setText(TimeUtils.getDateTimeString(giftRecieveInfo.getGiftSendTime(), "yyyy-MM-dd HH:mm:ss"));
            GiftInfo giftInfo = CoreManager.getCore(IGiftCore.class).findGiftInfoById(giftRecieveInfo.getGiftId());
            if (giftInfo != null) {
                nick = giftAttachment.getGiftRecieveInfo().getNick();
                targetNick = giftAttachment.getGiftRecieveInfo().getTargetNick();
//                if (!TextUtils.isEmpty(nick) && nick.length() > 6) {
//                    nick = nick.substring(0, 6) + "...";
//                }
//                if (!TextUtils.isEmpty(targetNick) && targetNick.length() > 6) {
//                    targetNick = targetNick.substring(0, 6) + "...";
//                }
                name = giftInfo.getGiftName();
                num = "X" + giftAttachment.getGiftRecieveInfo().getGiftNum();
                ImageLoadUtils.loadImage(mContext, giftInfo.getGiftUrl(), holder.ivGiftLogo);

            }
        } else {
            MultiGiftAttachment giftAttachment = (MultiGiftAttachment) attachment;
            MultiGiftReceiveInfo multiGiftRecieveInfo = giftAttachment.getMultiGiftRecieveInfo();
            if (multiGiftRecieveInfo == null) {
                return;
            }
            holder.tvTime.setText(TimeUtils.getPostTimeString(mContext, multiGiftRecieveInfo.getGiftSendTime(), true, false));
            GiftInfo giftInfo = CoreManager.getCore(IGiftCore.class).findGiftInfoById(multiGiftRecieveInfo.getGiftId());
            if (giftInfo != null) {
                nick = giftAttachment.getMultiGiftRecieveInfo().getNick();
//                if (!TextUtils.isEmpty(nick) && nick.length() > 6) {
//                    nick = nick.substring(0, 6) + "...";
//                }
                name = giftInfo.getGiftName();
                num = "X" + giftAttachment.getMultiGiftRecieveInfo().getGiftNum();
                ImageLoadUtils.loadImage(mContext, giftInfo.getGiftUrl(), holder.ivGiftLogo);
            }
        }
        holder.tvNick.setText(nick);
        holder.tvTarget.setText(targetNick);
        holder.tvGiftName.setText(name);
        holder.tvGiftNum.setText(num);

        holder.rlItem.setOnClickListener(v -> {
            if (onGiftRecordListener != null && message != null && message.getImChatRoomMember() != null) {
                onGiftRecordListener.OnGiftRecordItemListener(message.getImChatRoomMember().getAccount());
            }
        });
    }

    public void setDatas(List<ChatRoomMessage> datas) {
        if (datas != null && !datas.isEmpty()) {
            if (!ListUtils.isListEmpty(this.datas)) {
                this.datas.clear();
            }
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class GiftRecordViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlItem;
        private ImageView ivGiftLogo;
        private TextView tvNick, tvTarget, tvGiftName, tvGiftNum, tvTime;

        GiftRecordViewHolder(View itemView) {
            super(itemView);
            rlItem = itemView.findViewById(R.id.rl_gift_record);
            ivGiftLogo = itemView.findViewById(R.id.iv_gift_record);
            tvNick = itemView.findViewById(R.id.tv_gift_record_nick);
            tvTarget = itemView.findViewById(R.id.tv_gift_record_target);
            tvGiftName = itemView.findViewById(R.id.tv_gift_record_name);
            tvGiftNum = itemView.findViewById(R.id.tv_gift_record_num);
            tvTime = itemView.findViewById(R.id.tv_gift_record_time);
        }
    }

    public interface OnGiftRecordListener {
        void OnGiftRecordItemListener(String account);
    }

    public void setOnGiftRecordListener(OnGiftRecordListener onGiftRecordListener) {
        this.onGiftRecordListener = onGiftRecordListener;
    }
}
