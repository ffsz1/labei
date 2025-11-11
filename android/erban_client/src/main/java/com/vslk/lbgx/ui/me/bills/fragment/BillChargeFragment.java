package com.vslk.lbgx.ui.me.bills.fragment;

import com.vslk.lbgx.ui.me.bills.adapter.ChargeBillsAdapter;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.bills.IBillsCore;
import com.tongdaxing.xchat_core.bills.IBillsCoreClient;
import com.tongdaxing.xchat_core.bills.bean.BillItemEntity;
import com.tongdaxing.xchat_core.bills.bean.ExpendInfo;
import com.tongdaxing.xchat_core.bills.bean.ExpendListInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 新皮：充值记录页面
 * @author zwk 2018/5/31
 */
public class BillChargeFragment extends BillBaseFragment implements OnDateSetListener {

    public static final String TAG = "BillChargeFragment";
    private ChargeBillsAdapter adapter;

    @Override
    public void onFindViews() {
        super.onFindViews();
    }

    @Override
    public void onSetListener() {
        super.onSetListener();
    }

    @Override
    public void initiate() {
        super.initiate();
        adapter = new ChargeBillsAdapter(mBillItemEntityList);
        adapter.setOnLoadMoreListener(() -> {
            mCurrentCounter++;
            loadData();
        },mRecyclerView);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void loadData() {
        CoreManager.getCore(IBillsCore.class).getChargeBills(mCurrentCounter, PAGE_SIZE, time);
    }

    @CoreEvent(coreClientClass = IBillsCoreClient.class)
    public void onGetChargeBills(ExpendListInfo data) {
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
            List<Map<String, List<ExpendInfo>>> billList = data.getBillList();
            if (!billList.isEmpty()) {
                int size = mBillItemEntityList.size();
                List<BillItemEntity> billItemEntities = new ArrayList<>();
                for (int i = 0; i < billList.size(); i++) {
                    Map<String, List<ExpendInfo>> map = billList.get(i);
                    for (String key : map.keySet()) {
                        // key ---日期    value：list集合记录
                        List<ExpendInfo> expendInfos = map.get(key);
                        if (ListUtils.isListEmpty(expendInfos)) {
                            continue;
                        }
                        //正常item
                        for (ExpendInfo temp : expendInfos) {
                            billItemEntity = new BillItemEntity(BillItemEntity.ITEM_NORMAL);
                            billItemEntity.mChargeExpendInfo = temp;
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
    }

    @CoreEvent(coreClientClass = IBillsCoreClient.class)
    public void onGetChargeBillsError(String error) {
        if (mCurrentCounter == Constants.PAGE_START) {
            showNetworkErr();
        } else {
            adapter.loadMoreFail();
        }
    }
}
