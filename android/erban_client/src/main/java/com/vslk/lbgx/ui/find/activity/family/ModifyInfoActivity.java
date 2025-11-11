package com.vslk.lbgx.ui.find.activity.family;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.presenter.find.family.FindFamilyPresenter;
import com.vslk.lbgx.presenter.find.family.IFindFamilyView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityModifyFamilyInfoBinding;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.find.family.IFamilyCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/21
 * 描述        查看、编辑家族界面
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(FindFamilyPresenter.class)
public class ModifyInfoActivity extends BaseMvpActivity<IFindFamilyView, FindFamilyPresenter> implements IFindFamilyView {

    private boolean isEdit;
    private boolean isMyFamily;
    private FamilyInfo mFamilyInfo;
    private ActivityModifyFamilyInfoBinding mBinding;

    public static void start(Context context, boolean isEdit, boolean isMyFamily) {
        Intent intent = new Intent(context, ModifyInfoActivity.class);
        intent.putExtra("isMyFamily", isMyFamily);
        intent.putExtra("isEdit", isEdit);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        isMyFamily = getIntent().getBooleanExtra("isMyFamily", false);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_modify_family_info);
        initiate();
        setOnListener();
    }

    private void initiate() {
        mBinding.setIsEdit(isEdit);
        mFamilyInfo = getCacheInfo();
        if (mFamilyInfo != null) {
            mBinding.setInfo(mFamilyInfo);
            if (isMyFamily && mFamilyInfo.getRoleStatus() == 1) {
                if (isEdit) {
                    mBinding.toolbar.setRightBtnTitle("保存");
                } else {
                    mBinding.toolbar.setRightBtnTitle("编辑");
                }
                mBinding.toolbar.setOnRightBtnClickListener(view -> editFamilyNotice());
            }
        }
    }

    private void setOnListener() {
        mBinding.toolbar.setOnBackBtnListener(view -> finish());
    }

    private void editFamilyNotice() {
        if (isEdit) {
            saveFamilyNotice();
        } else {
            ModifyInfoActivity.start(this, true, true);
        }
    }

    private void saveFamilyNotice() {
        String etDesc = mBinding.etDesc.getText().toString();
        if (StringUtils.isEmpty(etDesc)) {
            toast("请输入家族公告哦！");
            return;
        }
        if (etDesc.equals(getCacheInfo().getFamilyNotice())) {
            finish();
        }
        //保存修改后的公告
        showLoading();
        getMvpPresenter().editFamilyTeam(getCacheInfo(), IFamilyCoreClient.NOTICE, etDesc);
    }

    @Override
    public void editFamilyTeam(String familyDesc) {
        toast("修改成功");
        getDialogManager().dismissDialog();
        getCacheInfo().setFamilyNotice(familyDesc);

        finish();
        //更新界面通知
        CoreManager.notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_NOTIFY_MODIFY_INFO);
    }

    @Override
    public void editFamilyTeamFail(String msg) {
        getDialogManager().dismissDialog();
        toast(msg);
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onNotifyModifyInfo() {
        mBinding.setInfo(getCacheInfo());
    }

    private FamilyInfo getCacheInfo() {
        if (isMyFamily) {
            return CoreManager.getCore(IFamilyCore.class).getFamilyInfo();
        }
        return CoreManager.getCore(IFamilyCore.class).getCacheInfo();
    }

}
