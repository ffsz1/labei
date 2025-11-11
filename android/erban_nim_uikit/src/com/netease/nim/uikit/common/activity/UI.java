package com.netease.nim.uikit.common.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.StatusBarUtil;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.ReflectionUtil;
import com.netease.nim.uikit.model.ToolBarOptions;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tongdaxing.erban.libcommon.swipeactivity.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class UI extends SwipeBackActivity {

    private boolean destroyed = false;

    private static Handler handler;

    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private boolean isAdd;

    @Override
    protected void onStart() {
        super.onStart();
        if (blackStatusBar()) {
            addView(StatusBarUtil.StatusBarLightMode(this));
        }
    }

    public boolean blackStatusBar() {
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        frameLayout = (FrameLayout) decorView.findViewById(android.R.id.content);
        //全局竖屏


        LogUtil.ui("activity: " + getClass().getSimpleName() + " onCreate()");
    }

    @Override
    public void onBackPressed() {
        invokeFragmentManagerNoteStateNotSaved();
        super.onBackPressed();
    }

    public void addView(int i) {
        if (i == 0 && !isAdd) {
            isAdd = true;
            View view = LayoutInflater.from(this).inflate(R.layout.layout_system_bar, frameLayout, false);
            frameLayout.addView(view);
        }
    }

    /**
     * 设置沉浸式状态栏
     */

    protected void setStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Log.e("setStatusBar", "setStatusBar: " + "~~~~~~~~~");
            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

//            try {
//                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
//                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
//                field.setAccessible(true);
//                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
//            } catch (Exception e) {
//            }


        }
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#00000000"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }

    @Override
    protected void onResume() {
        //Attempt to invoke virtual method 'void android.support.v4.app.Fragment.setNextAnim(int)' on a null object reference
        //com.trello.rxlifecycle2.components.support.RxAppCompatActivity.onResume(RxAppCompatActivity.java:73)
        try {
            super.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onNavigateUpClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolBar(int toolBarId, ToolBarOptions options) {
        toolbar = (Toolbar) findViewById(toolBarId);
        if (options.titleId != 0) {
            setTitle(options.titleId);
        }
        if (!TextUtils.isEmpty(options.titleString)) {
            setTitle(options.titleString);
        }
//        if (options.logoId != 0) {
//            toolbar.setLogo(options.logoId);
//        }
        setSupportActionBar(toolbar);
        if (options.isNeedNavigate) {
            toolbar.setNavigationIcon(options.navigateId);
            toolbar.setContentInsetStartWithNavigation(0);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigateUpClicked();
                }
            });
        }
    }

    public void setToolBar(int toolBarId, int navigateId) {
        toolbar = (Toolbar) findViewById(toolBarId);
        setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(navigateId);
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavigateUpClicked();
            }
        });
    }

    public void setToolBar(int toolbarId, int titleId, int logoId) {
        toolbar = (Toolbar) findViewById(toolbarId);
        setTitle(titleId);
        toolbar.setLogo(logoId);
        setSupportActionBar(toolbar);
    }

    public Toolbar getToolBar() {
        return toolbar;
    }

    public int getToolBarHeight() {
        if (toolbar != null) {
            return toolbar.getHeight();
        }

        return 0;
    }

    public void onNavigateUpClicked() {
        onBackPressed();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    public void setSubTitle(String subTitle) {
        if (toolbar != null) {
            toolbar.setSubtitle(subTitle);
        }
    }

    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }

    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 延时弹出键盘
     *
     * @param focus 键盘的焦点项
     */
    protected void showKeyboardDelayed(View focus) {
        final View viewToFocus = focus;
        if (focus != null) {
            focus.requestFocus();
        }

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewToFocus == null || viewToFocus.isFocused()) {
                    showKeyboard(true);
                }
            }
        }, 200);
    }


    public boolean isDestroyedCompatible() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isDestroyedCompatible17();
        } else {
            return destroyed || super.isFinishing();
        }
    }

    @TargetApi(17)
    private boolean isDestroyedCompatible17() {
        return super.isDestroyed();
    }

    /**
     * fragment management
     */
    public TFragment addFragment(TFragment fragment) {
        List<TFragment> fragments = new ArrayList<TFragment>(1);
        fragments.add(fragment);

        List<TFragment> fragments2 = addFragments(fragments);
        return fragments2.get(0);
    }

    public List<TFragment> addFragments(List<TFragment> fragments) {
        List<TFragment> fragments2 = new ArrayList<TFragment>(fragments.size());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        boolean commit = false;
        for (int i = 0; i < fragments.size(); i++) {
            // install
            TFragment fragment = fragments.get(i);
            int id = fragment.getContainerId();

            // exists
            TFragment fragment2 = (TFragment) fm.findFragmentById(id);

            if (fragment2 == null) {
                fragment2 = fragment;
                transaction.add(id, fragment);
                commit = true;
            }

            fragments2.add(i, fragment2);
        }

        if (commit) {
            try {
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {

            }
        }

        return fragments2;
    }

    public TFragment switchContent(TFragment fragment) {
        return switchContent(fragment, false);
    }

    protected TFragment switchContent(TFragment fragment, boolean needAddToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(fragment.getContainerId(), fragment);
        if (needAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        try {
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fragment;
    }

    protected boolean displayHomeAsUpEnabled() {
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                return onMenuKeyDown();

            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    protected boolean onMenuKeyDown() {
        return false;
    }

    private void invokeFragmentManagerNoteStateNotSaved() {
        FragmentManager fm = getSupportFragmentManager();
        ReflectionUtil.invokeMethod(fm, "noteStateNotSaved", null);
    }

    protected void switchFragmentContent(TFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(fragment.getContainerId(), fragment);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean isCompatible(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }

    protected <T extends View> T findView(int resId) {
        return (T) (findViewById(resId));
    }

}
