package com.vslk.lbgx.ui.me.bills.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongdaxing.erban.R;

/**
 * <p>  账单item </p>
 * Created by Administrator on 2017/11/6.
 */
public class BillItemView extends RelativeLayout {

    private static final String TAG = "BillItemView";

    private int LEFT_ICON_RES_ID = getResources().getIdentifier("icon_bills_gift", "drawable", getContext().getPackageName());
    private float MARGIN_START = getResources().getDimension(R.dimen.bill_item_view_margin_start);
    private float MARGIN_END = getResources().getDimension(R.dimen.bill_item_view_margin_end);
    private float TEXT_SIZE = getResources().getDimension(R.dimen.bill_item_view_text_size);
    private int TEXT_COLOR = getContext().getResources().getColor(R.color.color_333333);
    private float TEXT_MARGIN_START = getResources().getDimension(R.dimen.bill_item_view_text_margin_start);
    private float UNDER_LINE_HEIGHT = getResources().getDimension(R.dimen.bill_item_view_under_line_height);

    private ImageView mIvLeftIcon;
    private TextView mTvItemText;
    private ImageView rightIcon;
    private View mViewLine;
    private Context mContext;

    public BillItemView(Context context) {
        this(context,null);
    }

    public BillItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BillItemView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyle) {
        mContext = context;
        inflate(context, R.layout.layout_bill_item_view, this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BillItemView, defStyle, 0);
        int leftIcon = a.getResourceId(R.styleable.BillItemView_left_icon, LEFT_ICON_RES_ID);
        String itemText = a.getString(R.styleable.BillItemView_item_text);
        boolean isHide = a.getBoolean(R.styleable.BillItemView_hide_line, false);
        float marginStart = a.getDimension(R.styleable.BillItemView_margin_start, MARGIN_START);
        float marginEnd = a.getDimension(R.styleable.BillItemView_margin_end, MARGIN_END);
        float textSize = a.getDimension(R.styleable.BillItemView_item_text_size, TEXT_SIZE);
        int textColor = a.getColor(R.styleable.BillItemView_item_text_color, TEXT_COLOR);
        float textMarginStart = a.getDimension(R.styleable.BillItemView_text_margin_start, TEXT_MARGIN_START);
        float underlineHeight = a.getDimension(R.styleable.BillItemView_under_line_height, UNDER_LINE_HEIGHT);
        a.recycle();
        mIvLeftIcon = (ImageView) findViewById(R.id.iv_left_icon);
        mTvItemText = (TextView) findViewById(R.id.tv_item_text);
        rightIcon = findViewById(R.id.iv_arrow_right);
        mViewLine = findViewById(R.id.view_line);

        setupData(marginStart, marginEnd, textSize, textColor, textMarginStart, underlineHeight);

        setBackgroundResource(R.drawable.bg_common_touch_while);
        setData(leftIcon, itemText);
        setViewLine(isHide);
    }

    private void setupData(float marginStart, float marginEnd, float textSize, int textColor, float textMarginStart, float underlineHeight) {
        LayoutParams layoutParams = (LayoutParams) mIvLeftIcon.getLayoutParams();
        layoutParams.setMarginStart((int) marginStart);
        mIvLeftIcon.setLayoutParams(layoutParams);
        LayoutParams textLayoutParams = (LayoutParams) mTvItemText.getLayoutParams();
        textLayoutParams.setMarginStart((int) textMarginStart);
        mTvItemText.setLayoutParams(textLayoutParams);
        mTvItemText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mTvItemText.setTypeface(Typeface.DEFAULT);
        mTvItemText.setTextColor(textColor);
        LayoutParams rightIconLayoutParams = (LayoutParams) rightIcon.getLayoutParams();
        rightIconLayoutParams.setMarginEnd((int) marginEnd);
        rightIcon.setLayoutParams(rightIconLayoutParams);
        mViewLine.setMinimumHeight((int) underlineHeight);
    }

    public void setData(int leftIconResId, int itemTextResId) {
        setData(leftIconResId, mContext.getResources().getString(itemTextResId));
    }

    public void setData(int leftIconResId, String itemText) {
        mIvLeftIcon.setImageResource(leftIconResId);
        mTvItemText.setText(itemText);
    }

    public void setViewLine(boolean isHide) {
        mViewLine.setVisibility(!isHide ? VISIBLE : GONE);
    }
}
