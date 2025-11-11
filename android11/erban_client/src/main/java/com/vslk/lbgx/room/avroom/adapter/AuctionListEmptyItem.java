package com.vslk.lbgx.room.avroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.list.BaseGroupItem;
import com.tongdaxing.xchat_framework.list.ViewHolder;

/**
 * Created by chenran on 2017/8/9.
 */

public class AuctionListEmptyItem extends BaseGroupItem{

    public AuctionListEmptyItem(Context context, int viewType) {
        super(context, viewType);
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup group) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_auction_empty, null);
        AuctionListEmptyItem.AuctionListEmptyHolder holder = new AuctionListEmptyItem.AuctionListEmptyHolder(view);
        return holder;
    }

    @Override
    public void updateHolder(ViewHolder holder, int groupPos, int childPos) {

    }

    private static class AuctionListEmptyHolder extends ViewHolder {
        private TextView textView;
        public AuctionListEmptyHolder(View itemView) {
            super(itemView);

        }
    }
}
