package com.vslk.lbgx.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vslk.lbgx.ui.other.OnePiexlActivity;

/**
 *
 * @author chenran
 * @date 2017/11/16
 */

public class OnePixelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            //屏幕关闭启动1像素Activity
            Intent it = new Intent(context, OnePiexlActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            //屏幕打开 结束1像素
            context.sendBroadcast(new Intent("finish"));
            Intent main = new Intent(Intent.ACTION_MAIN);
            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            main.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(main);
        }
    }
}