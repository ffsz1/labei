package com.vslk.lbgx.im.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.im.custom.bean.NoticeAttachment;

/**
 * Created by chenran on 2017/9/21.
 */

public class MsgViewHolderContent extends MsgViewHolderBase implements View.OnClickListener{

    private ImageView bg;
    private TextView title;
    private TextView desc;
    private LinearLayout container;

    public MsgViewHolderContent(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.layout_msg_view_holder_content;
    }

    @Override
    protected void inflateContentView() {
        bg = findViewById(R.id.bg_image);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        container = findViewById(R.id.layout);
    }

    @Override
    protected void bindContentView() {
        NoticeAttachment attachment = (NoticeAttachment) message.getAttachment();
        if (!StringUtil.isEmpty(attachment.getPicUrl())) {
            bg.setVisibility(View.VISIBLE);
            ImageLoadUtils.loadImage(bg.getContext(), attachment.getPicUrl(), bg, R.drawable.ic_default_avatar);
        } else {
            bg.setVisibility(View.GONE);
        }

        title.setText(attachment.getTitle());
        desc.setText(attachment.getDesc());
        container.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        NoticeAttachment attachment = (NoticeAttachment) message.getAttachment();
        if (!StringUtil.isEmpty(attachment.getWebUrl())) {
            CommonWebViewActivity.start(context, attachment.getWebUrl());
        }
    }
}
