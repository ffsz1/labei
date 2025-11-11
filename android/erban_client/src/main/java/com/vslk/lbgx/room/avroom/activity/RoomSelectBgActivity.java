package com.vslk.lbgx.room.avroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.room.avroom.adapter.RoomSelectBgAdapter;
import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.ChatSelectBgBean;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 房间背景选择页面
 *
 * @author Administrator
 * @date 2018/3/23
 */
public class RoomSelectBgActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RoomSelectBgAdapter mRoomSelectBgAdapter;
    private String backPic = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (StringUtils.isNotEmpty(getIntent().getStringExtra("backPic"))) {
            backPic = getIntent().getStringExtra("backPic");
        }
        setContentView(R.layout.activity_chat_room_select_bg);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_chat_room_select_bg);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRoomSelectBgAdapter = new RoomSelectBgAdapter();
        mRoomSelectBgAdapter.selectIndex = backPic;
        mRoomSelectBgAdapter.setItemAction(item -> {
            Intent intent = new Intent();
            intent.putExtra("selectIndex", item.id);
            intent.putExtra("selectUrl", item.picUrl);
            setResult(2, intent);
            finish();
        });
        mRecyclerView.setAdapter(mRoomSelectBgAdapter);
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("roomId", (AvRoomDataManager.get().mCurrentRoomInfo == null ? 0
                : AvRoomDataManager.get().mCurrentRoomInfo.getRoomId()) + "");
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getRoomBackList(), params,
                new OkHttpManager.MyCallBack<ServiceResult<List<ChatSelectBgBean>>>() {
                    @Override
                    public void onError(Exception e) {
                        setData(null);
                    }

                    @Override
                    public void onResponse(ServiceResult<List<ChatSelectBgBean>> response) {
                        if (response != null && response.isSuccess()) {
                            setData(response.getData());
                        }
                    }
                });

        ((AppToolBar)findView(R.id.toolbar)).setOnBackBtnListener(view -> finish());
    }

    private void setData(List<ChatSelectBgBean> response) {
        List<ChatSelectBgBean> chatSelectBgBeans = new ArrayList<>();
        if (!ListUtils.isListEmpty(response)) {
            chatSelectBgBeans.addAll(response);
        }
        chatSelectBgBeans.add(new ChatSelectBgBean("0", 1, "", "默认"));
        mRoomSelectBgAdapter.setNewData(chatSelectBgBeans);
        mRoomSelectBgAdapter.notifyDataSetChanged();
    }
}
