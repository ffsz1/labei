package com.vslk.lbgx.ui.common.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_framework.util.util.ResolutionUtils;

import java.util.List;

public class CustomPopupDialog extends AlertDialog implements OnClickListener {

    private ViewGroup mRootView;
	private ViewGroup mContentView;
	private TextView mMessageTv;
	private TextView mCancelBtn;
    private View v2;

    public CustomPopupDialog(Context context, String title, List<ButtonItem> buttons) {
		super(context);
        show();

        mRootView = (ViewGroup) View.inflate(getContext(), R.layout.layout_custom_popup_dialog, null);
        mContentView = (ViewGroup) mRootView.findViewById(R.id.ll_more);
		mMessageTv = (TextView) mRootView.findViewById(R.id.tv_message);
		/*mCancelBtn = (TextView) mRootView.findViewById(R.id.btn_cancel);
		mCancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                if(bottomButton!=null && bottomButton.mClickListener!=null){
                    bottomButton.mClickListener.onClick();
                }
				dismiss();
			}
		});*/

		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = (int) (ResolutionUtils.getScreenWidth(context) * 0.85);
		window.setGravity( Gravity.CENTER );
		window.setAttributes(params);
        window.setBackgroundDrawableResource(android.R.color.transparent);
		//window.setWindowAnimations(R.style.DialogAnimation);
        window.setContentView(mRootView);

        int size = buttons == null ? 0 : buttons.size();
        if(size > 0){
            if(!TextUtils.isEmpty(title)){
                setMessage(title);
            }
            mContentView.setVisibility(View.VISIBLE);
            for (int i = 0; i < size; i++) {
                if (i == 0 && TextUtils.isEmpty(title)) {
                    addItem(buttons.get(0), 0);
                } else {
                    addDivider();
                    addItem(buttons.get(i), i == size - 1 ? 2 : 1);
                }
            }
        }
		/*if(bottomButton != null && bottomButton.mText!=null && !bottomButton.mText.isEmpty()){
			mCancelBtn.setVisibility(View.VISIBLE);
			mCancelBtn.setText(bottomButton.mText);
		}*/
	}

	public CustomPopupDialog setMessage(String text) {
		mMessageTv.setVisibility(View.VISIBLE);
		mMessageTv.setText(text);
		return this;
	}

	public void addItem(final ButtonItem buttonItem, int level){
        TextView item = (TextView) LayoutInflater.from(getContext()).inflate(buttonItem.resourceID, mContentView, false);
		item.setText(buttonItem.mText);
        item.getBackground().setLevel(level);
        if (buttonItem.mButtonType == ButtonItem.BUTTON_TYPE_CANCEL) {
//            item.setTextColor(getContext().getResources().getColorStateList(R.drawable.dialog_bottom_btn_color));
            item.setTextColor(getContext().getResources().getColor(R.color.text_color_system));
        }
        if(buttonItem.mClickListener!=null) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonItem.mClickListener.onClick();
                    dismiss();
                }
            });
        }else{
            item.setTextColor(getContext().getResources().getColor(R.color.text_color_system));
            item.setGravity(Gravity.LEFT);
            item.setText("　　"+buttonItem.mText);
        }
		mContentView.addView(item, mContentView.getChildCount());
	}

    public void addDivider(){
        View v2 = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_popup_dialog_divider, mContentView, false);
        v2.setVisibility(View.VISIBLE);
        mContentView.addView(v2, mContentView.getChildCount());
    }

	@Override
	public void onClick(View v) {
	}

}
