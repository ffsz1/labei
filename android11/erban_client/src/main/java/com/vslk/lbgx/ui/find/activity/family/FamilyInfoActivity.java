package com.vslk.lbgx.ui.find.activity.family;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jph.takephoto.app.TakePhotoActivity;
import com.vslk.lbgx.presenter.find.family.IFindFamilyView;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.find.adapter.AvatarMemberAdapter;
import com.netease.nim.uikit.NimUIKit;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityFamilyInfoBinding;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.find.family.IFamilyCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/21
 * 描述        家族信息界面
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class FamilyInfoActivity extends TakePhotoActivity implements IFindFamilyView {

    private boolean isMyFamily;
    private FamilyInfo familyInfo;
    private static final String IS_MY_FAMILY = "isMyFamily";

    private AvatarMemberAdapter mAdapter;
    private ActivityFamilyInfoBinding mBinding;

    public static void start(Context context, boolean isMyFamily) {
        Intent intent = new Intent(context, FamilyInfoActivity.class);
        intent.putExtra(IS_MY_FAMILY, isMyFamily);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isMyFamily = getIntent().getBooleanExtra(IS_MY_FAMILY, false);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_family_info);
        familyInfo = cacheInfo();
        initView();
        setOnListener();
    }

    private void initView() {
        mBinding.content.setIsMyFamily(isMyFamily);
        if (familyInfo != null) {
            mBinding.content.setInfo(familyInfo);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, HORIZONTAL, false);
            mBinding.content.recyclerView.setLayoutManager(layoutManager);
            mAdapter = new AvatarMemberAdapter();
            mBinding.content.recyclerView.setAdapter(mAdapter);

            mAdapter.setNewData(familyInfo.getFamilyUsersDTOS());
        }

        CoreManager.getCore(IFamilyCore.class).checkFamilyJoin();
    }

    private void setOnListener() {
        back(mBinding.toolbar);
        mBinding.content.setClick(this);
        mBinding.apply.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enter_chat:
                if (familyInfo != null && isMyFamily) {
                    NimUIKit.startTeamSession(this, String.valueOf(familyInfo.getRoomId()));
                }
                break;
            case R.id.family_desc:
                ModifyInfoActivity.start(this, false, isMyFamily);
                break;
            case R.id.family_count:
                MemberListActivity.start(this, CoreManager.getCore(IFamilyCore.class).checkIsMyFamily(familyInfo));
                break;
            case R.id.apply:
                applyEnterFamily();
                break;
            default:
                break;
        }
    }

    private void applyEnterFamily() {
        if (familyInfo == null) {
            return;
        }
        String title = "您确定要加入 " + familyInfo.getFamilyName() + " 吗？";
        getDialogManager().showYuMengOkCancelDialog(title, true, new DialogManager.OkCancelDialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                getDialogManager().showProgressDialog(FamilyInfoActivity.this);
                CoreManager.getCore(IFamilyCore.class).applyJoinFamilyTeam(familyInfo);
            }
        });
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onCheckFamilyJoin(FamilyInfo familyInfo) {
        if (familyInfo == null) {
            mBinding.apply.setVisibility(View.VISIBLE);
        }
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onApplyJoinFamilyTeam() {
        getDialogManager().dismissDialog();
        mBinding.apply.setVisibility(View.GONE);
        toast("申请加入家族已提交，等待族长进行审核...");
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onApplyJoinFamilyTeamFail(String errorMsg) {
        getDialogManager().dismissDialog();
        toast(errorMsg);
    }

    private FamilyInfo cacheInfo() {
        if (isMyFamily) {
            return CoreManager.getCore(IFamilyCore.class).getFamilyInfo();
        }
        return CoreManager.getCore(IFamilyCore.class).getCacheInfo();
    }

}
