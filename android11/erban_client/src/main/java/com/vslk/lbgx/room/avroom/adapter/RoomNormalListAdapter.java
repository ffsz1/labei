package com.vslk.lbgx.room.avroom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.ui.widget.SquareImageView;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;

import java.util.List;


/**
 * Created by chenran on 2017/10/11.
 */

public class RoomNormalListAdapter extends RecyclerView.Adapter<RoomNormalListAdapter.RoomNormalListHolder> implements View.OnClickListener {
    private Context context;
    private List<IMChatRoomMember> normalList;
    private OnRoomNormalListOperationClickListener listOperationClickListener;

    public RoomNormalListAdapter(Context context) {
        this.context = context;
    }

    public void setNormalList(List<IMChatRoomMember> normalList) {
        this.normalList = normalList;
    }

    public List<IMChatRoomMember> getNormalList() {
        return normalList;
    }

    public void setListOperationClickListener(OnRoomNormalListOperationClickListener listOperationClickListener) {
        this.listOperationClickListener = listOperationClickListener;
    }

    @Override
    public RoomNormalListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_room_normal, parent, false);
        return new RoomNormalListHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomNormalListHolder holder, int position) {
        IMChatRoomMember chatRoomMember = normalList.get(position);
        holder.nick.setText(chatRoomMember.getNick());
        holder.operationImg.setTag(chatRoomMember);
        holder.operationImg.setOnClickListener(this);
        holder.ivGenger.setBackground(chatRoomMember.getGender() == 1 ?
                context.getResources().getDrawable(R.drawable.icon_man) :
                context.getResources().getDrawable(R.drawable.icon_woman));
        ImageLoadUtils.loadSmallRoundBackground(context, chatRoomMember.getAvatar(), holder.avatar);
    }

    @Override
    public int getItemCount() {
        if (normalList == null) {
            return 0;
        } else {
            return normalList.size();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null && v.getTag() instanceof IMChatRoomMember) {
            IMChatRoomMember chatRoomMember = (IMChatRoomMember) v.getTag();
            if (listOperationClickListener != null) {
                listOperationClickListener.onRemoveOperationClick(chatRoomMember);
            }
        }
    }

    public class RoomNormalListHolder extends RecyclerView.ViewHolder {
        private SquareImageView avatar;
        private TextView nick;
        private TextView operationImg;
        private ImageView ivGenger;


        public RoomNormalListHolder(View itemView) {
            super(itemView);
            avatar = (SquareImageView) itemView.findViewById(R.id.avatar);
            nick = (TextView) itemView.findViewById(R.id.nick);
            operationImg = (TextView) itemView.findViewById(R.id.remove_opration);
            ivGenger = (ImageView) itemView.findViewById(R.id.iv_gender);
        }
    }

    public interface OnRoomNormalListOperationClickListener {
        void onRemoveOperationClick(IMChatRoomMember chatRoomMember);
    }
}
