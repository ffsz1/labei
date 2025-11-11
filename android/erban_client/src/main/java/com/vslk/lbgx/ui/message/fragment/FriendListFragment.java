package com.vslk.lbgx.ui.message.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.fragment.BaseFragment;
import com.vslk.lbgx.ui.message.adapter.FriendListAdapter;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.erban.BR;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.im.friend.IIMFriendCore;
import com.tongdaxing.xchat_core.im.friend.IIMFriendCoreClient;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;

/**
 * 好友列表界面
 *
 * @author chenran
 * @date 2017/9/18
 */
public class FriendListFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private FriendListAdapter adapter;
    private List<NimUserInfo> infoList;


    @Override
    public void onFindViews() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
    }

    @Override
    public void onSetListener() {

    }

    @Override
    public void initiate() {
//        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_fragment_friend_list, null);
//        binding = DataBindingUtil.bind(headView);
//        binding.setClick(this);


        adapter = new FriendListAdapter(R.layout.list_item_friend, BR.userInfo);
//        adapter.setHeaderAndEmpty(true);
//        adapter.addHeaderView(headView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setEmptyView(getEmptyView(recyclerView, getString(R.string.no_frenids_text)));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (infoList == null || position >= infoList.size()) {
                    toast("数据异常请稍后重试");
                    return;
                }

                NimUIKit.startP2PSession(getActivity(), infoList.get(position).getAccount());
            }
        });


        List<NimUserInfo> userInfos = CoreManager.getCore(IIMFriendCore.class).getMyFriends();
        setData(userInfos);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_friend_list;
    }

    @CoreEvent(coreClientClass = IIMFriendCoreClient.class)
    public void onFriendListUpdate(List<NimUserInfo> userInfos) {
        setData(userInfos);
    }

    private void setData(List<NimUserInfo> userInfos) {
        infoList = userInfos;
        if (!ListUtils.isListEmpty(userInfos)) {
            hideStatus();
            adapter.setNewData(userInfos);
            adapter.notifyDataSetChanged();
        } else {
            showNoData("暂无好友");
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdate(UserInfo userInfo) {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<NimUserInfo> userInfos = CoreManager.getCore(IIMFriendCore.class).getMyFriends();
                setData(userInfos);
            }
        }, 250);

    }



 /*   @CoreEvent(coreClientClass = IIMLoginClient.class)
    public void onImLoginSuccess(LoginInfo loginInfo) {
        List<NimUserInfo> userInfos = CoreManager.getCore(IIMFriendCore.class).getMyFriends();
        setData(userInfos);
    }*/

}
