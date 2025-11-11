package com.hncxco.library_ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.hncxco.library_ui.R;

import static android.widget.RelativeLayout.ALIGN_PARENT_END;
import static android.widget.RelativeLayout.CENTER_IN_PARENT;
import static android.widget.RelativeLayout.CENTER_VERTICAL;

/**
 * 标题栏
 *
 * @author zwk 2018/11/8
 */
public class AppToolBar extends LinearLayout {
    private static final int VALUE_0 = 0;
    private static final int VALUE_30 = 30;
    private static final int VALUE_34 = 34;
    private static final String TITLE_NAME = "标题";
    private static final int DEFUALT_LINE_COLOR = 0xFFF7F7F7;
    private static final int DEFUALT_TITLE_COLOR = 0xFF1A1A1A;
    private int titleHeight = 40;

    //背景风格 默认 白色  其他自己设置颜色
    private int barBackColor = Color.WHITE;
    private RelativeLayout rlTitleContent;
    //左侧返回键
    private ImageView ivLeft;//左侧返回图标
    private boolean hasLeftImg = true;//默认添加在TitleBar左边
    private int leftImageState = View.VISIBLE;
    @DrawableRes
    private int leftImgRes = R.drawable.uilib_arrow_left;//左侧图标
    private int ivMarginLeft = VALUE_30;//左侧图标的左边外边距距
    private int ivPadding = VALUE_0;//左侧图标的内边距

    //标题
    private TextView tvTitle;
    private int titleHMargin = 0;//水平margin
    private float titleSize = VALUE_34;
    private int titleColor = DEFUALT_TITLE_COLOR;
    private String title = TITLE_NAME;

    //右侧 文本 和 图片
    private TextView tvRightTitle;//右边标题
    private boolean hasRightTitle = false;//默认不添加在TitleBar右边
    private int rightTitleState = View.VISIBLE;
    private float rightTitleSize = VALUE_30;//右边标题字体大小
    private int rightTitleColor = DEFUALT_TITLE_COLOR;//右边标题的字体颜色
    private String rightTitle = TITLE_NAME;//右边标题文本
    private int tvRightMarginRight = VALUE_30;//右边标题的右边外边距
    private int tvRightPadding = 0;

    private ImageView ivRight;//右边图标
    private boolean hasRightImg = false;//默认不添在TitleBar中
    private int rightImageState = View.VISIBLE;
    @DrawableRes
    private int ivRightImgRes = VALUE_0;//右边图标drawable
    private int ivRightMarginRight = VALUE_30;//右边图标的右边外边距
    private int ivRightPadding = VALUE_0;//右边图标的内边距


    //底部水平线
    private View line;
    private boolean hasLine = true;//默认是有线的
    private int lineColor = Color.BLACK;
    private int lineHeight = 2;
    private int lineMarginLeft = VALUE_0;
    private int lineMarginRight = VALUE_0;

    private boolean isImmersive = true;
    private int mStatusBarHeight;
    private View statusBar;

    public AppToolBar(Context context) {
        this(context, null);
    }

    public AppToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initContentView(context);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AppToolBar);
        if (array != null) {
            barBackColor = array.getColor(R.styleable.AppToolBar_barBackColor, Color.WHITE);

            isImmersive = array.getBoolean(R.styleable.AppToolBar_isImmersive, true);

            hasLeftImg = array.getBoolean(R.styleable.AppToolBar_hasLeftImg, true);
            leftImageState = array.getInt(R.styleable.AppToolBar_leftImageState, VISIBLE);
            leftImgRes = array.getResourceId(R.styleable.AppToolBar_leftImgRes, R.drawable.uilib_arrow_left);
            ivMarginLeft = array.getDimensionPixelSize(R.styleable.AppToolBar_ivMarginLeft, VALUE_30);
            ivPadding = array.getDimensionPixelSize(R.styleable.AppToolBar_ivPadding, VALUE_0);
            if (array.hasValue(R.styleable.AppToolBar_title))
                title = array.getString(R.styleable.AppToolBar_title);
            titleHMargin = array.getDimensionPixelSize(R.styleable.AppToolBar_titleHMargin, ConvertUtils.dp2px(40));
            titleSize = array.getDimension(R.styleable.AppToolBar_titleSize, ConvertUtils.dp2px(17));
            titleColor = array.getColor(R.styleable.AppToolBar_titleColor, DEFUALT_TITLE_COLOR);

            hasLine = array.getBoolean(R.styleable.AppToolBar_hasLine, true);
            lineColor = array.getColor(R.styleable.AppToolBar_lineColor, DEFUALT_LINE_COLOR);
            lineHeight = array.getDimensionPixelSize(R.styleable.AppToolBar_lineHeight, 2);
            lineMarginLeft = array.getDimensionPixelSize(R.styleable.AppToolBar_lineMarginLeft, VALUE_0);
            lineMarginRight = array.getDimensionPixelSize(R.styleable.AppToolBar_lineMarginRight, VALUE_0);

            hasRightTitle = array.getBoolean(R.styleable.AppToolBar_hasRightTitle, false);
            rightTitleState = array.getInt(R.styleable.AppToolBar_rightTextState, VISIBLE);
            if (array.hasValue(R.styleable.AppToolBar_rightTitle))
                rightTitle = array.getString(R.styleable.AppToolBar_rightTitle);
            rightTitleColor = array.getColor(R.styleable.AppToolBar_rightTitleColor, DEFUALT_TITLE_COLOR);
            rightTitleSize = array.getDimension(R.styleable.AppToolBar_rightTitleSize, ConvertUtils.dp2px(15));
            tvRightMarginRight = array.getDimensionPixelSize(R.styleable.AppToolBar_tvRightMarginRight, VALUE_30);
            tvRightPadding = array.getDimensionPixelSize(R.styleable.AppToolBar_tvRightPadding, ConvertUtils.dp2px(5));

            hasRightImg = array.getBoolean(R.styleable.AppToolBar_hasRightImg, false);
            rightImageState = array.getInt(R.styleable.AppToolBar_rightImageState, VISIBLE);
            ivRightImgRes = array.getResourceId(R.styleable.AppToolBar_ivRightImgRes, VALUE_0);
            ivRightMarginRight = array.getDimensionPixelSize(R.styleable.AppToolBar_ivRightMarginRight, VALUE_30);
            ivRightPadding = array.getDimensionPixelSize(R.styleable.AppToolBar_ivRightPadding, ConvertUtils.dp2px(5));

            array.recycle();
        }
    }

    @SuppressLint("ResourceType")
    private void initContentView(Context context) {
        setOrientation(VERTICAL);
        setBackgroundColor(barBackColor);

        titleHeight = ConvertUtils.dp2px(45);
        mStatusBarHeight = BarUtils.getStatusBarHeight();
        if (isImmersive) {
            statusBar = new View(context);
            statusBar.setId(View.generateViewId());
            LayoutParams statusRl = new LayoutParams(LayoutParams.MATCH_PARENT, mStatusBarHeight == 0 ? ConvertUtils.dp2px(44) : mStatusBarHeight);
            statusBar.setLayoutParams(statusRl);
            addView(statusBar);
        }

        rlTitleContent = new RelativeLayout(context);
        LayoutParams lpContent = new LayoutParams(LayoutParams.MATCH_PARENT, titleHeight, 1);
        rlTitleContent.setLayoutParams(lpContent);
        addView(rlTitleContent);
        int px = ConvertUtils.dp2px(27);
        if (ivLeft == null && hasLeftImg) {
            ivLeft = new ImageView(context);
            RelativeLayout.LayoutParams ivRl = new RelativeLayout.LayoutParams(px, px);
            ivRl.addRule(CENTER_VERTICAL);
            ivRl.setMargins(VALUE_0, VALUE_0, VALUE_0, VALUE_0);
            ivRl.setMarginStart(ivMarginLeft);
            if (ivPadding > VALUE_0)//防止图片太小扩大点击区域
                ivLeft.setPadding(ivPadding, ivPadding, ivPadding, ivPadding);
            ivLeft.setLayoutParams(ivRl);
            if (leftImgRes > VALUE_0)
                ivLeft.setImageResource(leftImgRes);
            ivLeft.setVisibility(leftImageState);
            rlTitleContent.addView(ivLeft);
        }
        if (tvTitle == null) {
            tvTitle = new TextView(context);
            RelativeLayout.LayoutParams tvRl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tvRl.addRule(CENTER_IN_PARENT);
            tvRl.setMargins(titleHMargin, VALUE_0, titleHMargin, VALUE_0);
            tvTitle.setLayoutParams(tvRl);
            tvTitle.setText(title);
            tvTitle.setSingleLine(true);
            tvTitle.setEllipsize(TextUtils.TruncateAt.END);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
            tvTitle.setTextColor(titleColor);
            rlTitleContent.addView(tvTitle);
        }

        //默认不在布局中避免多消耗性能 需要时添加 而且必须添加后才能使用
        if (tvRightTitle == null && hasRightTitle) {
            tvRightTitle = new TextView(context);
            RelativeLayout.LayoutParams tvRightRl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            tvRightRl.addRule(CENTER_VERTICAL);
            tvRightRl.addRule(ALIGN_PARENT_END);
            tvRightRl.setMargins(VALUE_0, VALUE_0, VALUE_0, VALUE_0);
            tvRightRl.setMarginEnd(tvRightMarginRight);
            tvRightTitle.setPadding(tvRightPadding, tvRightPadding, tvRightPadding, tvRightPadding);
            tvRightTitle.setLayoutParams(tvRightRl);
            tvRightTitle.setText(rightTitle);
            tvRightTitle.setGravity(Gravity.CENTER);
            tvRightTitle.setVisibility(rightTitleState);
            tvRightTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTitleSize);
            tvRightTitle.setTextColor(rightTitleColor);
            rlTitleContent.addView(tvRightTitle);
        }
        if (ivRight == null && hasRightImg) {
            ivRight = new ImageView(context);
            RelativeLayout.LayoutParams ivRightRl = new RelativeLayout.LayoutParams(px, px);
            ivRightRl.addRule(CENTER_VERTICAL);
            ivRightRl.addRule(ALIGN_PARENT_END);
            ivRightRl.setMargins(VALUE_0, VALUE_0, VALUE_0, VALUE_0);
            ivRightRl.setMarginEnd(ivRightMarginRight);
            if (ivRightPadding > VALUE_0)//防止图片太小扩大点击区域
                ivRight.setPadding(ivRightPadding, ivRightPadding, ivRightPadding, ivRightPadding);
            ivRight.setLayoutParams(ivRightRl);
            if (ivRightImgRes > VALUE_0)
                ivRight.setImageResource(ivRightImgRes);
            ivRight.setVisibility(rightImageState);
            rlTitleContent.addView(ivRight);
        }

        if (line == null && hasLine) {
            line = new View(context);
            LayoutParams vRl = new LayoutParams(LayoutParams.MATCH_PARENT, lineHeight);
//            vRl.addRule(ALIGN_PARENT_BOTTOM);
            vRl.setMargins(VALUE_0, VALUE_0, VALUE_0, VALUE_0);
            vRl.setMarginEnd(lineMarginRight);
            vRl.setMarginStart(lineMarginLeft);
            line.setLayoutParams(vRl);
            line.setBackgroundColor(lineColor);
            addView(line);
        }
    }

    /**
     * 动态设置状态栏颜色
     *
     * @param staturBarColor
     */
    public void setStatusBarBackgroundColor(@ColorInt int staturBarColor) {
        if (statusBar != null) {
            statusBar.setBackgroundColor(staturBarColor);
        }
    }

    /**
     * 动态设置状态栏颜色
     *
     * @param staturBarRes
     */
    public void setStatusBarBackgroundRes(@ColorRes int staturBarRes) {
        if (statusBar != null) {
            statusBar.setBackgroundResource(staturBarRes);
        }
    }

    public View getStatusBar() {
        return statusBar;
    }

    /**
     * 动态修改左侧返回键
     *
     * @param imgRes
     */
    public void setLeftImgRes(@DrawableRes int imgRes) {
        if (ivLeft != null) {
            ivLeft.setImageResource(imgRes);
        }
    }

    /**
     * 获取左侧返回键ImageView
     *
     * @return
     */
    public ImageView getIvLeft() {
        return ivLeft;
    }


    /**
     * 获取标题
     *
     * @return
     */
    public TextView getTvTitle() {
        return tvTitle;
    }

    /**
     * 动态设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    /**
     * 动态设置标题字符串资源id
     *
     * @param titleResId
     */
    public void setTitle(@StringRes int titleResId) {
        if (tvTitle != null) {
            tvTitle.setText(getContext().getString(titleResId));
        }
    }

    /**
     * 动态修改标题颜色
     *
     * @param titleColor
     */
    public void setTitleColor(@ColorInt int titleColor) {
        if (tvTitle != null) {
            tvTitle.setTextColor(titleColor);
        }
    }

    /**
     * 动态修改标题字体大小
     *
     * @param titleSize
     */
    public void setTitleSize(float titleSize) {
        if (tvTitle != null) {
            tvTitle.setTextSize(titleSize);
        }
    }

    /**
     * 动态修改右侧标题
     *
     * @param title
     */
    public void setRightBtnTitle(String title) {
        if (tvRightTitle != null) {
            tvRightTitle.setText(title);
        }
    }

    /**
     * 动态修改右侧标题
     *
     * @param titleResId
     */
    public void setRightBtnTitle(int titleResId) {
        if (tvRightTitle != null) {
            tvRightTitle.setText(getContext().getString(titleResId));
        }
    }

    /**
     * 动态修改右侧标题字体颜色
     *
     * @param rightTitleColorId
     */
    public void setRightBtnTitleColor(@ColorInt int rightTitleColorId) {
        if (tvRightTitle != null) {
            tvRightTitle.setTextColor(rightTitleColorId);
        }
    }

    /**
     * 动态修改右侧标题字体大小
     *
     * @param rightTitleSize
     */
    public void setRightBtnTitleSize(float rightTitleSize) {
        if (tvRightTitle != null) {
            tvRightTitle.setTextSize(rightTitleSize);
        }
    }

    /**
     * 获取右侧标题
     *
     * @return
     */
    public TextView getTvRightTitle() {
        return tvRightTitle;
    }

    /**
     * 设置右边图片按钮的图片资源
     *
     * @param resId resourceId
     */
    public void setRightBtnImage(@DrawableRes int resId) {
        if (ivRight != null) {
            ivRight.setImageResource(resId);
        }
    }

    /**
     * 获取右侧图标
     *
     * @return
     */
    public ImageView getIvRight() {
        return ivRight;
    }

    /**
     * 设置底部边线的颜色
     *
     * @param lineColor
     */
    public void setLineBackgroundColor(@ColorInt int lineColor) {
        if (line != null) {
            line.setBackgroundColor(lineColor);
        }
    }

    /**
     * 设置底部边线的颜色
     *
     * @param lineRes
     */
    public void setLineBackgroundResource(@ColorRes int lineRes) {
        if (line != null) {
            line.setBackgroundResource(lineRes);
        }
    }

    public View getLine() {
        return line;
    }

    /**
     * 设置状态栏的状态
     *
     * @param visibility 显示 隐藏 占位
     */
    public void setStatusBarVisibility(int visibility) {
        if (statusBar != null) {
            statusBar.setVisibility(visibility);
        }
    }

    /**
     * 设置左侧图片的状态
     *
     * @param visibility 显示 隐藏 占位
     */
    public void setLeftImageVisibility(int visibility) {
        if (ivLeft != null) {
            ivLeft.setVisibility(visibility);
        }
    }

    /**
     * 设置标题的状态
     *
     * @param visibility 显示 隐藏 占位
     */
    public void setTitleVisibility(int visibility) {
        if (tvTitle != null) {
            tvTitle.setVisibility(visibility);
        }
    }

    /**
     * 设置右侧标题按钮的状态
     *
     * @param visibility
     */
    public void setRightTitleBtnVisibility(int visibility) {
        if (tvRightTitle != null) {
            tvRightTitle.setVisibility(visibility);
        }
    }

    /**
     * 设置右侧图标的状态
     *
     * @param visibility
     */
    public void setRightImageBtnVisibility(int visibility) {
        if (ivRight != null) {
            ivRight.setVisibility(visibility);
        }
    }

    /**
     * 设置底部横线的状态
     *
     * @param visibility
     */
    public void setLineVisibility(int visibility) {
        if (line != null) {
            line.setVisibility(visibility);
        }
    }

    /**
     * 返回键点击监听
     */
    public void setOnBackBtnListener(OnClickListener onBackBtnListener) {
        if (ivLeft != null) {
            ivLeft.setOnClickListener(onBackBtnListener);
        }
    }

    /**
     * 右边按钮点击监听
     */
    public void setOnRightBtnClickListener(OnClickListener onRightBtnClickListener) {
        if (ivRight != null) {
            ivRight.setOnClickListener(onRightBtnClickListener);
        }
        if (tvRightTitle != null) {
            tvRightTitle.setOnClickListener(onRightBtnClickListener);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 左侧图片点击时间监听
     */
    public void setOnLeftImgBtnClickListener(final OnLeftImgBtnClickListener onLeftImgBtnClickListener) {
        if (ivLeft != null) {
            ivLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onLeftImgBtnClickListener != null) {
                        onLeftImgBtnClickListener.onLeftBtnImgClickListener();
                    }
                }
            });
        }
    }

    /**
     * 右侧图片点击事件
     *
     * @param onRightImgBtnClickListener
     */
    public void setOnRightImgBtnClickListener(final OnRightImgBtnClickListener onRightImgBtnClickListener) {
        if (ivRight != null) {
            ivRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRightImgBtnClickListener != null) {
                        onRightImgBtnClickListener.onRightImgBtnClickListener();
                    }
                }
            });
        }
    }

    /**
     * 右侧标题点击事件
     *
     * @param onRightTitleClickListener
     */
    public void setOnRightTitleClickListener(final OnRightTitleClickListener onRightTitleClickListener) {
        if (tvRightTitle != null) {
            tvRightTitle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRightTitleClickListener != null) {
                        onRightTitleClickListener.onRightTitleClickListener();
                    }
                }
            });
        }
    }

    public interface OnLeftImgBtnClickListener {
        void onLeftBtnImgClickListener();
    }

    public interface OnRightImgBtnClickListener {
        void onRightImgBtnClickListener();
    }

    public interface OnRightTitleClickListener {
        void onRightTitleClickListener();
    }
}
