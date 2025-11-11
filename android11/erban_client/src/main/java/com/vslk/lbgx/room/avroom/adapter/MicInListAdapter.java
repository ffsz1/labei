package com.vslk.lbgx.room.avroom.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.ui.widget.LevelView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2018/4/26.
 */

public class MicInListAdapter extends BaseQuickAdapter<Json, MicInListAdapter.Viewholder> {


    public boolean isAdmin;
    private Context context;

    public MicInListAdapter(Context context, int layoutResId, @Nullable List<Json> data) {
        super(layoutResId, data);
        this.context = context;

    }




    @Override
    protected void convert(Viewholder viewholder, Json item) {



        ImageLoadUtils.loadImage(context, item.str("avatar"), viewholder.ivMicInListItemIcon);
        viewholder.lvMicInListItem.setExperLevel(item.num("experLevel"));
        viewholder.tvMicInListItemId.setText("id:" + item.str("erbanNo"));
        viewholder.tvMicInListItemName.setText(item.str("nick"));



        viewholder.buMoveToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Json> data = getData();
                if (data.size() > 0) {
                    long l = data.get(0).num_l("time");

                    if (item.num_l("time") == l)
                        return;

                    item.set("time", l - 1);
                }

                CoreManager.notifyClients(IAVRoomCoreClient.class, IAVRoomCoreClient.micInlistMoveToTop, item.num("uid"), "", item.num("uid"));
            }
        });
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.buMoveToTop.setVisibility(position == 0 || !isAdmin ? View.GONE : View.VISIBLE);
    }

    class Viewholder extends BaseViewHolder {
        @BindView(R.id.iv_mic_in_list_item_icon)
        ImageView ivMicInListItemIcon;
        @BindView(R.id.lv_mic_in_list_item)
        LevelView lvMicInListItem;
        @BindView(R.id.tv_mic_in_list_item_name)
        TextView tvMicInListItemName;
        @BindView(R.id.tv_mic_in_list_item_id)
        TextView tvMicInListItemId;
        @BindView(R.id.bu_mic_in_list_move_top)
        Button buMoveToTop;


        public Viewholder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
