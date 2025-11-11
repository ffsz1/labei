package com.hncxco.library_ui.widget.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hncxco.library_ui.widget.ViewHolder;

/**
 * Function: 对话框通用类
 * Author: Edward on 2019/1/9
 */
public abstract class BaseDialog extends AppCompatDialogFragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getContentView(), container, false);
        convertView(ViewHolder.create(view));
        initDialog();
        return view;
    }

    private void initDialog() {
        Dialog dialog = getDialog();
        if (null != dialog) {
            Window window = dialog.getWindow();
            if (null != window) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置透明
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (null != dialog) {
            Window window = dialog.getWindow();
            if (null != window) {
                window.setLayout(getDialogWidth(), getDialogHeight());
            }
        }
    }

    @Override
    public void show(FragmentManager fragmentManager, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(this, tag).addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }


    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected int getDialogWidth() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    protected int getDialogHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    public abstract void convertView(ViewHolder holder);

    public abstract int getContentView();
}
