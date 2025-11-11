package com.vslk.lbgx.base.fragment;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.base.callback.IAcitivityBase;
import com.vslk.lbgx.base.callback.IDataStatus;
import com.vslk.lbgx.reciever.ConnectiveChangedReceiver;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.common.fragment.AbsStatusFragment;
import com.vslk.lbgx.ui.common.fragment.LoadingFragment;
import com.vslk.lbgx.ui.common.fragment.NetworkErrorFragment;
import com.vslk.lbgx.ui.common.fragment.NoDataFragment;
import com.vslk.lbgx.ui.common.fragment.ReloadFragment;
import com.vslk.lbgx.ui.common.widget.StatusLayout;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.AbstractMvpFragment;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.erban.libcommon.listener.IDisposableAddListener;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.tongdaxing.xchat_framework.util.util.UIUtils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.util.List;
import java.util.Stack;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author alvin hwang
 */
public abstract class BaseMvpFragment<V extends IMvpBaseView, P extends AbstractMvpPresenter<V>>
        extends AbstractMvpFragment<V, P> implements KeyEvent.Callback, IDataStatus,IMvpBaseView,
        ConnectiveChangedReceiver.ConnectiveChangedListener, FragmentManager.OnBackStackChangedListener, IAcitivityBase,
        IDisposableAddListener {

    protected View mView;
    protected Context mContext;
    protected CompositeDisposable mCompositeDisposable;
    /**
     * 避免空布局导致首页协调布局滑动失效
     */
    private boolean isClickReload = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable = new CompositeDisposable();
        mContext = getContext();
        onInitArguments(getArguments());
    }


    public void onNewIntent(Intent intent) {

    }

    protected void onInitArguments(Bundle bundle) {

    }

    protected boolean isDataBinding() {
        return false;
    }

    protected View bindRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        if (!isDataBinding()) {
            mView = inflater.inflate(getRootLayoutId(), container, false);
            ButterKnife.bind(this, mView);
        } else {
            mView = bindRootView(inflater, container, savedInstanceState);
        }
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        CoreManager.addClient(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        CoreManager.removeClient(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private boolean mIsInitView;
    private boolean mIsLoaded;
    /**
     * 当前fragment是否还活着
     */
    private boolean mIsDestroyView = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        restoreState(savedInstanceState);
        onFindViews();
        initiate();
        onSetListener();
        mIsInitView = true;
        if (getUserVisibleHint() && !mIsLoaded && mIsDestroyView) {
            mIsDestroyView = false;
            onLazyLoadData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mIsInitView && !mIsLoaded && mIsDestroyView) {
                mIsDestroyView = false;
                mIsLoaded = true;
                onLazyLoadData();
            }
        } else {
            mIsLoaded = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIsDestroyView = true;
    }

    /**
     * 数据懒加载
     */
    protected void onLazyLoadData() {

    }

    protected void restoreState(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {
        super.onStart();
        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getFragmentManager().removeOnBackStackChangedListener(this);
    }

    /**
     * 跳转逻辑
     */
    protected void autoJump(int skipType, String skipUri,String activity,Json json) {
        switch (skipType) {
            case 3:
                if (StringUtils.isNotEmpty(skipUri)) {
                    CommonWebViewActivity.start(getActivity(), skipUri);
                }
                break;
            case 2:
                if (StringUtils.isNotEmpty(skipUri)) {
                    AVRoomActivity.start(getActivity(), JavaUtil.str2long(skipUri));
                }
                break;
            case 1:
                jumpAvRoomActivity(activity, json);
                break;
            default:
                break;
        }
    }

    private void jumpAvRoomActivity(String activity, Json json) {
        if (StringUtils.isNotEmpty(activity)) {
            try {
                activity = getContext().getPackageName() + ".ui." + activity;
                Intent intent = new Intent(mContext, Class.forName(activity));
                if (json != null && json.key_names().length > 0) {
                    String[] strings = json.key_names();
                    for (String key : strings) {
                        String value = json.str(key);
                        intent.putExtra(key, value);
                    }
                }
                mContext.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    protected void autoJump(String activity, String url, Json json) {
        if ("".equals(activity)) {
            if (!"".equals(url)) {
                if (url.startsWith("/")) {
                    url = UriProvider.JAVA_WEB_URL + url;
                }
                CommonWebViewActivity.start(mContext, url);
                return;
            }
        }
        try {
            activity = getContext().getPackageName() + ".ui." + activity;
            Intent intent = new Intent(mContext, Class.forName(activity));
            if (json != null && json.key_names().length > 0) {
                String[] strings = json.key_names();
                for (String key : strings) {
                    String value = json.str(key);
                    intent.putExtra(key, value);
                }
            }
            mContext.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
        super.onDestroy();
    }

    protected int getRootLayoutId() {
        return 0;
    }

    @Override
    public void onBackStackChanged() {

    }

    /**
     * 网络连接变化
     */
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
            MLog.debug(this, "change2NoConnection");
        }
    }

    /**
     * 连上wifi
     */
    @Override
    public void connectiveWifi() {
        if (isTopActive()) {
            MLog.debug(this, "connectiveWifi");
        }
    }

    /**
     * 连上移动数据网络
     */
    @Override
    public void connectiveMobileData() {
        if (isTopActive()) {
            MLog.debug(this, "connectiveMobileData");
        }
    }

    /**
     * 移动数据网络 改为连上wifi
     */
    @Override
    public void mobileDataChange2Wifi() {
        if (isTopActive()) {
            MLog.debug(this, "mobileDataChange2Wifi");
        }
    }

    public Stack<Integer> activityForResult = new Stack<Integer>();

    /**
     * 为解决嵌套Fragment 收不到onActivityResult消息问题， Fragment需要调用onFragment
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        while (!activityForResult.isEmpty()) {
            Integer index = activityForResult.pop();

            FragmentManager manager = getChildFragmentManager();
            if (manager == null) { //说明为activity是manager
                manager = getFragmentManager();
            }
            if (manager != null) {
                @SuppressLint("RestrictedApi")
                List<Fragment> list = manager.getFragments();
                if (list != null && list.size() > index) {
                    list.get(index).onActivityResult(requestCode, resultCode, data);
                }
            } else {
                MLog.error(this, "嵌套fragment出现问题");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        Fragment fragment = getFragmentInParent();
        if (fragment == null) {
            super.startActivityForResult(intent, requestCode);
        } else {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    protected Fragment getFragmentInParent() {

        FragmentManager manager;

        Fragment parentFragment = this.getParentFragment();

        if (parentFragment != null) {
            manager = parentFragment.getChildFragmentManager();
        } else {
            manager = this.getFragmentManager();
        }

        @SuppressLint("RestrictedApi")
        List<Fragment> list = manager.getFragments();
        if (list != null) {
            int index = list.indexOf(this);

            if (parentFragment != null && parentFragment instanceof BaseMvpFragment) {
                ((BaseMvpFragment) parentFragment).activityForResult.push(index);
            }
        }
        return parentFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * --------------------------------------------------
     * -------------------------数据状态相关-----------------
     * --------------------------------------------------
     */

    private static final String STATUS_TAG = "STATUS_TAG";

    @Override
    public View.OnClickListener getLoadListener() {
        return v -> onReloadData();
    }

    /**
     * 重新加载页面数据：通常应用于无网络切换到有网络情况，重新刷新页面
     */
    public void onReloadData() {

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
    public void showNoData(CharSequence charSequence) {
        showNoData(0, charSequence);
    }

    @Override
    public void showNoLogin() {

    }

    @Override
    public void showLoading(int drawable, int tips) {
        showLoading(getView(), drawable, tips);
    }

    @Override
    public void showLoading(View view, int drawable, int tips) {
        showLoading(view, drawable, tips, 0);
    }

    @SuppressLint("ResourceType")
    protected void showLoading(View view, int drawable, int tips, int paddingBottom) {
        if (!checkActivityValid()) {
            return;
        }
        if (view == null) {
            return;
        }
        View status = view.findViewById(R.id.status_layout);
        if (status == null || status.getId() <= 0) {
            return;
        }
        Fragment fragment;
        if (paddingBottom == 0) {
            fragment = LoadingFragment.newInstance(drawable, tips);
        } else {
            fragment = LoadingFragment.newInstance(drawable, tips, paddingBottom);
        }
        getChildFragmentManager().beginTransaction().replace(status.getId(), fragment, STATUS_TAG)
                .commitAllowingStateLoss();
    }

    @Override
    public void showReload(int drawable, int tips) {
        showReload(getView(), drawable, tips);
    }

    @SuppressLint("ResourceType")
    @Override
    public void showReload(View view, int drawable, int tips) {
        if (!checkActivityValid()) {
            return;
        }

        if (view == null) {
            MLog.error(this, "xuwakao, showReload view is NULL");
            return;
        }
        View status = view.findViewById(R.id.status_layout);
        if (status == null || status.getId() <= 0) {
            MLog.error(this, "xuwakao, had not set layout id ");
            return;
        }
        ReloadFragment fragment = ReloadFragment.newInstance(drawable, tips);
        fragment.setListener(getLoadListener());
        getChildFragmentManager().beginTransaction().replace(status.getId(), fragment, STATUS_TAG)
                .commitAllowingStateLoss();
    }

    @Override
    public void showNoData(int drawable, CharSequence charSequence) {
        showNoData(getView(), 0, charSequence);
    }

    @SuppressLint("ResourceType")
    @Override
    public void showNoData(View view, int drawable, CharSequence charSequence) {
        if (!checkActivityValid()) {
            return;
        }

        if (view == null) {
            MLog.error(this, "xuwakao, showNoData view is NULL");
            return;
        }
        View status = view.findViewById(R.id.status_layout);
        if (status == null || status.getId() <= 0) {
            MLog.error(this, "xuwakao, had not set layout id ");
            return;
        }
        NoDataFragment fragment = NoDataFragment.newInstance(drawable, charSequence, isClickReload);
        fragment.setListener(getLoadListener());
        getChildFragmentManager().beginTransaction().replace(status.getId(), fragment, STATUS_TAG)
                .commitAllowingStateLoss();
    }

    @SuppressLint("ResourceType")
    @Override
    public void showNetworkErr() {
        if (!checkActivityValid()) {
            return;
        }

        if (getView() == null) {
            MLog.error(this, "xuwakao, showNetworkErr view is NULL");
            return;
        }
        View view = getView().findViewById(R.id.status_layout);
        if (view == null || view.getId() <= 0) {
            MLog.error(this, "xuwakao, had not set layout id ");
            return;
        }
        NetworkErrorFragment fragment = NetworkErrorFragment.newInstance(isClickReload);
        fragment.setListener(getLoadListener());
        getChildFragmentManager().beginTransaction().replace(view.getId(), fragment, STATUS_TAG)
                .commitAllowingStateLoss();
    }

    @Override
    public void hideStatus() {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(STATUS_TAG);
        if (fragment != null) {
            getChildFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        } else {
            MLog.warn(this, "xuwakao, status fragment is NULL");
        }
    }

    @Override
    public void showPageError(int tips) {
        showPageError(getView(), tips);
    }

    @Override
    public void showPageError(View view, int tips) {
        if (!checkActivityValid()) {
            return;
        }

        if (view == null) {
            MLog.error(this, "xuwakao, showReload view is NULL");
            return;
        }
        View more = view.findViewById(R.id.loading_more);
        if (more == null) {
            MLog.error(this, "xuwakao, showReload more is NULL");
            return;
        }
        StatusLayout statusLayout = (StatusLayout) more.getParent();
        statusLayout.showErrorPage(tips, getLoadMoreListener());
    }

    @Override
    public void showPageLoading() {
        if (!checkActivityValid()) {
            return;
        }

        if (getView() == null) {
            MLog.error(this, "xuwakao, showReload view is NULL");
            return;
        }
        View more = getView().findViewById(R.id.loading_more);
        if (more == null) {
            MLog.error(this, "xuwakao, showReload more is NULL");
            return;
        }
        StatusLayout statusLayout = (StatusLayout) more.getParent();
        statusLayout.showLoadMore();
    }

    protected boolean checkActivityValid() {
        return UIUtils.checkActivityValid(getActivity());
    }

    //====================================================================================//
    //==============================请求过滤条件筛选的返回================================//
    //====================================================================================//
    public boolean isTopActive() {// 到我的音乐后，RecommendFragment仍然是isTopActive()
        boolean isVisible = this.isResumed() && this.isVisible() && getUserVisibleHint();
        if (!isVisible) {
            return false;
        }

        FragmentManager manager = this.getChildFragmentManager();
        if (manager == null) {
            return true;
        }

        int count = manager.getFragments() == null ? 0 : manager.getFragments().size();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                Fragment fragment = manager.getFragments().get(i);
                if (fragment == null) {
                    return true;
                }

                if (fragment instanceof AbsStatusFragment) {
                    return true;
                }

                if (fragment.isVisible()) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * 当前网络是否可用
     */
    public boolean isNetworkAvailable() {
        return NetworkUtils.isNetworkStrictlyAvailable(getActivity());
    }

    /**
     * 通用消息提示
     */
    public void toast(int resId) {
        toast(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 通用消息提示
     */
    public void toast(String tip) {
        toast(tip, Toast.LENGTH_SHORT);
    }

    /**
     * 通用消息提示
     */
    public void toast(int resId, int length) {
        SingleToastUtil.showToast(BasicConfig.INSTANCE.getAppContext(), resId);
    }

    /**
     * 通用消息提示
     */
    public void toast(String tip, int length) {
        SingleToastUtil.showToast(BasicConfig.INSTANCE.getAppContext(), tip, length);
    }

    public boolean checkNetToast() {
        boolean flag = isNetworkAvailable();
        if (!flag) {
            toast(R.string.str_network_not_capable);
        }
        return flag;
    }

    protected DialogManager getDialogManager() {
        return getBaseActivity().getDialogManager();
    }

    public BaseMvpActivity getBaseActivity() {
        return (BaseMvpActivity) getActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showOptionsMenu();
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            AudioManager audio = (AudioManager) (getContext().getSystemService(Service.AUDIO_SERVICE));
            audio.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            AudioManager audio = (AudioManager) (getContext().getSystemService(Service.AUDIO_SERVICE));
            audio.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            return true;
        }
        return false;
    }

    protected void showOptionsMenu() {

    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        return false;
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable != null && disposable != null) {
            mCompositeDisposable.add(disposable);
        }
    }

    public void setClickReload(boolean clickReload) {
        isClickReload = clickReload;
    }
}