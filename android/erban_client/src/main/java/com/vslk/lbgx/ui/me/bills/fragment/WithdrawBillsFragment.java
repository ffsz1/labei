package com.vslk.lbgx.ui.me.bills.fragment;

import com.vslk.lbgx.ui.me.bills.adapter.WithdrawBillsAdapter;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.bills.IBillsCore;
import com.tongdaxing.xchat_core.bills.IBillsCoreClient;
import com.tongdaxing.xchat_core.bills.bean.BillItemEntity;
import com.tongdaxing.xchat_core.bills.bean.IncomeInfo;
import com.tongdaxing.xchat_core.bills.bean.IncomeListInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p> 提现账单（不包含红包） </p>
 *
 * @author Administrator
 * @date 2017/11/7
 */
public class WithdrawBillsFragment extends BillBaseFragment implements OnDateSetListener {

    private WithdrawBillsAdapter adapter;
    public static final String TAG = WithdrawBillsFragment.class.getSimpleName();

    @Override
    public void initiate() {
        super.initiate();
        adapter = new WithdrawBillsAdapter(mBillItemEntityList);
        adapter.setOnLoadMoreListener(() -> {
            mCurrentCounter++;
            loadData();
        }, mRecyclerView);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        CoreManager.getCore(IBillsCore.class).getWithdrawBills(mCurrentCounter, PAGE_SIZE, time);
    }

    @CoreEvent(coreClientClass = IBillsCoreClient.class)
    public void onGetWithdrawBills(IncomeListInfo data) {
        mRefreshLayout.setRefreshing(false);
        if (null != data) {
            if (mCurrentCounter == Constants.PAGE_START) {
                hideStatus();
                mBillItemEntityList.clear();
                adapter.setNewData(mBillItemEntityList);
                mRecyclerView.smoothScrollToPosition(0);
            } else {
                adapter.loadMoreComplete();
            }
            BillItemEntity billItemEntity;
            List<Map<String, List<IncomeInfo>>> billList = data.getBillList();
            if (!billList.isEmpty()) {
                List<BillItemEntity> billItemEntities = new ArrayList<>();
                for (int i = 0; i < billList.size(); i++) {
                    Map<String, List<IncomeInfo>> map = billList.get(i);
                    for (String key : map.keySet()) {
                        // key ---日期    value：list集合记录
                        List<IncomeInfo> incomeInfos = map.get(key);
                        if (ListUtils.isListEmpty(incomeInfos)) {
                            continue;
                        }
                        //正常item
                        for (IncomeInfo temp : incomeInfos) {
                            billItemEntity = new BillItemEntity(BillItemEntity.ITEM_NORMAL);
                            billItemEntity.mWithdrawInfo = temp;
                            //目的是为了比较
                            billItemEntity.time = key;
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
    }

    @CoreEvent(coreClientClass = IBillsCoreClient.class)
    public void onGetWithdrawBillsError(String error) {
        mRefreshLayout.setRefreshing(false);
        if (mCurrentCounter == Constants.PAGE_START) {
            mRefreshLayout.setRefreshing(false);
            showNetworkErr();
        } else {
            adapter.loadMoreFail();
        }
    }
}
