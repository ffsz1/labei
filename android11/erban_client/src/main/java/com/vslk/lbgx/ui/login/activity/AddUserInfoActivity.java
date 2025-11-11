package com.vslk.lbgx.ui.login.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.vslk.lbgx.ui.common.permission.PermissionActivity;
import com.vslk.lbgx.ui.common.widget.dialog.ShowDefaultHeaderPopupDialog;
import com.vslk.lbgx.ui.me.user.activity.ModifyInfoActivity;
import com.vslk.lbgx.ui.widget.TextWatcherListener;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityAddinfoBinding;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.auth.ThirdUserInfo;
import com.tongdaxing.xchat_core.file.IFileCore;
import com.tongdaxing.xchat_core.file.IFileCoreClient;
import com.tongdaxing.xchat_core.linked.ILinkedCore;
import com.tongdaxing.xchat_core.linked.LinkedInfo;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;
import com.tongdaxing.xchat_framework.util.util.file.JXFileUtils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouxiangfeng
 * @date 2017/5/12
 */

public class AddUserInfoActivity extends TakePhotoActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, ShowDefaultHeaderPopupDialog.OnDefaultHeaderItemSelectedListener {


    private static final String CAMERA_PREFIX = "picture_";

    private DatePickerDialog datePickerDialog;
    private String avatarUrl;
    private String mCameraCapturingName;
    private File cameraOutFile;
    private File photoFile;
    private ActivityAddinfoBinding addinfoBinding;
    private int sexCode = 2;//默认选中女生
    private String data = "757353600000";
    private String birth;
    private List<ButtonItem> buttonItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_addinfo);
        addinfoBinding.setClick(this);
        setAppToolBar();
        setSwipeBackEnable(false);
        addWXUserInfo();
        setOnListener();
        init();
    }

    private void setAppToolBar() {
        TextPaint tp = addinfoBinding.toolbar.getTvTitle().getPaint();
        tp.setFakeBoldText(true);
    }

    private boolean alreadySelectedPic = false;

    /**
     * 检测下一步是否可以点击
     */
    public boolean checkNextStep() {
        return (StringUtils.isNotEmpty(addinfoBinding.etNick.getText().toString()))
                && (StringUtils.isNotEmpty(addinfoBinding.tvBirth.getText().toString()) &&
                !addinfoBinding.tvBirth.getText().toString().equals("请选择出生年月")) && alreadySelectedPic;
    }

    @Override
    protected boolean needSteepStateBar() {
        return true;
    }

    private void addWXUserInfo() {
        ThirdUserInfo thirdUserInfo = CoreManager.getCore(IAuthCore.class).getThirdUserInfo();
        if (thirdUserInfo != null) {
            avatarUrl = thirdUserInfo.getUserIcon();
            alreadySelectedPic = true;
            if (!StringUtil.isEmpty(thirdUserInfo.getUserGender())) {
                if (thirdUserInfo.getUserGender().equals("m")) {
                    sexCode = 1;
                    addinfoBinding.rbMan.setChecked(true);
                } else {
                    sexCode = 2;
                    addinfoBinding.rbFemale.setChecked(true);
                }
            } else {
                sexCode = 1;
                addinfoBinding.rbMan.setChecked(true);
            }
            String nick = thirdUserInfo.getUserName();
            if (!StringUtil.isEmpty(nick) && nick.length() > 15) {
                addinfoBinding.etNick.setText(nick.substring(0, 15));
            } else {
                addinfoBinding.etNick.setText(nick);
            }
            if (thirdUserInfo.getBirth() > 0) {
                String birth = TimeUtils.getDateTimeString(thirdUserInfo.getBirth(), "yyyy-MM-dd");
                addinfoBinding.tvBirth.setText(birth);
                this.birth = birth;
            }
            ImageLoadUtils.loadCircleImage(this, avatarUrl, addinfoBinding.civAvatar, R.drawable.ic_default_avatar);
        }
    }

    private void init() {
        int year = TimeUtils.getYear(Long.parseLong(data));
        int month = TimeUtils.getMonth(Long.parseLong(data));
        int day = TimeUtils.getDayOfMonth(Long.parseLong(data));
        datePickerDialog = DatePickerDialog.newInstance(this, year, (month - 1), day, true);

        initButtonItems();
    }

    private void initButtonItems() {
        ButtonItem upItem = new ButtonItem("拍照上传", this::checkPermissionAndStartCamera);
        ButtonItem localItem = new ButtonItem("本地相册", () -> {
            String mCameraCapturingName = CAMERA_PREFIX + System.currentTimeMillis() + ".jpg";
            File cameraOutFile = JXFileUtils.getTempFile(AddUserInfoActivity.this, mCameraCapturingName);
            if (!cameraOutFile.getParentFile().exists()) {
                cameraOutFile.getParentFile().mkdirs();
            }
            Uri uri = Uri.fromFile(cameraOutFile);
            CompressConfig compressConfig = new CompressConfig.Builder().create();
            getTakePhoto().onEnableCompress(compressConfig, true);
            CropOptions options = new CropOptions.Builder().setWithOwnCrop(true).create();
            getTakePhoto().onPickFromGalleryWithCrop(uri, options);
        });
        buttonItems.add(upItem);
        buttonItems.add(localItem);
    }


    private void setOnListener() {
        addinfoBinding.toolbar.setOnBackBtnListener(view -> {
            CoreManager.getCore(IAuthCore.class).logout();
            finish();
        });
        addinfoBinding.etNick.addTextChangedListener(new TextWatcherListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                addinfoBinding.okBtn.setEnabled(checkNextStep());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_female:
                sexCode = 2;
                addinfoBinding.okBtn.setEnabled(checkNextStep());
                break;
            case R.id.rb_man:
                sexCode = 1;
                addinfoBinding.okBtn.setEnabled(checkNextStep());
                break;
            case R.id.tv_birth:
                if (datePickerDialog.isAdded()) {
                    datePickerDialog.dismiss();
                } else {
                    datePickerDialog.setVibrate(true);
                    datePickerDialog.setYearRange(Constants.START_BIRTH, Constants.END_BIRTH);
                    datePickerDialog.show(getSupportFragmentManager(), "DATEPICKER_TAG");
                }
                break;

            case R.id.ok_btn:
                String nick = addinfoBinding.etNick.getText().toString();
                if (nick.trim().isEmpty()) {
                    Snackbar.make(addinfoBinding.getRoot(), "昵称不能为空！", Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (sexCode == 0) {
                    Snackbar.make(addinfoBinding.getRoot(), "请选择性别", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile != null) {
                    getDialogManager().showProgressDialog(AddUserInfoActivity.this, "正在上传请稍后...");
                    CoreManager.getCore(IFileCore.class).upload(photoFile);
                    return;
                }
                //用户如果自己拍照作为头像就上传，如果为空就代表没拍照，直接拿微信头像上传
                if (StringUtils.isEmpty(avatarUrl)) {
                    Snackbar.make(addinfoBinding.getRoot(), "请上传头像！", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                commit();
                break;
            case R.id.rl_change_avatar:
                getDialogManager().showDefaultHeaderPopupDialog(this, sexCode);
                break;
            default:
                break;
        }
    }

    private void commit() {
        String nick = addinfoBinding.etNick.getText().toString();
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        userInfo.setNick(nick);
        userInfo.setAvatar(avatarUrl);
        userInfo.setGender(sexCode);
        userInfo.setBirthStr(birth);
        getDialogManager().showProgressDialog(this, "请稍后...");
        LinkedInfo linkedInfo = CoreManager.getCore(ILinkedCore.class).getLinkedInfo();

        String channel = "";
        String roomUid = "";
        String uid = "";
        if (linkedInfo != null) {
            channel = linkedInfo.getChannel();
            roomUid = linkedInfo.getRoomUid();
            uid = linkedInfo.getUid();
        }
        CoreManager.getCore(IUserCore.class).requestCompleteUserInfo(userInfo, channel, uid, roomUid);
    }

    PermissionActivity.CheckPermListener checkPermissionListener = new PermissionActivity.CheckPermListener() {
        @Override
        public void superPermission() {
            mCameraCapturingName = CAMERA_PREFIX + System.currentTimeMillis() + ".jpg";
            cameraOutFile = JXFileUtils.getTempFile(AddUserInfoActivity.this, mCameraCapturingName);
            if (!cameraOutFile.getParentFile().exists()) {
                cameraOutFile.getParentFile().mkdirs();
            }
            Uri uri = Uri.fromFile(cameraOutFile);
            getTakePhoto().onEnableCompress(CompressConfig.ofDefaultConfig(), true);
            getTakePhoto().onPickFromCapture(uri);
        }
    };

    private void checkPermissionAndStartCamera() {
        //低版本授权检查
        checkPermission(checkPermissionListener, R.string.ask_camera, android.Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//拍照1003 -1     选择相册6709 -1
        super.onActivityResult(requestCode, resultCode, data);
        MLog.debug(this, "PictureTaskerAct.onActivityResult, resultCode = " + resultCode);
        if (resultCode != Activity.RESULT_OK) {
            MLog.info(this, "return is not ok,resultCode=%d", resultCode);
            return;
        }

        if (requestCode == Method.NICK) {
            if (null != data) {
                String nick = data.getStringExtra(ModifyInfoActivity.CONTENTNICK);
                addinfoBinding.etNick.setText(nick);
            }
        }

        if (requestCode == 1003 && resultCode == -1) {
            getDialogManager().dismissDialog();
            alreadySelectedPic = true;
        } else if (requestCode == 6709 && resultCode == -1) {
            getDialogManager().dismissDialog();
            alreadySelectedPic = true;
        }
    }

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUpload(String url) {
        avatarUrl = url;
        getDialogManager().dismissDialog();
        ImageLoadUtils.loadCircleImage(this, avatarUrl, addinfoBinding.civAvatar, R.drawable.ic_default_avatar);
        addinfoBinding.okBtn.setEnabled(checkNextStep());
        commit();
    }

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUploadFail() {
        toast("上传失败");
        getDialogManager().dismissDialog();
        addinfoBinding.okBtn.setEnabled(checkNextStep());
    }


    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoComplete(UserInfo userInfo) {
        getDialogManager().dismissDialog();
        CoreManager.getCore(IAuthCore.class).setThirdUserInfo(null);
        finish();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoCompleteFaith(String error) {
        toast(error);
        getDialogManager().dismissDialog();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String monthstr;
        if ((month + 1) < 10) {
            monthstr = "0" + (month + 1);
        } else {
            monthstr = String.valueOf(month + 1);
        }
        String daystr;
        if (day < 10) {
            daystr = "0" + day;
        } else {
            daystr = String.valueOf(day);
        }
        String selectDate = String.valueOf(year) + "-" + monthstr + "-" + daystr;
        addinfoBinding.tvBirth.setText(selectDate);
        addinfoBinding.okBtn.setEnabled(checkNextStep());
        this.birth = selectDate;
    }

    @Override
    public void onItemSelected(int resIds, String avatarUrl) {
        photoFile = null;//此处必须置空
        this.avatarUrl = avatarUrl;
        ImageLoadUtils.loadCircleImage(this, avatarUrl, addinfoBinding.civAvatar, R.drawable.ic_default_avatar);
        alreadySelectedPic = true;
        addinfoBinding.okBtn.setEnabled(checkNextStep());
    }

    @Override
    public void clickTakePhotos() {
        checkPermissionAndStartCamera();
    }

    @Override
    public void clickAlbum() {
        String mCameraCapturingName = CAMERA_PREFIX + System.currentTimeMillis() + ".jpg";
        File cameraOutFile = JXFileUtils.getTempFile(AddUserInfoActivity.this, mCameraCapturingName);
        if (!cameraOutFile.getParentFile().exists()) {
            cameraOutFile.getParentFile().mkdirs();
        }
        Uri uri = Uri.fromFile(cameraOutFile);
        CompressConfig compressConfig = new CompressConfig.Builder().create();
        getTakePhoto().onEnableCompress(compressConfig, true);
        CropOptions options = new CropOptions.Builder().setWithOwnCrop(true).create();
        getTakePhoto().onPickFromGalleryWithCrop(uri, options);
    }

    public interface Method {

        int NICK = 2;
    }

    @Override
    public void takeSuccess(TResult result) {
        photoFile = new File(result.getImage().getCompressPath());
        ImageLoadUtils.loadCircleImage(this, photoFile.getAbsolutePath(), addinfoBinding.civAvatar, R.drawable.ic_default_avatar);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        addinfoBinding.okBtn.setEnabled(checkNextStep());
        toast(msg);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (datePickerDialog != null) {
            datePickerDialog.setOnDateSetListener(null);
            datePickerDialog = null;
        }
        super.onDestroy();
    }
}
