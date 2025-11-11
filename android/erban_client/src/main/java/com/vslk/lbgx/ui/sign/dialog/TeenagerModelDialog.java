package com.vslk.lbgx.ui.sign.dialog;

import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.hncxco.library_ui.widget.ViewHolder;
import com.hncxco.library_ui.widget.dialog.BaseDialog;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.WebUrl;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;

/**
 * Function:青少年模式对话框
 * Author: Edward on 2019/7/11
 */
public class TeenagerModelDialog extends BaseDialog {
    private TextView tvCloseTeenagerModel;
    private TextView tvIKnown;
//    private CountDownTimer countDownTimer;

    @Override
    public void convertView(ViewHolder viewHolder) {
        tvIKnown = viewHolder.getView(R.id.tv_i_known);
        tvCloseTeenagerModel = viewHolder.getView(R.id.tv_close_teenager_model);
        tvIKnown.setOnClickListener(v -> dismiss());
        tvCloseTeenagerModel.setOnClickListener(v -> {
            CommonWebViewActivity.start(getActivity(), WebUrl.TEENAGER_MODEL_URL);
            dismiss();
        });
//        countDownTimer = new CountDownTimer(5 * 1000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                tvIKnown.setText("我知道了（" + millisUntilFinished / 1000 + "s）");
//            }
//
//            @Override
//            public void onFinish() {
//                dismiss();
//            }
//        };
//        countDownTimer.start();
    }

    @Override
    public void onDestroyView() {
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
        super.onDestroyView();
    }

    @Override
    protected int getDialogWidth() {
        return ConvertUtils.dp2px(295);
    }

    @Override
    protected int getDialogHeight() {
        return ConvertUtils.dp2px(360);
    }

    @Override
    public int getContentView() {
        return R.layout.dialog_teenager_model;
    }
}
