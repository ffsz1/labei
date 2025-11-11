package com.vslk.lbgx.room.avroom.widget.dialog;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.room.avroom.adapter.ProbabilityAdapter;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.hncxco.library_ui.widget.ViewHolder;
import com.hncxco.library_ui.widget.dialog.BaseDialog;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.pay.IPayCore;
import com.tongdaxing.xchat_core.room.bean.ProbabilityInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.bean.StartFingerGuessingGameInfo;
import com.tongdaxing.xchat_core.room.model.FingerGuessingGameModel;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Function:开始猜拳
 * Author: Edward on 2019/6/20
 */
public class StartFingerGuessingGameDialog extends BaseDialog implements View.OnClickListener {
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_count_limit_max)
    TextView tvCountLimitMax;
    @BindView(R.id.tv_finger_guessing_game_record)
    TextView tvFingerGuessingGameRecord;
    @BindView(R.id.fl_rock)
    FrameLayout flRock;
    @BindView(R.id.fl_scissors)
    FrameLayout flScissors;
    @BindView(R.id.fl_paper)
    FrameLayout flPaper;
    @BindView(R.id.iv_gift1)
    ImageView ivGift1;
    @BindView(R.id.iv_gift2)
    ImageView ivGift2;
    @BindView(R.id.iv_gift3)
    ImageView ivGift3;
    @BindView(R.id.fl_gift1)
    FrameLayout flGift1;
    @BindView(R.id.fl_gift2)
    FrameLayout flGift2;
    @BindView(R.id.fl_gift3)
    FrameLayout flGift3;
    @BindView(R.id.tv_money1)
    TextView tvMoney1;
    @BindView(R.id.tv_money2)
    TextView tvMoney2;
    @BindView(R.id.tv_money3)
    TextView tvMoney3;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.ll_probability)
    RecyclerView recyclerViewProbability;
    @BindView(R.id.tv_count1)
    TextView tvCount1;
    @BindView(R.id.tv_count2)
    TextView tvCount2;
    @BindView(R.id.tv_count3)
    TextView tvCount3;

    private int curChoose = 2;//默认石头
    private RoomInfo roomInfo;
    private StartFingerGuessingGameInfo.GiftInfoVOListBean curSelectedGift = null;
    private FingerGuessingGameModel fingerGuessingGameModel;

    @Override
    public int getContentView() {
        return R.layout.dialog_start_finger_guessing_game;
    }

    public StartFingerGuessingGameDialog() {
        fingerGuessingGameModel = new FingerGuessingGameModel();
    }

    @Override
    public void convertView(ViewHolder viewHolder) {
        ButterKnife.bind(this, viewHolder.getConvertView());
        roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        tvConfirm.setOnClickListener(this);
        flRock.setOnClickListener(this);
        flScissors.setOnClickListener(this);
        flPaper.setOnClickListener(this);
        flGift1.setOnClickListener(this);
        flGift2.setOnClickListener(this);
        flGift3.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvFingerGuessingGameRecord.setOnClickListener(this);
        loadData();
    }

    private void loadData() {
        if (roomInfo == null) {
            return;
        }
        fingerGuessingGameModel.getProbability(roomInfo.getRoomId(), new OkHttpManager.MyCallBack<ServiceResult<List<ProbabilityInfo>>>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<List<ProbabilityInfo>> response) {
                if (response == null) {
                    return;
                }
                if (response.isSuccess() && !ListUtils.isListEmpty(response.getData())) {
                    setProbabilityLayout(response.getData());
                    curProbability = response.getData().get(0).getProbability();
                    getFingerGuessingGameData(roomInfo.getRoomId(), curProbability);
                }
            }
        });
    }

    private int oldPosition = 0;

    private void setProbabilityLayout(List<ProbabilityInfo> list) {
        ProbabilityInfo temp = list.get(0);//默认将第一个设置为选中状态
        temp.setSelected(true);
        list.set(oldPosition, temp);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewProbability.setLayoutManager(layoutManager);
        ProbabilityAdapter adapter = new ProbabilityAdapter(R.layout.item_probability, list);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (adapter1 instanceof ProbabilityAdapter && oldPosition != position) {//重复点击无效
                ProbabilityAdapter probabilityAdapter = (ProbabilityAdapter) adapter1;
                ProbabilityInfo temp1 = probabilityAdapter.getItem(position);
                if (temp1 != null && roomInfo != null) {
                    curProbability = temp1.getProbability();
                    getFingerGuessingGameData(roomInfo.getRoomId(), curProbability);
                    temp1.setSelected(true);
                    probabilityAdapter.setData(position, temp1);
                    ProbabilityInfo info = probabilityAdapter.getItem(oldPosition);
                    if (info != null) {
                        info.setSelected(false);
                        probabilityAdapter.setData(oldPosition, info);
                    }
                    oldPosition = position;
                }
            }
        });
        recyclerViewProbability.setAdapter(adapter);
        adapter.setNewData(list);
    }

    private int curProbability = 1;

    private void getFingerGuessingGameData(long roomId, int probability) {
        fingerGuessingGameModel.startFingerGuessingGame(roomId, probability, new OkHttpManager.MyCallBack<ServiceResult<StartFingerGuessingGameInfo>>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<StartFingerGuessingGameInfo> response) {
                if (response.isSuccess() && response.getData() != null) {
                    initData(response.getData());
                }
            }
        });
    }

    private StartFingerGuessingGameInfo startFingerGuessingGameInfo;

    private void initData(StartFingerGuessingGameInfo info) {
        startFingerGuessingGameInfo = info;
        flGift1.setBackgroundResource(R.drawable.ic_finger_guessing_game_selected);
        flGift2.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
        flGift3.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);

        flRock.setBackgroundResource(R.drawable.ic_finger_guessing_game_selected);
        flScissors.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
        flPaper.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
        curChoose = 2;//默认石头
        curSelectedGift = startFingerGuessingGameInfo.getGiftInfoVOList().get(0);//默认选中
        tvHint.setText("若无人参与,开心将在" + info.getMoraTime() + "分钟内退还");
        tvCountLimitMax.setText(info.getNum() + "/50");
        List<StartFingerGuessingGameInfo.GiftInfoVOListBean> list = info.getGiftInfoVOList();
        if (!ListUtils.isListEmpty(list) && list.size() >= 3) {
            tvMoney1.setText(String.valueOf(list.get(0).getGiftGold()));
            tvMoney2.setText(String.valueOf(list.get(1).getGiftGold()));
            tvMoney3.setText(String.valueOf(list.get(2).getGiftGold()));
            tvCount1.setText("X " + list.get(0).getGiftNum());
            tvCount2.setText("X " + list.get(1).getGiftNum());
            tvCount3.setText("X " + list.get(2).getGiftNum());
            ImageLoadUtils.loadImage(getActivity(), list.get(0).getGiftUrl(), ivGift1, R.drawable.ic_default_avatar);
            ImageLoadUtils.loadImage(getActivity(), list.get(1).getGiftUrl(), ivGift2, R.drawable.ic_default_avatar);
            ImageLoadUtils.loadImage(getActivity(), list.get(2).getGiftUrl(), ivGift3, R.drawable.ic_default_avatar);
        }
    }

    @Override
    protected int getDialogHeight() {
        return super.getDialogHeight();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.fl_gift1:
                if (startFingerGuessingGameInfo != null) {
                    curSelectedGift = startFingerGuessingGameInfo.getGiftInfoVOList().get(0);
                    flGift1.setBackgroundResource(R.drawable.ic_finger_guessing_game_selected);
                    flGift2.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
                    flGift3.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
                }
                break;
            case R.id.fl_gift2:
                if (startFingerGuessingGameInfo != null) {
                    curSelectedGift = startFingerGuessingGameInfo.getGiftInfoVOList().get(1);
                    flGift1.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
                    flGift2.setBackgroundResource(R.drawable.ic_finger_guessing_game_selected);
                    flGift3.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
                }
                break;
            case R.id.fl_gift3:
                if (startFingerGuessingGameInfo != null) {
                    curSelectedGift = startFingerGuessingGameInfo.getGiftInfoVOList().get(2);
                    flGift1.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
                    flGift2.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
                    flGift3.setBackgroundResource(R.drawable.ic_finger_guessing_game_selected);
                }
                break;
            case R.id.fl_rock://石头
                curChoose = 2;
                flRock.setBackgroundResource(R.drawable.ic_finger_guessing_game_selected);
                flScissors.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
                flPaper.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
                break;
            case R.id.fl_scissors://剪刀
                curChoose = 1;
                flRock.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
                flScissors.setBackgroundResource(R.drawable.ic_finger_guessing_game_selected);
                flPaper.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
                break;
            case R.id.fl_paper://布
                curChoose = 3;
                flRock.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
                flScissors.setBackgroundResource(R.drawable.ic_finger_guessing_game_select);
                flPaper.setBackgroundResource(R.drawable.ic_finger_guessing_game_selected);
                break;
            case R.id.tv_finger_guessing_game_record:
                FingerGuessingGameRecordDialog dialog = new FingerGuessingGameRecordDialog();
                dialog.show(getFragmentManager(), "");
                break;
            case R.id.tv_confirm:
                confirmPk();
                break;

        }
    }

    private void confirmPk() {
        if (roomInfo == null || curSelectedGift == null) {
            return;
        }
        fingerGuessingGameModel.confirmFingerGuessingGame(roomInfo.getRoomId(), curProbability, curChoose,
                curSelectedGift.getGiftId(), curSelectedGift.getGiftNum(), new OkHttpManager.MyCallBack<Json>() {
                    @Override
                    public void onError(Exception e) {
                        if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                            SingleToastUtil.showShortToast(e.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(Json response) {
                        if (response == null) {
                            onError(new Exception("数据错误"));
                            return;
                        }
                        if (response.num("code") == 200) {
                            CoreManager.getCore(IPayCore.class).getWalletInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid());
                            dismiss();
                        } else {
                            onError(new Exception(response.str("message")));
                        }
                    }
                });
    }
}
