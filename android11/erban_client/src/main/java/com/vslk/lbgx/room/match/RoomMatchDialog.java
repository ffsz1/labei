package com.vslk.lbgx.room.match;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.face.IFaceCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.util.Map;

/**
 * 龙珠 — 速配功能
 * Created by zwk on 13/08/2018.
 */
public class RoomMatchDialog extends BaseDialogFragment implements View.OnClickListener {
    //速配的三个球
    private ImageView ivAutoMatch1;
    private ImageView ivAutoMatch2;
    private ImageView ivAutoMatch3;
    private ImageView ivAbandon;
//    private ImageView ivRule;

    //底部双功能按钮
    private Button btnMatch;
    private Button btnShow;
    private AnimationDrawable anim;
    private AnimationDrawable anim2;
    private AnimationDrawable anim3;
    private String result = "";

    private int[] nums = new int[3];
    //匹配中防止动画重复执行
    private boolean isMatching = false;


    public static RoomMatchDialog newInstance(String result) {
        RoomMatchDialog match = new RoomMatchDialog();
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        match.setArguments(bundle);
        return match;
    }


    @Override
    public int show(FragmentTransaction transaction, String tag) {
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(this, tag).addToBackStack(null);
        return transaction.commitAllowingStateLoss();

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
        View view = inflater.inflate(R.layout.dialog_room_match, window.findViewById(android.R.id.content), false);
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
        if (StringUtils.isNotEmpty(result)) {//有结果
            btnMatch.setEnabled(false);
            btnShow.setEnabled(true);
            String[] str = result.split(",");
            if (str != null && str.length >= 3) {
                setMatchState(str);
            }
        } else {
            btnMatch.setEnabled(true);
            btnShow.setEnabled(false);
        }
    }

    /**
     * 取消速配活动状态
     */
    public void cancelRoomMatchConfirm() {
        if (nums == null || nums.length < 3 || nums[0] == 0 || nums[1] == 0 || nums[2] == 0) {
            btnMatch.setEnabled(true);
            btnShow.setEnabled(false);
            return;
        }
        String result = nums[0] + "," + nums[1] + "," + nums[2];
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("roomId", AvRoomDataManager.get().mCurrentRoomInfo != null ? (AvRoomDataManager.get().mCurrentRoomInfo.getRoomId() + "") : "");
        params.put("type", 1 + "");
        params.put("result", result);
        OkHttpManager.getInstance().doPostRequest(UriProvider.cancelRoomMatchConfirm(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                btnMatch.setEnabled(false);
                btnShow.setEnabled(true);
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    CoreManager.getCore(IFaceCore.class).sendRoomMatchAbandon(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH_GIVE_UP);
                    dismiss();
                } else {
                    if (getContext() != null && response != null) {
                        Toast.makeText(getContext(), response.str("message"), Toast.LENGTH_SHORT).show();
                    }
                    btnMatch.setEnabled(false);
                    btnShow.setEnabled(true);
                }
            }
        });
    }

    private void initView(View view) {
        ivAutoMatch1 = view.findViewById(R.id.iv_match_auto1);
        ivAutoMatch2 = view.findViewById(R.id.iv_match_auto2);
        ivAutoMatch3 = view.findViewById(R.id.iv_match_auto3);
        btnMatch = view.findViewById(R.id.btn_match_ok);
        btnShow = view.findViewById(R.id.btn_match_show);
        ivAbandon = view.findViewById(R.id.iv_match_abandon);
//        ivRule = view.findViewById(R.id.iv_match_rules);
    }

    private void initClickListener() {
        btnMatch.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        ivAbandon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_match_ok:
                randomAnim();
                break;
            case R.id.btn_match_show:
                btnMatch.setEnabled(false);
                btnShow.setEnabled(false);
                getRoomMatchConfirm();
                break;
            case R.id.iv_match_abandon:
                btnMatch.setEnabled(false);
                btnShow.setEnabled(false);
                cancelRoomMatchConfirm();
                break;
        }
    }


    /**
     * 上报速配活动选择结果
     */
    public void getRoomMatchChoose() {
        String result = "";
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("roomId", AvRoomDataManager.get().mCurrentRoomInfo != null ? (AvRoomDataManager.get().mCurrentRoomInfo.getRoomId() + "") : "");
        params.put("type", 1 + "");
        params.put("result", result);
        params.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        OkHttpManager.getInstance().doPostRequest(UriProvider.postRoomMatchChoose(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                resetMatchState();
                isMatching = false;
                setCancelable(true);
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    String result = response.str("data");
                    if (StringUtils.isNotEmpty(result)) {//有结果
                        String[] str = result.split(",");
                        if (str != null && str.length >= 3) {
                            stopAnim();
                            setMatchState(str);
                            CoreManager.getCore(IFaceCore.class).sendRoomMatchFace(false, nums, IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH_SPEED);
                            btnMatch.setEnabled(false);
                            btnShow.setEnabled(true);
                            ivAbandon.setVisibility(View.VISIBLE);
                        } else {
                            resetMatchState();
                        }
                    } else {
                        resetMatchState();
                    }
                } else {
                    resetMatchState();
                    if (getContext() != null && response != null)
                        Toast.makeText(getContext(), response.str("message"), Toast.LENGTH_SHORT).show();
                }
                isMatching = false;
                setCancelable(true);
            }
        });
    }

    /**
     * 设置三个图片状态
     *
     * @param str
     */
    /**
     * 设置三个图片状态
     */
    private void setMatchState(String[] str) {
        for (int i = 0; i < 3; i++) {
            nums[i] = Integer.valueOf(str[i]);
            if (i == 0) {
                ivAutoMatch1.setImageResource(RoomMatchUtil.getMatchResId(nums[i]));
            } else if (i == 1) {
                ivAutoMatch2.setImageResource(RoomMatchUtil.getMatchResId(nums[i]));
            } else {
                ivAutoMatch3.setImageResource(RoomMatchUtil.getMatchResId(nums[i]));
            }
        }
    }

    /**
     * 重设状态
     */
    private void resetMatchState() {
        stopAnim();
        ivAutoMatch1.setImageResource(R.drawable.ic_match_question_mark);
        ivAutoMatch2.setImageResource(R.drawable.ic_match_question_mark);
        ivAutoMatch3.setImageResource(R.drawable.ic_match_question_mark);
        btnMatch.setEnabled(true);
        btnShow.setEnabled(false);
    }

    /**
     * 显示速配活动状态
     */
    public void getRoomMatchConfirm() {
        if (nums == null || nums.length < 3)
            return;
        String result = nums[0] + "," + nums[1] + "," + nums[2];
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("roomId", AvRoomDataManager.get().mCurrentRoomInfo != null ? (AvRoomDataManager.get().mCurrentRoomInfo.getRoomId() + "") : "");
        params.put("type", 1 + "");
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
                    ivAbandon.setVisibility(View.GONE);
                    CoreManager.getCore(IFaceCore.class).sendRoomMatchFace(true, nums, IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH_SPEED);
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

    /**
     * 开始动画
     */
    private void randomAnim() {
        if (isMatching)
            return;
        isMatching = true;
        setCancelable(false);
        if (anim == null)
            anim = (AnimationDrawable) getResources().getDrawable(R.drawable.anim_match_random);
        if (anim2 == null)
            anim2 = (AnimationDrawable) getResources().getDrawable(R.drawable.anim_match_random);
        if (anim3 == null)
            anim3 = (AnimationDrawable) getResources().getDrawable(R.drawable.anim_match_random);
        ivAutoMatch1.setImageDrawable(anim);
        ivAutoMatch2.setImageDrawable(anim2);
        ivAutoMatch3.setImageDrawable(anim3);
        anim.start();
        anim2.start();
        anim3.start();
        Handler handler = new Handler();
        handler.postDelayed(this::getRoomMatchChoose, 1000);
    }

    /**
     * 停止动画
     */
    private void stopAnim() {
        if (anim != null && anim.isRunning())
            anim.stop();
        if (anim2 != null && anim2.isRunning())
            anim2.stop();
        if (anim3 != null && anim3.isRunning())
            anim3.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ivAutoMatch1 != null)
            ivAutoMatch1.clearAnimation();
        if (ivAutoMatch2 != null)
            ivAutoMatch2.clearAnimation();
        if (ivAutoMatch3 != null)
            ivAutoMatch3.clearAnimation();
    }
}
