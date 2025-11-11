package com.vslk.lbgx.ui.me.bills.fragment;

import com.vslk.lbgx.ui.me.bills.adapter.GiftIncomeAdapter;
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
 * 新皮：收入页面
 * @author zwk 2018/5/31
 */
public class BillIncomeFragment extends BillBaseFragment implements OnDateSetListener {

    public static final String TAG = BillIncomeFragment.class.getSimpleName();
    private GiftIncomeAdapter adapter;

    @Override
    public void initiate() {
        super.initiate();
        adapter = new GiftIncomeAdapter(mBillItemEntityList);
        adapter.setOnLoadMoreListener(() -> {
            mCurrentCounter++;
            loadData();
        }, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void loadData() {
        CoreManager.getCore(IBillsCore.class).getGiftIncomeBills(mCurrentCounter, PAGE_SIZE, time);
    }

    @CoreEvent(coreClientClass = IBillsCoreClient.class)
    public void onGetIncomeBills(IncomeListInfo data) {
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
            List<Map<String, List<IncomeInfo>>> billList = data.getBillList();
            if (!billList.isEmpty()) {
                List<BillItemEntity> billItemEntities = new ArrayList<>();
                BillItemEntity billItemEntity;
                for (int i = 0; i < billList.size(); i++) {
                    Map<String, List<IncomeInfo>> map = billList.get(i);
                    for (String key : map.keySet()) {
//                        // key ---日期    value：list集合记录
                        List<IncomeInfo> incomeInfos = map.get(key);
                        if (ListUtils.isListEmpty(incomeInfos)) {
                            continue;
                        }
                        //正常item
                        for (IncomeInfo temp : incomeInfos) {
                            billItemEntity = new BillItemEntity(BillItemEntity.ITEM_NORMAL);
                            billItemEntity.mGiftInComeInfo = temp;
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
    public void onGetIncomeBillsError(String error) {
        if (mCurrentCounter == Constants.PAGE_START) {
            mRefreshLayout.setRefreshing(false);
            showNetworkErr();
        } else {
            adapter.loadMoreFail();
        }
    }

}
