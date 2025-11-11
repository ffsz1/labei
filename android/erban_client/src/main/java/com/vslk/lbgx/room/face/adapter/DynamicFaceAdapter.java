package com.vslk.lbgx.room.face.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.room.face.FaceInfo;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;

import java.io.File;
import java.util.List;

/**
 * @author chenran
 * @date 2017/9/8
 */

public class DynamicFaceAdapter extends BaseAdapter implements View.OnClickListener {
    private List<FaceInfo> faceInfoList;
    private Context context;
    private OnFaceItemClickListener onFaceItemClickListener;

    public void setOnFaceItemClickListener(OnFaceItemClickListener onFaceItemClickListener) {
        this.onFaceItemClickListener = onFaceItemClickListener;
    }

    public DynamicFaceAdapter(Context context, List<FaceInfo> faceInfoList) {
        this.faceInfoList = faceInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (faceInfoList == null) {
            return 0;
        } else {
            return faceInfoList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        FaceViewHolder holder;
        if (null == convertView) {
            holder = new FaceViewHolder();
            //mContext指的是调用的Activity
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_face, null);
            holder.faceIcon = (ImageView) convertView.findViewById(R.id.face_image);
            holder.faceName = (TextView) convertView.findViewById(R.id.face_name);
            holder.container = convertView.findViewById(R.id.face_layout);
            convertView.setTag(holder);
        } else {
            holder = (FaceViewHolder) convertView.getTag();//FIXME 为什么去掉LinearLayout根布局之后，这里居然会拿到FaceInfo？待研究
        }
        FaceInfo faceInfo = faceInfoList.get(position);
        holder.container.setTag(faceInfo);
        holder.container.setOnClickListener(this);
        File file = new File(faceInfo.getFacePath(faceInfo.getIconImageIndex()));
        ImageLoadUtils.loadImage(BasicConfig.INSTANCE.getAppContext(), file, holder.faceIcon);
        holder.faceName.setText(faceInfo.getCNName());

        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (onFaceItemClickListener != null) {
            FaceInfo faceInfo = (FaceInfo) v.getTag();
            onFaceItemClickListener.onFaceItemClick(faceInfo);
        }
    }

    public interface OnFaceItemClickListener {
        void onFaceItemClick(FaceInfo faceInfo);
    }

    private class FaceViewHolder {
        private ImageView faceIcon;
        private TextView faceName;
        private View container;
    }
}
