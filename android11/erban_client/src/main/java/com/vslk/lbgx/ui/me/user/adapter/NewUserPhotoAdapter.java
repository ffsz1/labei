package com.vslk.lbgx.ui.me.user.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.glide.GlideApp;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.user.bean.UserPhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenran on 2017/7/24.
 */

public class NewUserPhotoAdapter extends RecyclerView.Adapter<NewUserPhotoAdapter.UserPhtotViewHolder> {

    private List<UserPhoto> photoUrls;
    private boolean isSelf;
    private int type;
    private int itemWidth;
    public NewUserPhotoAdapter(int type, int itemWidth) {
        if (this.photoUrls == null) {
            this.photoUrls = new ArrayList<>();
        }
        this.type = type;
        this.itemWidth = itemWidth;
    }

    public interface ImageClickListener {
        void click(int position, UserPhoto userPhoto);

        void addClick();
    }

    private ImageClickListener mImageClickListener;

    public void setImageClickListener(NewUserPhotoAdapter.ImageClickListener imageClickListener) {
        mImageClickListener = imageClickListener;
    }

    @Override
    public UserPhtotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = null;
        if (type == 0) {
            item = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_user_photo_new, parent, false);
        } else {
            item = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_user_photo_edit_new, parent, false);
        }
        return new UserPhtotViewHolder(item);
    }

    @Override
    public void onBindViewHolder(UserPhtotViewHolder holder, final int position) {
        LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) holder.mPhotoImage.getLayoutParams();
        ll.width = itemWidth;
        ll.height = itemWidth;
        holder.mPhotoImage.setLayoutParams(ll);
        if (isSelf && position == 0) {
            holder.mPhotoImage.setOnClickListener(v -> {
                if (mImageClickListener != null) {
                    mImageClickListener.addClick();
                }
            });

            GlideApp.with(holder.mPhotoImage.getContext()).load(R.drawable.icon_add_photo).into(holder.mPhotoImage);


        } else {
            UserPhoto photo;
            if (isSelf) {
                photo = photoUrls.get(position - 1);
            } else {
                photo = photoUrls.get(position);
            }
            int radius = (int) (holder.mPhotoImage.getContext().getResources().getDisplayMetrics().density) * 15;

            ImageLoadUtils.loadSmallRoundBackground(holder.mPhotoImage.getContext(), photo.getPhotoUrl(),
                    holder.mPhotoImage, radius);


            holder.mPhotoImage.setOnClickListener(v -> {
                if (mImageClickListener != null) {
                    if (isSelf) {
                        mImageClickListener.click(position - 1, photo);
                    } else {
                        mImageClickListener.click(position, photo);
                    }
                }
            });
        }


    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    @Override
    public int getItemCount() {
        if (isSelf) {
            if (ListUtils.isListEmpty(photoUrls)) {
                return 1;
            } else {
                return (photoUrls.size() + 1) >= 10 ? 10 : (photoUrls.size() + 1);
            }
        } else {
            if (ListUtils.isListEmpty(photoUrls)) {
                return 0;
            } else {
                return photoUrls.size() > 10 ? 10 : photoUrls.size();
            }
        }
    }

    public class UserPhtotViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhotoImage;

        public UserPhtotViewHolder(View itemView) {
            super(itemView);
            mPhotoImage = itemView.findViewById(R.id.iv_user_photo);
        }

    }

    public void setPhotoUrls(List<UserPhoto> photoUrls) {
        if (!ListUtils.isListEmpty(photoUrls)) {
            this.photoUrls = photoUrls;
        }
        notifyDataSetChanged();
    }
}
