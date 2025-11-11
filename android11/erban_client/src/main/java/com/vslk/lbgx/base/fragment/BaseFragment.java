package com.vslk.lbgx.base.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.base.callback.IAcitivityBase;
import com.vslk.lbgx.base.callback.IDataStatus;
import com.vslk.lbgx.reciever.ConnectiveChangedReceiver;
import com.vslk.lbgx.ui.common.fragment.AbsStatusFragment;
import com.vslk.lbgx.ui.common.fragment.LoadingFragment;
import com.vslk.lbgx.ui.common.fragment.NetworkErrorFragment;
import com.vslk.lbgx.ui.common.fragment.NoDataFragment;
import com.vslk.lbgx.ui.common.fragment.ReloadFragment;
import com.vslk.lbgx.ui.common.widget.StatusLayout;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.listener.IDisposableAddListener;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.UIUtils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Stack;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author alvin hwang
 */
public abstract class BaseFragment extends RxFragment implements KeyEvent.Callback, IDataStatus,
        ConnectiveChangedReceiver.ConnectiveChangedListener, FragmentManager.OnBackStackChangedListener, IAcitivityBase, View.OnClickListener,IDisposableAddListener {

    //是否已经已完成（网络、数据库）请求数据
    protected boolean requested = false;

    //保留对最初的视图的软引用
    private SoftReference<View> mViewReference;

    protected CompositeDisposable mCompositeDisposable;

    protected View mView;
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        onInitArguments(getArguments());
    }

    public void onNewIntent(Intent intent) {

    }

    protected void onInitArguments(Bundle bundle) {

    }

    @Override
    public void onClick(View view) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int rootLayoutId = getRootLayoutId();
        mView = inflater.inflate(rootLayoutId, container, false);
        bindView();
        return mView;
    }

    public void bindView() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCompositeDisposable = new CompositeDisposable();
        CoreManager.addClient(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
        CoreManager.removeClient(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onFindViews();
        onSetListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        restoreState(savedInstanceState);
        initiate();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public abstract int getRootLayoutId();

    @Override
    public void onBackStackChanged() {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    /**
     * wifi 转 2G/3G/4G
     */
    @Override
    public void wifiChange2MobileData() {
    }

    public View getEmptyView(ViewGroup viewGroup, String s) {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_view, viewGroup, false);
        TextView view = inflate.findViewById(R.id.no_data_text);
        view.setText(s);
        return inflate;
    }

    public View getEmptyView(ViewGroup viewGroup, String s,@DrawableRes int id) {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_view, viewGroup, false);
        TextView view = inflate.findViewById(R.id.no_data_text);
        ImageView img = inflate.findViewById(R.id.no_data_icon);
        view.setText(s);
        img.setImageResource(id);
        return inflate;
    }

    /**
     * 有网络变为无网络
     */
    public void change2NoConnection() {
        if (isTopActive()) {
            MLog.debug(this, "change2NoConnection");
        }
    }

    /**
     * 连上wifi
     */
    public void connectiveWifi() {
        if (isTopActive()) {
            MLog.debug(this, "connectiveWifi");
        }
    }

    /**
     * 连上移动数据网络
     */
    public void connectiveMobileData() {
        if (isTopActive()) {
            MLog.debug(this, "connectiveMobileData");
        }
    }

    /**
     * 移动数据网络 改为连上wifi
     */
    public void mobileDataChange2Wifi() {
        if (isTopActive()) {
            MLog.debug(this, "mobileDataChange2Wifi");
        }
    }

    public Stack<Integer> activityForResult = new Stack<Integer>();

    /**
     * 为解决嵌套Fragment 收不到onActivityResult消息问题， Fragment需要调用onFragment
     *
     * @param requestCode
     * @param resultCode
     * @param data
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

            if (parentFragment != null && parentFragment instanceof BaseFragment) {
                ((BaseFragment) parentFragment).activityForResult.push(index);
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
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReloadData();
            }
        };
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
        getChildFragmentManager().beginTransaction().replace(status.getId(), fragment, STATUS_TAG).commitAllowingStateLoss();
    }

    @Override
    public void showReload(int drawable, int tips) {
        showReload(getView(), drawable, tips);
    }

    @Override
    public void showReload(View view, int drawable, int tips) {
        if (!checkActivityValid())
            return;

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
        getChildFragmentManager().beginTransaction().replace(status.getId(), fragment, STATUS_TAG).commitAllowingStateLoss();
    }

    @Override
    public void showNoData(int drawable, CharSequence charSequence) {
        showNoData(getView(), 0, charSequence);
    }

    @Override
    public void showNoData(View view, int drawable, CharSequence charSequence) {
        if (!checkActivityValid())
            return;

        if (view == null) {
            MLog.error(this, "xuwakao, showNoData view is NULL");
            return;
        }
        View status = view.findViewById(R.id.status_layout);
        if (status == null || status.getId() <= 0) {
            MLog.error(this, "xuwakao, had not set layout id ");
            return;
        }
        NoDataFragment fragment = NoDataFragment.newInstance(drawable, charSequence);
        fragment.setListener(getLoadListener());
        getChildFragmentManager().beginTransaction().replace(status.getId(), fragment, STATUS_TAG).commitAllowingStateLoss();
    }

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
        NetworkErrorFragment fragment = new NetworkErrorFragment();
        fragment.setListener(getLoadListener());
        getChildFragmentManager().beginTransaction().replace(view.getId(), fragment, STATUS_TAG).commitAllowingStateLoss();
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
        if (!checkActivityValid())
            return;

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
        if (!checkActivityValid())
            return;

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
     *
     * @return
     */
    public boolean isNetworkAvailable() {
        return NetworkUtils.isNetworkStrictlyAvailable(getActivity());
    }

    /**
     * 通用消息提示
     *
     * @param resId
     */
    public void toast(int resId) {
        toast(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 通用消息提示
     *
     * @param tip
     */
    public void toast(String tip) {
        toast(tip, Toast.LENGTH_SHORT);
    }

    /**
     * 通用消息提示
     *
     * @param resId
     * @param length
     */
    public void toast(int resId, int length) {
        SingleToastUtil.showToast(BasicConfig.INSTANCE.getAppContext(), resId);
    }

    /**
     * 通用消息提示
     *
     * @param tip
     * @param length
     */
    public void toast(String tip, int length) {
        SingleToastUtil.showToast(BasicConfig.INSTANCE.getAppContext(), tip, length);
    }

    public boolean checkNetToast() {
        boolean flag = isNetworkAvailable();
        if (!flag)
            toast(R.string.str_network_not_capable);
        return flag;
    }

    protected DialogManager getDialogManager() {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseMvpActivity) {
            return ((BaseMvpActivity) activity).getDialogManager();
        } else {
            return getBaseActivity().getDialogManager();
        }
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showOptionsMenu();
        }
//        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            AudioManager audio = (AudioManager) (getContext().getSystemService(Service.AUDIO_SERVICE));
//            audio.adjustStreamVolume(
//                    AudioManager.STREAM_VOICE_CALL,
//                    AudioManager.ADJUST_LOWER,
//                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//            AudioManager audio = (AudioManager) (getContext().getSystemService(Service.AUDIO_SERVICE));
//            audio.adjustStreamVolume(
//                    AudioManager.STREAM_VOICE_CALL,
//                    AudioManager.ADJUST_RAISE,
//                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//            return true;
//        }
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
        if (mCompositeDisposable != null && disposable != null)
            mCompositeDisposable.add(disposable);
    }
}