package com.vslk.lbgx.room.avroom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseFragment;
import com.vslk.lbgx.room.widget.dialog.ListDataDialog;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 轰趴房用户在线列表+贡献榜
 *
 * @author chenran
 * @date 2017/9/1
 */
public class HomePartyUserListFragment extends BaseFragment implements View.OnClickListener {
    private TextView onlineLayout;
    private TextView contributeLayout;

    private OnlineUserFragment mOnlineUserFragment;
    private RoomContributeFragment mRoomContributeFragment;

    private boolean mIsShowOnline = true;
    public int mSelectPage = -1;
    private long mRoomUid;

    public static HomePartyUserListFragment newInstance(long roomUid) {
        HomePartyUserListFragment fragment = new HomePartyUserListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.ROOM_UID, roomUid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void onInitArguments(Bundle bundle) {
        super.onInitArguments(bundle);
        if (bundle != null) {
            mRoomUid = bundle.getLong(Constants.ROOM_UID, 0);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mOnlineUserFragment != null)
            mOnlineUserFragment.onNewIntent(intent);
        if (mRoomContributeFragment != null)
            mRoomContributeFragment.onNewIntent(intent);
        if (intent != null)
            mRoomUid = intent.getLongExtra(Constants.ROOM_UID, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mIsShowOnline = outState.getBoolean(Constants.KEY_ROOM_IS_SHOW_ONLINE, true);
    }

    @Override
    protected void restoreState(@Nullable Bundle savedInstanceState) {
        super.restoreState(savedInstanceState);
        if (savedInstanceState != null) {
            savedInstanceState.putBoolean(Constants.KEY_ROOM_IS_SHOW_ONLINE, mIsShowOnline);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFindViews() {
        onlineLayout = mView.findViewById(R.id.tv_online);
        contributeLayout = mView.findViewById(R.id.tv_contribute);
    }

    @Override
    public void onSetListener() {
        onlineLayout.setOnClickListener(this);
        contributeLayout.setOnClickListener(this);
    }

    @Override
    public void initiate() {
//        showOnlineList(mIsShowOnline);
        Disposable mDisposable = IMNetEaseManager.get().getChatRoomEventObservable()
                .subscribe(new Consumer<RoomEvent>() {
                    @Override
                    public void accept(RoomEvent roomEvent) throws Exception {
                        if (roomEvent == null) return;
                        int event = roomEvent.getEvent();
                        switch (event) {
                            case RoomEvent.ENTER_ROOM:
                                //切换房间，或是首次进去时加载数据
                                loadFirstLoadData();
                                break;
                            default:
                        }
                    }
                });
        mCompositeDisposable.add(mDisposable);
        if (!AvRoomDataManager.get().isFirstEnterRoomOrChangeOtherRoom(mRoomUid)) {
            loadFirstLoadData();
        }
    }


    private void loadFirstLoadData() {
        if (mOnlineUserFragment != null)
            mOnlineUserFragment.firstLoad();
        if (mRoomContributeFragment != null)
            mRoomContributeFragment.loadData();
    }

    public void showOnlineList(boolean isShow) {
        mIsShowOnline = isShow;
        mSelectPage = mIsShowOnline ? 0 : 1;
        showBackground(isShow);
        if (isShow) {
            ListDataDialog.newOnlineUserListInstance(getContext())
                    .show(getChildFragmentManager());
        } else {
            ListDataDialog.newContributionListInstance(getContext())
                    .show(getFragmentManager());
        }
    }

    private void showBackground(boolean isOnline) {
        if (isOnline) {
            onlineLayout.setBackgroundResource(R.drawable.bg_game_room_tab_select);
            contributeLayout.setBackground(null);
        } else {
            contributeLayout.setBackgroundResource(R.drawable.bg_game_room_tab_select);
            onlineLayout.setBackground(null);
        }
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_game_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_online:
                showOnlineList(true);
                break;
            case R.id.tv_contribute:
                showOnlineList(false);
                break;
            default:
        }
    }
}
