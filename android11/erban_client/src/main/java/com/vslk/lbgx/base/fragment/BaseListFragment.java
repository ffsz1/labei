package com.vslk.lbgx.base.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.List;
import java.util.Map;

public class BaseListFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    public int pageSize = 10;
    //后端这个参数还有可能是pageNo
    public String pageNoParmasName = "pageNum";
    private int page = 1;
    public int maxPage = 50;

    public BaseListFragment setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    private Handler handler = new Handler();

    public BaseQuickAdapter getBaseQuickAdapter() {
        return adapter;
    }


    private BaseQuickAdapter adapter = null;


    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private RecyclerView recyclerView;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_base_list;
    }

    @Override
    public void onFindViews() {
        recyclerView = mView.findViewById(R.id.rv_base_list);
        swipeRefreshLayout = mView.findViewById(R.id.refresh_layout_base_list);


    }

    @Override
    public void onSetListener() {

    }

    @Override
    public void initiate() {
        if (getBaseQuickAdapter() == null)
            return;
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(getBaseQuickAdapter());
        getBaseQuickAdapter().setEmptyView(getEmptyView(recyclerView, getEmptyStr()));
        getBaseQuickAdapter().setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                if (NetworkUtil.isNetAvailable(mContext)) {
                    request();
                } else {
                    getBaseQuickAdapter().loadMoreEnd(true);
                }

                if (page > maxPage) {
                    getBaseQuickAdapter().loadMoreEnd(true);
                }
            }
        }, recyclerView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                request();
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        request();
    }


    private void request() {
        Map<String,String> param = CommonParamUtil.getDefaultParam();
        param.put(pageNoParmasName, "" + page);
        param.put("pageSize", pageSize + "");
        Json params = getParams();
        for (int i = 0; i < params.key_names().length; i++) {
            String key = params.key_names()[i];
            param.put(key, params.str(key));
        }
        OkHttpManager.getInstance().getRequest(getShortUrl(), param, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                swipeRefreshLayout.setRefreshing(false);
                getBaseQuickAdapter().loadMoreComplete();
                getBaseQuickAdapter().loadMoreEnd(true);
                toast("网络异常");
                if (handler != null)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            getDialogManager().dismissDialog();
                        }
                    });
            }

            @Override
            public void onResponse(Json json) {
                if (handler != null)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            getDialogManager().dismissDialog();
                        }
                    });

                swipeRefreshLayout.setRefreshing(false);
                if (json.num("code") != 200) {
                    toast(json.str("message", "网络异常"));
                    return;
                }
                List<Json> data;
                if (iDataFilter != null) {
                    data = iDataFilter.dataFilter(json);
                } else {
                    data = json.jlist("data");
                }

                if (page > Constants.PAGE_START) {
                    if (ListUtils.isListEmpty(data)){
                        getBaseQuickAdapter().loadMoreEnd(true);
                    }else {
                        getBaseQuickAdapter().addData(data);
                        getBaseQuickAdapter().loadMoreComplete();
                    }
                } else if (page == Constants.PAGE_START) {
                    getBaseQuickAdapter().setNewData(data);
                }
                if (data.size() < pageSize) {
                    getBaseQuickAdapter().loadMoreEnd(true);
                }
            }
        });
    }


    private IDataFilter iDataFilter;

    public void setDataFilter(IDataFilter iDataFilter) {
        this.iDataFilter = iDataFilter;
    }

    public interface IDataFilter {
        List<Json> dataFilter(Json json);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    private String shortUrl = "";
    private Json params = new Json();
    private String emptyStr = "";

    public Json getParams() {
        return params;
    }

    public BaseListFragment setOtherParams(Json params) {
        this.params = params;
        return this;
    }

    public String getEmptyStr() {
        return emptyStr;
    }

    public BaseListFragment setEmptyStr(String emptyStr) {
        this.emptyStr = emptyStr;
        return this;
    }

    public BaseListFragment setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
        return this;
    }

    public String getShortUrl() {
        return shortUrl;
    }


    public BaseListFragment setAdapter(BaseQuickAdapter shareFansAdapter) {
        this.adapter = shareFansAdapter;
        return this;
    }
}
