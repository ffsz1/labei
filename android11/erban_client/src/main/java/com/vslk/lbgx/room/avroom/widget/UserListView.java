package com.vslk.lbgx.room.avroom.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.vslk.lbgx.room.avroom.adapter.UserListViewAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * 经聊，竞拍房麦上用户列表
 *
 * @author chenran
 * @date 2017/7/28
 */
public class UserListView extends RecyclerView {
    private UserListViewAdapter adapter;
    private Disposable mDisposable;

    public UserListView(Context context) {
        super(context);
        init();
    }

    public UserListView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public UserListView(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        init();
    }

    public void setListViewItemClickListener(UserListViewAdapter.UserListViewItemClickListener listViewItemClickListener) {
        adapter.setListener(listViewItemClickListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDisposable = IMNetEaseManager.get()
                .getChatRoomEventObservable()
                .subscribe(new Consumer<RoomEvent>() {
                    @Override
                    public void accept(RoomEvent roomEvent) throws Exception {
                        if (roomEvent == null) return;
                        int event = roomEvent.getEvent();
                        switch (event) {
                            case RoomEvent.KICK_DOWN_MIC:
                            case RoomEvent.KICK_OUT_ROOM:
                            case RoomEvent.UP_MIC:
                            case RoomEvent.DOWN_MIC:
                            case RoomEvent.ADD_BLACK_LIST:
                                int micPosition = roomEvent.getMicPosition();
                                if (-1 == micPosition || adapter == null) return;
                                adapter.updateList();
                                break;
                            case RoomEvent.SPEAK_STATE_CHANGE:
                                onSpeakStateUpdate(roomEvent.getMicPositionList());
                                break;
                            default:
                        }
                    }
                });
    }

    private void onSpeakStateUpdate(List<Integer> positions) {
        int count = getChildCount();
        for (int i = 0; i < positions.size(); i++) {
            // 房主不在recycler view中
            if (positions.get(i) == -1) continue;
            int pos = positions.get(i);
            if (pos >= count) continue;
            View speakState = getChildAt(pos).findViewById(R.id.user_speek);
            if (speakState != null) {
                speakState.setBackground(null);
                speakState.setBackgroundResource(R.drawable.home_party_mic_list_anim);
                AnimationDrawable animationDrawable = (AnimationDrawable) speakState.getBackground();
                animationDrawable.start();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
        super.onDetachedFromWindow();
    }

    private void init() {
        findView();
    }

    private void findView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(mLayoutManager);

        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null) {
            adapter = new UserListViewAdapter(roomInfo.getType());
        } else {
            adapter = new UserListViewAdapter(1);
        }
        setAdapter(adapter);

        //初始化用户列表
        adapter.updateList();
    }
}
