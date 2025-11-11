package com.netease.nim.uikit.session.actions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.media.picker.PickImageHelper;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.activity.PreviewImageFromCameraActivity;
import com.netease.nim.uikit.common.util.file.AttachmentStore;
import com.netease.nim.uikit.common.util.media.ImageUtil;
import com.netease.nim.uikit.common.util.storage.StorageType;
import com.netease.nim.uikit.common.util.storage.StorageUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.session.activity.P2PMessageActivity;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nim.uikit.session.constant.RequestCode;
import com.netease.nim.uikit.session.helper.SendImageHelper;
import com.tongdaxing.xchat_framework.util.util.ChatUtil;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.TempCallback;

import java.io.File;

/**
 * Created by zhoujianghua on 2015/7/31.
 */
public abstract class PickImageAction extends BaseAction {

    private static final int PICK_IMAGE_COUNT = 9;
    private static final int PORTRAIT_IMAGE_WIDTH = 720;

    public static final String MIME_JPEG = "image/jpeg";
    public static final String JPG = ".jpg";

    private boolean multiSelect;
    private boolean crop = false;

    protected abstract void onPicked(File file);

    protected PickImageAction(int iconResId, int titleId, boolean multiSelect) {
        super(iconResId, titleId);
        this.multiSelect = multiSelect;
    }

    @Override
    public void onClick() {
        if (ChatUtil.checkBanned())
            return;
        if (hasExternalStoragePermission(getActivity())) {
            TempCallback.ITempCallback iTempCallback = TempCallback.getiTempCallback();
            if (iTempCallback != null && iTempCallback.canSendPic()) {
                int requestCode = makeRequestCode(RequestCode.PICK_IMAGE);
                showSelector(getTitleId(), requestCode, multiSelect, tempFile());
            } else {
                SingleToastUtil.showShortToast("由于系统升级暂停使用此功能");
            }
        } else {
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage("当前应用缺少必要权限,请点击设置跳转权限页面")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            getActivity().startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_BASE_APPLICATION);
            dialog.show();
            //放在show()之后，不然有些属性是没有效果的，比如height和width
            Window dialogWindow = dialog.getWindow();
            WindowManager m = dialogWindow.getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            // 设置宽度
            p.width = (int) (d.getWidth() * 0.95); // 宽度设置为屏幕的0.95
            p.gravity = Gravity.CENTER;//设置位置
            //p.alpha = 0.8f;//设置透明度
            dialogWindow.setAttributes(p);
        }
    }

    public static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(
                "android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private String tempFile() {
        String filename = StringUtil.get32UUID() + JPG;
        return StorageUtil.getWritePath(filename, StorageType.TYPE_TEMP);
    }

    /**
     * 打开图片选择器
     */
    private void showSelector(int titleId, final int requestCode, final boolean multiSelect, final String outPath) {
        PickImageHelper.PickImageOption option = new PickImageHelper.PickImageOption();
        option.titleResId = titleId;
        option.multiSelect = multiSelect;
        option.multiSelectMaxCount = PICK_IMAGE_COUNT;
        option.crop = crop;
        option.cropOutputImageWidth = PORTRAIT_IMAGE_WIDTH;
        option.cropOutputImageHeight = PORTRAIT_IMAGE_WIDTH;
        option.outputPath = outPath;

        PickImageHelper.pickImage(getActivity(), requestCode, option);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCode.PICK_IMAGE:
                onPickImageActivityResult(requestCode, data);
                break;
            case RequestCode.PREVIEW_IMAGE_FROM_CAMERA:
                onPreviewImageActivityResult(requestCode, data);
                break;
        }
    }

    /**
     * 图片选取回调
     */
    private void onPickImageActivityResult(int requestCode, Intent data) {
        if (data == null) {
            Toast.makeText(getActivity(), R.string.picker_image_error, Toast.LENGTH_LONG).show();
            return;
        }
        boolean local = data.getBooleanExtra(Extras.EXTRA_FROM_LOCAL, false);
        if (local) {
            // 本地相册
            sendImageAfterSelfImagePicker(data);
        } else {
            // 拍照
            Intent intent = new Intent();
            if (!handleImagePath(intent, data)) {
                return;
            }

            intent.setClass(getActivity(), PreviewImageFromCameraActivity.class);
            getActivity().startActivityForResult(intent, makeRequestCode(RequestCode.PREVIEW_IMAGE_FROM_CAMERA));
        }
    }

    /**
     * 是否可以获取图片
     */
    private boolean handleImagePath(Intent intent, Intent data) {
        String photoPath = data.getStringExtra(Extras.EXTRA_FILE_PATH);
        if (TextUtils.isEmpty(photoPath)) {
            Toast.makeText(getActivity(), R.string.picker_image_error, Toast.LENGTH_LONG).show();
            return false;
        }

        File imageFile = new File(photoPath);
        intent.putExtra("OrigImageFilePath", photoPath);
        File scaledImageFile = ImageUtil.getScaledImageFileWithMD5(imageFile, MIME_JPEG);

        boolean local = data.getExtras().getBoolean(Extras.EXTRA_FROM_LOCAL, true);
        if (!local) {
            // 删除拍照生成的临时文件
            AttachmentStore.delete(photoPath);
        }

        if (scaledImageFile == null) {
            Toast.makeText(getActivity(), R.string.picker_image_error, Toast.LENGTH_LONG).show();
            return false;
        } else {
            ImageUtil.makeThumbnail(getActivity(), scaledImageFile);
        }
        intent.putExtra("ImageFilePath", scaledImageFile.getAbsolutePath());
        return true;
    }

    /**
     * 从预览界面点击发送图片
     */
    private void sendImageAfterPreviewPhotoActivityResult(Intent data) {
        SendImageHelper.sendImageAfterPreviewPhotoActivityResult(data, new SendImageHelper.Callback() {

            @Override
            public void sendImage(File file, boolean isOrig) {
                onPicked(file);
            }
        });
    }

    /**
     * 发送图片
     */
    private void sendImageAfterSelfImagePicker(final Intent data) {
        SendImageHelper.sendImageAfterSelfImagePicker(getActivity(), data, new SendImageHelper.Callback() {

            @Override
            public void sendImage(File file, boolean isOrig) {
                onPicked(file);

            }
        });
    }

    /**
     * 拍摄回调
     */
    private void onPreviewImageActivityResult(int requestCode, Intent data) {
        if (data.getBooleanExtra(PreviewImageFromCameraActivity.RESULT_SEND, false)) {
            sendImageAfterPreviewPhotoActivityResult(data);
        } else if (data.getBooleanExtra(PreviewImageFromCameraActivity.RESULT_RETAKE, false)) {
            String filename = StringUtil.get32UUID() + JPG;
            String path = StorageUtil.getWritePath(filename, StorageType.TYPE_TEMP);

            if (requestCode == RequestCode.PREVIEW_IMAGE_FROM_CAMERA) {
                PickImageActivity.start(getActivity(), makeRequestCode(RequestCode.PICK_IMAGE), PickImageActivity.FROM_CAMERA, path);
            }
        }
    }
}
