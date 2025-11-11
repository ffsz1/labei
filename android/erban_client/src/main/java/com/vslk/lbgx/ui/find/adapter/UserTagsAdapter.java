package com.vslk.lbgx.ui.find.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.vslk.lbgx.ui.widget.cloud.TagsAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.vslk.lbgx.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moxun on 16/1/19.
 */
public class UserTagsAdapter extends TagsAdapter {

    private List<UserInfo> dataSet = new ArrayList<>();

    private OnTagItemSelectedListener onTagItemSelectedListener;

    public UserTagsAdapter(List<UserInfo> userInfos) {
        dataSet.clear();
        dataSet.addAll(userInfos);
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public View getView(final Context context, final int position, ViewGroup parent) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.speed_dating_cloud_item, parent, false);
        UserInfo userInfo = dataSet.get(position);
        TextView username = rootView.findViewById(R.id.username);
        username.setSelected(true);
        RoundedImageView ivUserHead = rootView.findViewById(R.id.iv_user_head);
        ImageLoadUtils.loadCircleImage(context, userInfo.getAvatar(), ivUserHead,
                R.drawable.ic_default_avatar);
        //        DrawableTextView dot = rootView.findViewById(R.id.dot);
        //if (position == 10) {
        //    username.setTextColor(context.getResources().getColor(R.color.color_ffffcc00));
        //    dot.changeSoildColor(context.getResources().getColor(R.color.color_ffffcc00));
        //} else if (position == 25) {
        //    username.setTextColor(context.getResources().getColor(R.color.color_ffff0066));
        //    dot.changeSoildColor(context.getResources().getColor(R.color.color_ffff0066));
        //} else {
        //username.setTextColor(context.getResources().getColor(R.color.color_ffa59fd8));
        //dot.changeSoildColor(context.getResources().getColor(R.color.color_ffa59fd8));
        //}
        username.setText(userInfo.getNick());
        rootView.setOnClickListener(v -> {
            if (onTagItemSelectedListener != null) {
                onTagItemSelectedListener.onTagItemSelected(userInfo);
            }
        });
        return rootView;
    }

    public interface OnTagItemSelectedListener {
        void onTagItemSelected(UserInfo userInfo);
    }

    public void setOnTagItemSelectedListener(OnTagItemSelectedListener listener) {
        this.onTagItemSelectedListener = listener;
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return position % 7;
    }
}
