package com.vslk.lbgx.room.avroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.list.BaseGroupItem;
import com.tongdaxing.xchat_framework.list.ViewHolder;

/**
 * Created by chenran on 2017/8/9.
 */

public class AuctionListHeaderItem extends BaseGroupItem{
    public AuctionListHeaderItem(Context context, int viewType) {
        super(context, viewType);
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup group) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_auction_header, null);
        AuctionListHeaderItem.AuctionListHeaderHolder holder = new AuctionListHeaderItem.AuctionListHeaderHolder(view);
        return holder;
    }

    @Override
    public void updateHolder(ViewHolder holder, int groupPos, int childPos) {
        AuctionListHeaderItem.AuctionListHeaderHolder auctionListHolder = (AuctionListHeaderItem.AuctionListHeaderHolder) holder;
        if (auctionListHolder != null) {
            if (getViewType() == AuctionListAdapter.VIEW_TYPE_WEEK_HEADER) {
                auctionListHolder.image.setImageResource(R.drawable.icon_week_auction_list);
            } else {
                auctionListHolder.image.setImageResource(R.drawable.icon_total_auction_list);
            }
        }
    }

    private static class AuctionListHeaderHolder extends ViewHolder {
        private ImageView image;
        public AuctionListHeaderHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.auction_list_header);
        }
    }
}
