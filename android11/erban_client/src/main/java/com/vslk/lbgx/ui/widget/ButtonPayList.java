package com.vslk.lbgx.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongdaxing.erban.R;

/**
 * Created by Administrator on 2018/3/29.
 */

public class ButtonPayList extends RelativeLayout {

    private View inflate;
    private TextView tabName;
    private View line;

    public TextView getTabName() {
        return tabName;
    }

    public View getLine() {
        return line;
    }

    public ButtonPayList(Context context) {

        this(context,null);
    }

    public ButtonPayList(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate = View.inflate(context, R.layout.button_pay_list, this);
        tabName = inflate.findViewById(R.id.tv_button_pay_list);
        line = inflate.findViewById(R.id.line_button_pay_list);
    }
}
