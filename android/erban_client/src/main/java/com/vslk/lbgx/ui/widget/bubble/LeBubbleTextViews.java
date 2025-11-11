package com.vslk.lbgx.ui.widget.bubble;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dupengtao on 15/7/25.
 * <p/>
 * <declare-styleable name="LeBubbleTextView">
 * <!-- Corner radius for LeBubbleTextView. -->
 * <attr name="bubbleCornerRadius" format="dimension"/>
 * <!-- Background color for LeBubbleTextView. -->
 * <attr name="bubbleBackgroundColor" format="color"/>
 * <!-- text size for LeBubbleTextView. -->
 * <attr name="bubbleTextSize" format="dimension"/>
 * <!-- text color for LeBubbleTextView. -->
 * <attr name="bubbleTextColor" format="color"/>
 * <!-- text for LeBubbleTextView. -->
 * <attr name="bubbleText" format="string"/>
 * <p/>
 * <!-- direction for arrow. -->
 * <attr name="bubbleArrowDirection">
 * <enum name="left" value="1"/>
 * <enum name="top" value="2"/>
 * <enum name="right" value="3"/>
 * <enum name="bottom" value="4"/>
 * </attr>
 * <!-- direction for arrow. -->
 * <attr name = "relativePosition" format = "fraction" />
 * <!-- Press state Background color for LeBubbleTextView. -->
 * <attr name="bubbleBackgroundPressColor" format="color"/>
 * </declare-styleable>
 */
public class LeBubbleTextViews extends LeBubbleView implements Runnable {

    private TextView tvContent;
    private ArrayList<TextView> views;

    public LeBubbleTextViews(Context context) {
        super(context);
    }

    public LeBubbleTextViews(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LeBubbleTextViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initChildView(float radius, int backgroundColor, int textColor, float textSize, String... contents) {
        super.initChildView(radius, backgroundColor, textColor, textSize, contents);
         views = new ArrayList<>(contents.length);
        int curId = 0;
        View curView = null;
        for(String content : contents){
            tvContent = new TextView(mContext);
            tvContent.setId(curId);
            tvContent.setTextColor(textColor);
            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tvContent.setText(content);
            int px22 = dip2px(21);
            int px16 = dip2px(15);
            tvContent.setPaddingRelative(px22, px16, px22, px16);
            if(curId > 0){

                LayoutParams contentTvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                contentTvParams.setMargins(dip2px(22), dip2px(1), dip2px(22), dip2px(15));
                //contentTvParams.addRule(RelativeLayout.ALIGN_TOP,titleTextView.getId());
                contentTvParams.addRule(BELOW, curView.getId());
                tvContent.setLayoutParams(contentTvParams);

//                RelativeLayout.LayoutParams curParams = (RelativeLayout.LayoutParams)curView.getLayoutParams();
//                LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//                curParams.addRule(RelativeLayout.BELOW, tvContent.getId());

                conRl.addView(tvContent,contentTvParams);
            }
            views.add(tvContent);
            curId = generateViewId();
            curView = tvContent;
        }
    }

    public List<TextView> getContentTextViews() {
        return views;
    }

}
