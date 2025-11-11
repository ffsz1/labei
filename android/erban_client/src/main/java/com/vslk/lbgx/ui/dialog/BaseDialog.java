package com.vslk.lbgx.ui.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tongdaxing.erban.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialog extends DialogFragment {
    private View rootView;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFullScreen); //dialog全屏
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(layout(), null);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = this.getDialog().getWindow();
        //去掉dialog默认的padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        if (setAnim() == 1) {
            //设置dialog的动画
            lp.windowAnimations = R.style.dialgBottomDialogAnimation;
        } else if (setAnim() != 0) {
            lp.windowAnimations = setAnim();
        }
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    protected ArrayList<View> find(int... ids) {
        ArrayList<View> views = new ArrayList<>();
        for (int id : ids) {
            View view = rootView.findViewById(id);
            views.add(view);
        }
        return views;
    }

    /**
     * 获取状态栏高度（单位：px）
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId) == 0 ? 60 : resources.getDimensionPixelSize(resourceId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (close() != 0) {
            rootView.findViewById(close()).setOnClickListener(v -> dismiss());
        }
        bindView(rootView);
    }

    protected abstract int layout();

    protected abstract int setAnim();

    protected abstract int close();

    protected abstract void bindView(View view);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
