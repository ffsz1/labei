package com.vslk.lbgx.ui.me.shopping.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vslk.lbgx.base.fragment.BaseFragment;
import com.vslk.lbgx.ui.me.shopping.adapter.FriendListGiftAdapter;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.erban.BR;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.im.friend.IIMFriendCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;

public class FriendListGiftFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private FriendListGiftAdapter adapter;
    public FriendListGiftAdapter.IGiveAction iGiveAction;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public void onFindViews() {
        recyclerView = mView.findViewById(R.id.rv_list);
    }

    @Override
    public void onSetListener() {

    }

    @Override
    public void initiate() {
        adapter = new FriendListGiftAdapter(R.layout.list_item_share_fans, BR.userInfo);
        if (iGiveAction != null) {
            adapter.iGiveAction = this.iGiveAction;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setEmptyView(getEmptyView(recyclerView, getString(R.string.no_frenids_text)));
        recyclerView.setAdapter(adapter);
        List<NimUserInfo> userInfos = CoreManager.getCore(IIMFriendCore.class).getMyFriends();
        setData(userInfos);
    }

    private void setData(List<NimUserInfo> userInfos) {

        if (userInfos != null && userInfos.size() > 0) {
            hideStatus();
            adapter.setNewData(userInfos);
            adapter.notifyDataSetChanged();
        }
    }
}
