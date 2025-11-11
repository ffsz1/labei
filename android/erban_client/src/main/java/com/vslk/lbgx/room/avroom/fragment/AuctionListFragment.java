package com.vslk.lbgx.room.avroom.fragment;

import android.view.View;
import android.widget.ListView;

import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.auction.bean.AuctionListUserInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.presenter.AuctionListPresenter;
import com.tongdaxing.xchat_core.room.view.IAuctionListView;
import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.room.avroom.adapter.AuctionListAdapter;
import com.vslk.lbgx.room.avroom.adapter.AuctionListEmptyItem;
import com.vslk.lbgx.room.avroom.adapter.AuctionListHeaderItem;
import com.vslk.lbgx.room.avroom.adapter.AuctionListItem;
import com.vslk.lbgx.room.widget.dialog.UserInfoDialog;

import java.util.List;

/**
 * @author xiaoyu
 * @date 2017/12/28
 */
@CreatePresenter(AuctionListPresenter.class)
public class AuctionListFragment extends BaseMvpFragment<IAuctionListView, AuctionListPresenter>
        implements AuctionListItem.OnAuctionListItemClick, View.OnClickListener, IAuctionListView {
    private ListView listView;
    private AuctionListAdapter adapter;
    private List<AuctionListUserInfo> weekAuctionList;
    private List<AuctionListUserInfo> totalAuctionList;
    private RoomInfo roomInfo;

    @Override
    public void onFindViews() {
        listView = mView.findViewById(R.id.auction_list);
        adapter = new AuctionListAdapter();
        listView.setAdapter(adapter);
    }

    @Override
    public void onSetListener() {
    }

    @Override
    public void initiate() {
        roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        setAuctionEmptyList();
        if (roomInfo != null)
            getMvpPresenter().requestWeekAuctionList(roomInfo.getUid());
    }

    private void setAuctionEmptyList() {
        AuctionListHeaderItem headerItem = new AuctionListHeaderItem(getContext(), AuctionListAdapter.VIEW_TYPE_WEEK_HEADER);
        adapter.addItem(headerItem);

        AuctionListEmptyItem emptyItem = new AuctionListEmptyItem(getContext(), AuctionListAdapter.VIEW_TYPE_EMPTY);
        adapter.addItem(emptyItem);

        headerItem = new AuctionListHeaderItem(getContext(), AuctionListAdapter.VIEW_TYPE_TOTAL_HEADER);
        adapter.addItem(headerItem);

        emptyItem = new AuctionListEmptyItem(getContext(), AuctionListAdapter.VIEW_TYPE_EMPTY);
        adapter.addItem(emptyItem);
    }

    private void setAuctionList() {
        adapter.clear();

        AuctionListHeaderItem headerItem = new AuctionListHeaderItem(getContext(), AuctionListAdapter.VIEW_TYPE_WEEK_HEADER);
        adapter.addItem(headerItem);

        if (weekAuctionList != null && weekAuctionList.size() > 0) {
            for (int i = 0; i < weekAuctionList.size(); i++) {
                AuctionListItem auctionListItem = new AuctionListItem(getContext(), AuctionListAdapter.VIEW_TYPE_WEEK_ITEM, weekAuctionList.get(i), i);
                auctionListItem.setOnAuctionListItemClick(this);
                adapter.addItem(auctionListItem);
            }
        } else {
            AuctionListEmptyItem emptyItem = new AuctionListEmptyItem(getContext(), AuctionListAdapter.VIEW_TYPE_EMPTY);
            adapter.addItem(emptyItem);
        }

        headerItem = new AuctionListHeaderItem(getContext(), AuctionListAdapter.VIEW_TYPE_TOTAL_HEADER);
        adapter.addItem(headerItem);

        if (totalAuctionList != null && totalAuctionList.size() > 0) {
            for (int i = 0; i < totalAuctionList.size(); i++) {
                AuctionListItem auctionListItem = new AuctionListItem(getContext(), AuctionListAdapter.VIEW_TYPE_TOTAL_ITEM, totalAuctionList.get(i), i);
                auctionListItem.setOnAuctionListItemClick(this);
                adapter.addItem(auctionListItem);
            }
        } else {
            AuctionListEmptyItem emptyItem = new AuctionListEmptyItem(getContext(), AuctionListAdapter.VIEW_TYPE_EMPTY);
            adapter.addItem(emptyItem);
        }
    }

    public void onPagerSelect() {
        if (roomInfo != null)
            getMvpPresenter().requestWeekAuctionList(roomInfo.getUid());
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_auction_list;
    }

    @Override
    public void onRequestWeekAuctionList(List<AuctionListUserInfo> auctionListUserInfos) {
        this.weekAuctionList = auctionListUserInfos;
        getMvpPresenter().requestTotalAuctionList(roomInfo.getUid());
    }

    @Override
    public void onRequestTotalAuctionList(List<AuctionListUserInfo> auctionListUserInfos) {
        this.totalAuctionList = auctionListUserInfos;
        setAuctionList();
    }

    @Override
    public void onAuctionListItemClick(AuctionListUserInfo auctionListUserInfo) {
        UserInfoDialog.showUserDialog(this.getContext(), auctionListUserInfo.getUid());
    }

    @Override
    public void onClick(View v) {
    }
}
