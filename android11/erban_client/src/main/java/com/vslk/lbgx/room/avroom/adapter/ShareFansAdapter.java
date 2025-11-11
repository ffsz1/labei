package com.vslk.lbgx.room.avroom.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.ui.widget.LevelView;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.List;


public class ShareFansAdapter extends BaseQuickAdapter<Json, ShareFansAdapter.ViewHolder> {
    private boolean moreOption = false;

    public Json sendHistory = new Json();

    public boolean isMoreOption() {
        return moreOption;
    }

    public void setMoreOption(boolean moreOption) {
        this.moreOption = moreOption;
        notifyDataSetChanged();
    }

    public ShareFansAdapter(@Nullable List<Json> data) {
        super(R.layout.list_item_share_fans, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Json json) {

        ImageLoadUtils.loadCircleImage(mContext, json.str("avatar"), viewHolder.imageView,R.drawable.ic_default_avatar);
        viewHolder.tvItemName.setText(json.str("nick"));

        viewHolder.levelViewNewUserList.setExperLevel(json.num("experLevel"));

        int hasSend = sendHistory.num(json.str("uid"));
        boolean moreOption = this.moreOption && hasSend != 1;
        viewHolder.buInvite.setVisibility(moreOption ? View.GONE : View.VISIBLE);
        viewHolder.ivShareFansOption.setVisibility(!moreOption ? View.GONE : View.VISIBLE);
        if (hasSend == 1) {
            viewHolder.buInvite.setText("已邀请");
            viewHolder.buInvite.setTextColor(Color.parseColor("#666666"));
            viewHolder.buInvite.setBackground(null);
        } else {
            viewHolder.buInvite.setText("邀请");
            viewHolder.buInvite.setTextColor(Color.WHITE);
            viewHolder.buInvite.setBackgroundResource(R.drawable.shape_car_pay);
        }

        boolean select = json.boo("select");
        viewHolder.ivShareFansOption.setImageResource(select ? R.drawable.ic_share_select : R.drawable.ic_share_unselect);
        long uid = json.num_l("uid");
        viewHolder.buInvite.setOnClickListener(v -> {
            if (itemAction != null) {
                if (hasSend != 1) {
                    itemAction.itemClickAction(uid);
                }
            }

        });
        viewHolder.ivShareFansOption.setOnClickListener(v -> {

            json.set("select", !json.boo("select"));
            notifyDataSetChanged();

        });

    }


    public ItemAction itemAction;

    public interface ItemAction {
        void itemClickAction(long uid);
    }

    public class ViewHolder extends BaseViewHolder {
        ImageView imageView;
        TextView tvItemName;
        DrawableTextView buInvite;
        ImageView ivShareFansOption;
        LevelView levelViewNewUserList;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            tvItemName = view.findViewById(R.id.tv_item_name);
            buInvite = view.findViewById(R.id.bu_invite);
            ivShareFansOption = view.findViewById(R.id.iv_share_fans_option);
            levelViewNewUserList = view.findViewById(R.id.level_view_new_user_list);
        }
    }


}
