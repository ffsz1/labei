package com.vslk.lbgx.ui.me.setting.activity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vslk.lbgx.XChatApplication;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * Created by chenran on 2017/10/16.
 */
public class LabActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);

        //根据ID找到RadioGroup实例
        RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
        RadioButton button1 = (RadioButton) findViewById(R.id.test);
        RadioButton button2 = (RadioButton) findViewById(R.id.product);
        RadioButton button3 = (RadioButton) findViewById(R.id.product18);

        if (UriProvider.JAVA_WEB_URL.contains(UriProvider.DEBUG)) {
            button1.setChecked(true);
            button2.setChecked(false);
            button3.setChecked(false);
        } else if (UriProvider.JAVA_WEB_URL.contains(UriProvider.PRODUCT)) {
            button1.setChecked(false);
            button2.setChecked(true);
            button3.setChecked(false);
        } else if (UriProvider.JAVA_WEB_URL.contains(UriProvider.PRODUCT_18)) {
            button1.setChecked(false);
            button2.setChecked(false);
            button3.setChecked(true);
        }

        //绑定一个匿名监听器
        group.setOnCheckedChangeListener((arg0, arg1) -> {
            XChatApplication xChatApplication = (XChatApplication) getApplication();
            if (arg1 == R.id.test) {
                xChatApplication.init(1);
            } else if (arg1 == R.id.product) {
                xChatApplication.init(2);
            } else {
                xChatApplication.init(3);
            }
            CoreManager.getCore(IAuthCore.class).logout();
            finish();
        });
    }
}
