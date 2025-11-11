package com.vslk.lbgx.ui.find.activity.family;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.presenter.find.family.FindFamilyPresenter;
import com.vslk.lbgx.presenter.find.family.IFindFamilyView;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.find.adapter.MemberListAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityMemberManagerBinding;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.utils.NetworkUtils;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.find.family.bean.MemberInfo;
import com.tongdaxing.xchat_core.find.family.bean.MemberListInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tongdaxing.xchat_core.Constants.PAGE_START_ZERO;
import static com.tongdaxing.xchat_core.find.family.IFamilyCoreClient.ADMIN;
import static com.tongdaxing.xchat_core.find.family.IFamilyCoreClient.FAMILY_MANAGER_MAX_COUNT;
import static com.tongdaxing.xchat_core.find.family.IFamilyCoreClient.NORMAL;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/27
 * 描述        成员管理
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(FindFamilyPresenter.class)
public class MemberManagerActivity extends BaseMvpActivity<IFindFamilyView, FindFamilyPresenter> implements
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener, IFindFamilyView {

    private boolean isManager;
    private FamilyInfo cacheInfo;
    private MemberListAdapter mAdapter;
    private int currentPage = PAGE_START_ZERO;
    private ActivityMemberManagerBinding mBinding;
    private static final String IS_MANAGER = "isManager";

    /**
     * 记录选择的数量  最大值为4
     */
    private int choiceMember = FAMILY_MANAGER_MAX_COUNT;
    private int tempChoiceCount = FAMILY_MANAGER_MAX_COUNT;

    private Map<Integer, MemberInfo> memberInfos = new HashMap<>();

    public static void start(Context context, boolean isManager) {
        Intent intent = new Intent(context, MemberManagerActivity.class);
        intent.putExtra(IS_MANAGER, isManager);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isManager = getIntent().getBooleanExtra(IS_MANAGER, false);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_member_manager);
        mBinding.toolbar.setTitle(isManager ? getString(R.string.manager_member) : getString(R.string.appoint));
        mBinding.toolbar.setRightTitleBtnVisibility(isManager ? View.GONE : View.VISIBLE);
        initiate();
        initData();
        onSetListener();
    }

    private void initiate() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        ((DefaultItemAnimator)mBinding.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mAdapter = new MemberListAdapter(isManager);
        mAdapter.disableLoadMoreIfNotFullPage(mBinding.recyclerView);
        mBinding.recyclerView.setAdapter(mAdapter);

        cacheInfo = CoreManager.getCore(IFamilyCore.class).getFamilyInfo();
        if (cacheInfo != null) {
            showLoading();
            mAdapter.setRoleStatus(cacheInfo.getRoleStatus());
            getMvpPresenter().getMemberList(currentPage, cacheInfo);
        }
    }

    private void initData() {
    }

    private void onSetListener() {
        mAdapter.setOnItemChildClickListener(this);
        mBinding.refreshLayout.setOnRefreshListener(this);
        mBinding.toolbar.setOnBackBtnListener(view -> finish());
        mBinding.toolbar.setOnRightBtnClickListener(view -> saveManagers());
        mAdapter.setOnLoadMoreListener(this, mBinding.recyclerView);
    }

    @Override
    public void onLoadMoreRequested() {
        if (NetworkUtils.isNetworkAvailable(this) && cacheInfo != null) {
            currentPage++;
            getMvpPresenter().getMemberList(currentPage, cacheInfo);
        } else {
            mAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void onRefresh() {
        this.currentPage = PAGE_START_ZERO;
        if (NetworkUtils.isNetworkAvailable(this) && cacheInfo != null) {
            getMvpPresenter().getMemberList(currentPage, cacheInfo);
        } else {
            showNetworkErr();
            mBinding.refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void getMemberListSuccess(MemberListInfo memberListInfo) {
        //首页
        if (currentPage == PAGE_START_ZERO) {
            hideStatus();
            mBinding.refreshLayout.setRefreshing(false);
            if (memberListInfo != null && !ListUtils.isListEmpty(memberListInfo.getFamilyTeamJoinDTOS())) {
                mAdapter.setNewData(memberListInfo.getFamilyTeamJoinDTOS());
                //查看当前设置副族长数量
                checkManagerCount(memberListInfo.getFamilyTeamJoinDTOS());
            } else {
                showNoData("暂无任何成员信息");
            }
        } else {
            //其他页
            if (memberListInfo != null && !ListUtils.isListEmpty(memberListInfo.getFamilyTeamJoinDTOS())) {
                mAdapter.loadMoreComplete();
                mAdapter.addData(memberListInfo.getFamilyTeamJoinDTOS());
            } else {
                mAdapter.loadMoreEnd(true);
            }
        }
        mAdapter.disableLoadMoreIfNotFullPage(mBinding.recyclerView);
    }

    private void checkManagerCount(List<MemberInfo> memberInfos) {
        if (!ListUtils.isListEmpty(memberInfos)) {
            this.choiceMember = FAMILY_MANAGER_MAX_COUNT;
            this.tempChoiceCount = FAMILY_MANAGER_MAX_COUNT;
            for (MemberInfo memberInfo : memberInfos) {
                if (memberInfo.getRoleStatus() == ADMIN) {
                    choiceMember--;
                    tempChoiceCount--;
                    if (choiceMember <= 0) {
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void getMemberListFail(String msg) {
        //首页
        if (currentPage == PAGE_START_ZERO) {
            hideStatus();
            mBinding.refreshLayout.setRefreshing(false);
            if (NetworkUtils.isNetworkAvailable(this)) {
                showNoData("暂无任何成员信息");
            } else {
                showNetworkErr();
            }
        } else {
            //其他页
            mAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void onReloadDate() {
        this.currentPage = PAGE_START_ZERO;
        if (NetworkUtils.isNetworkAvailable(this) && cacheInfo != null) {
            getMvpPresenter().getMemberList(currentPage, cacheInfo);
        } else {
            showNoData("暂无任何成员信息");
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.remove:
                removeMember(position);
                break;
            case R.id.appointment:
                appointmentMember(position);
                break;
            default:
                break;
        }
    }

    /**
     * 踢出成员
     */
    private void removeMember(int position) {
        if (mAdapter == null || ListUtils.isListEmpty(mAdapter.getData()) || cacheInfo == null) {
            return;
        }
        MemberInfo memberInfo = mAdapter.getData().get(position);
        String msgContent;
        if (isManager) {
            msgContent = "您确定要踢出成员 " + memberInfo.getNike() + " 吗？";
        } else {
            msgContent = "您确定要移除成员 " + memberInfo.getNike() + "的副族长职位吗？";
        }
        getDialogManager().showYuMengOkCancelDialog(msgContent, true, new DialogManager.OkCancelDialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                getDialogManager().showProgressDialog(MemberManagerActivity.this);
                if (isManager) {
                    getMvpPresenter().removeMember(cacheInfo, memberInfo, position);
                } else {
                    getMvpPresenter().removeAdmin(cacheInfo, memberInfo, position);
                }
            }
        });
    }

    @Override
    public void removeMemberSuccess(int position) {
        getDialogManager().dismissDialog();
        mAdapter.remove(position);
        toast("踢出用户成功");
    }

    @Override
    public void removeMemberFail(String msg) {
        getDialogManager().dismissDialog();
        toast(msg);
    }

    @Override
    public void removeAdmin(int position) {
        getDialogManager().dismissDialog();
        toast("移除管理员权限成功");
        MemberInfo memberInfo = mAdapter.getData().get(position);
        memberInfo.setRoleStatus(NORMAL);
        mAdapter.setData(position, memberInfo);

        this.choiceMember++;
        this.tempChoiceCount++;
    }

    @Override
    public void removeAdminFail(String msg) {
        getDialogManager().dismissDialog();
        toast(msg);
    }

    /**
     * 任命成员
     */
    private void appointmentMember(int position) {
        if (mAdapter == null || ListUtils.isListEmpty(mAdapter.getData())) {
            return;
        }
        MemberInfo memberInfo = mAdapter.getData().get(position);
        if (memberInfo.isCheck()) {
            tempChoiceCount++;
            memberInfo.setCheck(false);
            mAdapter.setData(position, memberInfo);
            memberInfos.remove(position);
        } else {
            if (tempChoiceCount <= 0) {
                toast("最多只能设置四个副族长");
                return;
            }
            tempChoiceCount--;
            memberInfo.setCheck(true);
            mAdapter.setData(position, memberInfo);
            memberInfos.put(position, memberInfo);
        }
    }

    private void saveManagers() {
        if (cacheInfo == null) {
            return;
        }
        if (choiceMember == 0) {
            toast("当前无法再设置家族副族长，最多只能设置4个副族长");
            return;
        }
        if (memberInfos.size() == 0) {
            toast("您未选择任何家族成员");
            return;
        }
        getDialogManager().showYuMengOkCancelDialog("你确定要设置当前选中的家族成员为副族长吗？", true, new DialogManager.OkCancelDialogListener() {

            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                getDialogManager().showProgressDialog(MemberManagerActivity.this, "请稍后...", false);
                getMvpPresenter().setupAdministrator(cacheInfo, map2Array());
            }
        });
    }

    @Override
    public void setupAdministrator() {
        getDialogManager().dismissDialog();
        toast("设置副族长职位成功");

        //改变列表状态
        for (Map.Entry<Integer, MemberInfo> entry : memberInfos.entrySet()) {
            int position = entry.getKey();
            MemberInfo memberInfo = entry.getValue();
            memberInfo.setRoleStatus(ADMIN);
            memberInfo.setCheck(false);
            mAdapter.setData(position, memberInfo);
        }
        //清空集合内容  无需清理当前数量标记
        memberInfos.clear();
    }

    @Override
    public void setupAdministratorFail(String msg) {
        getDialogManager().dismissDialog();
        toast(msg);
    }

    private String map2Array() {
        StringBuilder builder = new StringBuilder();
        for (MemberInfo memberInfo : memberInfos.values()) {
            builder.append(",").append(memberInfo.getUid());
        }
        String userIds = builder.toString();
        return userIds.substring(1, userIds.length());
    }
}
