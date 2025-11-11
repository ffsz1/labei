package com.vslk.lbgx.room.avroom.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.room.avroom.adapter.RoomConsumeListAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.room.presenter.LightChatConsumePresenter;
import com.tongdaxing.xchat_core.room.queue.bean.RoomConsumeInfo;
import com.tongdaxing.xchat_core.room.view.ILightChatConsumeView;
import com.vslk.lbgx.room.widget.dialog.UserInfoDialog;

import java.util.List;

/**
 * 房间消费排行界面
 * Created by chenran on 2017/10/1.
 */
@CreatePresenter(LightChatConsumePresenter.class)
public class LightChatConsumeFragment extends BaseMvpFragment<ILightChatConsumeView, LightChatConsumePresenter>
        implements ILightChatConsumeView {
    private RecyclerView recyclerView;
    private View mEmptyView;
    private RoomConsumeListAdapter mListAdapter;


    @Override
    public void onFindViews() {
        recyclerView = mView.findViewById(R.id.recycler_view);
    }

    @Override
    public void onSetListener() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void initiate() {
        mEmptyView = LayoutInflater.from(mContext).inflate(R.layout.list_item_room_consume_list_empty, null, false);
        mListAdapter = new RoomConsumeListAdapter(mContext);
        recyclerView.setAdapter(mListAdapter);
        mListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                List<RoomConsumeInfo> list = mListAdapter.getData();
                if (ListUtils.isListEmpty(list)) return;
                RoomConsumeInfo roomConsumeInfo = list.get(i);
                UserInfoDialog.showUserDialog(getContext(), roomConsumeInfo.getUid());
            }
        });

        getMvpPresenter().getRoomConsumeList();
    }


    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_room_consume_list;
    }


    @Override
    public void onGetRoomConsumeListSuccess(List<RoomConsumeInfo> roomConsumeInfos) {
        if (!ListUtils.isListEmpty(roomConsumeInfos)) {
            mListAdapter.setNewData(roomConsumeInfos);
        } else {
            mListAdapter.setEmptyView(mEmptyView);
        }
    }

    public void onGetRoomConsumeListFail(String msg) {
        toast(msg);
    }
}
