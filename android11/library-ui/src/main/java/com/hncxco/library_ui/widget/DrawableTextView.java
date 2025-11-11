package com.hncxco.library_ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.hncxco.library_ui.R;

/**
 * Created by 11876 on 2018/3/15.
 */

public class DrawableTextView extends AppCompatTextView {
    //-1 无特殊drawable  0 图形背景 矩形 圆角  1 图层背景 点击背景变换  2 渐变背景
    private int drawableType = -1;
    //View 的形状 -- 各种圆角还是非圆角
    private float radius = 0;
    private float leftTopRadius = 0;
    private float leftBottomRadius = 0;
    private float rightTopRadius = 0;
    private float rightBottomRadius = 0;
    //默认背景的外围线条粗细和颜色，以及内部填充颜色
    private int strikeColor = -1;
    private int strikeWidth = 0;
    private int soildColor = 0xffffffff;

    //STATE LIST 各种状态的情况
    private final int STATE_LIST_NONE = -1;//
    private final int STATE_LIST_FALSE = 0;
    private final int STATE_LIST_TRUE = 1;
    //enable 状态参数
    private int STATE_ENABLE = STATE_LIST_NONE;
    private int enableStrikeColor = -1;
    private int enableStrikeWidth = 0;
    private int enableSoildColor = 0xffffffff;
    //select 状态参数
    private int STATE_SELECT = STATE_LIST_NONE;
    private int selectStrikeColor = -1;
    private int selectStrikeWidth = 0;
    private int selectSoildColor = 0xffffffff;
    //check状态参数
    private int STATE_CHECK = STATE_LIST_NONE;
    private int checkStrikeColor = -1;
    private int checkStrikeWidth = 0;
    private int checkSoildColor = 0xffffffff;
    //press状态参数
    private int STATE_PRESS = STATE_LIST_NONE;
    private int pressStrikeColor = -1;
    private int pressStrikeWidth = 0;
    private int pressSoildColor = 0xffffffff;

    //渐变背景  -- startcolor、centerColor、endColor
    private int startColor = -1;
    private int centerColor = -1;
    private int endColor = -1;
    private int angle = 0;//渐变角度


    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
        if (a != null) {
            drawableType = a.getInt(R.styleable.DrawableTextView_drawableType, -1);
            radius = a.getDimension(R.styleable.DrawableTextView_d_radius, 0);
            leftTopRadius = a.getDimension(R.styleable.DrawableTextView_leftTopRadius, 0);
            leftBottomRadius = a.getDimension(R.styleable.DrawableTextView_leftBottomRadius, 0);
            rightTopRadius = a.getDimension(R.styleable.DrawableTextView_rightTopRadius, 0);
            rightBottomRadius = a.getDimension(R.styleable.DrawableTextView_rightBottomRadius, 0);
            strikeColor = a.getColor(R.styleable.DrawableTextView_strikeColor, -1);
            strikeWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView_strikeWidth, 0);
            soildColor = a.getColor(R.styleable.DrawableTextView_soildColor, 0xffffffff);
            angle = a.getInt(R.styleable.DrawableTextView_angle, 0);
            startColor = a.getColor(R.styleable.DrawableTextView_startColor, -1);
            centerColor = a.getColor(R.styleable.DrawableTextView_centerColor, -1);
            endColor = a.getColor(R.styleable.DrawableTextView_endColor, -1);
            STATE_ENABLE = a.getInt(R.styleable.DrawableTextView_enableState, STATE_LIST_NONE);
            if (STATE_ENABLE != STATE_LIST_NONE) {
                enableStrikeColor = a.getColor(R.styleable.DrawableTextView_enableStrikeColor, 0xffffffff);
                enableStrikeWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView_enableStrikeWidth, 0);
                enableSoildColor = a.getColor(R.styleable.DrawableTextView_enableSoildColor, 0xffffffff);
            }

            STATE_PRESS = a.getInt(R.styleable.DrawableTextView_pressState, STATE_LIST_NONE);
            if (STATE_PRESS != STATE_LIST_NONE) {
                pressStrikeColor = a.getColor(R.styleable.DrawableTextView_pressStrikeColor, 0xffffffff);
                pressStrikeWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView_pressStrikeWidth, 0);
                pressSoildColor = a.getColor(R.styleable.DrawableTextView_pressSoildColor, 0xffffffff);
            }

            STATE_CHECK = a.getInt(R.styleable.DrawableTextView_checkState, STATE_LIST_NONE);
            if (STATE_CHECK != STATE_LIST_NONE) {
                checkStrikeColor = a.getColor(R.styleable.DrawableTextView_checkStrikeColor, 0xffffffff);
                enableStrikeWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView_checkStrikeWidth, 0);
                enableSoildColor = a.getColor(R.styleable.DrawableTextView_checkSoildColor, 0xffffffff);
            }

            STATE_SELECT = a.getInt(R.styleable.DrawableTextView_selectState, STATE_LIST_NONE);
            if (STATE_SELECT != STATE_LIST_NONE) {
                selectStrikeColor = a.getColor(R.styleable.DrawableTextView_selectStrikeColor, 0xffffffff);
                selectStrikeWidth = a.getDimensionPixelSize(R.styleable.DrawableTextView_selectStrikeWidth, 0);
                selectSoildColor = a.getColor(R.styleable.DrawableTextView_selectSoildColor, 0xffffffff);
            }
        }
        a.recycle();
        initDrawable(context);
    }

    private void initDrawable(Context context) {
        switch (drawableType) {
            case 0:
                setBackground(getShapeDrawable());
                break;
            case 1:
                setBackground(getStateListDrawable());
                break;
            case 2:
                setGradientDrawable(context);
                break;
        }
    }

    private void changeDrawableColor() {
        switch (drawableType) {
            case 0:
                setBackground(getShapeDrawable());
                break;
            case 1:
                setBackground(getStateListDrawable());
                break;
            case 2:
                setGradientDrawable(getContext());
                break;
        }
    }

    /**
     * 改变shape的背景颜色  需传入ARGB八位  例如#FFFFFFFF
     *
     * @param changeColor 颜色值
     */
    public void changeSoildColor(int changeColor) {
        this.soildColor = changeColor;
        changeDrawableColor();
    }

    public void setDrawableType(int drawableType) {
        this.drawableType = drawableType;
        invalidate();
    }

    private GradientDrawable getShapeDrawable() {
        return getShapeDrawable(strikeWidth, strikeColor, soildColor);
    }

    private GradientDrawable getShapeDrawable(int strikeWidth, int strikeColor, int soildColor) {
        GradientDrawable shape = new GradientDrawable();
        //设置边框，参数为边框的类型，有矩形，椭圆，还有线等等，自己去试；
        shape.setShape(GradientDrawable.RECTANGLE);
        //边框为矩形的时候，还可以设置边框四个角的幅度
        if (radius > 0) {
            shape.setCornerRadius(radius);
        } else {//*订购左上角，右上角，右下角，左下角。
            if (leftTopRadius > 0 || rightTopRadius > 0 || rightBottomRadius > 0 || leftBottomRadius > 0) {
//                topLeftRadius, topLeftRadius,
//                        topRightRadius, topRightRadius,
//                        bottomRightRadius, bottomRightRadius,
//                        bottomLeftRadius, bottomLeftRadius
                shape.setCornerRadii(new float[]{
                        leftTopRadius,leftTopRadius,
                        rightTopRadius,rightTopRadius,
                        rightBottomRadius,rightBottomRadius,
                        leftBottomRadius,leftBottomRadius}
                        );
            }
        }
        //设置边框的厚度和颜色
        if (strikeWidth > 0) {
            shape.setStroke(strikeWidth, strikeColor);
        }
        //填充背景颜色
        if (startColor != -1 && endColor != -1) {
            shape.setOrientation(getGradientOrientation());
            if (centerColor != -1) {
                shape.setColors(new int[]{startColor, centerColor, endColor});
            } else {
                shape.setColors(new int[]{startColor, endColor});
            }
        } else {
            shape.setColor(soildColor);
        }
        return shape;
    }

    private StateListDrawable getStateListDrawable() {
        StateListDrawable stateList = new StateListDrawable();
        //获取对应的属性值 Android框架自带的属性 attr
        if (STATE_PRESS != STATE_LIST_NONE) {
            int press = android.R.attr.state_pressed;
            stateList.addState(new int[]{STATE_PRESS == STATE_LIST_TRUE ? press : -press},
                    getShapeDrawable(pressStrikeWidth, pressStrikeColor, pressSoildColor));
        }
        if (STATE_ENABLE != STATE_LIST_NONE) {
            int enable = android.R.attr.state_enabled;
            // -enable 代表 state_enable = false
            stateList.addState(new int[]{STATE_ENABLE == STATE_LIST_TRUE ? enable : -enable},
                    getShapeDrawable(enableStrikeWidth, enableStrikeColor, enableSoildColor));
        }
        if (STATE_SELECT != STATE_LIST_NONE) {
            int select = android.R.attr.state_selected;
            stateList.addState(new int[]{STATE_SELECT == STATE_LIST_TRUE ? select : -select},
                    getShapeDrawable(selectStrikeWidth, selectStrikeColor, selectSoildColor));
        }
        if (STATE_CHECK != STATE_LIST_NONE) {
            int checked = android.R.attr.state_checked;
            stateList.addState(new int[]{STATE_CHECK == STATE_LIST_TRUE ? checked : -checked},
                    getShapeDrawable(checkStrikeWidth, checkStrikeColor, checkSoildColor));
        }
        //默认状态，我们给它设置我空集合
        stateList.addState(new int[]{}, getShapeDrawable(strikeWidth, strikeColor, soildColor));
        return stateList;
    }


    private void setGradientDrawable(Context context) {
        GradientDrawable gradient = null;
        if (startColor != -1 && endColor != -1) {
            GradientDrawable.Orientation orientation = getGradientOrientation();
            if (centerColor != -1) {
                gradient = new GradientDrawable(orientation, new int[]{startColor, centerColor, endColor});
            } else {
                gradient = new GradientDrawable(orientation, new int[]{startColor, endColor});
            }
        }
        if (gradient != null) {
            setBackground(gradient);
        }
    }

    private GradientDrawable.Orientation getGradientOrientation() {
        GradientDrawable.Orientation orientation = null;
        switch (angle) {
            case 45:
                orientation = GradientDrawable.Orientation.BL_TR;
                break;
            case 90:
                orientation = GradientDrawable.Orientation.BOTTOM_TOP;
                break;
            case 135:
                orientation = GradientDrawable.Orientation.BR_TL;
                break;
            case 180:
                orientation = GradientDrawable.Orientation.RIGHT_LEFT;
                break;
            case 225:
                orientation = GradientDrawable.Orientation.TR_BL;
                break;
            case 270:
                orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                break;
            case 315:
                orientation = GradientDrawable.Orientation.TL_BR;
                break;
            default:
                orientation = GradientDrawable.Orientation.LEFT_RIGHT;
                break;
        }
        return orientation;
    }
}
