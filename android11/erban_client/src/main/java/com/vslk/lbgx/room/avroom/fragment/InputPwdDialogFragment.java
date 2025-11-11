package com.vslk.lbgx.room.avroom.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tongdaxing.erban.R;

/**
 * <p> 密码框输入  </p>
 *
 * @author Administrator
 * @date 2017/12/1
 */
public class InputPwdDialogFragment extends DialogFragment implements View.OnClickListener {
    private View mRootView;
    private TextView mTvTitle;
    private EditText mInputText;
    private TextView mFailText;
    private TextView mConfirmText;
    private TextView mCancelText;

    private String mTitle;
    private String mOk;
    private String mCancel;
    private String mResultCode;

    public static InputPwdDialogFragment newInstance(String title, String okLabel, String cancelLabel, String resultCode) {
        InputPwdDialogFragment fragment = new InputPwdDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("okLabel", okLabel);
        bundle.putString("cancelLabel", cancelLabel);
        bundle.putString("resultCode", resultCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString("title");
            mOk = arguments.getString("okLabel");
            mCancel = arguments.getString("cancelLabel");
            mResultCode = arguments.getString("resultCode");
        }

        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_pic_login_dialog, container);
        mTvTitle = (TextView) mRootView.findViewById(R.id.pic_login_title);
        mInputText = (EditText) mRootView.findViewById(R.id.pic_login_input);
        mFailText = (TextView) mRootView.findViewById(R.id.pic_login_fail_msg);
        mConfirmText = (TextView) mRootView.findViewById(R.id.btn_confirm);
        mCancelText = (TextView) mRootView.findViewById(R.id.btn_cancel);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInputText.requestFocus();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        mTvTitle.setText(mTitle);
        mCancelText.setText(mCancel);
        mConfirmText.setText(mOk);

        mConfirmText.setOnClickListener(this);
        mCancelText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (!TextUtils.isEmpty(mResultCode)) {
                    if (mResultCode.equals(mInputText.getText().toString())) {
                        if (mOnDialogBtnClickListener != null) {
                            mOnDialogBtnClickListener.onBtnConfirm();
                        }
                    } else {
                        mFailText.setVisibility(View.VISIBLE);
                    }
                } else {
                    mFailText.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_cancel:
                if (mOnDialogBtnClickListener != null) {
                    mOnDialogBtnClickListener.onBtnCancel();
                }
                break;
            default:
        }
    }

    private OnDialogBtnClickListener mOnDialogBtnClickListener;

    public void setOnDialogBtnClickListener(OnDialogBtnClickListener onDialogBtnClickListener) {
        mOnDialogBtnClickListener = onDialogBtnClickListener;
    }

    public interface OnDialogBtnClickListener {
        void onBtnConfirm();

        void onBtnCancel();
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(this, tag).addToBackStack(null);
        return transaction.commitAllowingStateLoss();

    }
}
