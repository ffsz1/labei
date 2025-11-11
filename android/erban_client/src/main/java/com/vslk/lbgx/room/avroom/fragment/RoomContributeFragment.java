package com.vslk.lbgx.room.avroom.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.fragment.BaseFragment;
import com.vslk.lbgx.room.avroom.adapter.RoomConsumeListAdapter;
import com.vslk.lbgx.room.widget.dialog.UserInfoDialog;
import com.vslk.lbgx.ui.widget.itemdecotion.DividerItemDecoration;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.IRoomCore;
import com.tongdaxing.xchat_core.room.IRoomCoreClient;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.queue.bean.RoomConsumeInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.List;

/**
 * <p>  房间贡献榜用户列表</p>
 *
 * @author Administrator
 * @date 2017/12/4
 */
public class RoomContributeFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private RoomConsumeListAdapter mConsumeListAdapter;
    private View mEmptyView;

    @Override
    public void onFindViews() {
        mRecyclerView = mView.findViewById(R.id.recycler_view);
    }

    @Override
    public void onSetListener() {

    }

    @Override
    public void initiate() {
        mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.list_item_room_consume_list_empty, null, false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mConsumeListAdapter = new RoomConsumeListAdapter(mContext);
        mRecyclerView.setAdapter(mConsumeListAdapter);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), OrientationHelper.VERTICAL,
                        2, R.color.app_bg));
        mConsumeListAdapter.setOnItemClickListener(this);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.common_single_recycler_view;
    }

    public void loadData() {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null) {
            CoreManager.getCore(IRoomCore.class).getRoomConsumeList(roomInfo.getUid());
        }
    }

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onGetRoomConsumeList(List<RoomConsumeInfo> roomConsumeInfos) {
        if (roomConsumeInfos != null && roomConsumeInfos.size() > 0) {
            mConsumeListAdapter.setNewData(roomConsumeInfos);
        } else {
            mConsumeListAdapter.setEmptyView(mEmptyView);
        }
    }

    @CoreEvent(coreClientClass = IRoomCoreClient.class)
    public void onGetRoomConsumeListFail(String msg) {
        toast(msg);
        mConsumeListAdapter.setEmptyView(mEmptyView);
    }

    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        List<RoomConsumeInfo> list = mConsumeListAdapter.getData();
        if (ListUtils.isListEmpty(list)) {
            return;
        }
        RoomConsumeInfo roomConsumeInfo = list.get(i);
        UserInfoDialog.showUserDialog(getContext(), roomConsumeInfo.getUid());
    }
}
