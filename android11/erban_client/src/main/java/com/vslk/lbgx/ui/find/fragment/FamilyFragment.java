package com.vslk.lbgx.ui.find.fragment;

import android.support.v4.app.Fragment;

import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.presenter.find.family.FindFamilyPresenter;
import com.vslk.lbgx.presenter.find.family.IFindFamilyView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.NetworkUtils;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.find.family.IFamilyCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/20
 * 描述        家族列表界面
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(FindFamilyPresenter.class)
public class FamilyFragment extends BaseMvpFragment<IFindFamilyView, FindFamilyPresenter> implements IFindFamilyView {

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_family;
    }

    @Override
    public void onFindViews() {

    }

    @Override
    public void onSetListener() {

    }

    @Override
    public void initiate() {

    }

    @Override
    protected void onLazyLoadData() {
        showLoading();
        getMvpPresenter().checkFamilyJoin();
    }

    @Override
    public void getCheckFamilyJoinSuccess(FamilyInfo info) {
        hideStatus();
        showContainer(info);
    }

    @Override
    public void getCheckFamilyJoinFail(String msg) {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            showNoData("获取数据失败");
        } else {
            showNetworkErr();
        }
        toast(msg);
    }

    private void showContainer(FamilyInfo info) {
        //通知更新顶部栏功能
        CoreManager.notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_GET_FAMILY_INFO);
        //切换界面显示内容
        getChildFragmentManager().beginTransaction().replace(R.id.container,
                info != null ? FamilyInfoFragment.newInstance() : FamilyListFragment.newInstance(),
                info != null ? FamilyInfoFragment.class.getSimpleName() : FamilyListFragment.class.getSimpleName())
                .commitAllowingStateLoss();
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onRefreshInfo() {
        FamilyInfo familyInfo = CoreManager.getCore(IFamilyCore.class).getFamilyInfo();
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof FamilyInfoFragment && familyInfo == null) {
            getChildFragmentManager().beginTransaction().replace(R.id.container,
                    FamilyListFragment.newInstance(), FamilyListFragment.class.getSimpleName()).commitAllowingStateLoss();
        } else if(fragment instanceof  FamilyListFragment && familyInfo != null){
            getChildFragmentManager().beginTransaction().replace(R.id.container,
                    FamilyInfoFragment.newInstance(), FamilyInfoFragment.class.getSimpleName()).commitAllowingStateLoss();
        }
    }

    @Override
    public void onReloadData() {
        showLoading();
        getMvpPresenter().checkFamilyJoin();
    }
}
