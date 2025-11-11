package com.vslk.lbgx.ui.common.widget.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tongdaxing.erban.R;


public class LoginPopupDialog extends Dialog implements OnClickListener {

	private ViewGroup mContentView;
	private View mBtnRegister;
	private View mBtnLogin;
	private View mQQLogin;
	private View mSinaLogin;
	private View mWeChatLogin;

	private OnLoginPopupDialogListener mL;
	
	public LoginPopupDialog(Context context, final OnLoginPopupDialogListener l) {
		super(context, R.style.Dialog_Fullscreen);
		mL = l;
		mContentView = (ViewGroup) View.inflate(getContext(), R.layout.layout_login_popup_dialog, null);
		//mTvTitle = (TextView) mContentView.findViewById(R.id.tv_title);
		mBtnRegister = mContentView.findViewById(R.id.btn_register);
		mBtnLogin = mContentView.findViewById(R.id.btn_login);
//		mQQLogin = mContentView.findViewById(R.id.qq_login);
//		mSinaLogin = mContentView.findViewById(R.id.sina_login);
//		mWeChatLogin = mContentView.findViewById(R.id.wechat_login);
		mBtnRegister.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
//		mQQLogin.setOnClickListener(this);
//		mSinaLogin.setOnClickListener(this);
//		mWeChatLogin.setOnClickListener(this);

		setContentView(mContentView);
		setCanceledOnTouchOutside(true);

		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setGravity( Gravity.BOTTOM );
		window.setAttributes(params);
		window.setWindowAnimations(R.style.DialogAnimation);
	}

	@Override
	public void onClick(View arg0) {
        if(arg0 == mBtnRegister){
            mL.onClickRegister();
        } else if(arg0 == mBtnLogin){
            mL.onClickLogin();
        } else if (arg0 == mQQLogin) {
			mL.onClickQQ();
		} else if (arg0 == mSinaLogin) {
			mL.onClickWeibo();
		} else if (arg0 == mWeChatLogin) {
			mL.onClickWeChat();
		}
		dismiss();
	}
	
	public interface OnLoginPopupDialogListener{
		void onClickRegister();
		void onClickLogin();
		void onClickWeibo();
		void onClickQQ();
		void onClickWeChat();
        void dismissDialog();
	}
}
