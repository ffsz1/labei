package com.vslk.lbgx.room.avroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.avroom.adapter.RoomBlackAdapter;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.hncxco.library_ui.widget.AppToolBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.im.room.IIMRoomCoreClient;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.presenter.RoomBlackPresenter;
import com.tongdaxing.xchat_core.room.view.IRoomBlackView;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * 黑名单
 *
 * @author chenran
 * @date 2017/10/11
 */
@CreatePresenter(RoomBlackPresenter.class)
public class RoomBlackListActivity extends BaseMvpActivity<IRoomBlackView, RoomBlackPresenter>
        implements IRoomBlackView, RoomBlackAdapter.RoomBlackDelete, OnLoadmoreListener {
    private TextView count;
    private RecyclerView recyclerView;
    private RoomBlackAdapter normalListAdapter;
    private int start = 0;
    private int loadSize = 30;
    private SmartRefreshLayout srlBlack;


    public static void start(Context context) {
        Intent intent = new Intent(context, RoomBlackListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_black_list);
        initView();
        showLoading();
        loadData();
    }

    private void loadData() {
        //一次最多只能加载200条
        getMvpPresenter().queryNormalListFromIm(loadSize, start);
    }

    private void initView() {
        count = (TextView) findViewById(R.id.count);
        srlBlack = (SmartRefreshLayout) findViewById(R.id.srl_black);
        srlBlack.setEnableRefresh(false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        normalListAdapter = new RoomBlackAdapter(R.layout.list_item_room_black);
        normalListAdapter.setRoomBlackDelete(this);
        normalListAdapter.setEnableLoadMore(false);
        normalListAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        srlBlack.setOnLoadmoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(normalListAdapter);

        ((AppToolBar)findViewById(R.id.toolbar)).setOnBackBtnListener(view -> finish());
    }

    @Override
    public void onDeleteClick(final IMChatRoomMember chatRoomMember) {

        getDialogManager().showOkCancelDialog("是否将" + chatRoomMember.getNick() + "移除黑名单列表？", true, new DialogManager.OkCancelDialogListener() {

            @Override
            public void onCancel() {

            }

            @Override
            public void onOk() {
                RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                if (roomInfo != null) {
                    getMvpPresenter().markBlackList(roomInfo.getRoomId(), chatRoomMember.getAccount(), false);
                }
            }
        });
    }

    @Override
    public View.OnClickListener getLoadListener() {
        return v -> {
            showLoading();
            loadData();
        };
    }

    @CoreEvent(coreClientClass = IIMRoomCoreClient.class)
    public void onMemberBeRemoveManager(String account) {
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        if (uid == JavaUtil.str2long(account)) {
            finish();
            toast(R.string.remove_room_manager);
        }
    }

    @Override
    public void queryNormalListSuccess(List<IMChatRoomMember> blackList) {
        hideStatus();
        if (blackList != null && blackList.size() > 0) {
            if (normalListAdapter.getData() != null && normalListAdapter.getData().size() > 0) {
                srlBlack.finishLoadmore();
                normalListAdapter.addData(blackList);
                count.setText("黑名单" + normalListAdapter.getData().size() + "人");
            } else {
                normalListAdapter.setNewData(blackList);
                count.setText("黑名单" + blackList.size() + "人");
            }

        } else  {
            if (normalListAdapter.getData() == null || normalListAdapter.getData().size() < 1) {
                showNoData("暂没有设置黑名单");


                count.setText("黑名单0人");
            }else {
                srlBlack.finishLoadmoreWithNoMoreData();

            }
        }
    }

    @Override
    public void queryNormalListFail() {
        showNetworkErr();
    }

    @Override
    public void makeBlackListSuccess(String account, boolean mark) {
        if (account == null) {
            return;
        }
        List<IMChatRoomMember> normalList = normalListAdapter.getData();
        if (!ListUtils.isListEmpty(normalList)) {
            hideStatus();
            ListIterator<IMChatRoomMember> iterator = normalList.listIterator();
            for (; iterator.hasNext(); ) {
                if (Objects.equals(iterator.next().getAccount(), account)) {
                    iterator.remove();
                }
            }
            normalListAdapter.notifyDataSetChanged();
            count.setText("黑名单" + normalList.size() + "人");
            if (normalList.size() == 0) {
                showNoData("暂没有设置黑名单");
            }
        } else {
            showNoData("暂没有设置黑名单");
            count.setText("黑名单0人");
        }
        toast("操作成功");
    }

    @Override
    public void makeBlackListFail(String error) {
        toast(error);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        start = normalListAdapter.getData().size()+ 1;
        loadData();
    }
}
