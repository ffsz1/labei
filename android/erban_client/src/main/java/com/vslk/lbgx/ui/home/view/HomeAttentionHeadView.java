package com.vslk.lbgx.ui.home.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.home.adpater.HomeAttentionAdapter;
import com.vslk.lbgx.ui.message.activity.AttentionListActivity;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.user.bean.AttentionInfo;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dell
 */
public class HomeAttentionHeadView extends LinearLayout {

    private LinearLayout llEmpty;
    private RecyclerView rvAttention;
    private LinearLayout llReEmpty;
    private TextView allAttention;
    private HomeAttentionAdapter mAdapter;

    private TextView reMsg;

    public HomeAttentionHeadView(Context context) {
        super(context);
        inflate(context, R.layout.ly_home_attention_head, this);
        llEmpty = findViewById(R.id.ll_head_attention_empty);
        llReEmpty = findViewById(R.id.ll_recommend_empty);
        allAttention = findViewById(R.id.showAllAttention);
        allAttention.setOnClickListener(view -> AttentionListActivity.start(context));

        reMsg = findViewById(R.id.re_msg);
        rvAttention = findViewById(R.id.rv_home_attention_room);
        int distance = ConvertUtils.dp2px(10);
        rvAttention.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.left = distance;
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAttention.setLayoutManager(linearLayoutManager);
        mAdapter = new HomeAttentionAdapter(false);
        mAdapter.setOnItemClickListener(
                (adapter, view, position) -> AVRoomActivity.start(context, mAdapter.getData().get(position).getUid()));
        rvAttention.setAdapter(mAdapter);
    }

    /**
     * 用户关注
     */
    public void addData(AttentionInfo data) {
        if (rvAttention.getVisibility() == VISIBLE) {
            if (mAdapter != null) {
                List<AttentionInfo> rooms = mAdapter.getData();
                if (rooms.size() >= 6) {
                    rooms.add(0, data);
                    rooms.remove(rooms.size());
                    mAdapter.setNewData(rooms);
                } else {
                    mAdapter.addData(0, data);
                }
            }
        } else {
            //显示界面数据
            rvAttention.setVisibility(VISIBLE);
            llEmpty.setVisibility(View.GONE);

            //设置数据
            List<AttentionInfo> homeRooms = new ArrayList<>();
            homeRooms.add(data);
            mAdapter.setNewData(homeRooms);
        }
    }

    /**
     * 根据数据更新布局
     */
    public void setListData(List<AttentionInfo> data) {
        if (mAdapter != null && !ListUtils.isListEmpty(data)) {
            if (rvAttention.getVisibility() == View.GONE) {
                rvAttention.setVisibility(View.VISIBLE);
            }
            if (llEmpty.getVisibility() == View.VISIBLE) {
                llEmpty.setVisibility(View.GONE);
            }
            if (data.size() > 6) {
                data = new ArrayList<>(data.subList(0, 6));
            }
            mAdapter.setNewData(data);
        } else {
            if (rvAttention.getVisibility() == View.VISIBLE) {
                rvAttention.setVisibility(View.GONE);
            }
            if (llEmpty.getVisibility() == View.GONE) {
                llEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    public void showNoData(boolean showNoData, String msg) {
        llReEmpty.setVisibility(showNoData ? VISIBLE : GONE);
        if (StringUtils.isNotEmpty(msg)) {
            reMsg.setText(msg);
        }
    }

}
