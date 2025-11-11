package com.vslk.lbgx.room.match;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.blankj.utilcode.util.ConvertUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomChoiceBean;
import com.tongdaxing.xchat_core.room.face.IFaceCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.StringUtils;
import com.vslk.lbgx.base.fragment.BaseDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 龙珠 -- 选择功能
 * Created by zwk on 13/08/2018.
 */
public class RoomChoiceDialog extends BaseDialogFragment implements View.OnClickListener {
    private RecyclerView rvChoice;
    private RoomChoiceAdapter mAdpater;
    private boolean lock = false;
    //底部双功能按钮iv_match_rules
    private Button btnMatch;
    private Button btnShow;
    private String result = "";

    private int[] drawableId = {R.drawable.ic_match_random, R.drawable.ic_match_num_1, R.drawable.ic_match_num_2, R.drawable.ic_match_num_3, R.drawable.ic_match_num_4,
            R.drawable.ic_match_num_5, R.drawable.ic_match_num_6, R.drawable.ic_match_num_7, R.drawable.ic_match_num_8, R.drawable.ic_match_num_9};

    private int[] nums = new int[1];

    public static RoomChoiceDialog newInstance(String result) {
        RoomChoiceDialog match = new RoomChoiceDialog();
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        match.setArguments(bundle);
        return match;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String str = arguments.getString("result");
            this.result = str != null ? str : "";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        // setup window and width
        View view = inflater.inflate(R.layout.dialog_room_choice, window.findViewById(android.R.id.content), false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        setCancelable(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initClickListener();
        initData();
//        getRoomMatchState();
        if (StringUtils.isNotEmpty(result)) {
            btnShow.setEnabled(true);
            String[] str = result.split(",");
            if (str != null && str.length >= 1) {
                lock = true;
                mAdpater.changeState(Integer.valueOf(str[0]));
            }
        } else {
            btnMatch.setEnabled(true);
        }
    }

    private void initView(View view) {
        btnMatch = view.findViewById(R.id.btn_match_ok);
        btnShow = view.findViewById(R.id.btn_match_show);
        rvChoice = view.findViewById(R.id.rv_room_choice);
        mAdpater = new RoomChoiceAdapter(R.layout.item_rv_room_choice);
        int distance = ConvertUtils.dp2px( 20);
        rvChoice.setLayoutManager(new GridLayoutManager(getContext(), 5));
        rvChoice.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = distance;
            }
        });
        rvChoice.setAdapter(mAdpater);
    }

    private void initClickListener() {
        btnMatch.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        mAdpater.setOnItemClickListener((adapter, view, position) -> {
            if (lock) {
                if (getContext() != null)
                    SingleToastUtil.showShortToast("已选择结果，请先展示结果！");
                return;
            }
            if (mAdpater == null || ListUtils.isListEmpty(mAdpater.getData()) || mAdpater.getData().size() <= position)
                return;
            mAdpater.changeState(position);
            btnMatch.setEnabled(mAdpater.getSelectPosition() != -1);
        });
    }

    private void initData() {
        List<RoomChoiceBean> datas = new ArrayList<>();
        for (int i = 0; i < drawableId.length; i++) {
            RoomChoiceBean choice = new RoomChoiceBean();
            choice.setPosition(i);
            choice.setResId(drawableId[i]);
            if (i == 0) {
                choice.setRandom(true);
            }
            datas.add(choice);
        }
        mAdpater.setNewData(datas);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_match_ok:
                if (mAdpater != null && mAdpater.getSelectPosition() != -1) {
                    nums[0] = mAdpater.getSelectPosition();
                    btnShow.setEnabled(false);
                    btnMatch.setEnabled(false);
                    lock = true;
                    getRoomMatchChoose();
                } else {
                    if (getContext() != null)
                        Toast.makeText(getContext(), "请先选择结果！", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_match_show:
                if (mAdpater != null && mAdpater.getSelectPosition() != -1) {
                    nums[0] = mAdpater.getSelectPosition();
                    btnShow.setEnabled(false);
                    btnMatch.setEnabled(false);
                    getRoomMatchConfirm();
                }
                break;
        }
    }


    /**
     * 上报速配活动选择结果
     */
    public void getRoomMatchChoose() {
        if (nums == null || nums.length < 1)
            return;
        String result = nums[0] + "";
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("roomId", AvRoomDataManager.get().mCurrentRoomInfo != null ? (AvRoomDataManager.get().mCurrentRoomInfo.getRoomId() + "") : "");
        params.put("type", 2 + "");
        params.put("result", result);
        params.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        OkHttpManager.getInstance().doPostRequest(UriProvider.postRoomMatchChoose(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                btnMatch.setEnabled(true);
                btnShow.setEnabled(false);
                if (getContext() != null && e != null)
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    //接口调用成功后才显示
                    lock = true;
                    CoreManager.getCore(IFaceCore.class).sendRoomMatchFace(false, nums, IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH_CHOICE);
                    btnMatch.setEnabled(false);
                    btnShow.setEnabled(true);
                } else {
                    btnMatch.setEnabled(true);
                    btnShow.setEnabled(false);
                    if (getContext() != null && response != null)
                        Toast.makeText(getContext(), response.str("message"), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 获取速配活动状态
     */
    public void getRoomMatchConfirm() {
        if (nums == null || nums.length < 1)
            return;
        String result = nums[0] + "";
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("roomId", AvRoomDataManager.get().mCurrentRoomInfo != null ? (AvRoomDataManager.get().mCurrentRoomInfo.getRoomId() + "") : "");
        params.put("type", 2 + "");
        params.put("result", result);
        params.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        OkHttpManager.getInstance().doPostRequest(UriProvider.postRoomMatchConfirm(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                btnMatch.setEnabled(false);
                btnShow.setEnabled(true);

            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    lock = false;
                    CoreManager.getCore(IFaceCore.class).sendRoomMatchFace(true, nums, IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH_CHOICE);
                    dismiss();
                } else {
                    if (getContext() != null && response != null)
                        Toast.makeText(getContext(), response.str("message"), Toast.LENGTH_SHORT).show();
                    btnMatch.setEnabled(false);
                    btnShow.setEnabled(true);
                }
            }
        });
    }

}
