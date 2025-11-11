package com.vslk.lbgx.room.gift.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.tongdaxing.erban.R;

public class GiftAllMicView extends RelativeLayout {
    private TextView tvAll;
    private CheckBox cbSelect;
    private boolean isSelect = false;
    private OnAllMicSelectListener onAllMicSelectListener;

    public GiftAllMicView(Context context) {
        super(context);
        inflate(context,R.layout.layout_gift_all_mic, this);
        ViewGroup.LayoutParams vl = getLayoutParams();
        vl.width = ConvertUtils.dp2px(48);
        vl.height = ConvertUtils.dp2px(51);
        setLayoutParams(vl);
        tvAll = findViewById(R.id.tv_gift_all);
        cbSelect = findViewById(R.id.cb_gift_all);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbSelect.isChecked()) {
                    setCheck(false);
                } else {
                    setCheck(true);
                }
                if (onAllMicSelectListener != null)
                    onAllMicSelectListener.onCheck(isSelect);
            }
        });

    }

    public void setCheck(boolean state) {
        if (cbSelect != null)
            cbSelect.setChecked(state);
        if (tvAll != null)
            tvAll.setSelected(state);
        isSelect = state;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public interface OnAllMicSelectListener {
        void onCheck(boolean isAllMic);
    }

    public void setOnAllMicSelectListener(OnAllMicSelectListener onAllMicSelectListener) {
        this.onAllMicSelectListener = onAllMicSelectListener;
    }
}
