package com.vslk.lbgx.ui.dialog;

import android.view.View;

import com.tongdaxing.erban.R;

public class ChoiceHomeDialog extends BaseDialog {
    @Override
    protected int layout() {
        return R.layout.dialog_choice_home;
    }

    @Override
    protected int setAnim() {
        return 1;
    }

    @Override
    protected int close() {
        return R.id.close;
    }

    @Override
    protected void bindView(View view) {
        view.setOnClickListener(v -> dismiss());
        view.findViewById(R.id.iv_room).setOnClickListener(v -> {
            listener.onRoom();
            dismiss();
        });
        view.findViewById(R.id.iv_xy).setOnClickListener(v -> {
            listener.onXY();
            dismiss();
        });
    }

    private ICHListener listener;

    public void setListener(ICHListener listener) {
        this.listener = listener;
    }

    public interface ICHListener {
        void onRoom();

        void onXY();
    }
}
