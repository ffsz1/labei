package com.vslk.lbgx.im.transfer;


import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.tongdaxing.erban.R;
import com.vslk.lbgx.ui.dialog.BaseDialog;

public class TransferDialog extends BaseDialog {

    private View send;

    private OnConfirmListener listener;

    private SingleClick singleClick = new SingleClick();

    @Override
    protected int layout() {
        return R.layout.dialog_layout_transfer;
    }

    @Override
    protected int setAnim() {
        return 0;
    }

    @Override
    protected int close() {
        return R.id.btn_close;
    }

    @Override
    protected void bindView(View view) {
        send = view.findViewById(R.id.btn_send);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        final String gold = getArguments().getString("gold");
        final String nick = getArguments().getString("nick");
        tvTitle.setText(gold + "开心给用户" + nick + "吗？");
        send.setOnClickListener(v -> {
            if (listener == null) {
                ToastUtils.showShort("listener is null");
            } else {
                if(!singleClick.isFastClick()){// 禁止连点
                    listener.onConfirm();
                }
            }
        });
    }

    public interface OnConfirmListener {
        void onConfirm();
    }

    public void setListener(OnConfirmListener l) {
        this.listener = l;
    }

    public class SingleClick {
        private long lastClickTime;
        public synchronized boolean isFastClick() {
            long time = System.currentTimeMillis();
            if ( time - lastClickTime < 1000) {
                return true;
            }
            lastClickTime = time;
            return false;
        }
    }
}
