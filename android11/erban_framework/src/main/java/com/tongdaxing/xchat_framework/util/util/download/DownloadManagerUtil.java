package com.tongdaxing.xchat_framework.util.util.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.google.gson.internal.LinkedTreeMap;
import com.tongdaxing.xchat_framework.util.cache.CacheClientFactory;
import com.tongdaxing.xchat_framework.util.cache.ReturnCallback;
import com.tongdaxing.xchat_framework.util.util.MimeType;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.io.File;

/**
 * Created by lijun on 2015/7/20.
 */
public class DownloadManagerUtil {

    public static final String DOWNLOAD_FILE_ID = "DOWNLOAD_FILE_ID";

    private static LinkedTreeMap<String, String> downloadIdAndUrls;
    private static BroadcastReceiver downloadReceiver;

    public static boolean existDownloadId(long id) {
        if (null != downloadIdAndUrls) {
            return downloadIdAndUrls.containsKey(String.valueOf(id));
        }

        return false;
    }

    public static void addDownloadId(Context context, final long id, final String url) {
        if (null == context) {
            return;
        }

        CacheClientFactory.getPublic().get(DOWNLOAD_FILE_ID, new ReturnCallback() {
            @Override
            public void onReturn(Object data) throws Exception {
                try {
                    if (data != null && data instanceof LinkedTreeMap) {
                        downloadIdAndUrls = (LinkedTreeMap) data;
                    } else {
                        downloadIdAndUrls = new LinkedTreeMap();
                    }

                } catch (Exception e) {
                    MLog.warn("CacheClient", e.toString());
                    downloadIdAndUrls = new LinkedTreeMap();
                }

                if (null != downloadIdAndUrls) {
                    downloadIdAndUrls.put(String.valueOf(id), url);
                    CacheClientFactory.getPublic().put(DOWNLOAD_FILE_ID, downloadIdAndUrls);
                }
            }
        });
    }

    public static void removeDownloadId(Context context, final long id) {
        if (null == context) {
            return;
        }

        CacheClientFactory.getPublic().get(DOWNLOAD_FILE_ID, new ReturnCallback() {
            @Override
            public void onReturn(Object data) throws Exception {
                try {
                    if (data != null && data instanceof LinkedTreeMap) {
                        downloadIdAndUrls = (LinkedTreeMap) data;
                    } else {
                        downloadIdAndUrls = new LinkedTreeMap();
                    }

                } catch (Exception e) {
                    MLog.warn("CacheClient", e.toString());
                    downloadIdAndUrls = new LinkedTreeMap();
                }

                if (null != downloadIdAndUrls && downloadIdAndUrls.containsKey(id)) {
                    downloadIdAndUrls.remove(String.valueOf(id));
                    CacheClientFactory.getPublic().put(DOWNLOAD_FILE_ID, downloadIdAndUrls);
                }
            }
        });
    }

    public static void unregisterReceiver(Context context) {
        if (null != context && null != downloadReceiver) {
            context.unregisterReceiver(downloadReceiver);
        }
    }

    public static void registerReceiver(Context context, BroadcastReceiver receiver) {
        if (null != context && null != receiver) {
            downloadReceiver = receiver;

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
            context.registerReceiver(receiver, intentFilter);
        }
    }

    /**
     *
     * @param url
     * @param context
     * @return
     */
    public static long downloadApkFromUrl(String url, Context context) {
        return downloadFileFromUrl(url, context, MimeType.APK, true);
    }

    /**
     *
     * @param url
     * @param context
     * @param mimeType
     * @param onlyInWifi
     * @return
     */
    public static long downloadFileFromUrl(String url, Context context, String mimeType, boolean onlyInWifi) {
        if (StringUtils.isEmpty(url) || null == context) {
            return -1;
        }

        String fileName = StringUtils.substringAfterLast(url, File.separator);

        Uri source = Uri.parse(url);

        // Make a new request pointing to the .apk url
        DownloadManager.Request request = new DownloadManager.Request(source);
        // appears the same in Notification bar while downloading
        request.setDescription("正在下载");
        request.setTitle(fileName);
        request.setMimeType(mimeType);

        if (onlyInWifi) {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        }
        // save the file in the "Downloads" folder of SDCARD
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return manager.enqueue(request);
    }
}
