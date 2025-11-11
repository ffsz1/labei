package com.vslk.lbgx.room.egg.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.vslk.lbgx.room.egg.adapter.PoundEggWinPrizeRecordAdapter;
import com.hncxco.library_ui.widget.NestedScrollSwipeRefreshLayout;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.gift.EggGiftInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Function:
 * Author: Edward on 2019/5/8
 */
public class PoundEggWinPrizeRecordDialog extends BaseDialogFragment {
    public static final String KEY_TITLE = "KEY_TITLE";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_pay_income_list)
    RecyclerView rvPayIncomeList;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.swipe_refresh)
    NestedScrollSwipeRefreshLayout mSwipeRefreshLayout;
    Unbinder unbinder;

    private PoundEggWinPrizeRecordAdapter rankListAdapter;

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, null);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(this, tag).addToBackStack(null);
        return transaction.commitAllowingStateLoss();

    }

    public static PoundEggWinPrizeRecordDialog newInstance(String title) {
        PoundEggWinPrizeRecordDialog listDataDialog = new PoundEggWinPrizeRecordDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        listDataDialog.setArguments(bundle);
        return listDataDialog;
    }

    public PoundEggWinPrizeRecordDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        View view = inflater.inflate(R.layout.dialog_pound_egg_win_prize_record, window.findViewById(android.R.id.content),
                false);
        unbinder = ButterKnife.bind(this, view);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        setCancelable(true);
        initView();
        initCallback();
        loadData(Constants.PAGE_START);
        return view;
    }

    private void initCallback() {
        ivClose.setOnClickListener(v -> dismiss());
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            loadData(Constants.PAGE_START);
        });

        rankListAdapter.setOnLoadMoreListener(() -> {
            loadData(currPage + 1);
        }, rvPayIncomeList);
    }

    private int currPage = Constants.PAGE_START;


    private void initView() {
        if (getArguments() != null) {
            String title = getArguments().getString(KEY_TITLE);
//            tvTitle.setText(title);
        }
        rvPayIncomeList.setLayoutManager(new LinearLayoutManager(getContext()));
        rankListAdapter = new PoundEggWinPrizeRecordAdapter();
        rvPayIncomeList.setAdapter(rankListAdapter);
    }

    public void setupFailView(boolean isRefresh) {
        if (isRefresh) {
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            rankListAdapter.loadMoreFail();
        }
    }

    public void setupSuccessView(List<EggGiftInfo> eggGiftInfos, boolean isRefresh) {
        if (isRefresh) {
            mSwipeRefreshLayout.setRefreshing(false);
            if (ListUtils.isListEmpty(eggGiftInfos)) {
                eggGiftInfos = new ArrayList<>();
            }
            rankListAdapter.setNewData(eggGiftInfos);
        } else {
            rankListAdapter.loadMoreComplete();
            if (!ListUtils.isListEmpty(eggGiftInfos)) {
                rankListAdapter.addData(eggGiftInfos);
            }
        }
        //不够10个的话，不显示加载更多
        if (eggGiftInfos.size() < Constants.PAGE_SIZE) {
            rankListAdapter.setEnableLoadMore(false);
        }
    }

    private void loadData(int page) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket() + "");
        params.put("pageNum", String.valueOf(page));
        params.put("pageSize", String.valueOf(Constants.PAGE_SIZE));
        OkHttpManager.getInstance().getRequest(UriProvider.getPoundEggRewordRecord(), params, new OkHttpManager.MyCallBack<ServiceResult<List<EggGiftInfo>>>() {
            @Override
            public void onError(Exception e) {
                setupFailView(page == Constants.PAGE_START);
            }

            @Override
            public void onResponse(ServiceResult<List<EggGiftInfo>> response) {
                currPage = page;
                if (response.isSuccess()) {
                    setupSuccessView(response.getData(), page == Constants.PAGE_START);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
