package com.vslk.lbgx.ui.rank.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.ImageView;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.databinding.ActivityGradeRuleBinding;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/3
 * 描述        等级规则
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class GradeRuleActivity extends BaseActivity {

    private ActivityGradeRuleBinding mBinding;
    private boolean isCharm;

    @BindView(R.id.iv_rule)
    ImageView ruleIv;

    public static void start(Context context, boolean isCharmFlag) {
        Intent intent = new Intent(context, GradeRuleActivity.class);
        intent.putExtra("isCharm", isCharmFlag);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_grade_rule);
        //设置标题
        mBinding.toolbar.setOnBackBtnListener(view -> finish());
        ButterKnife.bind(this);
        isCharm = getIntent().getBooleanExtra("isCharm", false);
        if (isCharm) {
            ruleIv.setBackgroundResource(R.mipmap.meili_rate);
        } else {
            ruleIv.setBackgroundResource(R.mipmap.money_rate);
        }
    }
}
