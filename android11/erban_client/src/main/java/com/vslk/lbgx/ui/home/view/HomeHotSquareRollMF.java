package com.vslk.lbgx.ui.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongwen.marqueen.MarqueeFactory;
import com.vslk.lbgx.ui.find.activity.SquareActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;

/**
 * Function:
 * Author: Edward on 2019/6/24
 */
public class HomeHotSquareRollMF extends MarqueeFactory<View, SquareRollInfo> {
    public HomeHotSquareRollMF(Context mContext) {
        super(mContext);
    }

    @Override
    public View generateMarqueeItemView(SquareRollInfo data) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_home_hot_square_roll_marquee, null);
        view.setOnClickListener(v -> SquareActivity.start(mContext));
        ImageView iv1 = view.findViewById(R.id.iv_user_head1);
        TextView tvName1 = view.findViewById(R.id.tv_name1);
        TextView tvContent1 = view.findViewById(R.id.tv_content1);

        ImageView iv2 = view.findViewById(R.id.iv_user_head2);
        TextView tvName2 = view.findViewById(R.id.tv_name2);
        TextView tvContent2 = view.findViewById(R.id.tv_content2);

//        ImageView iv3 = view.findViewById(R.id.iv_user_head3);

        if (data != null && data.publicChatRoomAttachment1 != null) {
            ImageLoadUtils.loadCircleImage(mContext, data.publicChatRoomAttachment1.getAvatar(), iv1, R.drawable.ic_default_avatar);
            tvName1.setText(data.publicChatRoomAttachment1.getNick());
            tvContent1.setText(": " + data.publicChatRoomAttachment1.getMsg());
        }

        if (data != null && data.publicChatRoomAttachment2 != null) {
            ImageLoadUtils.loadCircleImage(mContext, data.publicChatRoomAttachment2.getAvatar(), iv2, R.drawable.ic_default_avatar);
            tvName2.setText(data.publicChatRoomAttachment2.getNick());
            tvContent2.setText(": " + data.publicChatRoomAttachment2.getMsg());
        }

        /*if (data != null && data.publicChatRoomAttachment3 != null) {
            iv3.setVisibility(View.VISIBLE);
            ImageLoadUtils.loadCircleImage(mContext, data.publicChatRoomAttachment3.getAvatar(), iv3, R.drawable.ic_default_avatar);
        } else {
            iv3.setVisibility(View.GONE);
        }*/
        return view;
    }
}
