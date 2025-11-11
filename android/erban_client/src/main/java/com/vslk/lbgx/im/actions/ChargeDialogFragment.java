package com.vslk.lbgx.im.actions;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tongdaxing.erban.R;

public class ChargeDialogFragment extends DialogFragment implements View.OnClickListener {

    static ChargeDialogFragment instance(String title, ChargeDialogListener listener) {
        ChargeDialogFragment dialogFragment = new ChargeDialogFragment();
        dialogFragment.listener = listener;
        dialogFragment.title = title;
        dialogFragment.setCancelable(false);
        return dialogFragment;
    }

    private ChargeDialogListener listener;
    private String title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_ok_cancel_dialog, container, true);
    }

    TextView tvTitle;
    TextView btnOk;
    TextView btnCancel;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvTitle = (TextView) view.findViewById(R.id.message);
        tvTitle.setText(title);
        btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        btnOk = (TextView) view.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onClick(v, this);
    }

    interface ChargeDialogListener {
        /**
         * dialog点击处理
         *
         * @param view
         * @param fragment
         */
        void onClick(View view, ChargeDialogFragment fragment);
    }
}
