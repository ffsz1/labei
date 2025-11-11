package com.vslk.lbgx.ui.me.shopping.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.util.Json;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/11.
 */

public class BlackListAdapter extends BaseQuickAdapter<Json, BlackListAdapter.ViewHolder> {

    private BaseActivity context;

    public BlackListAdapter(BaseActivity context) {
        super(R.layout.item_black_list);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder helper, Json item) {
        ImageLoadUtils.loadImage(context, item.str("avatar"), helper.ivBlackListIcon);
        helper.tvBlackListName.setText(item.str("nick"));
        helper.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(iBlackListItemClick!=null)
                    iBlackListItemClick.onItemClick(item);


            }
        });
    }

    private IBlackListItemClick iBlackListItemClick;

    public void setiBlackListItemClick(IBlackListItemClick iBlackListItemClick) {
        this.iBlackListItemClick = iBlackListItemClick;
    }

    public interface IBlackListItemClick{
        void onItemClick(Json json);
    }


    public class ViewHolder extends BaseViewHolder {
        private View view;
        @BindView(R.id.iv_black_list_icon)
        ImageView ivBlackListIcon;
        @BindView(R.id.tv_black_list_name)
        TextView tvBlackListName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


}
