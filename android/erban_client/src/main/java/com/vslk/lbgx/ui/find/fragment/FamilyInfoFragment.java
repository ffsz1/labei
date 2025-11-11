package com.vslk.lbgx.ui.find.fragment;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.vslk.lbgx.base.fragment.BaseFragment;
import com.vslk.lbgx.ui.find.activity.family.MemberListActivity;
import com.vslk.lbgx.ui.find.activity.family.ModifyInfoActivity;
import com.vslk.lbgx.ui.find.adapter.AvatarMemberAdapter;
import com.netease.nim.uikit.NimUIKit;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.FragmentFamilyInfoBinding;
import com.tongdaxing.erban.libcommon.utils.NetworkUtils;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.find.family.IFamilyCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/26
 * 描述        家族信息界面
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class FamilyInfoFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private FamilyInfo familyInfo;
    private AvatarMemberAdapter mAdapter;
    private FragmentFamilyInfoBinding mBinding;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_family_info;
    }

    @Override
    public void onFindViews() {

        mBinding = DataBindingUtil.bind(mView);
    }

    @Override
    public void initiate() {
        familyInfo = CoreManager.getCore(IFamilyCore.class).getFamilyInfo();
        if (familyInfo != null) {
            mBinding.content.setInfo(familyInfo);
            mBinding.content.setIsMyFamily(true);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), HORIZONTAL, false);
            mBinding.content.recyclerView.setLayoutManager(layoutManager);
            mAdapter = new AvatarMemberAdapter();
            mBinding.content.recyclerView.setAdapter(mAdapter);
            mAdapter.setNewData(familyInfo.getFamilyUsersDTOS());
        }
    }

    @Override
    public void onSetListener() {
        mBinding.content.setClick(this);
        mBinding.refreshLayout.setOnRefreshListener(this);
        mBinding.content.enterChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enter_chat:
                if (familyInfo != null) {
                    NimUIKit.startTeamSession(getActivity(), String.valueOf(familyInfo.getRoomId()));
                }
                break;
            case R.id.family_desc:
                ModifyInfoActivity.start(getActivity(), false, true);
                break;
            case R.id.family_count:
                MemberListActivity.start(getActivity(), true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            CoreManager.getCore(IFamilyCore.class).checkFamilyJoin();
        } else {
            mBinding.refreshLayout.setRefreshing(false);
        }
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onCheckFamilyJoin(FamilyInfo familyInfo) {
        mBinding.refreshLayout.setRefreshing(false);
        if (familyInfo != null) {
            this.familyInfo = familyInfo;
            mBinding.content.setInfo(familyInfo);
            mBinding.content.setIsMyFamily(true);
            mAdapter.setNewData(this.familyInfo.getFamilyUsersDTOS());
        }
        CoreManager.notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_REFRESH_INFO);
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onNotifyModifyInfo() {
        familyInfo = CoreManager.getCore(IFamilyCore.class).getFamilyInfo();
        mBinding.content.setInfo(familyInfo);
    }

    public static Fragment newInstance() {
        return new FamilyInfoFragment();
    }
}
