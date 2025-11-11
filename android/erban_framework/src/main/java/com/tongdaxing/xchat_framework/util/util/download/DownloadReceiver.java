package com.tongdaxing.xchat_framework.util.util.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tongdaxing.xchat_framework.util.util.MimeType;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

/**
 * Created by lijun on 2015/7/20.
 */
public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MLog.debug(this, "DM onReceive: %s", intent);

        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (!DownloadManagerUtil.existDownloadId(id)) {
                return;
            }

            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (MimeType.APK.equalsIgnoreCase(dm.getMimeTypeForDownloadedFile(id))) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(dm.getUriForDownloadedFile(id),
                        dm.getMimeTypeForDownloadedFile(id));
                context.startActivity(intent);
            }

            DownloadManagerUtil.removeDownloadId(context, id);
        } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            // can not receive the event ???

//            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
//            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//            //点击通知栏取消下载
//            dm.remove(ids);
//            SingleToastUtil.showToast(context, "已经取消下载");
//
//            for (int i = 0, size = ids.length; i < size; i++) {
//                DownloadManagerUtil.removeDownloadId(context, ids[i]);
//            }
        }
    }
}
