package com.vslk.lbgx.room.widget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.vslk.lbgx.room.avroom.adapter.MicInListAdapter;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCoreClient;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/26.
 */

public class MicInListDialog extends BottomSheetDialog {

    private Context context;
    @BindView(R.id.tv_mic_in_list_dialog_title)
    TextView tvMicInListDialogTitle;
    @BindView(R.id.rv_mic_in_list_dialog)
    SwipeMenuRecyclerView rvMicInListDialog;
    @BindView(R.id.bu_mic_in_list_dialog_submit)
    Button buMicInListDialogSubmit;

    TreeSet<Json> treeSet;

    List<Json> jsons = new ArrayList<>();

    private MicInListAdapter adapter;
    public boolean isAdmin = false;
    public boolean isRoomOwner = false;


    public MicInListDialog(@NonNull Context context) {
        super(context, R.style.ErbanBottomSheetDialog);
        this.context = context;
        treeSet = new TreeSet<>((o1, o2) -> {
            long l1 = o1.num_l("time");
            long l2 = o2.num_l("time");

            if (l1 > l2) {
                return 1;
            } else {
                return -1;
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mic_in_list);
        findViewById(R.id.ll_dialog_mic_in_list).setOnTouchListener((v, event) -> true);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);


        ButterKnife.bind(this);

        setCanceledOnTouchOutside(true);

        initDeleteMenu();

        refreshData();
        buMicInListDialogSubmit.setVisibility(isRoomOwner ? View.GONE : View.VISIBLE);
        buMicInListDialogSubmit.setOnClickListener(v -> {
            if (iSubmitAction != null) {
                iSubmitAction.onSubmitClick();
            }
        });

        //初始化之后再开始监听事件
        CoreManager.addClient(this);
    }

    private void initDeleteMenu() {
        if (!isAdmin)
            return;
        SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, viewType) -> {
            SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
            deleteItem.setText("删除");
            deleteItem.setTextColor(Color.WHITE);
            deleteItem.setWidth(300);
            deleteItem.setHeight(-1);
            deleteItem.setBackgroundColor(Color.parseColor("#fd2772"));
            swipeRightMenu.addMenuItem(deleteItem);
        };
        rvMicInListDialog.setSwipeMenuItemClickListener(menuBridge -> {

            menuBridge.closeMenu();
            int adapterPosition = menuBridge.getAdapterPosition();
            List<Json> data = adapter.getData();
            Json json = data.get(adapterPosition);
            if (json == null) {
                return;
            }
            String uid = json.str("uid");
            RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
            if (roomInfo != null && !TextUtils.isEmpty(uid))
                IMNetEaseManager.get().removeMicInList(uid, roomInfo.getRoomId() + "", null);
        });
        rvMicInListDialog.setSwipeMenuCreator(swipeMenuCreator);
    }

    public ISubmitAction iSubmitAction;

    public interface ISubmitAction {
        void onSubmitClick();
    }

    public void refreshData() {


        SparseArray<Json> mMicInListMap = AvRoomDataManager.get().mMicInListMap;

        boolean checkInMicInlist = AvRoomDataManager.get().checkInMicInlist();

        buMicInListDialogSubmit.setText(checkInMicInlist ? "取消排麦" : "排麦");

        treeSet.clear();
        for (int i = 0; i < mMicInListMap.size(); i++) {
            Json json = mMicInListMap.valueAt(i);
            treeSet.add(json);
        }

        List<Json> jsonList = new ArrayList<>();
        for (Json json : treeSet) {
            jsonList.add(json);
        }
        jsons = jsonList;


        int size = jsons.size();

        tvMicInListDialogTitle.setText("排麦人数 " + size);


        rvMicInListDialog.setLayoutManager(new LinearLayoutManager(context));
        if (adapter == null) {
            adapter = new MicInListAdapter(context, R.layout.item_mic_in_list, jsons);
            rvMicInListDialog.setAdapter(adapter);
            adapter.isAdmin = this.isAdmin;
        } else {
            adapter.isAdmin = this.isAdmin;
            adapter.setNewData(jsons);

        }

    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CoreManager.removeClient(this);
    }

    @CoreEvent(coreClientClass = IAVRoomCoreClient.class)
    public void onMicInListChange() {
        refreshData();
    }

    @CoreEvent(coreClientClass = IAVRoomCoreClient.class)
    public void micInListDismiss(String msg) {
        if (!TextUtils.isEmpty(msg))
            SingleToastUtil.showShortToast(msg);
        dismiss();
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
