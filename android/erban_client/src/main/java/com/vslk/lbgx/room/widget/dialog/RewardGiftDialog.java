package com.vslk.lbgx.room.widget.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

/**
 * 龙珠 -- 选择功能
 * Created by zwk on 13/01/2018.
 */
public class RewardGiftDialog extends BaseDialogFragment implements View.OnClickListener {
    private ImageView ivClose;
    private String giftName;
    private int giftCount;
    private String giftUrl;
    private TextView tvCount;
    private ImageView ivGift;

    public static RewardGiftDialog newInstance(String giftName, int giftCount, String giftUrl) {
        RewardGiftDialog match = new RewardGiftDialog();
        Bundle bundle = new Bundle();
        bundle.putString("giftName", giftName);
        bundle.putString("giftUrl", giftUrl);
        bundle.putInt("giftCount", giftCount);
        match.setArguments(bundle);
        return match;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String str = getArguments().getString("giftName");
            this.giftName = str != null ? str : "";
            this.giftCount = getArguments().getInt("giftCount", giftCount);
            String url = getArguments().getString("giftUrl");
            this.giftUrl = url != null ? url : "";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        // setup window and width
        View view = inflater.inflate(R.layout.dialog_room_gift_reword, window.findViewById(android.R.id.content), false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        setCancelable(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initClickListener();
    }

    private void initView(View view) {
        ivClose = view.findViewById(R.id.iv_gift_reward_close);
        tvCount = view.findViewById(R.id.tv_gift_reward_count);
        ivGift = view.findViewById(R.id.iv_gift_reward);
        tvCount.setText(giftName + "X" + giftCount);
        if (StringUtils.isNotEmpty(giftUrl))
            ImageLoadUtils.loadImage(getContext(), giftUrl, ivGift);
    }

    private void initClickListener() {
        ivClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
