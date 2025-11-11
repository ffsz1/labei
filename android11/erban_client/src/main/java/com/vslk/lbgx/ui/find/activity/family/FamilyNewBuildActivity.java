package com.vslk.lbgx.ui.find.activity.family;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.ui.common.permission.PermissionActivity;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.vslk.lbgx.ui.widget.TextWatcherListener;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.file.IFileCore;
import com.tongdaxing.xchat_core.file.IFileCoreClient;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.find.family.IFamilyCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.tongdaxing.xchat_framework.util.util.file.JXFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vslk.lbgx.constant.Extras.CHOICE_AVATAR;
import static com.vslk.lbgx.constant.Extras.CHOICE_BG;
import static com.vslk.lbgx.constant.Extras.NO_SELECT;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/21
 * 描述        创建家族
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class FamilyNewBuildActivity extends TakePhotoActivity {

    @BindView(R.id.toolbar) AppToolBar mToolBar;
    @BindView(R.id.family_bg) ImageView familyBg;
    @BindView(R.id.avatar_bg) ImageView avatarBg;
    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.name) EditText familyName;
    @BindView(R.id.name_count) TextView nameCount;
    @BindView(R.id.desc) EditText familyDesc;
    @BindView(R.id.desc_count) TextView descCount;
    @BindView(R.id.buildFamily) Button create;
    @BindView(R.id.checkbox) CheckBox choice;
    @BindView(R.id.protocol) TextView protocol;

    private FamilyInfo cacheInfo;
    private int isType = NO_SELECT;
    private static final String CAMERA_PREFIX = "picture_";
    private List<ButtonItem> buttonItems = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, FamilyNewBuildActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_new_build);
        ButterKnife.bind(this);
        onSetListener();
    }

    private void onSetListener() {
        mToolBar.setOnBackBtnListener(view -> finish());
        familyBg.setOnClickListener(this);
        avatar.setOnClickListener(this);
        create.setOnClickListener(this);
        protocol.setOnClickListener(this);

        familyName.addTextChangedListener(new TextWatcherListener() {

            @Override
            public void afterTextChanged(Editable editable) {
                nameCount.setText(String.format("%s/8", String.valueOf(familyName.getText().length())));
            }
        });
        familyDesc.addTextChangedListener(new TextWatcherListener() {

            @Override
            public void afterTextChanged(Editable editable) {
                descCount.setText(String.format("%s/50", String.valueOf(familyDesc.getText().length())));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.family_bg:
                choiceFamilyImg(CHOICE_BG);
                break;
            case R.id.avatar:
                choiceFamilyImg(CHOICE_AVATAR);
                break;
            case R.id.buildFamily:
                newBuild();
                break;
            case R.id.protocol:
                CommonWebViewActivity.start(this, "");
                break;
            default:
                break;
        }
    }

    private void choiceFamilyImg(int choiceType) {
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
                getCacheInfo().setFamilyBg(url);
                ImageLoadUtils.loadAvatar(this, url, avatarBg);
            } else if (isType == CHOICE_AVATAR) {
                getCacheInfo().setFamilyLogo(url);
                ImageLoadUtils.loadCircleImage(this, url, avatar, R.drawable.ic_default_avatar);
            }
        }
    }

    @CoreEvent(coreClientClass = IFileCoreClient.class)
    public void onUploadFail() {
        toast("上传失败");
        getDialogManager().dismissDialog();
    }

    private FamilyInfo getCacheInfo() {
        if (cacheInfo == null) {
            cacheInfo = new FamilyInfo();
        }
        return cacheInfo;
    }

    private void newBuild() {
        if (StringUtils.isEmpty(getCacheInfo().getFamilyBg())) {
            toast("家族背景不能为空哦！");
            return;
        }
        if (StringUtils.isEmpty(getCacheInfo().getFamilyLogo())) {
            toast("家族头像不能为空哦！");
            return;
        }
        String name = familyName.getText().toString();
        if (StringUtils.isEmpty(name)) {
            toast("家族名称不能为空哦！");
            return;
        }
        getCacheInfo().setFamilyName(name);
        String desc = familyDesc.getText().toString();
        if (StringUtils.isEmpty(desc)) {
            toast("家族公告不能为空哦！");
            return;
        }
        getCacheInfo().setFamilyNotice(desc);
        if (!choice.isChecked()) {
            toast("创建家族需要同意创建家族的协议哦！");
            return;
        }
        //创建家族
        getDialogManager().showProgressDialog(this);
        CoreManager.getCore(IFamilyCore.class).newBuild(getCacheInfo());
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onNewBuildFamily(String isNewBuild) {
        getDialogManager().dismissDialog();
        toast("申请提交成功，等待审核中...");
        finish();
    }

    @CoreEvent(coreClientClass = IFamilyCoreClient.class)
    public void onNewBuildFamilyFail(String message) {
        getDialogManager().dismissDialog();
        toast(message);
    }

}
