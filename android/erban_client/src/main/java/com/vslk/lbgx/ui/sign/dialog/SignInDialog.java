package com.vslk.lbgx.ui.sign.dialog;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.hncxco.library_ui.widget.ViewHolder;
import com.hncxco.library_ui.widget.dialog.BaseDialog;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.mengcoin.MengCoinBean;
import com.tongdaxing.xchat_core.mengcoin.MengCoinTaskBean;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.vslk.lbgx.model.mengcoin.MengCoinModel;
import com.vslk.lbgx.ui.sign.TaskCenterActivity;
import com.vslk.lbgx.ui.sign.adapter.SignInAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Function:签到对话框
 * Author: Edward on 2019/5/24
 */
public class SignInDialog extends BaseDialog {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_sign_in_count)
    TextView tvSignInCount;
    @BindView(R.id.tv_add_value)
    TextView tvAddValue;
    private SignInAdapter signInAdapter;
    private MengCoinModel mengCoinModel;

    @Override
    public void convertView(ViewHolder viewHolder) {
        ButterKnife.bind(this, viewHolder.getConvertView());
        mengCoinModel = new MengCoinModel();
        signInAdapter = new SignInAdapter(R.layout.item_sign_in, null);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(signInAdapter);
        getCurrentUserMengCoinTaskList();
    }

    /**
     * 获取当前用户的萌币任务列表接口
     */
    public void getCurrentUserMengCoinTaskList() {
        mengCoinModel.getCurrentUserMengCoinTaskList(new OkHttpManager.MyCallBack<ServiceResult<MengCoinTaskBean>>() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(ServiceResult<MengCoinTaskBean> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        setData(response.getData().getWeeklyMissions());
                    }
                }
            }
        });
    }

    private void setData(List<MengCoinBean> list) {
        if (!ListUtils.isListEmpty(list) && signInAdapter != null) {
            tvSignInCount.setText("已连续签到 " + signInCount(list) + " 天");
            MengCoinBean temp = getLastInfo(list);
            if (temp != null) {
                tvAddValue.setVisibility(View.VISIBLE);
                tvAddValue.setText("+" + (int) temp.getMcoinAmount());
            } else {
                tvAddValue.setVisibility(View.GONE);
            }
            signInAdapter.setNewData(list);
        }
    }

    private MengCoinBean getLastInfo(List<MengCoinBean> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getMissionStatus() == 3) {
                return list.get(i);
            }
        }
        return null;
    }

    private int signInCount(List<MengCoinBean> list) {
        int count = 0;
        if (!ListUtils.isListEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMissionStatus() == 3) {
                    count++;
                }
            }
        }
        return count;
    }

    @OnClick({R.id.sign_in_task_center, R.id.iv_close})
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_task_center) {
            TaskCenterActivity.start(getActivity());
            dismiss();
        } else if (view.getId() == R.id.iv_close) {
            dismiss();
        }
    }

    @Override
    protected int getDialogHeight() {
        return ConvertUtils.dp2px(360);
    }

    @Override
    public int getContentView() {
        return R.layout.dialog_sigin_in;
    }
}
