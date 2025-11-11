package com.vslk.lbgx.im.transfer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.hncxco.library_ui.widget.AppToolBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.vslk.lbgx.utils.ImageLoadUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransferActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    AppToolBar toolbar;
    @BindView(R.id.iv_user_head)
    RoundedImageView ivUserHead;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.et_gold)
    EditText etGold;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    private String sessionId;
    private TransferDialog dialog;

    public static void start(Activity activity, String sessionId, int requestCode) {
        Intent intent = new Intent(activity, TransferActivity.class);
        intent.putExtra("sessionId", sessionId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);
        tvOk.setClickable(false);
        tvOk.setEnabled(false);
        etGold.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    tvOk.setClickable(false);
                    tvOk.setEnabled(false);
                } else {
                    tvOk.setClickable(true);
                    tvOk.setEnabled(true);
                }
            }
        });
        tvOk.setOnClickListener(v -> {
            String gold = etGold.getText().toString();
            if (gold.isEmpty()) {
                Toast.makeText(this, "请输入金额", Toast.LENGTH_LONG).show();
            } else {
                hieInput(v);
                String nick = tvNick.getText().toString();
                dialog = new TransferDialog();
                Bundle bundle = new Bundle();
                bundle.putString("sessionId", sessionId);
                bundle.putString("gold", gold);
                bundle.putString("nick", nick);
                dialog.setArguments(bundle);
                dialog.setListener(() -> {
                    sendTransfer(sessionId, gold);
                    if (dialog !=  null){
                        dialog.dismiss();
                    }
                });
                dialog.show(getSupportFragmentManager(), "transfer");
            }
        });
        etGold.requestFocus();
        toolbar.setOnLeftImgBtnClickListener(() -> finish());
        getToUser();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hieInput(toolbar);
    }

    @Override
    protected void onDestroy() {
        if (dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
        super.onDestroy();
    }

    private void hieInput(View v) {
        InputMethodManager manager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) {
            manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void getToUser() {
        sessionId = getIntent().getStringExtra("sessionId");
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("queryUid", sessionId);
        //新增参数
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getUserInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<UserInfo>>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<UserInfo> response) {
                if (null != response) {
                    tvNick.setText(response.getData().getNick());
                    ImageLoadUtils.loadCircleImage(TransferActivity.this, response.getData().getAvatar(), ivUserHead, R.drawable.ic_default_avatar);
                }
            }
        });
    }

    public void sendTransfer(String sessionId, String gold) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        String uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
        params.put("uid", uid);
        params.put("recvUid", sessionId);
        params.put("sendUid", uid);
        params.put("goldNum", gold);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.getGold2gold(), params, new OkHttpManager.MyCallBack<ServiceResult<TransUserBean>>() {
            @Override
            public void onError(Exception e) {
                ToastUtils.showLong(e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<TransUserBean> response) {
                if (response.isSuccess()) {
                    Intent intent = new Intent();
                    intent.putExtra("transfer", response.getData());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    String message = response.getMessage();
                    Toast.makeText(getApplication(),message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
