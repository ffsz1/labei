package com.vslk.lbgx.ui.find.activity.family;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.vslk.lbgx.presenter.find.family.IFindFamilyView;
import com.vslk.lbgx.ui.common.permission.PermissionActivity;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityFamilyManagerBinding;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.file.IFileCore;
import com.tongdaxing.xchat_core.file.IFileCoreClient;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.find.family.IFamilyCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;
import com.tongdaxing.xchat_framework.util.util.file.JXFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.vslk.lbgx.constant.Extras.CHOICE_AVATAR;
import static com.vslk.lbgx.constant.Extras.CHOICE_BG;
import static com.vslk.lbgx.constant.Extras.NO_SELECT;
import static com.tongdaxing.xchat_core.find.family.IFamilyCoreClient.BG;
import static com.tongdaxing.xchat_core.find.family.IFamilyCoreClient.FORBID_TIME;
import static com.tongdaxing.xchat_core.find.family.IFamilyCoreClient.LOGO;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/21
 * 描述        家族管理
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class FamilyManagerActivity extends TakePhotoActivity implements IFindFamilyView, View.OnClickListener {

    private int isType = NO_SELECT;
    private ActivityFamilyManagerBinding mBinding;

    private static final String CAMERA_PREFIX = "picture_";
    private List<ButtonItem> buttonItems = new ArrayList<>();
    private FamilyInfo familyInfo;

    public static void start(Context context) {
        Intent intent = new Intent(context, FamilyManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_family_manager);
        mBinding.setClick(this);
        initView();
    }

    private void initView() {
        familyInfo = getCacheInfo();
        if (familyInfo != null) {
            mBinding.setInfo(familyInfo);
            mBinding.familyId.setText("ID:" + familyInfo.getFamilyId());
            mBinding.familyTime.setText("创建于/ " + TimeUtils.getDateTimeString(familyInfo.getTimes(), "yyyy-MM-dd"));
            mBinding.remind.setSelected(familyInfo.getOpe() == 1);
        }
        mBinding.toolbar.setOnBackBtnListener(view -> finish());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.manager:
                FamilySettingsActivity.start(this, false);
                break;
            case R.id.exit:
                exitFamily();
                break;
            case R.id.applyMsg:
                ApplyMsgListActivity.start(this);
                break;
            case R.id.avatar_container:
                if (modifyTime()) {
                    choiceFamilyImg(CHOICE_AVATAR);
                } else {
                    toast("每个月只能更换一次家族头像!");
                }
                break;
            case R.id.familyBg:
                if (modifyTime()) {
                    choiceFamilyImg(CHOICE_BG);
                } else {
                    toast("每个月只能更换一次家族背景!");
                }
                break;
            case R.id.remind:
                changeRemind();
                break;
            default:
                break;
        }
    }

    /*--------------------------select avatar or family_bg -------------------------------------------------*/

    private void choiceFamilyImg(int choiceType) {
        FamilyInfo familyInfo = getCacheInfo();
        if (familyInfo == null) {
            return;
        }
        this.isType = choiceType;
        if (ListUtils.isListEmpty(buttonItems)) {
            initBottomItem();
        }
        getDialogManager().showCommonPopupDialog(buttonItems, "取消", false);
    }

    private void initBottomItem() {
        ButtonItem buttonItem = new ButtonItem("拍照上传", this::checkPermissionAndStartCamera);
        ButtonItem buttonItem1 = new ButtonItem("本地相册", () -> {
            String mCameraCapturingName = CAMERA_PREFIX + System.currentTimeMillis() + ".jpg";
            File cameraOutFile = JXFileUtils.getTempFile(this, mCameraCapturingName);
            if (!cameraOutFile.getParentFile().exists()) {
                cameraOutFile.getParentFile().mkdirs();
            }
            Uri uri = Uri.fromFile(cameraOutFile);
            CompressConfig compressConfig = new CompressConfig.Builder().create();
            getTakePhoto().onEnableCompress(compressConfig, true);
            CropOptions options = new CropOptions.Builder().setWithOwnCrop(true).create();
            getTakePhoto().onPickFromGalleryWithCrop(uri, options);

        });
        buttonItems.add(buttonItem);
        buttonItems.add(buttonItem1);
    }

    private void checkPermissionAndStartCamera() {
        //低版本授权检查
        checkPermission(checkPermissionListener, R.string.ask_camera, android.Manifest.permission.CAMERA);
    }

    PermissionActivity.CheckPermListener checkPermissionListener = this::takePhoto;

    private void takePhoto() {
        String mCameraCapturingName = CAMERA_PREFIX + System.currentTimeMillis() + ".jpg";
        File cameraOutFile = JXFileUtils.getTempFile(this, mCameraCapturingName);
        if (!cameraOutFile.getParentFile().exists()) {
            cameraOutFile.getParentFile().mkdirs();
        }
        Uri uri = Uri.fromFile(cameraOutFile);
        CompressConfig compressConfig = new CompressConfig.Builder().create();
        getTakePhoto().onEnableCompress(compressConfig, false);
        CropOptions options = new CropOptions.Builder().setWithOwnCrop(true).create();
        getTakePhoto().onPickFromCaptureWithCrop(uri, options);
    }

    @Override
    public void takeSuccess(TResult result) {
        getDialogManager().showProgressDialog(this, "请稍后...");
        CoreManager.getCore(IFileCore.class).upload(new File(result.getImage().getCompressPath()));
    }

    @Override
    public void takeFail(TResult result, String msg) {
//        toast(msg);
    }

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUpload(String url) {
        getDialogManager().dismissDialog();
        if (isType != NO_SELECT) {
            if (isType == CHOICE_BG) {
                CoreManager.getCore(IFamilyCore.class).editFamilyTeam(familyInfo, BG, url);
            } else if (isType == CHOICE_AVATAR) {
                CoreManager.getCore(IFamilyCore.class).editFamilyTeam(familyInfo, LOGO, url);
            }
        }
    }

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUploadFail() {
        toast("上传失败");
        getDialogManager().dismissDialog();
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onModifyInfo(int type, String url) {
        getDialogManager().dismissDialog();
        toast(type == BG ? "设置背景成功" : "设置头像成功");
        if (type == BG) {
            familyInfo.setFamilyBg(url);
        } else {
            familyInfo.setFamilyLogo(url);
            mBinding.setInfo(familyInfo);
        }
        CoreManager.notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_NOTIFY_MODIFY_INFO);
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onModifyInfoFail(String message) {
        getDialogManager().dismissDialog();
        toast(message);
    }

    private boolean modifyTime() {
        return System.currentTimeMillis() - familyInfo.getUpdateTime() >= FORBID_TIME;
    }

    /*--------------------------select avatar or family_bg -------------------------------------------------*/

    private void changeRemind() {
        FamilyInfo familyInfo = getCacheInfo();
        if (familyInfo == null) {
            return;
        }
        getDialogManager().showProgressDialog(this);
        CoreManager.getCore(IFamilyCore.class).setMsgNotify(familyInfo);
    }

    private void exitFamily() {
        FamilyInfo familyInfo = getCacheInfo();
        if (familyInfo == null) {
            return;
        }
        getDialogManager().showYuMengOkCancelDialog("退出家族风险告知", getString(R.string.exit_family_info), "确认退出", "再考虑下",
                new DialogManager.OkCancelDialogListener() {

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onOk() {
                        getDialogManager().showProgressDialog(FamilyManagerActivity.this);
                        CoreManager.getCore(IFamilyCore.class).exitFamily(familyInfo);
                    }
                });
    }


    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onExitFamily() {
        getDialogManager().dismissDialog();
        toast("退出申请已提交，等待族长审核即可退出家族");
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onExitFamilyFail(String msg) {
        getDialogManager().dismissDialog();
        toast(msg);
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onSetMsgNotify() {
        getDialogManager().dismissDialog();
        FamilyInfo cacheInfo = getCacheInfo();
        cacheInfo.setOpe(cacheInfo.getOpe() == 1 ? 2 : 1);
        CoreManager.getCore(IFamilyCore.class).setFamilyInfo(cacheInfo);
        //改变显示的状态
        mBinding.remind.setSelected(cacheInfo.getOpe() == 1);
        //更新云信提醒状态
        CoreManager.getCore(IFamilyCore.class).muteTeam(cacheInfo.getRoomId(), cacheInfo.getOpe());
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onSetMsgNotifyFail(String msg) {
        getDialogManager().dismissDialog();
        toast(msg);
    }

    private FamilyInfo getCacheInfo() {
        return CoreManager.getCore(IFamilyCore.class).getFamilyInfo();
    }

}
