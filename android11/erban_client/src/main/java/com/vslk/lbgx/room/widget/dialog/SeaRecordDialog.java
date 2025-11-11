package com.vslk.lbgx.room.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.vslk.lbgx.room.avroom.adapter.RoomGiftRecordAdapter;

import java.util.List;

/**
 * @author dell
 */
public class SeaRecordDialog extends Dialog implements RoomGiftRecordAdapter.OnGiftRecordListener {

    private RecyclerView mRecyclerView;
    private RoomGiftRecordAdapter mGiftRecordAdapter;
    private View mEmptyView;
    private Context mContext;

    public SeaRecordDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gift_record_list);

        TextView titleTv = findViewById(R.id.tv_list_data_title);
        titleTv.setText("打call记录");
        titleTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.call_record,0,  0, 0);

        TextView blankTv=findViewById(R.id.tv_gift_history_empty);
        blankTv.setText("暂时还没有人打call");

        Window window = getWindow();
        // setup window and width
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.RIGHT);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        mRecyclerView = findViewById(R.id.recycler_view);
        mEmptyView = findViewById(R.id.tv_gift_history_empty);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = ConvertUtils.dp2px(14);
            }
        });
        mGiftRecordAdapter = new RoomGiftRecordAdapter(mContext);
        mRecyclerView.setAdapter(mGiftRecordAdapter);
        mGiftRecordAdapter.setOnGiftRecordListener(this);
        /*findViewById(R.id.iv_close_dialog).setOnClickListener(v -> {
            if (isShowing()) {
                dismiss();
            }
        });*/
    }

    public void loadData(List<ChatRoomMessage> data) {
        if (mGiftRecordAdapter != null) {
            if (data == null || data.isEmpty()) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mGiftRecordAdapter.setDatas(data);
            }
        }
    }

    @Override
    public void OnGiftRecordItemListener(String account) {
        if (mContext != null && StringUtils.isNotEmpty(account)) {
            new NewUserInfoDialog(mContext, JavaUtil.str2long(account)).show();
        }
        try {
            if (isShowing()) {
                dismiss();
            }
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

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
