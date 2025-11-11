package com.vslk.lbgx.ui.me.user.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vslk.lbgx.utils.ImageLoadUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nim.uikit.glide.GlideApp;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.bean.UserPhoto;

import java.util.List;

/**
 * Created by chenran on 2017/7/24.
 */

public class UserPhotoAdapter extends RecyclerView.Adapter<UserPhotoAdapter.UserPhtotViewHolder> {
    // private RealmList<UserPhoto> photoUrls;
    private List<UserPhoto> photoUrls;
    private boolean isSelf;
    private int type;

    public UserPhotoAdapter(List<UserPhoto> photoUrls, int type) {
        this.photoUrls = photoUrls;
        this.type = type;

    }

    public interface ImageClickListener {
        void click(int position, UserPhoto userPhoto);

        void addClick();
    }

    private ImageClickListener mImageClickListener;

    public void setImageClickListener(UserPhotoAdapter.ImageClickListener imageClickListener) {
        mImageClickListener = imageClickListener;
    }

    @Override
    public UserPhtotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = null;
        if (type == 0) {
            item = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_user_photo, parent, false);
        } else {
            item = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_user_photo_edit, parent, false);
        }
        return new UserPhtotViewHolder(item);
    }

    @Override
    public void onBindViewHolder(UserPhtotViewHolder holder, final int position) {
        if (isSelf && position == 0) {


            holder.mPhotoImage.setOnClickListener(v -> {
                if (mImageClickListener != null) {
                    mImageClickListener.addClick();
                }
            });

            holder.mPhotoImage.setImageResource(R.drawable.icon_add_photo);
            GlideApp.with(holder.mPhotoImage.getContext()).load(R.drawable.icon_add_photo).into(holder.mPhotoImage);
        } else {
            UserPhoto photo;
            if (isSelf) {
                photo = photoUrls.get(position - 1);
            } else {
                photo = photoUrls.get(position);
            }
            ImageLoadUtils.loadImage(holder.mPhotoImage.getContext(), photo.getPhotoUrl(), holder.mPhotoImage);
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
        if (photoUrls == null && isSelf) {
            return 1;
        } else {
            if (isSelf && photoUrls.size() < 51) {
                return photoUrls.size() + 1;
            } else {
                return photoUrls.size();
            }
        }
    }

    // ViewHolder
    public class UserPhtotViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView mPhotoImage;

        public UserPhtotViewHolder(View itemView) {
            super(itemView);

            mPhotoImage = itemView.findViewById(R.id.iv_user_photo);
        }

    }
}
