package com.vslk.lbgx.ui.message.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vslk.lbgx.base.fragment.BaseFragment;
import com.vslk.lbgx.event.ToHim;
import com.vslk.lbgx.ui.message.adapter.AttentionListAdapter;
import com.netease.nim.uikit.NimUIKit;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.praise.IPraiseClient;
import com.tongdaxing.xchat_core.user.AttentionCore;
import com.tongdaxing.xchat_core.user.AttentionCoreClient;
import com.tongdaxing.xchat_core.user.bean.AttentionInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.tongdaxing.erban.R.id.swipe_refresh;

public class AttentionFragment extends BaseFragment {

    private RecyclerView mRecylcerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AttentionListAdapter adapter;
    private List<AttentionInfo> mAttentionInfoList = new ArrayList<>();
    private int mPage = Constants.PAGE_START;


    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_list_attention;
    }

    @Override
    public void onFindViews() {
        initView();
    }

    @Override
    public void onSetListener() {
        setListener();
    }

    @Override
    public void initiate() {

        initData();
    }

    private void initData() {
        mRecylcerView.setAdapter(adapter);
        showLoading();
        onRefreshing();
    }

    private void initView() {
        mRecylcerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(swipe_refresh);
        mRecylcerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(onRefreshLisetener);
        adapter = new AttentionListAdapter(mAttentionInfoList);

        adapter.setRylListener(new AttentionListAdapter.onClickListener() {
            @Override
            public void rylListeners(AttentionInfo attentionInfo) {
                if (Constants.OFFICIAL == attentionInfo.getUid()) {
                    return;
                }
//                UserInfoActivity.start(getActivity(), attentionInfo.getUid());

                NimUIKit.startP2PSession(getActivity(), String.valueOf(attentionInfo.getUid()));
            }

            @Override
            public void findHimListeners(AttentionInfo attentionInfo) {
                if (Constants.OFFICIAL == attentionInfo.getUid()) {
                    return;
                }
                if (attentionInfo.getUserInRoom() != null) {
                    ToHim.postToHim(attentionInfo.getUserInRoom().getUid(), this.getClass().getName());
                } else {
                    ToHim.postToHim(attentionInfo.getUid(), this.getClass().getName());
                }
//                if (attentionInfo.getUserInRoom() != null) {
//                    AVRoomActivity.start(getActivity(), attentionInfo.getUserInRoom().getUid());
//                }
//                long formalRoomId =
//                        BasicConfig.INSTANCE.isDebuggable() ? PublicChatRoomController.devRoomId : PublicChatRoomController.formalRoomId;
//                if (attentionInfo.getUserInRoom() != null && attentionInfo.getUserInRoom().getRoomId() == formalRoomId) {
//                    toast("对方不在房间内");
//                    return;
//                }
//                getDialogManager().dismissDialog();
//                RoomInfo current = AvRoomDataManager.get().mCurrentRoomInfo;
//                if (attentionInfo.getUserInRoom() != null && attentionInfo.getUserInRoom().getUid() > 0) {
//                    if (current != null) {
//                        if (current.getUid() == attentionInfo.getUserInRoom().getUid()) {
//                            toast("已经和对方在同一个房间");
//                            return;
//                        }
//                    }
//                    AVRoomActivity.start(getActivity(), attentionInfo.getUserInRoom().getUid());
//                } else {
//                    toast("对方不在房间内");
//                }
            }
        });
        adapter.setOnLoadMoreListener(() -> {
            mPage++;
            onRefreshing();
        }, mRecylcerView);
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshLisetener = () -> {
        mPage = Constants.PAGE_START;
        onRefreshing();
    };

    private void onRefreshing() {
        CoreManager.getCore(AttentionCore.class)
                .getAttentionList(CoreManager.getCore(IAuthCore.class).getCurrentUid(), mPage, Constants.PAGE_SIZE);
    }

    @CoreEvent(coreClientClass = AttentionCoreClient.class)
    public void onGetAttentionList(List<AttentionInfo> attentionInfoList, int page) {
        mPage = page;
        if (!ListUtils.isListEmpty(attentionInfoList)) {
            if (mPage == Constants.PAGE_START) {
                hideStatus();
                swipeRefreshLayout.setRefreshing(false);
                mAttentionInfoList.clear();
                adapter.setNewData(attentionInfoList);
                if (attentionInfoList.size() < Constants.PAGE_SIZE) {
                    adapter.setEnableLoadMore(false);
                }
            } else {
                adapter.loadMoreComplete();
                adapter.addData(attentionInfoList);
            }
        } else {
            if (mPage == Constants.PAGE_START) {
                showNoData(getString(R.string.no_attention_text));
            } else {
                adapter.loadMoreEnd(true);
            }

        }
    }

    @CoreEvent(coreClientClass = AttentionCoreClient.class)
    public void onGetAttentionListFail(String error, int page) {
        mPage = page;
        if (mPage == Constants.PAGE_START) {
            swipeRefreshLayout.setRefreshing(false);
            showNetworkErr();
        } else {
            adapter.loadMoreFail();
            toast(error);
        }
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onCanceledPraise(long uid, boolean showNotice) {
        List<AttentionInfo> data = adapter.getData();
        if (!ListUtils.isListEmpty(data)) {
            ListIterator<AttentionInfo> iterator = data.listIterator();
            for (; iterator.hasNext(); ) {
                AttentionInfo attentionInfo = iterator.next();
                if (attentionInfo.isValid() && attentionInfo.getUid() == uid) {
                    iterator.remove();
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onReloadData() {
        showLoading();
        onRefreshing();
    }

    //    @Override
//    public View.OnClickListener getLoadListener() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPage = Constants.PAGE_START;
//                showLoading();
//                onRefreshing();
//            }
//        };
//    }
}
