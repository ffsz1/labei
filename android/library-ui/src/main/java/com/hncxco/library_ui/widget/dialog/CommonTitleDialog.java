package com.hncxco.library_ui.widget.dialog;

import android.view.View;
import android.widget.TextView;

import com.hncxco.library_ui.R;
import com.hncxco.library_ui.widget.ViewHolder;

/**
 * Function:通用标题对话框
 * Author: Edward on 2019/1/9
 */
public class CommonTitleDialog extends BaseDialog {
    private CommonTitleDialog() {
    }

    public CommonTitleDialog getCommonTitleDialog() {
        return this;
    }

    @Override
    public void convertView(ViewHolder holder) {
        holder.setText(R.id.tv_title, title);
        holder.setText(R.id.tv_sure, sureText);
        holder.setText(R.id.tv_cancel, cancelText);

        View view = holder.getView(R.id.one);
        TextView tvSure = holder.getView(R.id.tv_sure);
        TextView tvCancel = holder.getView(R.id.tv_cancel);

        if (onlyOne) {
            tvCancel.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else {
            tvCancel.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureDialogClickListener != null) {
                    sureDialogClickListener.onClick(v, getCommonTitleDialog());
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelDialogClickListener != null) {
                    cancelDialogClickListener.onClick(v, getCommonTitleDialog());
                }
            }
        });
    }

    public int getContentView() {
        return R.layout.dialog_common_title;
    }

    private String title;
    private String cancelText = "取消";
    private String sureText = "确定";
    private DialogClickListener cancelDialogClickListener;
    private DialogClickListener sureDialogClickListener;
    private boolean onlyOne = false;

    public static class Builder {
        private CommonTitleDialog commonTitleDialog;

        public Builder() {
            commonTitleDialog = new CommonTitleDialog();
        }

        public Builder setOnlyOneButton(boolean onlyOne) {
            commonTitleDialog.onlyOne = onlyOne;
            return this;
        }

        public Builder setTitle(String title) {
            commonTitleDialog.title = title;
            return this;
        }

        public Builder setCancelClickListener(String cancelText, DialogClickListener dialogClickListener) {
            commonTitleDialog.cancelText = cancelText;
            commonTitleDialog.cancelDialogClickListener = dialogClickListener;
            return this;
        }

        public Builder setSureClickListener(String sureText, DialogClickListener sureClickListener) {
            commonTitleDialog.sureText = sureText;
            commonTitleDialog.sureDialogClickListener = sureClickListener;
            return this;
        }

        public CommonTitleDialog build() {
            return commonTitleDialog;
        }
    }
}
