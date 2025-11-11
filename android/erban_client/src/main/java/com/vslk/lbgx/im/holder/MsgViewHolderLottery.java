package com.vslk.lbgx.im.holder;

import android.view.View;
import android.widget.TextView;

import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.UriProvider;

/**
 * Created by chenran on 2018/1/2.
 */

public class MsgViewHolderLottery extends MsgViewHolderBase implements View.OnClickListener{
    private TextView content;

    public MsgViewHolderLottery(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.layout_msg_view_holder_lottery;
    }

    @Override
    protected void inflateContentView() {
        content = findViewById(R.id.content);
        content.setOnClickListener(this);
    }

    @Override
    protected void bindContentView() {

    }

    @Override
    public void onClick(View v) {
        CommonWebViewActivity.start(context, UriProvider.getLotteryActivityPage());
    }
}
