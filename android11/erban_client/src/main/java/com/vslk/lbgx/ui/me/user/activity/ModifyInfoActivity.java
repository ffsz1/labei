package com.vslk.lbgx.ui.me.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 编辑个性签名和昵称页面
 * Created by zhouxiangfeng on 2017/5/13.
 */
public class ModifyInfoActivity extends BaseActivity {

    private EditText etEditText;
    private EditText etEditTextNick;
    public static final String CONTENT = "content";
    public static final String CONTENTNICK = "contentNick";
    private CoordinatorLayout layout_coordinator;
    private String title;

    private AppToolBar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        onFindViews();
        onSetListener();
        init();
        initData();
    }

    private void initData() {
        long currentUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        UserInfo userInfos = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(currentUid);
        if (!StringUtil.isEmpty(title) && userInfos != null) {
            if (title.equals("个性签名")) {
                etEditText.setText(userInfos.getUserDesc());
            } else {
                etEditTextNick.setText(userInfos.getNick());
            }
        }
    }

    private void init() {
        title = getIntent().getStringExtra("title");
        if (title.equals("个性签名")) {
            etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
        } else if (title.equals("昵称")) {
            etEditTextNick.setVisibility(View.VISIBLE);
            etEditText.setVisibility(View.GONE);
            etEditTextNick.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        }
        initAppToolBar(title);
    }

    public void initAppToolBar(String title) {
        mToolBar.setTitle(title);
    }

    /**
     * 正则表达式去掉数据中的空格\\s*,回车\n,换行符\r,制表符\t
     * @param str 传进来的字符串
     * @return 去掉空格和回车符
     */
    public static String removeStr(String str){
        // 正则表达式匹配空格和换行符
        Pattern par = Pattern.compile("\\s*|\t|\r|\n");
        Matcher mch = par.matcher(str);
        // 返回数据
        return mch.replaceAll("");
    }


    private void onSetListener() {
        back(mToolBar);
        mToolBar.setOnRightBtnClickListener(view -> {
            String content = etEditText.getText().toString();
            String contentNick = etEditTextNick.getText().toString();
            contentNick = removeStr(contentNick);
            //修改个人介绍
            if (!content.trim().isEmpty()) {
                Intent intent = new Intent();
                intent.putExtra(CONTENT, content);
                setResult(RESULT_OK, intent);
                finish();
            } else if (!contentNick.trim().isEmpty()) {
                Intent intent = new Intent();
                intent.putExtra(CONTENTNICK, contentNick);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Snackbar.make(layout_coordinator, "所填内容为空！", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void onFindViews() {
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);
        etEditText = (EditText) findViewById(R.id.et_content);
        etEditTextNick = (EditText) findViewById(R.id.et_content_nick);
        layout_coordinator = (CoordinatorLayout) findViewById(R.id.layout_coordinator);
    }

    public boolean isValid() {
        return etEditText.length() <= 60;
    }
}
