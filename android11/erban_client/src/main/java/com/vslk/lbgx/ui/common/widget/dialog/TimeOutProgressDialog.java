package com.vslk.lbgx.ui.common.widget.dialog;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tongdaxing.erban.R;

/**
 * Created by qinbo on 2014/7/2.
 */
public class TimeOutProgressDialog {
    private Activity mContext;
    private String msg;

    private long time;

    private DialogManager dialogManager;
    /**
     *
     * @param context
     * @param msg
     * @param time 单位微妙
     */
    public TimeOutProgressDialog(Activity context, String msg, long time){
        this.mContext = context;
        this.msg = msg;
        this.time = time;

        dialogManager = new DialogManager(mContext);
    }

    public TimeOutProgressDialog(Activity context){
        this.mContext = context;
        dialogManager = new DialogManager(mContext);
    }

    public void setTime(long time) {
        this.time = time;
    }


    private Handler handler = new Handler(Looper.myLooper());

    public void showProcessProgress() {
        /*if (dialogManager == null && mContext != null) {
            dialogManager = new DialogManager(mContext);
        }else{
            MLog.error(this, "parent activity not instance of ActivitySupport.");
        }*/
        if (dialogManager != null&&mContext != null) {
            hideProcessProgress();
            dialogManager.showProgressDialog(mContext, msg, false, false, null);
            handler.postDelayed(processProgressTimeoutTask, time);
        }
    }

    public void showProcessProgress(String tip, long seconds) {
        if (dialogManager != null && mContext != null) {
            hideProcessProgress();
            dialogManager.showProgressDialog(mContext, tip, false, false, null);
            handler.postDelayed(processProgressTimeoutTask, seconds);
        }
    }

    public void hideProcessProgress() {
        handler.removeCallbacks(processProgressTimeoutTask);
        if (dialogManager != null) {
            dialogManager.dismissDialog();
        }
    }

    private Runnable processProgressTimeoutTask = new Runnable() {
        @Override
        public void run() {
            //if (mContext != null) {
                if (dialogManager != null) {
                    dialogManager.dismissDialog();
                }
//                if (!NetworkUtils.isNetworkAvailable(mContext)) {
//                    Toast.makeText(mContext,R.string.str_network_not_capable, Toast.LENGTH_LONG).show();
//                } else {
                    Toast.makeText(mContext, R.string.str_network_not_capable, Toast.LENGTH_LONG).show();
                //}
            //}
        }
    };
}