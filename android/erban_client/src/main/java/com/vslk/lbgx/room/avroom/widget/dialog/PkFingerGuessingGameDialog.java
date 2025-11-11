package com.vslk.lbgx.room.avroom.widget.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.hncxco.library_ui.widget.ViewHolder;
import com.hncxco.library_ui.widget.dialog.BaseDialog;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.pay.IPayCore;
import com.tongdaxing.xchat_core.room.bean.PkFingerGuessingGameInfo;
import com.tongdaxing.xchat_core.room.model.FingerGuessingGameModel;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Function:
 * Author: Edward on 2019/6/20
 */
public class PkFingerGuessingGameDialog extends BaseDialog implements View.OnClickListener {
    @BindView(R.id.iv_user_head1)
    ImageView ivUserHead1;
    @BindView(R.id.iv_user_head2)
    ImageView ivUserHead2;
    @BindView(R.id.tv_name1)
    TextView tvName1;
    @BindView(R.id.tv_name2)
    TextView tvName2;
    @BindView(R.id.iv_gift)
    ImageView ivGift;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.fl_rock)
    FrameLayout flRock;
    @BindView(R.id.fl_scissors)
    FrameLayout flScissors;
    @BindView(R.id.fl_paper)
    FrameLayout flPaper;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_count)
    TextView tvCount;

    private FingerGuessingGameModel fingerGuessingGameModel;
    private PkFingerGuessingGameInfo info;

    public PkFingerGuessingGameDialog() {
        fingerGuessingGameModel = new FingerGuessingGameModel();
    }

    public static final String KEY = "key";

    @Override
    public void convertView(ViewHolder viewHolder) {
        ButterKnife.bind(this, viewHolder.getConvertView());
        Bundle bundle = getArguments();
        if (bundle != null) {
            info = bundle.getParcelable(KEY);
            if (info != null) {
                ImageLoadUtils.loadCircleImage(getActivity(), info.getGiftUrl(), ivGift, R.drawable.ic_default_avatar);
                tvName1.setText(info.getNick());
                tvName2.setText(info.getOpponentNick());
                ImageLoadUtils.loadCircleImage(getActivity(), info.getAvatar(), ivUserHead1, R.drawable.ic_default_avatar);
                ImageLoadUtils.loadCircleImage(getActivity(), info.getOpponentAvatar(), ivUserHead2, R.drawable.ic_default_avatar);
                tvCount.setText("X " + info.getGiftNum());
            }
        }
        flRock.setOnClickListener(this);
        flScissors.setOnClickListener(this);
        flPaper.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    @Override
    public int getContentView() {
        return R.layout.dialog_finger_guessing_game_pk;
    }

    private void confirmCommit() {
        if (info == null) {
            SingleToastUtil.showShortToast("数据异常");
            return;
        }
        DialogManager dialogManager = new DialogManager(getActivity());
        dialogManager.showProgressDialog(getActivity(), "请稍后...");
        fingerGuessingGameModel.confrimPkFingerGuessingGame(info.getRecordId(), curChoose, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                dialogManager.dismissDialog();
                if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                    SingleToastUtil.showToast(e.getMessage());
                }
            }

            @Override
            public void onResponse(Json response) {
                dialogManager.dismissDialog();
                if (response == null) {
                    onError(new Exception("数据错误"));
                    return;
                }
                if (response.num("code") == 200) {
                    CoreManager.getCore(IPayCore.class).getWalletInfo(CoreManager.getCore(IAuthCore.class).getCurrentUid());
                    dismiss();
                } else if (!TextUtils.isEmpty(response.str("message"))) {
                    SingleToastUtil.showToast(response.str("message"));
                } else {
                    onError(new Exception("数据错误"));
                }
            }
        });
    }

    private int curChoose = 2;//默认石头

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_confirm:
                confirmCommit();
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
        }
    }
}
