package com.vslk.lbgx.ui.common.widget.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_framework.util.util.FP;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.util.List;

//import javax.xml.soap.Text;

public class DialogManager {


    private Dialog mDialog;
    private Context mContext;
    private AlertDialog.Builder mBuilder;
    private boolean mCanceledOnClickBackKey = true;
    private boolean mCanceledOnClickOutside = true;
    private boolean mReCreate = true;
    private TextView mCancel;

    public DialogManager(Context context) {
        mContext = context;
        mBuilder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        mDialog = mBuilder.create();
    }

    public DialogManager(Context context, boolean noCreate) {
        mContext = context;
        mBuilder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        if (!noCreate) {
            mDialog = mBuilder.create();
        }
    }

    public DialogManager(Context context, boolean canceledOnClickBackKey, boolean canceledOnClickOutside) {
        mContext = context;
        mBuilder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        mDialog = mBuilder.create();
        mCanceledOnClickBackKey = canceledOnClickBackKey;
        mCanceledOnClickOutside = canceledOnClickOutside;
    }

    public Context getContext() {
        return mContext;
    }

    @TargetApi(17)
    public boolean checkActivityValid() {
        if (mContext == null) {
            MLog.warn(this, "Fragment " + this + " not attached to Activity");
            return false;
        }
        if (mDialog != null && mDialog.getWindow() == null) {
            MLog.warn(this, "window null");
            return false;
        }
        if (((Activity) mContext).isFinishing()) {
            MLog.warn(this, "activity is finishing");
            return false;
        }

        if (Build.VERSION.SDK_INT >= 17 && ((Activity) mContext).isDestroyed()) {
            MLog.warn(this, "activity is isDestroyed");
            return false;
        }
        return true;
    }

    public void dismissDialog() {
        //注释这个判断，因为不保留活动情况下，((Activity)mContext).isDestroyed() 为true 导致不执行dismiss一个dialog
        //而mDialog.getWindow()不为null，还是可以dismiss一个dialog的。
//        if(!checkActivityValid())
//            return;
        if (mContext != null && mDialog != null && mDialog.getWindow() != null) {
            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                if (!activity
                        .isFinishing())//如果dialog在延时比如handler。postDelay中调用,而activity.已经destory,会报异常java.lang
                // .IllegalArgumentException: View not attached to window manager
                {
                    try {
                        mDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                dismissTryCrash();
            }
        }
    }

    private void dismissTryCrash() {
        try {
            mDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDialogShowing() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }
        return false;
    }

    public void setCanceledOnClickBackKey(boolean cancelable) {
        mCanceledOnClickBackKey = cancelable;
    }

    public void setCanceledOnClickOutside(boolean cancelable) {
        mCanceledOnClickOutside = cancelable;
    }


    public void showOkBigTips(String title, String message, boolean cancelable, final OkDialogListener l) {

        if (!checkActivityValid()) {
            MLog.info(this, "showOkBigTips ActivityInvalid....");
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_ok_dialog_big_tip);

        TextView tip = (TextView) window.findViewById(R.id.message);
        tip.setText(title);
        TextView message_tips = (TextView) window.findViewById(R.id.message_tips);
        if (message != null || message != "") message_tips.setText(message);
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (l != null) {
                    l.onOk();
                }
            }
        });
    }

    public void showOkCancleCancelBigTips(String message, String tips, String okLabel, int okLabelColor,
            String cancelLabel,
            int cancelLabelColor, boolean cancelable, final OkCancelDialogListener l) {

        if (!checkActivityValid()) {
            MLog.info(this, "showOkCancleCancelBigTips ActivityInvalid....");
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_ok_cancel_label_dialog_big_message);

        TextView tip = (TextView) window.findViewById(R.id.message);
        tip.setText(message);
        TextView message_tips = (TextView) window.findViewById(R.id.message_tips);
        if (tips != null || tips != "") message_tips.setText(tips);
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        if (okLabelColor != 0) {
            ok.setTextColor(okLabelColor);
        }
        ok.setText(okLabel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (l != null) {
                    l.onOk();
                }
            }
        });

        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        if (cancelLabelColor != 0) {
            cancel.setTextColor(cancelLabelColor);
        }
        cancel.setText(cancelLabel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (l != null) {
                    l.onCancel();
                }
            }
        });
    }

    public interface OkDialogListener {
        void onOk();
    }

    public void showOkDialog(String message, final OkDialogListener l) {
        showOkDialog(message, mCanceledOnClickBackKey, l);
    }

    public void showOkDialog(String message, boolean cancelable, final OkDialogListener l) {
        showOkDialog(message, cancelable, l, false);
    }

    public void showOkDialog(String message, boolean cancelable, boolean canceledOnClickOutside,
            final OkDialogListener l) {
        showOkDialog(message, cancelable, canceledOnClickOutside, l, false);
    }

    public void showOkDialog(String message, boolean cancelable, final OkDialogListener l, boolean IsHtmlText) {
        if (!checkActivityValid()) {
            MLog.info(this, "showOkDialog ActivityInvalid....");
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }

        mDialog = mBuilder.create();

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(mCanceledOnClickOutside);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_ok_dialog);
        TextView msg = (TextView) window.findViewById(R.id.tv_msg);
        if (IsHtmlText) {
            msg.setText(Html.fromHtml(message));
            msg.setMovementMethod(LinkMovementMethod.getInstance());
//            setUrlSpans(msg);
        } else {
            msg.setText(message);
        }
        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.onOk();
                }
                mDialog.cancel();
            }
        });
    }

    public void showOkDialog(String message, boolean cancelable, boolean canceledOnClickOutside,
            final OkDialogListener l, boolean IsHtmlText) {
        if (!checkActivityValid()) {
            MLog.info(this, "showOkDialog ActivityInvalid..");
            return;
        }


        if (mDialog.isShowing()) {
            mDialog.hide();
        }

        mDialog = mBuilder.create();

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(canceledOnClickOutside);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_ok_dialog);
        TextView msg = (TextView) window.findViewById(R.id.tv_msg);
        if (IsHtmlText) {
            msg.setText(Html.fromHtml(message));
            msg.setMovementMethod(LinkMovementMethod.getInstance());
//            setUrlSpans(msg);
        } else {
            msg.setText(message);
        }
        TextView ok = window.findViewById(R.id.btn_ok);
        ok.setOnClickListener(v -> {
            if (l != null) {
                l.onOk();
            }
            mDialog.cancel();
        });
    }

    public void showOkAndLabelDialog(String message, String okLabel, boolean cancelable, boolean canceledOnClickOutside,
            boolean isHtmlText, boolean isUrl, final OkDialogListener l) {
        if (!checkActivityValid()) {
            MLog.info(this, "showOkAndLabelDialog ActivityInvalid..");
            return;
        }

        if (mDialog.isShowing()) {
            try {
                mDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mDialog = mBuilder.create();

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(canceledOnClickOutside);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_ok_dialog);
        TextView msg = (TextView) window.findViewById(R.id.tv_msg);
        if (isHtmlText) {
            msg.setText(Html.fromHtml(message));
        } else {
            msg.setText(message);
        }
        if (isUrl) {
            msg.setMovementMethod(LinkMovementMethod.getInstance());
        }
        TextView ok = window.findViewById(R.id.btn_ok);
        if (!FP.empty(okLabel)) {
            ok.setText(okLabel);
        }
        ok.setOnClickListener(v -> {
            if (l != null) {
                l.onOk();
            }
            mDialog.cancel();
        });
    }

    public void showDefaultHeaderPopupDialog(ShowDefaultHeaderPopupDialog.OnDefaultHeaderItemSelectedListener listener,int sexCode) {
        if (!checkActivityValid()) {
            MLog.info(this, "showCommonPopupDialog ActivityInvalid..");
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = new ShowDefaultHeaderPopupDialog(mContext, listener,sexCode);
        mDialog.setCancelable(mCanceledOnClickBackKey);
        mDialog.setCanceledOnTouchOutside(true);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCommonPopupDialog(List<ButtonItem> btnItems) {
        showCommonPopupDialog(null, btnItems, "");
    }

    public void showCommonPopupDialog(String cancelBtnText) {
        showCommonPopupDialog(null, null, cancelBtnText);
    }

    public void showCommonPopupDialog(String title, int layout_bottom_select_dialog, ButtonItem bottomButton) {
        showCommonPopupDialog(null, null, bottomButton);
    }

    public void showCommonPopupDialog(String title, List<ButtonItem> btnItems) {
        showCommonPopupDialog(title, btnItems, "");
    }

    public void showCommonPopupDialog(List<ButtonItem> btnItems, String cancelBtnText) {
        showCommonPopupDialog(null, btnItems, cancelBtnText);
    }

    public void showCommonPopupDialog(List<ButtonItem> btnItems, String cancelBtnText, boolean isFullscreen) {
        showCommonPopupDialog(null, btnItems, cancelBtnText, isFullscreen);
    }

    public void showCommonPopupDialog(List<ButtonItem> btnItems, ButtonItem bottomButton) {
        showCommonPopupDialog(null, btnItems, bottomButton);
    }

    public void showCommonPopupDialog(String title, List<ButtonItem> btnItems, ButtonItem bottomButton) {

        if (!checkActivityValid()) {
            MLog.info(this, "showCommonPopupDialog ActivityInvalid.");
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = new CommonPopupDialog(mContext, title, btnItems, bottomButton);
        mDialog.setCancelable(mCanceledOnClickBackKey);
        mDialog.setCanceledOnTouchOutside(mCanceledOnClickOutside);
        mDialog.show();
    }

    public void showCommonPopupDialog(String title, List<ButtonItem> btnItems, String cancelBtnText, boolean isFullScreen) {

        if (!checkActivityValid()) {
            MLog.info(this, "showCommonPopupDialog ActivityInvalid..");
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = new CommonPopupDialog(mContext, title, btnItems, cancelBtnText, isFullScreen);
        mDialog.setCancelable(mCanceledOnClickBackKey);
        mDialog.setCanceledOnTouchOutside(true);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCommonPopupDialog(String title, List<ButtonItem> btnItems, String cancelBtnText) {
        if (!checkActivityValid()) {
            MLog.info(this, "showCommonPopupDialog ActivityInvalid..");
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = new CommonPopupDialog(mContext, title, btnItems, cancelBtnText);
        mDialog.setCancelable(mCanceledOnClickBackKey);
        mDialog.setCanceledOnTouchOutside(true);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCustomPopupDialog(String title, List<ButtonItem> btnItems) {
        if (!checkActivityValid()) {
            MLog.info(this, "showCommonPopupDialog ActivityInvalid...");
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = new CustomPopupDialog(mContext, title, btnItems);
        mDialog.setCancelable(mCanceledOnClickBackKey);
        mDialog.setCanceledOnTouchOutside(mCanceledOnClickOutside);
        mDialog.show();
    }

    public void showCommonPopupDialog(int id, String title, List<ButtonItem> btnItems, ButtonItem bottomButton) {
        if (!checkActivityValid()) {
            MLog.info(this, "showCommonPopupDialog ActivityInvalid....");
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = new CommonPopupDialog(id, mContext, title, btnItems, bottomButton);
        mDialog.setCancelable(mCanceledOnClickBackKey);
        mDialog.setCanceledOnTouchOutside(mCanceledOnClickOutside);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getShowingDialogId() {
        if (mDialog.isShowing() && mDialog instanceof CommonPopupDialog) {
            return ((CommonPopupDialog) mDialog).getId();
        }
        return 0;
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public interface InputPwdDialogListener {

        void onConfirm();

        void onCancel();
    }

    public interface OkCancelDialogListener {

        default void onCancel(){}

        void onOk();
    }

    public static abstract class AbsOkDialogListener implements OkCancelDialogListener {
        public void onCancel() {
            //do nothing
        }

        public abstract void onOk();
    }


    public void showOkCancelDialog(String message, boolean cancelable, final OkCancelDialogListener l) {
        showOkCancelDialog(message, "确定", "取消", cancelable, l);
    }

    public void showOkCancelDialog(String message, String okLabel, String cancelLabel, final OkCancelDialogListener l) {
        showOkCancelDialog(message, okLabel, cancelLabel, mCanceledOnClickBackKey, l);
    }

    public void showOkCancelWithTitleDialog(String titleStr, String message, String okLabel, String cancelLabel,
            final OkCancelDialogListener l) {
        if (!checkActivityValid()) {
            MLog.info(this, "showOkCancelDialog ActivityInvalid....");
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();

        mDialog.setCancelable(mCanceledOnClickBackKey);
        mDialog.setCanceledOnTouchOutside(mCanceledOnClickBackKey);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_ok_cancel_title_dialog);

        TextView title = window.findViewById(R.id.title);
        if (!FP.empty(titleStr)) {
            title.setVisibility(View.VISIBLE);
            title.setText(titleStr);
        } else {
            title.setVisibility(View.GONE);
        }

        TextView tip = window.findViewById(R.id.message);
        tip.setText(message);

        TextView ok = window.findViewById(R.id.btn_ok);
        ok.setText(okLabel);
        ok.setOnClickListener(v -> {
            mDialog.dismiss();
            if (l != null) {
                l.onOk();
            }
        });

        TextView cancel = window.findViewById(R.id.btn_cancel);
        cancel.setText(cancelLabel);
        cancel.setOnClickListener(v -> {
            mDialog.dismiss();
            if (l != null) {
                l.onCancel();
            }
        });
    }

    public void showGlobalOkCancelDialog(String message, boolean cancelable, final OkCancelDialogListener l) {
        if (!checkActivityValid()) {
            MLog.info(this, "showOkCancelDialog ActivityInvalid....");
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();

        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_ok_cancel_dialog);

        TextView tip = window.findViewById(R.id.message);
        tip.setText(message);

        TextView ok = window.findViewById(R.id.btn_ok);
        ok.setText("确定");
        ok.setOnClickListener(v -> {
            mDialog.dismiss();
            if (l != null) {
                l.onOk();
            }
        });

        TextView cancel = window.findViewById(R.id.btn_cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(v -> {
            mDialog.dismiss();
            if (l != null) {
                l.onCancel();
            }
        });
    }

    public void showOkCancelDialog(CharSequence message, CharSequence okLabel, CharSequence cancelLabel,
            boolean cancelable, final OkCancelDialogListener l) {

        if (!checkActivityValid()) {
            MLog.info(this, "showOkCancelDialog ActivityInvalid....");
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_ok_cancel_dialog);
        window.setLayout((ScreenUtils.getScreenWidth()/4*3), LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tip = window.findViewById(R.id.message);
        tip.setText(message);

        TextView ok = window.findViewById(R.id.btn_ok);
        ok.setText(okLabel);
        ok.setOnClickListener(v -> {
            dismissTryCrash();
            if (l != null) {
                l.onOk();
            }
        });

        TextView cancel = window.findViewById(R.id.btn_cancel);
        cancel.setText(cancelLabel);
        cancel.setOnClickListener(v -> {
            mDialog.dismiss();
            if (l != null) {
                l.onCancel();
            }
        });
    }

    public void showYuMengOkCancelDialog(String message, boolean cancelable, final OkCancelDialogListener l) {
        showYuMengOkCancelDialog("温馨提示", message, cancelable, l);
    }

    public void showYuMengOkCancelDialog(CharSequence title, String message, boolean cancelable,
            final OkCancelDialogListener l) {
        showOkCancelYuMengDialog(title, message, "确定", "取消", cancelable, l);
    }

    public void showYuMengOkCancelDialog(CharSequence title, String message, String okLabel, String cancelLabel,
            final OkCancelDialogListener l) {
        showOkCancelYuMengDialog(title, message, okLabel, cancelLabel, mCanceledOnClickBackKey, l);
    }

    public void showOkCancelYuMengDialog(CharSequence title, CharSequence message, CharSequence okLabel, CharSequence
            cancelLabel,
            boolean cancelable, final OkCancelDialogListener l) {

        if (!checkActivityValid()) {
            MLog.info(this, "showOkCancelDialog ActivityInvalid....");
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_yumeng_ok_cancel_dialog);
        TextView tip = window.findViewById(R.id.message);
        tip.setText(message);
        TextView ok = window.findViewById(R.id.btn_ok);
        TextView titleText = window.findViewById(R.id.title);
        titleText.setText(title);
        ok.setText(okLabel);
        ok.setOnClickListener(v -> {
            dismissTryCrash();
            if (l != null) {
                l.onOk();
            }
        });

        TextView cancel = window.findViewById(R.id.btn_cancel);
        cancel.setText(cancelLabel);
        cancel.setOnClickListener(v -> {
            mDialog.dismiss();
            if (l != null) {
                l.onCancel();
            }
        });
    }

    public void showOkCancelColorDialog(CharSequence message, CharSequence okLabel, int okLabelColor,
            CharSequence cancelLabel,
            int cancelLabelColor, boolean cancelable, final OkCancelDialogListener l) {

        if (!checkActivityValid()) {
            MLog.info(this, "showOkCancelColorDialog ActivityInvalid....");
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_ok_cancel_dialog);

        TextView tip = (TextView) window.findViewById(R.id.message);
        tip.setText(message);

        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        if (okLabelColor != 0) {
            ok.setTextColor(okLabelColor);
        }
        ok.setText(okLabel);
        ok.setOnClickListener(v -> {
            mDialog.dismiss();
            if (l != null) {
                l.onOk();
            }
        });

        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        if (cancelLabelColor != 0) {
            cancel.setTextColor(cancelLabelColor);
        }
        cancel.setText(cancelLabel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (l != null) {
                    l.onCancel();
                }
            }
        });
    }


    public void showOkCancelDialog(SpannableString message, String okLabel, String cancelLabel,
            boolean cancelable, final OkCancelDialogListener l) {

        if (!checkActivityValid()) {
            MLog.info(this, "showOkCancelDialog ActivityInvalid..");
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(mCanceledOnClickOutside);
        try {
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_ok_cancel_dialog);

        TextView tip = (TextView) window.findViewById(R.id.message);
        tip.setText(message);
        tip.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText(okLabel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.onOk();
                }
                dismissTryCrash();
            }
        });

        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setText(cancelLabel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.onCancel();
                }
                dismissTryCrash();
            }
        });

    }

    public void showCustomViewDialog(View contentView) {
        if (!checkActivityValid()) {
            MLog.info(this, "showOkCancelColorDialog ActivityInvalid....");
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();
        mDialog.show();
        Window window = mDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setContentView(contentView);

    }


    public void setReCreate(boolean reCreate) {
        mReCreate = reCreate;
    }

    public void showProgressDialog(Context context) {
        showProgressDialog(context, "请稍后...", mCanceledOnClickBackKey);
    }

    public void showProgressDialog(Context context, String msg) {
        showProgressDialog(context, msg, mCanceledOnClickBackKey);
    }

    public void showProgressDialog(Context context, String msg, boolean cancelable) {
        showProgressDialog(context, msg, cancelable, null);
    }

    public void showProgressDialog(Context context, String msg, boolean cancelable,
            DialogInterface.OnDismissListener listener) {
        showProgressDialog(context, msg, cancelable, mCanceledOnClickOutside, listener);
    }

    /**
     * @param cancelable        点击返回键是否可取消
     * @param outSideCancelable 点击对话框外部是否可取消
     */
    public void showProgressDialog(Context context, String msg, boolean cancelable, boolean outSideCancelable,
            DialogInterface.OnDismissListener listener) {
        if (!checkActivityValid()) {
            MLog.info(this, "showProgressDialog ActivityInvalid");
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        if (mReCreate) {
            mDialog = mBuilder.create();
        }
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(outSideCancelable);
        if (null != mContext) {
            try {
                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mDialog.setContentView(R.layout.layout_progress_dialog);
        TextView tvTip = mDialog.findViewById(R.id.tv_tip);
        tvTip.setText(msg);
        if (listener != null) {
            mDialog.setOnDismissListener(listener);
        }
    }

    private int mProgressMax = 0;
    private String mTip;

    public void setText(String text) {
        mTip = text;
    }

    public void setMax(int max) {
        mProgressMax = max;
    }

    public void setProgress(int progress) {
        if (mDialog != null && mDialog.isShowing() && mProgressMax > 0) {
            ((TextView) mDialog.findViewById(R.id.tv_tip)).setText(mTip + progress * 100 / mProgressMax + "%");
        }
    }

    public void hideProgressDialog() {
        if (mDialog != null) {
            mDialog.hide();
        }
    }

    /**
     * 加好友或群验证码的对话框
     */
    public void showInputPwdDialog(String title, String okLabel, String cancelLabel, final String resultCode,
            final InputPwdDialogListener listener) {

        if (!checkActivityValid()) {
            MLog.info(this, "showPicAddFriendGroupDialog ActivityInvalid....");
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog.show();

        Window window = mDialog.getWindow();
        window.setContentView(R.layout.layout_pic_login_dialog);
        TextView titleView = (TextView) window.findViewById(R.id.pic_login_title);
        final EditText input = (EditText) window.findViewById(R.id.pic_login_input);
        final TextView failMsg = (TextView) window.findViewById(R.id.pic_login_fail_msg);
        TextView confirm = (TextView) window.findViewById(R.id.btn_confirm);
        TextView mCancel = (TextView) window.findViewById(R.id.btn_cancel);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        //只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //加上下面这一行弹出对话框时软键盘随之弹出
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
        if (!TextUtils.isEmpty(cancelLabel)) {
            mCancel.setText(cancelLabel);
        }
        if (!TextUtils.isEmpty(okLabel)) {
            confirm.setText(okLabel);
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtil.isEmpty(resultCode)) {
                    if (resultCode.equals(input.getText().toString())) {
                        if (listener != null) {
                            listener.onConfirm();
                            mDialog.dismiss();
                        }
                    } else {
                        failMsg.setVisibility(View.VISIBLE);
                    }
                } else {
                    failMsg.setVisibility(View.VISIBLE);
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancel();
                    mDialog.dismiss();
                }
            }
        });
    }


    public interface OnClickListener {
        public void onClick(View view, int btnIndex);
    }

    private String getTicketProtcol(long channelId, long subChannelId, String title) {
        return "yy://pd-[sid=" + channelId + "&subid=" + subChannelId + "]\n" + title;
    }

    private String getTicketProtcol(long channelId, long subChannelId) {
        return "yy://pd-[sid=" + channelId + "&subid=" + subChannelId + "]";
    }

    public interface OkCancelMessageDialogListener {
        void onCancel();

        void onOk(String title, String msg);
    }
//
//    public void showListViewMenu(List<ListViewMenuItem> menuItems) {
//        if (!checkActivityValid()) {
//            MLog.info(this, "showListViewMenu ActivityInvalid...");
//            return;
//        }
//        if (mDialog.isShowing()) {
//            mDialog.hide();
//        }
//        mDialog = new ListViewMenu(mContext, menuItems);
//        mDialog.setCancelable(mCanceledOnClickBackKey);
//        mDialog.setCanceledOnTouchOutside(mCanceledOnClickOutside);
//        mDialog.show();
//    }


    public interface OnInputPasswordClickListener {
        void onOk(String password);

        void onCancel();
    }

    public static boolean isHtmlAlertDialog(String html) {
        try {
            return html.matches(".*<([^>]*)>.*");
        } catch (Exception e) {
            return false;
        }
    }

    public AlertDialog.Builder getBuilder() {
        return mBuilder;
    }

}
