package com.vslk.lbgx.room.avroom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionListUserInfo;
import com.tongdaxing.xchat_framework.list.BaseGroupItem;
import com.tongdaxing.xchat_framework.list.ViewHolder;

/**
 * Created by chenran on 2017/8/9.
 */

public class AuctionListItem extends BaseGroupItem implements View.OnClickListener{
    private AuctionListUserInfo auctionListUserInfo;
    private OnAuctionListItemClick onAuctionListItemClick;
    private int position;

    public AuctionListItem(Context context, int viewType, AuctionListUserInfo auctionListUserInfo, int position) {
        super(context, viewType);
        this.auctionListUserInfo = auctionListUserInfo;
        this.position = position;
    }

    public void setOnAuctionListItemClick(OnAuctionListItemClick onAuctionListItemClick) {
        this.onAuctionListItemClick = onAuctionListItemClick;
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup group) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_auction_list, null);
        AuctionListHolder holder = new AuctionListHolder(view);
        return holder;
    }

    @Override
    public void updateHolder(ViewHolder holder, int groupPos, int childPos) {
        AuctionListHolder auctionListHolder = (AuctionListHolder) holder;
        if (auctionListHolder != null) {
            auctionListHolder.nick.setText(auctionListUserInfo.getNick());
            auctionListHolder.coinText.setText(auctionListUserInfo.getPrice() + "");
            auctionListHolder.avatar.setOnClickListener(this);
            ImageLoadUtils.loadAvatar(getContext(), auctionListUserInfo.getAvatar(), auctionListHolder.avatar);
            Drawable drawable;
            if (auctionListUserInfo.getGender() == 1) {
                Drawable drawMan = getContext().getResources().getDrawable(R.drawable.icon_man);
                drawable = drawMan;
            } else {
                Drawable drawFemale = getContext().getResources().getDrawable(R.drawable.icon_woman);
                drawable = drawFemale;
            }
            auctionListHolder.nick.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

            if (getViewType() == AuctionListAdapter.VIEW_TYPE_WEEK_ITEM) {
                if (position <= 2) {
                    auctionListHolder.numberText.setVisibility(View.GONE);
                    auctionListHolder.numberImage.setVisibility(View.VISIBLE);
                    if (position == 0) {
                        auctionListHolder.numberImage.setImageResource(R.drawable.icon_auction_week_list_first);
                    } else if (position == 1) {
                        auctionListHolder.numberImage.setImageResource(R.drawable.icon_auction_week_list_second);
                    } else {
                        auctionListHolder.numberImage.setImageResource(R.drawable.icon_auction_week_list_third);
                    }
                } else {
                    auctionListHolder.numberText.setVisibility(View.VISIBLE);
                    auctionListHolder.numberImage.setVisibility(View.GONE);
                    auctionListHolder.numberText.setText("NO."+(position+1));
                }
            } else {
                auctionListHolder.numberText.setVisibility(View.VISIBLE);
                auctionListHolder.numberImage.setVisibility(View.GONE);
                auctionListHolder.numberText.setText("NO."+(position + 1));
                if (position <= 2) {
                    auctionListHolder.numberText.setTextColor(0xffeecb7f);
                } else {
                    auctionListHolder.numberText.setTextColor(Color.WHITE);
                }
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void onClick(View v) {
        if(onAuctionListItemClick != null) {
            onAuctionListItemClick.onAuctionListItemClick(auctionListUserInfo);
        }
    }

    private static class AuctionListHolder extends ViewHolder {
        private ImageView numberImage;
        private TextView numberText;
        private TextView nick;
        private TextView coinText;
        private CircleImageView avatar;
        public AuctionListHolder(View itemView) {
            super(itemView);
            numberImage = (ImageView) itemView.findViewById(R.id.auction_number_image);
            numberText = (TextView) itemView.findViewById(R.id.auction_number_text);
            nick = (TextView) itemView.findViewById(R.id.nick);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            coinText = (TextView) itemView.findViewById(R.id.coin_text);
        }
    }

    public interface OnAuctionListItemClick {
        void onAuctionListItemClick(AuctionListUserInfo auctionListUserInfo);
    }
}
