package com.vslk.lbgx.ui.message.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.bean.AttentionInfo;

import java.util.List;

/**
 * Created by chenran on 2017/9/21.
 */

public class AttentionUserAdapter extends RecyclerView.Adapter<AttentionUserAdapter.AttentionUserHolder> {
    private List<AttentionInfo> attentionInfoList;
    private Activity context;

    public AttentionUserAdapter(Activity context) {
        this.context = context;
    }

    public void setAttentionInfoList(List<AttentionInfo> attentionInfoList) {
        this.attentionInfoList = attentionInfoList;
    }

    @Override
    public AttentionUserAdapter.AttentionUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_attention_user, parent, false);
        return new AttentionUserHolder(item);
    }

    @Override
    public void onBindViewHolder(final AttentionUserAdapter.AttentionUserHolder holder, final int position) {
        final AttentionInfo attentionInfo = attentionInfoList.get(position);
        ImageLoadUtils.loadAvatar(holder.avatar.getContext(), attentionInfo.getAvatar(), holder.avatar, true);
        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttentionInfo attentionInfo = attentionInfoList.get(holder.getAdapterPosition());
                UserInfoActivity.start(context, attentionInfo.getUid());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (attentionInfoList == null) {
            return 0;
        } else {
            return attentionInfoList.size();
        }
    }


    public static class AttentionUserHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;

        public AttentionUserHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
        }
    }
}
