package com.vslk.lbgx.ui.rank.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.presenter.rankinglist.IRankingListView;
import com.vslk.lbgx.presenter.rankinglist.RankingListPresenter;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.ui.rank.adapter.RankingListAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.bean.RankingInfo;

import java.util.List;

import static com.vslk.lbgx.ui.rank.fragment.RankingListFragment.RANKING_TYPE_CHARM;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/26
 * 描述        对应排行榜
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(RankingListPresenter.class)
public class RankingItemFragment extends BaseMvpFragment<IRankingListView, RankingListPresenter> implements IRankingListView {

    private int type = RANKING_TYPE_CHARM;
    private int dateType;

    private SwipeRefreshLayout refreshLayout;
    private RankingListAdapter adapter;
    private RecyclerView recyclerView;
    private View headerView;

    @Override
    protected void onInitArguments(Bundle bundle) {
        if (bundle != null) {
            type = bundle.getInt("type");
            dateType = bundle.getInt("dateType");
        }
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_ranking_item;
    }

    @Override
    public void onFindViews() {
        refreshLayout = mView.findViewById(R.id.refresh_layout);
        recyclerView = mView.findViewById(R.id.recycler_view);
        headerView = View.inflate(mContext, R.layout.layout_ranking_list_header, null);
    }

    @Override
    protected void onLazyLoadData() {
        getMvpPresenter().refreshData(type, dateType);
    }

    @Override
    public void onSetListener() {
        refreshLayout.setOnRefreshListener(() -> getMvpPresenter().refreshData(type, dateType));
    }

    @Override
    public void initiate() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RankingListAdapter(R.layout.item_ranking_list, mContext, type);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            RankingInfo info = (RankingInfo) adapter.getItem(position);
            if (info != null) {
                UserInfoActivity.start(getContext(), info.getUid());
            }
        });
        adapter.setHeaderView(headerView);
        adapter.setOnHeaderChildViewClickListener(view -> {
            Long uid = Long.valueOf(view.getTag().toString());
            UserInfoActivity.start(getContext(), uid);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setupFailView(String message) {
        toast(message);
        refreshLayout.setRefreshing(false);
        adapter.setNewData(null);
    }

    @Override
    public void setupSuccessView(List<RankingInfo> rankingList) {
        refreshLayout.setRefreshing(false);
        recyclerView.post(() -> adapter.setNewData(rankingList));
    }

    public static Fragment newInstance(int type, int dateType) {
        Bundle bundle = new Bundle();
        RankingItemFragment fragment = new RankingItemFragment();
        bundle.putInt("type", type);
        bundle.putInt("dateType", dateType);
        fragment.setArguments(bundle);
        return fragment;
    }
}
