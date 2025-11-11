package com.vslk.lbgx.ui.message.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.fragment.BaseFragment;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.ui.message.adapter.FriendBlackAdapter;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;

import java.util.List;

/**
 * 黑名单列表
 *
 * @author zwk
 */
public class FriendBlackFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.OnItemChildClickListener {
    private SwipeRefreshLayout srlRefresh;
    private RecyclerView recyclerView;
    private FriendBlackAdapter blackAdapter;


    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_black_list;
    }

    //
    @Override
    public void onFindViews() {
        recyclerView = mView.findViewById(R.id.recycler_view);
        srlRefresh = mView.findViewById(R.id.srl_refresh);
        blackAdapter = new FriendBlackAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(blackAdapter);

    }

    @Override
    public void onSetListener() {
        srlRefresh.setOnRefreshListener(this);
        blackAdapter.setOnItemChildClickListener(this);
        blackAdapter.setRecyclerItemClickListener(new FriendBlackAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(int position, NimUserInfo item) {
                if (blackAdapter != null && !ListUtils.isListEmpty(blackAdapter.getData())) {
                    UserInfoActivity.start(mContext, JavaUtil.str2long(item.getAccount()));
                }
            }
        });
    }

    @Override
    public void initiate() {
        getData();
    }

    private void getData() {
        //先获取黑名单账号列表
        List<String> blackList = NIMClient.getService(FriendService.class).getBlackList();
        if (ListUtils.isListEmpty(blackList)) {
            srlRefresh.setRefreshing(false);
            showNoData();
            //显示空布局
            return;
        }
        //在获取黑名单账号信息
        NimUserInfoCache.getInstance().getUserInfoFromRemote(blackList, new RequestCallback<List<NimUserInfo>>() {
            @Override
            public void onSuccess(List<NimUserInfo> param) {
                hideStatus();
                srlRefresh.setRefreshing(false);
                if (blackAdapter != null) {
                    blackAdapter.setNewData(param);
                }
            }

            @Override
            public void onFailed(int code) {
                hideStatus();
                srlRefresh.setRefreshing(false);
                showNetworkErr();
            }

            @Override
            public void onException(Throwable exception) {
                hideStatus();
                srlRefresh.setRefreshing(false);
                showNetworkErr();
            }
        });
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (blackAdapter != null && !ListUtils.isListEmpty(blackAdapter.getData())) {
            NimUserInfo item = blackAdapter.getData().get(position);
            getDialogManager().showOkCancelDialog("是否将" + item.getName() + "移除黑名单列表？", true, new DialogManager.OkCancelDialogListener() {

                @Override
                public void onCancel() {

                }

                @Override
                public void onOk() {
                    removeBlackUser(item);
                }
            });
        }

    }

    private void removeBlackUser(NimUserInfo item) {

        NIMClient.getService(FriendService.class).removeFromBlackList(item.getAccount()).setCallback(new RequestCallback<Void>() {

            @Override
            public void onSuccess(Void param) {
                getData();
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    public static Fragment newInstance() {
        return new FriendBlackFragment();
    }

    @Override
    public void onRefresh() {
        getData();
    }
}