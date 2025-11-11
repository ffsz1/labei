package com.vslk.lbgx.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.vslk.lbgx.reciever.NotificationClickReceiver;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.model.AvRoomModel;

/**
 * @author chenran
 * @date 2017/11/16
 */

public class DaemonService extends Service {
    private static final String TAG = "DaemonService";
    public static final int NOTICE_ID = 100;
    private String title;

    public static void start(Context context, RoomInfo roomInfo) {
        Intent intent = new Intent(context, DaemonService.class);
        intent.putExtra("title", roomInfo.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stop(Context context) {
        Intent intent = new Intent(context, DaemonService.class);
        context.stopService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null) {
            title = roomInfo.getTitle();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getPackageName());
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && manager != null) {
                NotificationChannel notificationChannel = new NotificationChannel(getPackageName(), getResources().getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_LOW);
                notificationChannel.setShowBadge(true);
                //任何情况都显示通知完整内容
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                //删除通知（清除历史）渠道
                manager.deleteNotificationChannel(getPackageName());
                //创建通知渠道
                manager.createNotificationChannel(notificationChannel);
            }
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(title);
            builder.setContentText("点击返回房间");
            builder.setTicker("正在房间内");
            builder.setAutoCancel(true);
            Intent clickIntent = new Intent(this, NotificationClickReceiver.class);
            PendingIntent contentIntent = PendingIntent.getBroadcast(this.getApplicationContext(), (int) System.currentTimeMillis(), clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            Notification notification = builder.build();
            startForeground(NOTICE_ID, notification);
        }
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        try {
            stopForeground(true);
            AvRoomDataManager.get().release();
            new AvRoomModel().exitRoom(new CallBack<String>() {
                @Override
                public void onSuccess(String data) {

                }

                @Override
                public void onFail(int code, String error) {

                }
            });
            stop(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
