package com.vslk.lbgx.room.avroom.widget.dialog;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.blankj.utilcode.util.ConvertUtils;
import com.vslk.lbgx.room.avroom.adapter.FingerGuessingGameRecordAdapter;
import com.hncxco.library_ui.widget.ViewHolder;
import com.hncxco.library_ui.widget.dialog.BaseDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.room.bean.FingerGuessingGameRecordInfo;
import com.tongdaxing.xchat_core.room.model.FingerGuessingGameModel;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.util.util.DisplayUtils;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FingerGuessingGameRecordDialog extends BaseDialog implements OnRefreshListener, OnLoadmoreListener {
    private FingerGuessingGameModel fingerGuessingGameModel = new FingerGuessingGameModel();
    private FingerGuessingGameRecordAdapter adapter;
    @BindView(R.id.swipe_refresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    private int curPage = 0;

    @Override
    public void convertView(ViewHolder viewHolder) {
        ButterKnife.bind(this, viewHolder.getConvertView());
        ivClose.setOnClickListener(v -> dismiss());
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadmoreListener(this);
        adapter = new FingerGuessingGameRecordAdapter(R.layout.item_finger_guessing_game_record);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        loadData(0);
    }

    private void loadData(int page) {
        fingerGuessingGameModel.getFingerGuessingGameRecord(Constants.PAGE_SIZE, page, new OkHttpManager.MyCallBack<ServiceResult<List<FingerGuessingGameRecordInfo>>>() {
            @Override
            public void onError(Exception e) {
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();
                if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                    SingleToastUtil.showToast(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<FingerGuessingGameRecordInfo>> response) {
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadmore();
                if (response.isSuccess() & response.getData() != null) {
                    curPage = page;
                    if (curPage == Constants.PAGE_START_ZERO) {
                        adapter.setNewData(response.getData());
                    } else {
                        adapter.addData(response.getData());
                    }
                }
            }
        });
    }

    @Override
    public int getContentView() {
        return R.layout.dialog_finger_guessing_game_record;
    }

    @Override
    protected int getDialogWidth() {
        return DisplayUtils.getScreenWidth(Objects.requireNonNull(getActivity())) - ConvertUtils.dp2px( 30);
    }

    @Override
    protected int getDialogHeight() {
        return ConvertUtils.dp2px( 450);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        loadData(++curPage);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadData(0);
    }
}
