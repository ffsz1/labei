package com.vslk.lbgx.room.avroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.avroom.adapter.RoomNormalListAdapter;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.presenter.RoomManagerPresenter;
import com.tongdaxing.xchat_core.room.view.IRoomManagerView;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * 房间管理员列表
 *
 * @author chenran
 * @date 2017/10/11
 */
@CreatePresenter(RoomManagerPresenter.class)
public class RoomManagerListActivity extends BaseMvpActivity<IRoomManagerView, RoomManagerPresenter>
        implements RoomNormalListAdapter.OnRoomNormalListOperationClickListener, IRoomManagerView {
    private TextView count;
    private RecyclerView recyclerView;
    private RoomNormalListAdapter normalListAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, RoomManagerListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_manager_list);
        initView();
        showLoading();
        loadData();
    }

    private void loadData() {
        getMvpPresenter().queryManagerList(500);
    }

    private void initView() {
        count = (TextView) findViewById(R.id.count);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        normalListAdapter = new RoomNormalListAdapter(this);
        normalListAdapter.setListOperationClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(normalListAdapter);

        ((AppToolBar) findViewById(R.id.toolbar)).setOnBackBtnListener(view -> finish());
    }

    @Override
    public void onRemoveOperationClick(final IMChatRoomMember chatRoomMember) {
        getDialogManager().showOkCancelDialog("是否将" + chatRoomMember.getNick() + "移除管理员列表？",
                true, new DialogManager.OkCancelDialogListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onOk() {
                        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                        if (roomInfo != null) {
                            getMvpPresenter().markManagerList(roomInfo.getRoomId(), chatRoomMember.getAccount(), false);
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

    @Override
    public void queryManagerListSuccess(List<IMChatRoomMember> chatRoomMemberList) {
        hideStatus();
        if (chatRoomMemberList != null && chatRoomMemberList.size() > 0) {
            normalListAdapter.setNormalList(chatRoomMemberList);
            normalListAdapter.notifyDataSetChanged();
            count.setText("管理员" + chatRoomMemberList.size() + "人");
        } else {
            showNoData("暂没有设置管理员");
            count.setText("管理员0人");
        }
    }

    @Override
    public void queryManagerListFail() {
        showNetworkErr();
    }

    @Override
    public void markManagerListSuccess(String account) {
        if (account == null) {
            return;
        }
        List<IMChatRoomMember> list = normalListAdapter.getNormalList();
        if (!ListUtils.isListEmpty(list)) {
            hideStatus();
            ListIterator<IMChatRoomMember> iterator = list.listIterator();
            for (; iterator.hasNext(); ) {
                if (Objects.equals(iterator.next().getAccount(), account)) {
                    iterator.remove();
                }
            }
            normalListAdapter.notifyDataSetChanged();
            count.setText("管理员" + list.size() + "人");
            if (list.size() == 0) {
                showNoData("暂没有设置管理员");
            }
        } else {
            showNoData("暂没有设置管理员");
            count.setText("管理员0人");
        }
        toast("操作成功");
    }

    @Override
    public void markManagerListFail(String error) {
        toast(error);
    }
}
