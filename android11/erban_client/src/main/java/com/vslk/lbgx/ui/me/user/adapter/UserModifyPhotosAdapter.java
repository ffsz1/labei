package com.vslk.lbgx.ui.me.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.bean.UserPhoto;

import io.realm.RealmList;

/**
 * Created by chenran on 2017/7/24.
 */

public class UserModifyPhotosAdapter extends BaseAdapter {
    private Context mContext;
    private RealmList<UserPhoto> photoUrls;
    private PhotoItemClickListener listener;
    private boolean isEditMode;
    private boolean isSelf = false;

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public UserModifyPhotosAdapter(Context context, RealmList<UserPhoto> photoUrls, PhotoItemClickListener listener) {
        this.mContext = context;
        this.photoUrls = photoUrls;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        if (isSelf) {
            if (photoUrls == null) {
                return 1;
            } else {
                return photoUrls.size() + 1;
            }
        } else {
            if (photoUrls == null) {
                return 0;
            } else {
                return photoUrls.size();
            }
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        UserModifyPhotosViewHolder holder;
        if (null == convertView) {
            holder = new UserModifyPhotosViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_user_photos_modify, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_user_photo);
            holder.imageDelete = (ImageView) convertView.findViewById(R.id.iv_photo_delete);
            convertView.setTag(holder);
        } else {
            holder = (UserModifyPhotosViewHolder) convertView.getTag();
        }

        holder.imageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPhotoItemClick(position);
            }
        });
        holder.imageDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPhotoDeleteClick(position);
            }
        });
        if (isSelf) {
            if (position == 0) {
                holder.imageView.setImageResource(R.drawable.icon_add_photo);
                holder.imageDelete.setVisibility(View.GONE);
            } else {
                UserPhoto userPhoto = photoUrls.get(position - 1);
                int radius = (int) (holder.imageView.getContext().getResources().getDisplayMetrics().density) * 15;
                ImageLoadUtils.loadSmallRoundBackground(mContext, userPhoto.getPhotoUrl(), holder.imageView, radius);
                if (isEditMode) {
                    holder.imageDelete.setVisibility(View.VISIBLE);
                } else {
                    holder.imageDelete.setVisibility(View.GONE);
                }
            }
        } else {
            UserPhoto userPhoto = photoUrls.get(position);
            ImageLoadUtils.loadSmallRoundBackground(mContext, userPhoto.getPhotoUrl(), holder.imageView);
            if (isEditMode) {
                holder.imageDelete.setVisibility(View.VISIBLE);
            } else {
                holder.imageDelete.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    class UserModifyPhotosViewHolder {
        private ImageView imageView;
        private ImageView imageDelete;
    }

    public interface PhotoItemClickListener {
        void onPhotoItemClick(int position);

        void onPhotoDeleteClick(int position);
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

}
