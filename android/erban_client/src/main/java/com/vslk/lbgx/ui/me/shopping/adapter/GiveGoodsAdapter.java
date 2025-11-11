package com.vslk.lbgx.ui.me.shopping.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.ui.widget.LevelView;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.List;


/**
 * @author dell
 */
public class GiveGoodsAdapter extends BaseQuickAdapter<Json, GiveGoodsAdapter.ViewHolder> {

    public GiveGoodsAdapter(@Nullable List<Json> data) {
        super(R.layout.list_item_share_fans, data);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Json json) {

        ImageLoadUtils.loadImage(mContext, json.str("avatar"), viewHolder.imageView);
        String nick = json.str("nick");
        viewHolder.tvItemName.setText(nick);
        viewHolder.levelViewNewUserList.setExperLevel(json.num("experLevel"));
        String uid = json.str("uid");
        viewHolder.buInvite.setOnClickListener(v -> {
            if (itemAction != null) {
                itemAction.itemClickAction(uid, nick);
            }
        });


    }


    public ItemAction itemAction;

    public interface ItemAction {
        void itemClickAction(String uid, String userName);

    }

    public class ViewHolder extends BaseViewHolder {

        ImageView imageView;
        TextView tvItemName;
        LevelView levelViewNewUserList;
        DrawableTextView buInvite;
        ImageView ivShareFansOption;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            tvItemName = view.findViewById(R.id.tv_item_name);
            buInvite = view.findViewById(R.id.bu_invite);
            buInvite.setText("赠送");
            ivShareFansOption = view.findViewById(R.id.iv_share_fans_option);
            levelViewNewUserList = view.findViewById(R.id.level_view_new_user_list);
        }
    }


}
