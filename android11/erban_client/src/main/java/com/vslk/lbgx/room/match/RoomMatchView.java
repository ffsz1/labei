package com.vslk.lbgx.room.match;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.Map;

/**
 * 1.0.7 配对功能View
 */
public class RoomMatchView extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private ImageView ivPair, ivMatch, ivChoice;
    private boolean isShow = false;//双选择一起是否显示的标记

    public RoomMatchView(Context context) {
        this(context, null);
    }

    public RoomMatchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoomMatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        isShow = false;
        inflate(context, R.layout.layout_pair_container, this);
        ivPair = findViewById(R.id.iv_room_pair);
        ivMatch = findViewById(R.id.iv_room_match);
        ivChoice = findViewById(R.id.iv_room_choice);
        ivPair.setOnClickListener(this);
        ivMatch.setOnClickListener(this);
        ivChoice.setOnClickListener(this);
    }

    /**
     * 速配 隐藏和显示动画
     *
     * @param view
     * @param isShow
     */
    private void doMatchAnim(View view, boolean isShow) {
        if (view == null)
            return;
        view.clearAnimation();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", isShow ? 1f : 60f, isShow ? 60f : 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", isShow ? 1f : 60f, isShow ? 60f : 1f);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX", isShow ? 0 : -300, isShow ? -300 : 0);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(scaleX, scaleY, translateX); //设置动画
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(300);  //设置动画时间
        animatorSet.start(); //启动
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (view != null)
                    view.clearAnimation();
            }
        });
    }

    /**
     * 选择 隐藏和显示动画
     *
     * @param view
     * @param isShow
     */
    private void doSelectAnim(View view, boolean isShow) {
        if (view == null)
            return;
        view.clearAnimation();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", isShow ? 1f : 60f, isShow ? 60f : 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", isShow ? 1f : 60f, isShow ? 60f : 1f);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX", isShow ? 0 : -210, isShow ? -210 : 0);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(scaleX, scaleY, translateX); //设置动画
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(300);  //设置动画时间
        animatorSet.start(); //启动
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (view != null)
                    view.clearAnimation();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_room_pair:
                if (isShow) {//显示了，直接执行隐藏
                    doAnim();
                } else {//未显示
                    getRoomMatchState(false);
                }
                break;
            case R.id.iv_room_match:
                showSpeed("");
                break;
            case R.id.iv_room_choice:
                showChoice("");
                break;
        }
    }

    /**
     * 获取速配活动状态
     */
    public void getRoomMatchState(boolean enterRoom) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        params.put("roomId", AvRoomDataManager.get().mCurrentRoomInfo != null ? (AvRoomDataManager.get().mCurrentRoomInfo.getRoomId() + "") : "");
        OkHttpManager.getInstance().getRequest(UriProvider.getRoomMatch(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    Json data = response.json("data");
                    if (data != null) {
                        int result = data.num("status");
                        String arrs = data.str("result");
                        if (result == -1) {//没有权限
                            if (getVisibility() == View.VISIBLE)
                                setVisibility(View.GONE);
                        } else {
                            if (AvRoomDataManager.get().isRoomOwner()) {//房主显示
                                showView(result, enterRoom, arrs);
                            } else {
                                if (AvRoomDataManager.get().isOnMic(CoreManager.getCore(IAuthCore.class).getCurrentUid())) {
                                    showView(result, enterRoom, arrs);
                                }
                            }
                        }
                    }
                } else {
//                    if (getVisibility() == View.VISIBLE)
//                        setVisibility(View.GONE);
                    if (getContext() != null && response != null)
                        Toast.makeText(getContext(), response.str("message"), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showView(int result, boolean enterRoom, String arrs) {
        if (getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }
        if (!enterRoom) {//是否是进入房间
            if (result == 0) {//0没有选择过，1，2
                doAnim();
            } else if (result == 1) {//显示速配
                showSpeed(arrs);
            } else if (result == 2) {//显示选择
                showChoice(arrs);
            }
        }
    }

    /**
     * 显示速配弹框
     *
     * @param arrs
     */
    private void showSpeed(String arrs) {
        RoomMatchDialog match = RoomMatchDialog.newInstance(arrs);
        if (mContext instanceof BaseMvpActivity)
            match.show(((BaseMvpActivity) mContext).getSupportFragmentManager(), "match");
        if (isShow)
            doAnim();
    }

    /**
     * 显示选择弹框
     *
     * @param arrs
     */
    private void showChoice(String arrs) {
        RoomChoiceDialog choice = RoomChoiceDialog.newInstance(arrs);
        if (mContext instanceof BaseMvpActivity)
            choice.show(((BaseMvpActivity) mContext).getSupportFragmentManager(), "choice");
        if (isShow)
            doAnim();
    }


    /**
     * 执行动画
     */
    private void doAnim() {
        if (isShow) {
            isShow = false;
            ivPair.setImageResource(R.drawable.ic_room_pair_unselected);
            doMatchAnim(ivMatch, false);
            doSelectAnim(ivChoice, false);
        } else {
            ivPair.setImageResource(R.drawable.ic_room_pair_selected);
            isShow = true;
            doMatchAnim(ivMatch, true);
            doSelectAnim(ivChoice, true);
        }
    }


    public void setShowState(boolean hide) {
        if (hide) {
            if (isShow) {
                ivPair.setImageResource(R.drawable.ic_room_pair_unselected);
                doMatchAnim(ivMatch, false);
                doSelectAnim(ivChoice, false);
                isShow = false;
            }
            if (getVisibility() == View.VISIBLE)
                setVisibility(View.GONE);
        } else {
            if (getVisibility() == View.GONE)
                setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (ivMatch != null) {
            ivMatch.clearAnimation();
        }
        if (ivChoice != null) {
            ivChoice.clearAnimation();
        }
    }
}
