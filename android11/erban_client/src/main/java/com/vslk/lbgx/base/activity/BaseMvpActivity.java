package com.vslk.lbgx.base.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.vslk.lbgx.base.callback.IDataStatus;
import com.vslk.lbgx.base.fragment.BaseFragment;
import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.base.view.TitleBar;
import com.vslk.lbgx.reciever.ConnectiveChangedReceiver;
import com.vslk.lbgx.ui.common.fragment.LoadingFragment;
import com.vslk.lbgx.ui.common.fragment.NetworkErrorFragment;
import com.vslk.lbgx.ui.common.fragment.NoDataFragment;
import com.vslk.lbgx.ui.common.fragment.ReloadFragment;
import com.vslk.lbgx.ui.common.permission.EasyPermissions;
import com.vslk.lbgx.ui.common.permission.PermissionActivity;
import com.vslk.lbgx.ui.common.widget.StatusLayout;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.widget.DefaultToolBar;
import com.hncxco.library_ui.widget.AppToolBar;
import com.netease.nim.uikit.StatusBarUtil;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.glide.GlideApp;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.AbstractMvpActivity;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.erban.libcommon.listener.IDisposableAddListener;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.UIUtils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.lang.reflect.Method;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * @author alvin hwang
 */
public abstract class BaseMvpActivity<V extends IMvpBaseView, P extends AbstractMvpPresenter<V>> extends AbstractMvpActivity<V, P>
        implements IDataStatus, ConnectiveChangedReceiver.ConnectiveChangedListener, EasyPermissions.PermissionCallbacks, IDisposableAddListener {

    private DialogManager mDialogManager;
    protected TitleBar mTitleBar;
    protected DefaultToolBar mToolBar;
    protected CompositeDisposable mCompositeDisposable;
    private FrameLayout frameLayout;
    private boolean isAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable = new CompositeDisposable();
        if (setBgColor() > 0) {
            getWindow().setBackgroundDrawableResource(setBgColor());
        }
        if (needSteepStateBar()) {
            setStatusBar();
        }
        //全局竖屏
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        frameLayout = decorView.findViewById(android.R.id.content);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        CoreManager.addClient(this);

        mCompositeDisposable.add(IMNetEaseManager.get().getChatRoomEventObservable().subscribe(roomEvent -> {
            if (roomEvent == null) {
                return;
            }
            onReceiveChatRoomEvent(roomEvent);
        }));
    }

    protected void onReceiveChatRoomEvent(RoomEvent roomEvent) {

    }

    public void addView(int i) {
        if (i == 0 && !isAdd) {
            isAdd = true;
            View view = LayoutInflater.from(this).inflate(R.layout.layout_system_bar, frameLayout, false);
            frameLayout.addView(view);
        }
    }

    public void initTitleBar() {
        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        if (mTitleBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && needSteepStateBar()) {
                mTitleBar.setImmersive(true);
            }
            mTitleBar.setBackgroundColor(getResources().getColor(R.color.primary));
            mTitleBar.setTitleColor(getResources().getColor(R.color.text_primary));
        }
    }

    protected boolean needSteepStateBar() {
        return true;
    }

    protected int setBgColor() {
        return 0;
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Log.e(TAG, "setStatusBar: " + "++++++++++");
            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//            try {
//                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
//                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
//                field.setAccessible(true);
//                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
//            } catch (Exception e) {
//            }
        }
        StatusBarUtil.transparencyBar(this);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#00000000"));

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initTitleBar();
    }

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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
        if (mDialogManager != null) {
            mDialogManager = null;
        }
        super.onDestroy();
        LogUtil.i(this.getClass().getName(), "onDestroy");
        CoreManager.removeClient(this);
    }

    public DialogManager getDialogManager() {
        if (mDialogManager == null) {
            mDialogManager = new DialogManager(this);
            mDialogManager.setCanceledOnClickOutside(false);
        }
        return mDialogManager;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Fragment fragment = getTopFragment();
        if (fragment != null && fragment instanceof BaseFragment) {
            if (((BaseFragment) fragment).onKeyDown(keyCode, event)) {
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getTopFragment();
        if (fragment != null && (fragment instanceof BaseFragment || fragment instanceof BaseMvpFragment)) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Fragment getTopFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    return fragment;
                }
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        hideIME();
        try {
            super.onBackPressed();
        } catch (Exception ex) {
            MLog.error(this, ex);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Fragment fragment = getTopFragment();
                if (fragment != null && fragment instanceof BaseFragment) {
                    if (fragment.onOptionsItemSelected(item)) {
                        return true;
                    }
                }
                onBackPressed();
                return true;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlideApp.with(this).resumeRequests();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlideApp.with(this).pauseRequests();
    }

    /**
     * wifi 转 2G/3G/4G
     */
    @Override
    public void wifiChange2MobileData() {

    }

    /**
     * 有网络变为无网络
     */
    @Override
    public void change2NoConnection() {
        if (isTopActive()) {

        }
    }

    /**
     * 连上wifi
     */
    @Override
    public void connectiveWifi() {
        if (isTopActive()) {

        }
    }

    /**
     * 连上移动数据网络
     */
    @Override
    public void connectiveMobileData() {
        if (isTopActive()) {

        }
    }

    /**
     * 移动数据网络 改为连上wifi
     */
    @Override
    public void mobileDataChange2Wifi() {

    }

    protected boolean checkActivityValid() {
        return UIUtils.checkActivityValid(this);
    }

    /**
     * --------------------------------------------------
     * -------------------------数据状态状态相关-------------
     * --------------------------------------------------
     */

    private static final String STATUS_TAG = "STATUS_TAG";

    @Override
    public View.OnClickListener getLoadListener() {
        return v -> onReloadDate();
    }

    /**
     * 网络错误重新加载数据
     */
    public void onReloadDate() {

    }

    @Override
    public View.OnClickListener getLoadMoreListener() {
        return null;
    }

    @Override
    public View.OnClickListener getNoMobileLiveDataListener() {
        return null;
    }

    @Override
    public void showLoading() {
        showLoading(0, 0);
    }

    @Override
    public void showLoading(View view) {
        showLoading(view, 0, 0);
    }

    @Override
    public void showReload() {
        showReload(0, 0);
    }

    @Override
    public void showNoData() {
        showNoData(0, "");
    }

    @Override
    public void showNoLogin() {

    }

    @Override
    public void showLoading(int drawable, int tips) {
        if (!checkActivityValid()) {
            return;
        }

        View status = findViewById(R.id.status_layout);
        if (status == null || status.getId() <= 0) {
            MLog.error(this, "xuwakao, had not set layout id ");
            return;
        }
        Fragment fragment = LoadingFragment.newInstance(drawable, tips);
        getSupportFragmentManager().beginTransaction().replace(status.getId(), fragment, STATUS_TAG).commitAllowingStateLoss();
    }

    @Override
    public void showLoading(View view, int drawable, int tips) {

    }

    @Override
    public void showReload(int drawable, int tips) {
        if (!checkActivityValid()) {
            return;
        }

        View status = findViewById(R.id.status_layout);
        if (status == null || status.getId() <= 0) {
            MLog.error(this, "xuwakao, had not set layout id ");
            return;
        }
        ReloadFragment fragment = ReloadFragment.newInstance(drawable, tips);
        fragment.setListener(getLoadListener());
        getSupportFragmentManager().beginTransaction().replace(status.getId(), fragment, STATUS_TAG).commitAllowingStateLoss();
    }

    @Override
    public void showReload(View view, int drawable, int tips) {

    }

    @Override
    public void showNoData(CharSequence charSequence) {
        showNoData(0, charSequence);
    }

    @Override
    public void showNoData(int drawable, CharSequence charSequence) {
        if (!checkActivityValid()) {
            return;
        }

        View status = findViewById(R.id.status_layout);
        if (status == null || status.getId() <= 0) {
            MLog.error(this, "xuwakao, had not set layout id ");
            return;
        }
        NoDataFragment fragment = NoDataFragment.newInstance(drawable, charSequence);
        fragment.setListener(getLoadListener());
        getSupportFragmentManager().beginTransaction().replace(status.getId(), fragment, STATUS_TAG).commitAllowingStateLoss();
    }

    @Override
    public void showNoData(View view, int drawable, CharSequence charSequence) {

    }

    @Override
    public void showNetworkErr() {
        if (!checkActivityValid()) {
            return;
        }

        View status = findViewById(R.id.status_layout);
        if (status == null || status.getId() <= 0) {
            MLog.error(this, "xuwakao, had not set layout id ");
            return;
        }
        NetworkErrorFragment fragment = new NetworkErrorFragment();
        fragment.setListener(getLoadListener());
        getSupportFragmentManager().beginTransaction().replace(status.getId(), fragment, STATUS_TAG).commitAllowingStateLoss();
    }

    @Override
    public void hideStatus() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(STATUS_TAG);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void showPageError(int tips) {
        if (!checkActivityValid()) {
            return;
        }

        View more = findViewById(R.id.loading_more);
        if (more == null) {
            MLog.error(this, "xuwakao, showReload more is NULL");
            return;
        }
        StatusLayout statusLayout = (StatusLayout) more.getParent();
        statusLayout.showErrorPage(tips, getLoadMoreListener());
    }

    @Override
    public void showPageError(View view, int tips) {

    }

    @Override
    public void showPageLoading() {
        if (!checkActivityValid()) {
            return;
        }

        View more = findViewById(R.id.loading_more);
        if (more == null) {
            MLog.error(this, "xuwakao, showReload more is NULL");
            return;
        }
        StatusLayout statusLayout = (StatusLayout) more.getParent();
        statusLayout.showLoadMore();
    }

    /**
     * 当前网络是否可用
     *
     * @return
     */
    public boolean isNetworkAvailable() {
        return NetworkUtils.isNetworkStrictlyAvailable(this);
    }

    /**
     * --------------------------------------------------
     * -------------------------UI基本功能------------------
     * --------------------------------------------------
     */
    public void hideIME() {
        View v = getCurrentFocus();
        if (null != v) {
            hideIME(v);
        }
    }

    public void hideIME(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 当前无Fragment 或 无Fragment添加 视为顶部激活状态
     */
    public boolean isTopActive() {
        if (isTopActivity()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments == null || fragments.size() == 0) {
                return true;
            } else {
                int size = fragments.size();
                for (int i = 0; i < size; i++) {
                    // why null?
                    if (fragments.get(i) != null && fragments.get(i).isAdded()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean isTopActivity() {
        return UIUtils.isTopActivity(this);
    }

    /**
     * 通用消息提示
     */
    public void toast(int resId) {
        toast(resId, Toast.LENGTH_SHORT);
    }

    public void toast(String toast) {
        toast(toast, Toast.LENGTH_SHORT);
    }

    /**
     * 通用消息提示
     */
    public void toast(int resId, int length) {
        SingleToastUtil.showToast(BasicConfig.INSTANCE.getAppContext(), resId, length);
    }

    public void toast(String toast, int length) {
        SingleToastUtil.showToast(BasicConfig.INSTANCE.getAppContext(), toast, length);
    }

    /**
     * 权限回调接口
     */
    private PermissionActivity.CheckPermListener mListener;

    protected static final int RC_PERM = 123;

    public void checkPermission(PermissionActivity.CheckPermListener listener, int resString, String... mPerms) {
        mListener = listener;
        if (EasyPermissions.hasPermissions(this, mPerms)) {
            if (mListener != null) {
                mListener.superPermission();
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(resString), RC_PERM, mPerms);
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //同意了某些权限可能不是全部
    }

    @Override
    public void onPermissionsAllGranted() {
        if (mListener != null) {
            mListener.superPermission();//同意了全部权限的回调
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, getString(R.string.perm_tip),
                R.string.setting, R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        SettingActivity.gotoAppDetailIntent(BaseMvpActivity.this);
                        dialog.dismiss();
                    }
                }, perms);
    }

    /**
     * 当前Activity 是否有效
     */
    protected boolean isValid() {
        return !isFinishing() && !isDestroyed();
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable != null && disposable != null) {
            mCompositeDisposable.add(disposable);
        }
    }

    protected void back(AppToolBar toolBar) {
        toolBar.setOnBackBtnListener(this::back);
    }

    protected void back(View view) {
        finish();
    }

}