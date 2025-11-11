package com.vslk.lbgx.ui.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.base.callback.BaseAdapterItemListener;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.glide.GlideApp;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.home.HomeRoom;

/**
 * Created by chenran on 2017/10/3.
 */

public class SearchAdapter extends BaseQuickAdapter<HomeRoom, BaseViewHolder> {

    private Context context;
    private int tagHeight;

    public SearchAdapter(Context context) {
        super(R.layout.list_item_search);
        tagHeight = context.getResources().getDimensionPixelOffset(R.dimen.dp_15);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeRoom item) {
        ImageView avatar = holder.getView(R.id.avatar);
        ImageLoadUtils.loadCircleImage(avatar.getContext(), item.getAvatar(), avatar,R.drawable.ic_default_avatar);
        if (!item.isValid()) {
            holder.setGone(R.id.room_title, false);
        } else {
            holder.setGone(R.id.room_title, true).setText(R.id.room_title, item.getTitle());

            ImageView ivRoomTag = holder.getView(R.id.iv_room_tag);
            GlideApp.with(mContext)
                    .load(item.getTagPict())
                    .placeholder(R.mipmap.ic_tag_default)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.mipmap.ic_tag_default)
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                            float ratio = (drawable.getIntrinsicHeight() + 0.F) / drawable.getIntrinsicWidth();
                            int width = Math.round(tagHeight / ratio);
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivRoomTag.getLayoutParams();
                            params.width = width;
                            params.height = tagHeight;
                            ivRoomTag.setLayoutParams(params);
                            ivRoomTag.setImageDrawable(drawable);
                            return true;
                        }
                    }).into(ivRoomTag);
            //Drawable drawable;
            //TextView roomTitle = holder.getView(R.id.room_title);
            //if (item.getType() == RoomInfo.ROOMTYPE_LIGHT_CHAT) {
            //    drawable = roomTitle.getContext().getResources().getDrawable(R.drawable.icon_hot_light_chat);
            //} else if (item.getType() == RoomInfo.ROOMTYPE_AUCTION) {
            //    drawable = roomTitle.getContext().getResources().getDrawable(R.drawable.icon_hot_auction);
            //} else {
            //    drawable = roomTitle.getContext().getResources().getDrawable(R.drawable.icon_hot_home_party);
            //}
            //roomTitle.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }

        holder.setText(R.id.user_name, item.getNick())
                .setText(R.id.erban_no, "ID:" + item.getErbanNo())
                .setOnClickListener(R.id.container, view -> {
                    UserInfoActivity.start(context, item.getUid());
                    if (baseAdapterItemListener != null) {
                        baseAdapterItemListener.onItemClick(holder.getAdapterPosition());
                    }
                });
    }

    public BaseAdapterItemListener baseAdapterItemListener;
}
