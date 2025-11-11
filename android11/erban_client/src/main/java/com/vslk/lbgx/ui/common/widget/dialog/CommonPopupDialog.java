package com.vslk.lbgx.ui.common.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vslk.lbgx.ui.widget.marqueeview.Utils;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;

import java.util.List;

/**
 * @author xiaoyu
 */
public class CommonPopupDialog extends BottomSheetDialog implements OnClickListener {
    private static final int BUTTON_ITEM_ID = 135798642;

    private int mId;
    private ViewGroup mRootView;
    protected ViewGroup mContentView;
    private TextView mMessageTv;
    private TextView mCancelBtn;
    private int id;
    private String title;
    private List<ButtonItem> buttons;
    private ButtonItem bottomButton;
    private Context context;
    private boolean isFullScreen = true;

    public CommonPopupDialog(Context context, String title, List<ButtonItem> buttons, String cancelBtnText) {
        this(0, context, title, buttons, new ButtonItem(cancelBtnText, null));
    }

    public CommonPopupDialog(Context context, String title, List<ButtonItem> buttons, final ButtonItem bottomButton) {
        this(0, context, title, buttons, bottomButton);
    }

    public CommonPopupDialog(Context context, String title, List<ButtonItem> buttons, String cancelBtnText, boolean isFullScreen) {
        this(0, context, title, buttons, new ButtonItem(cancelBtnText, null));
        this.isFullScreen = isFullScreen;
    }

    public CommonPopupDialog(int id, Context context, String title, List<ButtonItem> buttons, final ButtonItem bottomButton) {
        super(context, R.style.ErbanBottomSheetDialog);
        this.context = context;
        this.id = id;
        this.title = title;
        this.buttons = buttons;
        this.bottomButton = bottomButton;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mId = id;
        mRootView = (ViewGroup) View.inflate(getContext(), R.layout.layout_common_popup_dialog, null);
        mContentView = mRootView.findViewById(R.id.ll_more);
        mMessageTv = mRootView.findViewById(R.id.tv_message);
        mCancelBtn = mRootView.findViewById(R.id.btn_cancel);
        mCancelBtn.setOnClickListener(v -> {
            if (bottomButton != null && bottomButton.mClickListener != null) {
                bottomButton.mClickListener.onClick();
            }
            dismiss();
        });

        setContentView(mRootView);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setWindowAnimations(R.style.ErbanCommonWindowAnimationStyle);

        int size = buttons == null ? 0 : buttons.size();
        if (size > 0) {
            if (title != null && !title.isEmpty()) {
                setMessage(title);
            }
            mContentView.setVisibility(View.VISIBLE);
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    if (title != null && !title.isEmpty()) {
                        addDivider();
                    }
                } else {
                    addDivider();
                }
                addItem(buttons.get(i));
            }
        }
        if (bottomButton != null && bottomButton.mText != null && !bottomButton.mText.isEmpty()) {
            mCancelBtn.setVisibility(View.VISIBLE);
            mCancelBtn.setText(bottomButton.mText);
        }
        FrameLayout bottomSheet = findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(false);
            BottomSheetBehavior.from(bottomSheet).setPeekHeight(
                    (int) context.getResources().getDimension(R.dimen.dialog_common_button_item_height) * (size + 2) +
                            (size - 1) * Utils.dip2px(context, 0.5F) + Utils.dip2px(context, 5));
        }
    }

    public CommonPopupDialog setMessage(String text) {
        mMessageTv.setVisibility(View.VISIBLE);
        mMessageTv.setText(text);
        return this;
    }

    public void addItem(final ButtonItem buttonItem) {
        TextView item = (TextView) LayoutInflater.from(getContext()).inflate(buttonItem.resourceID, mContentView, false);
        if (buttonItem.mTheme != -1) {
            item.setTextAppearance(getContext(), buttonItem.mTheme);
        }
        item.setText(buttonItem.mText);
        item.setOnClickListener(v -> {
            buttonItem.mClickListener.onClick();
            dismiss();
        });
        item.setId(BUTTON_ITEM_ID + mContentView.getChildCount());
        mContentView.addView(item, mContentView.getChildCount());
    }

    public void addDivider() {
        View v2 = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_popup_dialog_divider, mContentView, false);
        v2.setVisibility(View.VISIBLE);
        mContentView.addView(v2, mContentView.getChildCount());
    }

    public int getId() {
        return mId;
    }

    @Override
    public void onClick(View v) {

    }
}
