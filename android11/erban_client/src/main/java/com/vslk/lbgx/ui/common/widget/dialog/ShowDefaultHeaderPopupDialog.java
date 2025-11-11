package com.vslk.lbgx.ui.common.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatDialog;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.file.FileCoreImpl;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

/**
 * @author xiaoyu
 */
public class ShowDefaultHeaderPopupDialog extends AppCompatDialog implements OnClickListener {
    private ViewGroup mRootView;
    private TextView mCancelBtn;
    private TextView tvTakePhoto;
    private TextView tvAlbum;
    private Context context;
    private AppToolBar appToolBar;
    private int sexCode;
    private RadioButton[] radioButtons;

    public ShowDefaultHeaderPopupDialog(Context context, OnDefaultHeaderItemSelectedListener listener, int sexCode) {
        super(context, R.style.ErbanBottomSheetDialog);
        this.context = context;
        this.sexCode = sexCode;
        this.listener = listener;
    }

    private int[] widgetId = {R.id.ic_default_0, R.id.ic_default_1, R.id.ic_default_2};

    private int[] maleImgId = {R.drawable.selector_male_1, R.drawable.selector_male_2, R.drawable.selector_male_3};

    private int[] femaleImgId = {R.drawable.selector_female_1, R.drawable.selector_female_2, R.drawable.selector_female_3};

    private String[] maleUrl = {"ic_img_nan1_nor_v1.png", "ic_img_nan2_nor_v1.png", "ic_img_nan3_nor_v1.png"};

    private String[] femaleUrl = {"ic_img_nv1_nor_v1.png", "ic_img_nv2_nor_v1.png", "ic_img_nv3_nor_v1.png"};
//
//    private int[] maleAvatar = {R.drawable.ic_sex_male_normal, R.drawable.ic_img_nan2_nor, R.drawable.ic_img_nan3_nor,
//            R.drawable.ic_img_nan4_nor, R.drawable.ic_img_nan5_nor, R.drawable.ic_img_nan6_nor};
//
//    private int[] femaleAvatar = {R.drawable.ic_sex_female_normal, R.drawable.ic_img_nv2_nor, R.drawable.ic_img_nv3_nor,
//            R.drawable.ic_img_nv4_nor, R.drawable.ic_img_nv5_nor, R.drawable.ic_img_nv6_nor};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = (ViewGroup) View.inflate(getContext(), R.layout.layout_show_header_popup_dialog, null);
        tvTakePhoto = mRootView.findViewById(R.id.tv_take_photo);
        tvAlbum = mRootView.findViewById(R.id.tv_album);
        appToolBar = mRootView.findViewById(R.id.app_tool_bar);
        mCancelBtn = mRootView.findViewById(R.id.btn_cancel);
        avatarUrl = FileCoreImpl.accessUrl + (sexCode == 1 ? maleUrl[0] : femaleUrl[0]);
//        resIds = sexCode == 1 ? maleAvatar[0] : femaleAvatar[0];
        mCancelBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemSelected(resIds, avatarUrl);
                dismiss();
            } else {
                SingleToastUtil.showShortToast("参数异常!");
            }
        });

        radioButtons = new RadioButton[widgetId.length];
        for (int i = 0; i < widgetId.length; i++) {
            radioButtons[i] = mRootView.findViewById(widgetId[i]);
            radioButtons[i].setButtonDrawable(sexCode == 1 ? maleImgId[i] : femaleImgId[i]);
            radioButtons[i].setOnClickListener(this);
        }

        appToolBar.setOnLeftImgBtnClickListener(this::dismiss);
        tvAlbum.setOnClickListener(v -> {
            if (listener != null) {
                listener.clickAlbum();
            }
        });
        tvTakePhoto.setOnClickListener(v -> {
            if (listener != null) {
                listener.clickTakePhotos();
            }
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

        FrameLayout bottomSheet = findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
        }
    }

    private OnDefaultHeaderItemSelectedListener listener;

    public interface OnDefaultHeaderItemSelectedListener {
        void onItemSelected(int resIds, String avatarUrl);

        void clickTakePhotos();

        void clickAlbum();
    }

    private int oldPosition = 0;
    private String avatarUrl;
    private int resIds;

    @Override
    public void onClick(View view) {
        int currentPosition;
        switch (view.getId()) {
            case R.id.ic_default_1:
                currentPosition = 1;
                break;
            case R.id.ic_default_2:
                currentPosition = 2;
                break;
//            case R.id.ic_default_3:
//                currentPosition = 3;
//                break;
//            case R.id.ic_default_4:
//                currentPosition = 4;
//                break;
//            case R.id.ic_default_5:
//                currentPosition = 5;
//                break;
            default:
                currentPosition = 0;
                break;
        }
        if (currentPosition == oldPosition) {//重复点击无效
            return;
        }
        avatarUrl = FileCoreImpl.accessUrl + (sexCode == 1 ? maleUrl[currentPosition] : femaleUrl[currentPosition]);
//        resIds = (sexCode == 1 ? maleAvatar[currentPosition] : femaleAvatar[currentPosition]);
        radioButtons[currentPosition].setChecked(true);
        radioButtons[oldPosition].setChecked(false);
        oldPosition = currentPosition;
    }
}
