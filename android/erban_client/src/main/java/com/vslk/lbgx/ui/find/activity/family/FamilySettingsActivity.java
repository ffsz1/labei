package com.vslk.lbgx.ui.find.activity.family;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.presenter.find.family.FindFamilyPresenter;
import com.vslk.lbgx.presenter.find.family.IFindFamilyView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityFamilySettingBinding;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/21
 * 描述        族长管理设置界面
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(FindFamilyPresenter.class)
public class FamilySettingsActivity extends BaseMvpActivity<IFindFamilyView, FindFamilyPresenter> implements View.OnClickListener, IFindFamilyView {

    private boolean isVerify;
    private static final String IS_VERIFY = "is_verify";
    private ActivityFamilySettingBinding mBinding;

    public static void start(Context context, boolean isVerify) {
        Intent intent = new Intent(context, FamilySettingsActivity.class);
        intent.putExtra(IS_VERIFY, isVerify);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isVerify = getIntent().getBooleanExtra(IS_VERIFY, false);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_family_setting);
        mBinding.toolbar.setTitle(isVerify ? getString(R.string.join_method) : getString(R.string.family_manager));
        mBinding.setIsVerify(isVerify);
        mBinding.setClick(this);

        mBinding.toolbar.setOnBackBtnListener(view -> finish());

        FamilyInfo info = getFamilyInfo();
        if (info != null) {
            mBinding.setInfo(info);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.kick_out:
                MemberManagerActivity.start(this, true);
                break;
            case R.id.vice_manager:
                MemberManagerActivity.start(this, false);
                break;
            case R.id.join_method:
                FamilySettingsActivity.start(this, true);
                break;
            case R.id.no_verify_content:
                setVerification(false);
                break;
            case R.id.verify_content:
                setVerification(true);
                break;
            default:
                break;
        }
    }

    /**
     * 设置加入方式
     */
    private void setVerification(boolean verification) {
        FamilyInfo familyInfo = getFamilyInfo();
        if (familyInfo == null) {
            return;
        }
        if (verification) {
            if (familyInfo.getVerification() != 1) {
                setApplyMethod(familyInfo);
            }
        } else {
            if (familyInfo.getVerification() != 0) {
                setApplyMethod(familyInfo);
            }
        }
    }

    private void setApplyMethod(FamilyInfo familyInfo) {
        getDialogManager().showProgressDialog(this);
        getMvpPresenter().setApplyJoinMethod(familyInfo, familyInfo.getVerification() == 0 ? 1 : 0);
    }

    @Override
    public void setApplyJoinMethod() {
        getDialogManager().dismissDialog();
        FamilyInfo familyInfo = getFamilyInfo();
        familyInfo.setVerification(familyInfo.getVerification() == 0 ? 1 : 0);
        CoreManager.getCore(IFamilyCore.class).setFamilyInfo(familyInfo);

        //改变状态
        mBinding.setInfo(familyInfo);
    }

    @Override
    public void setApplyJoinMethodFail(String msg) {
        getDialogManager().dismissDialog();
        toast(msg);
    }

    private FamilyInfo getFamilyInfo() {
        return CoreManager.getCore(IFamilyCore.class).getFamilyInfo();
    }

}
