package com.vslk.lbgx.room.egg;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.vslk.lbgx.room.avroom.adapter.CallGiftAdapter;
import com.vslk.lbgx.room.avroom.adapter.CallUsersAdapter;
import com.vslk.lbgx.room.egg.call.CallCoreImpl;
import com.vslk.lbgx.room.egg.call.ICallCore;
import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.ui.dialog.BaseDialog;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

//public class RoomCallDialog extends BaseDialog {
//    @BindView(R.id.rv_users)
//    RecyclerView rvUsers;
//    @BindView(R.id.rv_gift)
//    RecyclerView rvGift;
//    @BindView(R.id.avatar)
//    CircleImageView avatar;
//    @BindView(R.id.tv_hl_count)
//    TextView tvHlCount;
//    @BindView(R.id.fl_hl)
//    FrameLayout flHl;
//    @BindView(R.id.iv_bg)
//    ImageView ivBg;
//    @BindView(R.id.fl_host)
//    FrameLayout flHost;
//    @BindView(R.id.tv_num)
//    TextView tvNum;
//    @BindView(R.id.tv_recharge)
//    LinearLayout tvRecharge;
//    @BindView(R.id.iv_call)
//    ImageView ivCall;
//
//    private MicMemberInfo callMicMemberInfo = null;
//    private GiftInfo callGift = null;
//    private CallUsersAdapter callUsersAdapter;
//    private CallGiftAdapter callGiftAdapter;
//    private MicMemberInfo hostMicMemberInfo;
//    private double goldNum = 0;
//    private ICallCore iCallCore;
//    private PoundEggDialog lotteryBoxDialog;
//    private boolean stop = true;
//    //初始化
//    private Handler handler;
//    private Runnable runnable;
//    private int starTime = 3000;
//    private CallRuleDialog callRuleDialog;
//
//    @Override
//    protected int layout() {
//        return R.layout.dialog_room_call;
//    }
//
//    @Override
//    protected int setAnim() {
//        return 1;
//    }
//
//    @Override
//    protected int close() {
//        return R.id.rl_back;
//    }
//
//    @Override
//    protected void bindView(View view) {
//        iCallCore = CallCoreImpl.getInstance().getICallCore();
//        callUsersAdapter = new CallUsersAdapter(new ArrayList<>());
//        callGiftAdapter = new CallGiftAdapter(new ArrayList<>());
//        rvUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        rvGift.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        rvUsers.setAdapter(callUsersAdapter);
//        rvGift.setAdapter(callGiftAdapter);
//        flHost.setOnClickListener(v -> {
//            // 排除自己
//            if (!AvRoomDataManager.get().isOwner(hostMicMemberInfo.getUid())) {
//                upHostUser(true, hostMicMemberInfo);
//            } else {
//                SingleToastUtil.showToast("不能给自己打call");
//            }
//        });
//        callUsersAdapter.setCallUserListener((info -> upHostUser(false, info)));
//        callGiftAdapter.setCallGiftListener(info -> callGift = info);
//        tvRecharge.setOnClickListener(v -> WalletActivity.start(getActivity()));
//        ivCall.setOnClickListener(v -> {
//            if (callMicMemberInfo == null) {
//                SingleToastUtil.showToast("请选择打call对象");
//                return;
//            }
////            if (AvRoomDataManager.get().isOwner(callMicMemberInfo.getUid())) {
////                SingleToastUtil.showToast("不可以给自己打call");
////                return;
////            }
//            if (callGift == null) {
//                SingleToastUtil.showToast("请选择打call礼物");
//                return;
//            }
//            if (iCallCore != null) iCallCore.sendCall(callMicMemberInfo, callGift, (call -> {
//                int conchNum = call.getConchNum();
//                if (tvHlCount != null) {
//                    tvHlCount.setText(String.valueOf(conchNum));
//                    if (goldNum >= call.getGoldPrice())
//                        goldNum = goldNum - call.getGoldPrice();
//                    tvNum.setText(getContext().getString(R.string.gold_num_text, goldNum));
//                }
//            }));
//        });
//        flHl.setOnClickListener(v -> {
//            toNext();
//        });
//
//        view.findViewById(R.id.iv_call_rule).setOnClickListener(v -> {
//            if (callRuleDialog == null) {
//                callRuleDialog = new CallRuleDialog();
//            }
//            if (callRuleDialog.isAdded()) {
//                callRuleDialog.dismiss();
//            } else {
//                callRuleDialog.show(getChildFragmentManager(), "call_rule");
//            }
//        });
//
//        List<GiftInfo> gifts = CoreManager.getCore(IGiftCore.class).getGiftInfosByType(1);
//        startPolling();
//        if (gifts == null || gifts.size() == 0) {
//            return;
//        }
//
//        if (gifts.get(0).getGoldPrice() == 1000) {
//            Collections.reverse(gifts);
//        }
//        for (GiftInfo info : gifts) {
//            info.setSelected(false);
//        }
//        gifts.get(0).setSelected(true);
//
//        callGift = gifts.get(0);
//        callGiftAdapter.setNewData(gifts);
//    }
//
//    private void toNext() {
//        lotteryBoxDialog = PoundEggDialog.newOnlineUserListInstance();
//        Bundle bundle = new Bundle();
//        String count = tvHlCount.getText().toString();
//        bundle.putString("count", count);
//        lotteryBoxDialog.setArguments(bundle);
//        lotteryBoxDialog.setEggListener((c -> tvHlCount.setText(String.valueOf(c))));
//        if (lotteryBoxDialog.isAdded())
//            lotteryBoxDialog.dismiss();
//        else
//            lotteryBoxDialog.show(getChildFragmentManager(), "lotteryBoxDialog");
//    }
//
//    /**
//     * 定时任务
//     */
//    private void startPolling() {
//        stop = true;
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (stop) {
//                    handler.postDelayed(this, starTime);
//                    if (iCallCore != null) iCallCore.getGold((wallet -> {
//                        updateWalletInfo(wallet);
//                        if (lotteryBoxDialog != null) lotteryBoxDialog.sub(wallet.getConchNum());
//                    }));
//
//                }
//            }
//        };
//        handler.postDelayed(runnable, starTime);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (iCallCore != null) iCallCore.getGold((wallet -> {
//            updateWalletInfo(wallet);
//            if (lotteryBoxDialog != null) lotteryBoxDialog.sub(wallet.getConchNum());
//        }));
//        loadMultiPeople();
//    }
//
//    private void loadMultiPeople() {
//        UserInfo roomOwner = CoreManager.getCore(IAVRoomCore.class).getRoomOwner();
//        hostMicMemberInfo = new MicMemberInfo();
//        hostMicMemberInfo.setRoomOwnner(true);
//        if (roomOwner != null) {
//            hostMicMemberInfo.setNick(roomOwner.getNick() == null ? "" : roomOwner.getNick());
//            hostMicMemberInfo.setAvatar(roomOwner.getAvatar() == null ? "" : roomOwner.getAvatar());
//            hostMicMemberInfo.setUid(roomOwner.getUid());
//            upHostUser(true, hostMicMemberInfo);
//        }
//
//
////        SparseArray<RoomQueueInfo> mMicQueueMemberMap = AvRoomDataManager.get().mMicQueueMemberMap;
////        List<MicMemberInfo> micMemberInfos = new ArrayList<>();
////        for (int i = 0; i < mMicQueueMemberMap.size(); i++) {
////            MicMemberInfo micMemberInfo = new MicMemberInfo();
////            IMChatRoomMember mChatRoomMember = mMicQueueMemberMap.get(mMicQueueMemberMap.keyAt(i)).mChatRoomMember;
////            if (mChatRoomMember == null) {
////                continue;
////            }
////            // 合法判断
////            String account = mChatRoomMember.getAccount();
////            LogUtils.d("checkHasOwner", account + "   dd");
////            if (TextUtils.isEmpty(account) ||
////                    TextUtils.isEmpty(mChatRoomMember.getNick()) ||
////                    TextUtils.isEmpty(mChatRoomMember.getAvatar())) {
////                continue;
////            }
////            // 排除自己
////            if (AvRoomDataManager.get().isOwner(account)) {
////                continue;
////            }
////            // 设置房主
////            if (AvRoomDataManager.get().isRoomOwner(account)) {
////                continue;
////            }
////            micMemberInfo.setNick(mChatRoomMember.getNick());
////            micMemberInfo.setAvatar(mChatRoomMember.getAvatar());
////            micMemberInfo.setMicPosition(mMicQueueMemberMap.keyAt(i));
////            micMemberInfo.setUid(JavaUtil.str2long(account));
////            micMemberInfos.add(micMemberInfo);
////        }
////        callUsersAdapter.setNewData(micMemberInfos);
//    }
//
//
//    /**
//     * 更新金额信息
//     */
//    private void updateWalletInfo(WalletInfo walletInfo) {
//        if (walletInfo != null) {
//            goldNum = walletInfo.getGoldNum();
//            tvNum.setText(getContext().getString(R.string.gold_num_text, goldNum));
//            tvHlCount.setText(String.valueOf(walletInfo.getConchNum()));
//        }
//    }
//
//    private boolean checkHasOwner(SparseArray<RoomQueueInfo> mMicQueueMemberMap) {
//        UserInfo roomOwner = CoreManager.getCore(IAVRoomCore.class).getRoomOwner();
//        if (roomOwner == null) {
//            return true;
//        }
//        for (int i = 0; i < mMicQueueMemberMap.size(); i++) {
//            RoomQueueInfo roomQueueInfo = mMicQueueMemberMap.get(mMicQueueMemberMap.keyAt(i));
//            if (roomQueueInfo != null && roomQueueInfo.mChatRoomMember != null) {
//                String account = roomQueueInfo.mChatRoomMember.getAccount();
//                if ((roomOwner.getUid() + "").equals(account))
//                    return true;
//            }
//        }
//        return false;
//    }
//
//    private void upHostUser(boolean ish, MicMemberInfo info) {
//        if (ish) {
//            ivBg.setImageResource(R.drawable.bg_room_call_user);
//            callUsersAdapter.upOldPosition();
//            ImageLoadUtils.loadAvatar(getContext(), info.getAvatar(), avatar);
//        } else {
//            ivBg.setImageResource(R.drawable.bg_room_call_user_off);
//        }
//        callMicMemberInfo = info;
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (iCallCore != null) {
//            iCallCore = null;
//        }
//        stop = false;
//        if (handler != null) {
//            handler = null;
//            runnable = null;
//        }
//    }
//}
