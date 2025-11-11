package com.vslk.lbgx.ui.home;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongdaxing.erban.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TabLayoutAnimUtils {
    private TabLayout mTabLayout;
    private List<String> mTitleList = new ArrayList<>();
    private Context mContext;

    public TabLayoutAnimUtils(Context context, TabLayout tabLayout) {
        this.mContext = context;
        this.mTabLayout = tabLayout;
    }

    public void setTitleList(List<String> mTitleList) {
        if (mTitleList == null) return;
        this.mTitleList.addAll(mTitleList);
        setCustomViews(false);
    }

    public void setTitleList(List<String> mTitleList, boolean isLine) {
        if (mTitleList == null) return;
        this.mTitleList.addAll(mTitleList);
        setCustomViewsMsg(isLine);
    }

    /**
     * 设置每个TabLayout的自定义View
     * 注意：TabLayout和Viewpager配合使用的时候必须先mViewPager.setAdapter（），再初始化该方法，然后addOnTabSelectedListener；因为adapter刷新会让mCustomViewView空，
     */
    public void setCustomViews(boolean isLine) {
        int mSelectedTabPosition = mTabLayout.getSelectedTabPosition();
        for (int i = 0; i < this.mTitleList.size(); i++) {
            TabLayout.Tab mTab = this.mTabLayout.getTabAt(i).setCustomView(getTabView(i, mSelectedTabPosition));
            if (i == mSelectedTabPosition) {
                changeTabSelect(mTab, isLine);
            } else {
                changeTabNormal(mTab);
            }
        }
    }

    public void setCustomViewsMsg(boolean isLine) {
        int mSelectedTabPosition = mTabLayout.getSelectedTabPosition();
        for (int i = 0; i < this.mTitleList.size(); i++) {
            TabLayout.Tab mTab = this.mTabLayout.getTabAt(i).setCustomView(getTabView(i, mSelectedTabPosition));
            if (i == mSelectedTabPosition) {
                changeTabSelectMsg(mTab, isLine);
            } else {
                changeTabNormalMsg(mTab);
            }
        }
    }

    /**
     * 提供TabLayout的View
     * 根据index返回不同的View
     * 主意：默认选中的View要返回选中状态的样式
     */
    private View getTabView(int index, int mSelectedTabPosition) {
        //自定义View布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_item, null);
        TextView title = view.findViewById(R.id.title);
        title.setText(mTitleList.get(index));
        title.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff999999));
        title.setSelected(index == mSelectedTabPosition);
        return view;
    }

    /**
     * 改变TabLayout的View到选中状态
     * 使用属性动画改编Tab中View的状态
     */
    @SuppressLint("ObjectAnimatorBinding")
    public void changeTabSelect(TabLayout.Tab tab, boolean isLine) {
        View view = tab.getCustomView();
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_item, null);
            TextView title = view.findViewById(R.id.title);
            if (mTitleList.size() > 0) {
                title.setText(mTitleList.get(tab.getPosition()));
            }
            GradientsTool.setGradients(title, "#FF999999", "#FF999999");
        }

        ObjectAnimator anim = ObjectAnimator
                .ofFloat(view, "", 1.0F, 1.2F)
                .setDuration(200);
        anim.start();
        TextView title = view.findViewById(R.id.title);
        if (isLine) {
            view.findViewById(R.id.line).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.line).setVisibility(View.INVISIBLE);
        }
        GradientsTool.setGradients(title, "#000000", "#000000");
        View finalView = view;
        anim.addUpdateListener(animation -> {
            float cVal = (Float) animation.getAnimatedValue();
            finalView.setAlpha(0.5f + (cVal - 1f) * (0.5f / 0.1f));
            finalView.setScaleX(cVal);
            finalView.setScaleY(cVal);
        });
    }

    /**
     * 改变TabLayout的View到未选中状态
     */
    @SuppressLint("ObjectAnimatorBinding")
    public void changeTabNormal(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_item, null);
            TextView title = view.findViewById(R.id.title);
            if (mTitleList.size() > 0) {
                title.setText(mTitleList.get(tab.getPosition()));
            }
            GradientsTool.setGradients(title, "#FF999999", "#FF999999");
        }
        ObjectAnimator anim = ObjectAnimator
                .ofFloat(view, "", 1.0F, 0.9F)
                .setDuration(200);
        anim.start();
        TextView title = view.findViewById(R.id.title);
        GradientsTool.setGradients(title, "#FF999999", "#FF999999");
        view.findViewById(R.id.line).setVisibility(View.INVISIBLE);
        View finalView = view;
        anim.addUpdateListener(animation -> {
            float cVal = (Float) animation.getAnimatedValue();
            finalView.setAlpha(1f - (1f - cVal) * (0.5f / 0.1f));
            finalView.setScaleX(cVal);
            finalView.setScaleY(cVal);
        });
    }


    /**
     * 改变TabLayout的View到选中状态
     * 使用属性动画改编Tab中View的状态
     */
    @SuppressLint("ObjectAnimatorBinding")
    public void changeTabSelectMsg(TabLayout.Tab tab, boolean isLine) {
        View view = tab.getCustomView();
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_item, null);
            TextView title = view.findViewById(R.id.title);
            if (mTitleList.size() > 0) {
                title.setText(mTitleList.get(tab.getPosition()));
            }
            GradientsTool.setGradients(title, "#ffffff", "#ffffff");
        }
        ObjectAnimator anim = ObjectAnimator
                .ofFloat(view, "", 1.0F, 1.3F)
                .setDuration(200);
        anim.start();
        TextView title = view.findViewById(R.id.title);
        if (isLine) {
            view.findViewById(R.id.line).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.line).setVisibility(View.INVISIBLE);
        }
        GradientsTool.setGradients(title, "#ffffff", "#ffffff");
        View finalView = view;
        anim.addUpdateListener(animation -> {
            float cVal = (Float) animation.getAnimatedValue();
            finalView.setAlpha(0.5f + (cVal - 1f) * (0.5f / 0.1f));
            finalView.setScaleX(cVal);
            finalView.setScaleY(cVal);
        });
    }

    /**
     * 改变TabLayout的View到未选中状态
     */
    @SuppressLint("ObjectAnimatorBinding")
    public void changeTabNormalMsg(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_item, null);
            TextView title = view.findViewById(R.id.title);
            if (mTitleList.size() > 0) {
                title.setText(mTitleList.get(tab.getPosition()));
            }
            GradientsTool.setGradients(title, "#ffffff", "#ffffff");
        }
        ObjectAnimator anim = ObjectAnimator
                .ofFloat(view, "", 1.0F, 0.9F)
                .setDuration(200);
        anim.start();
        TextView title = view.findViewById(R.id.title);
        GradientsTool.setGradients(title, "#ffffff", "#ffffff");
        view.findViewById(R.id.line).setVisibility(View.INVISIBLE);
        View finalView = view;
        anim.addUpdateListener(animation -> {
            float cVal = (Float) animation.getAnimatedValue();
            finalView.setAlpha(1f - (1f - cVal) * (0.5f / 0.1f));
            finalView.setScaleX(cVal);
            finalView.setScaleY(cVal);
        });
    }


    /**
     * 改变tablayout指示器的宽度
     *
     * @param tabLayout
     * @param margin
     */
    public void changeTabIndicatorWidth(final TabLayout tabLayout, final int margin) {
        tabLayout.post(() -> {
            try {
                Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                mTabStripField.setAccessible(true);

                LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);

                int dp10 = margin == 0 ? 50 : margin;

                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);

                    Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                    mTextViewField.setAccessible(true);

                    TextView mTextView = (TextView) mTextViewField.get(tabView);

                    tabView.setPadding(0, 0, 0, 0);

                    int width = 120;
//                    width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure(0, 0);
                        width = mTextView.getMeasuredWidth();
                    }

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    params.width = width;
                    params.leftMargin = dp10;
                    params.rightMargin = dp10;
                    tabView.setLayoutParams(params);

                    tabView.invalidate();
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public void changeTabIndicatorWidth(TabLayout tabLayout) {
        changeTabIndicatorWidth(tabLayout, 0);
    }

    /**
     * 设置tablayout指示器的宽度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicatorWidth(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

}