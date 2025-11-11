package com.vslk.lbgx.ui.me.setting.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.ui.me.setting.presenter.MessageNotDisturbPresenter;
import com.vslk.lbgx.ui.me.setting.vew.IMsgNotDisturbView;
import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息免打扰界面
 *
 * @author zwk 2018/10/9
 */
@CreatePresenter(MessageNotDisturbPresenter.class)
public class MessageNotDisturbActivity extends BaseMvpActivity<IMsgNotDisturbView, MessageNotDisturbPresenter>
        implements IMsgNotDisturbView, View.OnClickListener {

    private RelativeLayout rlPrivateLatter;
    private TextView tvDisturbState;
    private int disturbState = 0;

    public static void start(Context context) {
        Intent intent = new Intent(context, MessageNotDisturbActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_not_disturb);
        rlPrivateLatter = (RelativeLayout) findViewById(R.id.rl_private_latter_not_disturb);
        rlPrivateLatter.setEnabled(false);
        tvDisturbState = (TextView) findViewById(R.id.tv_msg_disturb);
        rlPrivateLatter.setOnClickListener(this);
        getDialogManager().showProgressDialog(this, "加载中...");
        getMvpPresenter().getMsgDisturbState();

        ((AppToolBar)findViewById(R.id.toolbar)).setOnBackBtnListener(view -> finish());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_private_latter_not_disturb:
                List<ButtonItem> items = new ArrayList<>();
                if (disturbState != 1) {
                    ButtonItem buttonItem1 = new ButtonItem("所有对象", () -> {
                        getDialogManager().showProgressDialog(MessageNotDisturbActivity.this, "正在修改...");
                        getMvpPresenter().changeMsgDisturbState(1);
                    });
                    items.add(buttonItem1);
                }
                if (disturbState != 2) {
                    ButtonItem buttonItem2 = new ButtonItem("10级以下", () -> {
                        getDialogManager().showProgressDialog(MessageNotDisturbActivity.this, "正在修改...");
                        getMvpPresenter().changeMsgDisturbState(2);
                    });
                    items.add(buttonItem2);
                }
                if (disturbState != 0) {
                    ButtonItem buttonItem = new ButtonItem("关闭", () -> {
                        getDialogManager().showProgressDialog(MessageNotDisturbActivity.this, "正在修改...");
                        getMvpPresenter().changeMsgDisturbState(0);
                    });
                    items.add(buttonItem);
                }
                getDialogManager().showCommonPopupDialog(items, "取消");
                break;
            default:
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void getDisturbStateSuccess(int state) {
        getDialogManager().dismissDialog();
        disturbState = state;
        rlPrivateLatter.setEnabled(true);
        setDisturbState(state);
    }

    @Override
    public void getDisturbStateFail(String message) {
        getDialogManager().dismissDialog();
        SingleToastUtil.showToast(message);
    }

    @Override
    public void saveDisturbStateSuccess(int state) {
        getDialogManager().dismissDialog();
        disturbState = state;
        setDisturbState(state);
    }

    @Override
    public void saveDisturbStateFail(String message) {
        getDialogManager().dismissDialog();
        SingleToastUtil.showToast(message);
    }

    private void setDisturbState(int state) {
        switch (state) {
            case 0://关闭
                tvDisturbState.setText("关闭");
                break;
            case 1://所有人
                tvDisturbState.setText("所有人");
                break;
            case 2://10级以下
                tvDisturbState.setText("10级以下");
                break;
        }
    }
}
