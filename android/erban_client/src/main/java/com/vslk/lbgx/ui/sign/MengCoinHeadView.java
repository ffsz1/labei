package com.vslk.lbgx.ui.sign;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_core.mengcoin.MengCoinTaskBean;
import com.vslk.lbgx.ui.sign.adapter.TaskCenterSignInAdapter;
import com.vslk.lbgx.ui.sign.dialog.SignInRuleDialog;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件描述：
 *
 * @auther：zwk
 * @data：2019/1/15
 */
public class MengCoinHeadView extends RelativeLayout {
    private TaskCenterSignInAdapter taskCenterSignInAdapter;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_dian_dian_coin_value)
    TextView tvDianDianCoinValue;

    @OnClick({R.id.ll_pk, R.id.ll_lucky, R.id.tv_task_center_rule})
    public void muliteClick(View view) {
        switch (view.getId()) {
            case R.id.ll_pk:
                CommonWebViewActivity.start(getContext(), WebUrl.PK);
                break;
            case R.id.ll_lucky:
                CommonWebViewActivity.start(getContext(), WebUrl.LUCKY);
                break;
            case R.id.tv_task_center_rule:
                SignInRuleDialog signInRuleDialog = new SignInRuleDialog();
                signInRuleDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
                break;
            default:
                break;
        }
    }

    public MengCoinHeadView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.item_task_center_head, this);
        ButterKnife.bind(this, view);
        taskCenterSignInAdapter = new TaskCenterSignInAdapter(R.layout.item_task_center_sign_in, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(taskCenterSignInAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == 0) {//第一个
                    outRect.right = ConvertUtils.dp2px(5);
                } else if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {//最后一天
                    outRect.left = ConvertUtils.dp2px(5);
                } else {
                    outRect.left = ConvertUtils.dp2px(5);
                    outRect.right = ConvertUtils.dp2px(5);
                }

            }
        });
    }

    /**
     * 填充头部view的布局数据
     *
     * @param mengCoinTaskBean
     */
    public void setData(MengCoinTaskBean mengCoinTaskBean) {
        if (mengCoinTaskBean != null) {
            tvDianDianCoinValue.setText(String.valueOf((int) mengCoinTaskBean.getMcoinNum()));
            if (!ListUtils.isListEmpty(mengCoinTaskBean.getWeeklyMissions())) {
                taskCenterSignInAdapter.setNewData(mengCoinTaskBean.getWeeklyMissions());
            }
        }
    }
}
