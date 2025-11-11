package com.vslk.lbgx.ui.me.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.vslk.lbgx.base.view.TitleBar;
import com.vslk.lbgx.ui.common.permission.PermissionActivity;
import com.vslk.lbgx.ui.me.user.adapter.UserModifyPhotosAdapter;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TResult;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.file.IFileCore;
import com.tongdaxing.xchat_core.file.IFileCoreClient;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_core.user.bean.UserPhoto;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.file.JXFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenran on 2017/7/24.
 */

public class UserModifyPhotosActivity extends TakePhotoActivity implements UserModifyPhotosAdapter.PhotoItemClickListener {
    private long userId;
    private UserInfo userInfo;
    private GridView photoGridView;
    private boolean isEditMode;
    private UserModifyPhotosAdapter adapter;
    private UserModifyPhotosActivity mActivity;
    private boolean isSelf = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photos_modify);
        mActivity = this;
        userId = getIntent().getLongExtra("userId", 0);
        isSelf = getIntent().getBooleanExtra("isSelf", true);
        initView();
        userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(userId);
        userInfo.getPrivatePhoto();
        if (userInfo != null) {
            updateView();
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestUserInfo(UserInfo info) {
        if (info.getUid() == userId) {
            userInfo = info;
            updateView();
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdate(UserInfo info) {
        if (info.getUid() == userId) {
            userInfo = info;
            updateView();
            getDialogManager().dismissDialog();
        }
    }

    private void initView() {
        initTitleBar("相册");
        TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        if (isSelf) {
            titleBar.addAction(new TitleBar.TextAction("编辑") {
                @Override
                public void performAction(View view) {
                    notifyEditMode();
                }
            });
        }
        photoGridView = (GridView) findViewById(R.id.gridView);
    }

    private void updateView() {
        adapter = new UserModifyPhotosAdapter(this, userInfo.getPrivatePhoto(), this);
        adapter.setSelf(isSelf);
        photoGridView.setAdapter(adapter);
    }

    private void notifyEditMode() {
        adapter.setEditMode(!isEditMode);
        isEditMode = !isEditMode;
        adapter.notifyDataSetChanged();
    }

    private void takePhoto() {
        File cameraOutFile = JXFileUtils.getTempFile(this, "picture_" + System.currentTimeMillis() + ".jpg");
        if (!cameraOutFile.getParentFile().exists()) {
            cameraOutFile.getParentFile().mkdirs();
        }
        Uri uri = Uri.fromFile(cameraOutFile);
        CompressConfig compressConfig = new CompressConfig.Builder().create();
        compressConfig.setMaxSize(500 * 1024);
        getTakePhoto().onEnableCompress(compressConfig, false);
        getTakePhoto().onPickFromCapture(uri);
    }

    PermissionActivity.CheckPermListener checkPermissionListener = new PermissionActivity.CheckPermListener() {
        @Override
        public void superPermission() {
            takePhoto();
        }
    };

    private void checkPermissionAndStartCamera() {
        //低版本授权检查
        checkPermission(checkPermissionListener, R.string.ask_camera, android.Manifest.permission.CAMERA);
    }

    @Override
    public void onPhotoDeleteClick(int position) {
        getDialogManager().showProgressDialog(this, "请稍后");
        if (position != 0) {
            UserPhoto userPhoto = userInfo.getPrivatePhoto().get(position - 1);
            CoreManager.getCore(IUserCore.class).requestDeletePhoto(userPhoto.getPid());
        }
    }

    @Override
    public void onPhotoItemClick(int position) {
        if (isSelf && position == 0) {
            if (userInfo.getPrivatePhoto() != null && userInfo.getPrivatePhoto().size() >= 50) {
                toast("照片已达到最大上传数");
                return;
            }
            ButtonItem upItem = new ButtonItem("拍照上传", this::checkPermissionAndStartCamera);
            ButtonItem loaclItem = new ButtonItem("本地相册", () -> {
                CompressConfig compressConfig = new CompressConfig.Builder().create();
                compressConfig.setMaxSize(500 * 1024);
                getTakePhoto().onEnableCompress(compressConfig, true);
                getTakePhoto().onPickFromGallery();
            });
            List<ButtonItem> buttonItemList = new ArrayList<>();
            buttonItemList.add(upItem);
            buttonItemList.add(loaclItem);
            getDialogManager().showCommonPopupDialog(buttonItemList, "取消", false);
        } else {
            ArrayList<UserPhoto> userPhotos1 = new ArrayList<>();
            userPhotos1.addAll(userInfo.getPrivatePhoto());
            Intent intent = new Intent(mActivity, ShowPhotoActivity.class);
            int position1 = isSelf?position - 1:position;
            intent.putExtra("position", position1);
            intent.putExtra("photoList", userPhotos1);
            startActivity(intent);
        }
    }

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUploadPhoto(String url) {
        CoreManager.getCore(IUserCore.class).requestAddPhoto(url);
    }

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUploadPhotoFail() {
        toast("操作失败，请检查网络");
        getDialogManager().dismissDialog();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestAddPhoto() {
        getDialogManager().dismissDialog();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestAddPhotoFaith(String msg) {
        toast(msg);
        getDialogManager().dismissDialog();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestDeletePhoto() {
        getDialogManager().dismissDialog();
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onRequestDeletePhotoFaith(String msg) {
        toast("上传失败");
        getDialogManager().dismissDialog();
    }

    @Override
    public void takeSuccess(TResult result) {
        getDialogManager().showProgressDialog(this, "请稍后");
        //有空指针异常
        if (result == null || result.getImage() == null || StringUtil.isEmpty(result.getImage().getCompressPath())) {
            toast("图片地址异常，请重试！");
            getDialogManager().dismissDialog();
            return;
        }
        CoreManager.getCore(IFileCore.class).uploadPhoto(new File(result.getImage().getCompressPath()));
    }

    @Override
    public void takeFail(TResult result, String msg) {
//        toast(msg);
    }

}
