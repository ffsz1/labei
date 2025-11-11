package com.vslk.lbgx.ui.home.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_core.im.custom.bean.PublicChatRoomAttachment;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.find.activity.SquareActivity;
import com.vslk.lbgx.ui.home.adpater.HomeHotHeaderAdapter;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/25
 * 描述        热门头部
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class HomeHotHeaderView extends LinearLayout implements BaseQuickAdapter.OnItemClickListener {
    private HomeHotSquareRollMarqueeView<SquareRollInfo> homeHotSquareRollMarqueeView;
    private HomeHotSquareRollMF homeHotSquareRollMF;
    private LinearLayout rlSquareRoll;
    private HomeHotHeaderAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tvRecommendRanking;


    public HomeHotHeaderView(Context context) {
        super(context);
        initView(context);

    }

    /**
     * 初始化View
     */
    private void initView(Context context) {
        inflate(context, R.layout.ly_home_hot_head, this);
        rlSquareRoll = findViewById(R.id.rl_square_roll);
        homeHotSquareRollMarqueeView = findViewById(R.id.home_hot_square_roll_marquee_view);
        tvRecommendRanking = findViewById(R.id.tv_recommend_ranking);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        mAdapter = new HomeHotHeaderAdapter(context);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        rlSquareRoll.setOnClickListener(v -> SquareActivity.start(getContext()));

    }

    public static List<SquareRollInfo> convertInfo(List<PublicChatRoomAttachment> list) {
        List<com.vslk.lbgx.ui.home.view.SquareRollInfo> squareRollInfos = new ArrayList<>();

        int index = 0;
        while (index < list.size()) {
            SquareRollInfo squareRollInfo = new SquareRollInfo();
            if (index < list.size()) {
                squareRollInfo.publicChatRoomAttachment1 = list.get(index);
            }
            if (index + 1 < list.size()) {
                squareRollInfo.publicChatRoomAttachment2 = list.get(index + 1);
            }
            if (index + 2 < list.size()) {
                squareRollInfo.publicChatRoomAttachment3 = list.get(index + 2);
            }
            squareRollInfos.add(squareRollInfo);
            index += 3;
        }
        return squareRollInfos;
    }

    public void setSquareRoll(List<PublicChatRoomAttachment> list) {
        homeHotSquareRollMF = new HomeHotSquareRollMF(getContext());
        homeHotSquareRollMF.setData(convertInfo(list));
        homeHotSquareRollMarqueeView.setMarqueeFactory(homeHotSquareRollMF);
        homeHotSquareRollMarqueeView.startFlipping();
    }

    //设置数据
    public void setNewData(List<HomeRoom> roomList) {
        if (!ListUtils.isListEmpty(roomList)) {
            recyclerView.setVisibility(VISIBLE);
//            tvRecommendRanking.setVisibility(VISIBLE);
            tvRecommendRanking.setVisibility(GONE);
            mAdapter.setNewData(addPlaceholderData(roomList));
        } else {
            mAdapter.setNewData(null);
            tvRecommendRanking.setVisibility(GONE);
            recyclerView.setVisibility(GONE);
        }
    }

    private List<HomeRoom> addPlaceholderData(List<HomeRoom> roomList) {
        if (roomList.size() % 3 == 1) {
            HomeRoom homeRoom1 = new HomeRoom();
            homeRoom1.setType(1);
            homeRoom1.setAvatar(WebUrl.HOME_RECOMMEND_GREEN_CONVERTION);
            HomeRoom homeRoom2 = new HomeRoom();
            homeRoom2.setType(2);
            homeRoom2.setAvatar(WebUrl.HOME_RECOMMEND_PLACEHOLDER);
            roomList.add(homeRoom1);
            roomList.add(homeRoom2);
        } else if (roomList.size() % 3 == 2) {
            HomeRoom homeRoom = new HomeRoom();
            homeRoom.setType(2);
            homeRoom.setAvatar(WebUrl.HOME_RECOMMEND_PLACEHOLDER);
            roomList.add(homeRoom);
        }
        return roomList;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter != null && !ListUtils.isListEmpty(adapter.getData())) {
            HomeRoom homeRoom = mAdapter.getData().get(position);
            if (homeRoom == null) {
                return;
            }
            if (homeRoom.getType() == 1 && TextUtils.isEmpty(homeRoom.getTitle())) {
                CommonWebViewActivity.start(getContext(), WebUrl.HOME_RECOMMEND_GREEN_CONVERTION_URL);
            } else if (homeRoom.getType() == 2 && TextUtils.isEmpty(homeRoom.getTitle())) {
                CommonWebViewActivity.start(getContext(), WebUrl.HOME_RECOMMEND_PLACEHOLDER_URL);
            } else {
                AVRoomActivity.start(getContext(), homeRoom.getUid());
            }
        }
    }
}
