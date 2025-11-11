package com.vslk.lbgx.ui.me.bills.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.fragment.BaseFragment;
import com.vslk.lbgx.ui.me.bills.adapter.RedBagBillsAdapter;
import com.vslk.lbgx.ui.me.bills.event.DateInfoEvent;
import com.vslk.lbgx.ui.widget.RecyclerViewNoBugLinearLayoutManager;
import com.vslk.lbgx.ui.widget.itemdecotion.PowerGroupListener;
import com.vslk.lbgx.ui.widget.itemdecotion.PowerfulStickyDecoration;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.UIUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.bills.IBillsCore;
import com.tongdaxing.xchat_core.bills.IBillsCoreClient;
import com.tongdaxing.xchat_core.bills.bean.BillItemEntity;
import com.tongdaxing.xchat_core.bills.bean.RedBagInfo;
import com.tongdaxing.xchat_core.bills.bean.RedBagListInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.tongdaxing.xchat_framework.util.util.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>账单红包  </p>
 * Created by Administrator on 2017/11/7.
 */
public class WithdrawRedFragment extends BaseFragment {
    private static final String TAG = WithdrawRedFragment.class.getSimpleName();
    private List<BillItemEntity> mBillItemEntityList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context mContext;
    private RedBagBillsAdapter adapter;

    protected int mCurrentCounter = Constants.PAGE_START;//当前页
    protected static final int PAGE_SIZE = Constants.BILL_PAGE_SIZE;
    protected long time = System.currentTimeMillis();
    private TextView tvTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public void onFindViews() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh);
        tvTime = mView.findViewById(R.id.tv_time);
    }

    @Override
    public void onSetListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentCounter = 1;
                loadData();
            }
        });
    }

    @Override
    public void initiate() {
        tvTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(time)));
        adapter = new RedBagBillsAdapter(mBillItemEntityList);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mCurrentCounter++;
                loadData();
            }
        }, mRecyclerView);
        RecyclerViewNoBugLinearLayoutManager manager = new RecyclerViewNoBugLinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);

        PowerfulStickyDecoration decoration = PowerfulStickyDecoration.Builder
                .init(new PowerGroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        //获取组名，用于判断是否是同一组
                        if (mBillItemEntityList.size() > position && position >= 0) {
                            return mBillItemEntityList.get(position).time;
                        }
                        return null;
                    }

                    @Override
                    public View getGroupView(int position) {
                        //获取自定定义的组View
                        if (mBillItemEntityList.size() > position && position >= 0) {
                            View view = getLayoutInflater().inflate(R.layout.item_group, null, false);
                            ((TextView) view.findViewById(R.id.tv)).setText(TimeUtils.getDateTimeString(JavaUtil.str2long(mBillItemEntityList.get(position).time), "yyyy-MM-dd"));
                            return view;
                        } else {
                            return null;
                        }
                    }
                })
                .setGroupHeight(UIUtil.dip2px(getActivity(), 50))       //设置高度
                .isAlignLeft(true)                                 //靠右边显示   默认左边
                .setGroupBackground(getResources().getColor(R.color.colorPrimaryDark))    //设置背景   默认透明
                .build();
        mRecyclerView.addItemDecoration(decoration);
        showLoading();
        loadData();
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_xrexylerview;
    }

    private void loadData() {
        CoreManager.getCore(IBillsCore.class).getWithdrawRedBills(mCurrentCounter, PAGE_SIZE, time);
    }

    @CoreEvent(coreClientClass = IBillsCoreClient.class)
    public void onGetWithdrawRedBills(RedBagListInfo data) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (mCurrentCounter == Constants.PAGE_START) {
            hideStatus();
            mBillItemEntityList.clear();
            adapter.setNewData(mBillItemEntityList);
        } else {
            adapter.loadMoreComplete();
        }
        BillItemEntity billItemEntity;
        List<Map<String, List<RedBagInfo>>> billList = data.getBillList();
        if (!billList.isEmpty()) {
            tvTime.setVisibility(View.GONE);
            List<BillItemEntity> billItemEntities = new ArrayList<>();
            for (int i = 0; i < billList.size(); i++) {
                Map<String, List<RedBagInfo>> map = billList.get(i);
                for (String key : map.keySet()) {
                    // key ---日期    value：list集合记录
                    List<RedBagInfo> redBagInfos = map.get(key);
                    if (ListUtils.isListEmpty(redBagInfos)) continue;
                    //正常item
                    for (RedBagInfo temp : redBagInfos) {
                        billItemEntity = new BillItemEntity(BillItemEntity.ITEM_NORMAL);
                        billItemEntity.mRedBagInfo = temp;
                        billItemEntity.time = key;  //目的是为了比较
                        billItemEntities.add(billItemEntity);
                    }
                }
            }
            if (billItemEntities.size() < Constants.BILL_PAGE_SIZE && mCurrentCounter == Constants.PAGE_START) {
                adapter.setEnableLoadMore(false);
            }
            adapter.addData(billItemEntities);
        } else {
            if (mCurrentCounter == Constants.PAGE_START) {
                showNoData(getResources().getString(R.string.bill_no_data_text));
            } else {
                adapter.loadMoreEnd(true);
            }
        }
    }

    @CoreEvent(coreClientClass = IBillsCoreClient.class)
    public void onGetWithdrawRedBillsError(String error) {
        if (mCurrentCounter == Constants.PAGE_START) {
            mSwipeRefreshLayout.setRefreshing(false);
            showNetworkErr();
        } else {
            adapter.loadMoreFail();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDateInfoEvent(DateInfoEvent event) {
        if (event != null && event.position == 1) {
            time = event.millSeconds;
            mCurrentCounter = 1;
            showLoading();
            loadData();
        }
    }

    @Override
    public View.OnClickListener getLoadListener() {
        return v -> {
            mCurrentCounter = 1;
            showLoading();
            loadData();
        };
    }
}
