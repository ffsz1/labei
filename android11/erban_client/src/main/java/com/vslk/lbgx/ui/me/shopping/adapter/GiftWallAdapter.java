package com.vslk.lbgx.ui.me.shopping.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.user.bean.DressUpBean;
import com.tongdaxing.xchat_core.user.bean.GiftWallInfo;

import java.util.List;

/**
 * Created by chenran on 2017/10/17.
 */

public class GiftWallAdapter extends RecyclerView.Adapter<GiftWallAdapter.GiftWallHolder> {
    private List<GiftWallInfo> giftWallInfoList;
    private List<DressUpBean> dressUpBeanList;
    private Context context;
    private int dataType = 2;//适配器的数据类型, 0头饰，1座驾,2礼物

    public GiftWallAdapter(Context context) {
        this.context = context;
    }

    public void setGiftWallInfoList(List<GiftWallInfo> giftWallInfoList, List<DressUpBean> dressUpBeanList, int dataType) {
        this.dataType = dataType;

        if (dataType == 2)
            this.giftWallInfoList = giftWallInfoList;
        else
            this.dressUpBeanList = dressUpBeanList;

        notifyDataSetChanged();
    }

    @Override
    public GiftWallHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_gift_wall_info, parent, false);
        return new GiftWallHolder(item);
    }

    @Override
    public void onBindViewHolder(GiftWallHolder holder, int position) {

        holder.giftNum.setText("");
        holder.giftName.setText("");

        if (dataType == 2) {
            GiftWallInfo giftWallInfo = giftWallInfoList.get(position);
            holder.giftName.setText(giftWallInfo.getGiftName());
            holder.giftNum.setText("x " + giftWallInfo.getReciveCount());
            ImageLoadUtils.loadImage(context, giftWallInfo.getPicUrl(), holder.giftPic);

        } else if (dataType == 0) {

            DressUpBean dressUpBean = dressUpBeanList.get(position);
            String headwearName = dressUpBean.getHeadwearName();
            holder.giftName.setText(headwearName);
            ImageLoadUtils.loadImage(context, dressUpBean.getPicUrl(), holder.giftPic);

        } else if (dataType == 1) {

            DressUpBean dressUpBean = dressUpBeanList.get(position);
            String carName = dressUpBean.getCarName();
            holder.giftName.setText(carName);
            ImageLoadUtils.loadImage(context, dressUpBean.getPicUrl(), holder.giftPic);

        }
    }

    @Override
    public int getItemCount() {

        if (dataType == 2) {
            if (giftWallInfoList == null)
                return 0;
            else {
                return giftWallInfoList.size();
            }
        } else {
            if (dressUpBeanList == null)
                return 0;
            else {
                return dressUpBeanList.size();
            }
        }
    }


    public class GiftWallHolder extends RecyclerView.ViewHolder {
        private ImageView giftPic;
        private TextView giftName;
        private TextView giftNum;

        public GiftWallHolder(View itemView) {
            super(itemView);
            giftPic = (ImageView) itemView.findViewById(R.id.gift_img);
            giftName = (TextView) itemView.findViewById(R.id.gift_name);
            giftNum = (TextView) itemView.findViewById(R.id.gift_num);
        }

    }
}
