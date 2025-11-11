package com.vslk.lbgx.room.avroom.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.room.bean.RoomFunctionBean;
import com.tongdaxing.xchat_core.room.bean.RoomFunctionEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 房间功能弹框
 *
 * @author dell
 */
public class RoomFunctionDialog extends Dialog implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private RecyclerView rvFunction;
    private OnFunctionClickListener onFunctionClickListener;
    private RoomFunctionEnum[] functions;
    private int roleType = -1;
    private int publicChatSwitch;
    private int vehicleSwitch;
    private int giftSwitch;
    private RoomFunctionAdapter mAdapter;
    private RoomFunctionModel roomFunctionModel;

    public RoomFunctionDialog(@NonNull Context context) {
        super(context, R.style.GiftBottomSheetDialog);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_room_function);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        rvFunction = findViewById(R.id.rv_bottom_function);
        int distance = ConvertUtils.dp2px(10);
        rvFunction.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = distance;
                outRect.bottom = distance;
            }
        });
        mAdapter = new RoomFunctionAdapter(getContext());
        rvFunction.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public void setPublicChatSwitch(int publicChatSwitch) {
        this.publicChatSwitch = publicChatSwitch;
    }

    public void setVehicleSwitch(int vehicleSwitch) {
        this.vehicleSwitch = vehicleSwitch;
    }

    public void setGiftSwitch(int giftSwitch) {
        this.giftSwitch = giftSwitch;
    }

    public void initData() {
        if (roomFunctionModel == null) {
            roomFunctionModel = new RoomFunctionModel();
        }
        functions = roomFunctionModel.getRoomFunctionByType(roleType, publicChatSwitch, vehicleSwitch, giftSwitch);
        if (functions == null || functions.length <= 0) {
            return;
        }
        List<RoomFunctionBean> datas = new ArrayList<>();
        for (int i = 0; i < functions.length; i++) {
            datas.add(roomFunctionModel.getFunctionBean(functions[i]));
        }
        mAdapter.setNewData(datas);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (onFunctionClickListener != null && adapter != null && !ListUtils.isListEmpty(adapter.getData())) {
            onFunctionClickListener.onItemClick((RoomFunctionBean) adapter.getData().get(position));
        }
        dismiss();
    }


    public interface OnFunctionClickListener {
        void onItemClick(RoomFunctionBean bean);
    }

    public void setOnFunctionClickListener(OnFunctionClickListener onFunctionClickListener) {
        this.onFunctionClickListener = onFunctionClickListener;
    }


    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
