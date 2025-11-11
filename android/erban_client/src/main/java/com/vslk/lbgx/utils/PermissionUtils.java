package com.vslk.lbgx.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class PermissionUtils {
    //判断麦克风权限
    public static boolean isVoicePermission() {
        try {
            AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, 22050, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(22050, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT));
            record.startRecording();
            int recordingState = record.getRecordingState();
            if (recordingState == AudioRecord.RECORDSTATE_STOPPED) {
                return false;
            }
            record.release();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    //申请麦克风权限
    public static void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

    }

    public static void voicePermission(Activity activity) {
        if (!PermissionUtils.isVoicePermission()) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage("当前应用缺少必要权限,请点击设置跳转权限页面")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            activity.startActivity(intent);
                        }
                    })
//                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
//                            intent.setData(uri);
//                            activity.startActivity(intent);
//                        }
//                    })
                    .create();
            dialog.setOnKeyListener((dialog12, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_MENU) {
                    return true;
                }
                return false;
            });
            dialog.setCancelable(false);
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

}
