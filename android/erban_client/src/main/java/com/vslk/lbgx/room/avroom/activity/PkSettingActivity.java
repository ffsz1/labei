package com.vslk.lbgx.room.avroom.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.base.view.TitleBar;
import com.vslk.lbgx.room.avroom.fragment.OnlineUserFragment;
import com.vslk.lbgx.room.avroom.other.ButtonItemFactory;
import com.vslk.lbgx.room.widget.dialog.ListDataDialog;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.pk.IPKCoreClient;
import com.tongdaxing.xchat_core.pk.IPkCore;
import com.tongdaxing.xchat_core.pk.bean.PkVoteInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PkSettingActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.riv_pk_user_icon_1)
    RoundedImageView rivPkUserIcon1;
    @BindView(R.id.tv_pk_user_name_1)
    TextView tvPkUserName1;
    @BindView(R.id.riv_pk_user_icon_2)
    RoundedImageView rivPkUserIcon2;
    @BindView(R.id.tv_pk_user_name_2)
    TextView tvPkUserName2;
    @BindView(R.id.tv_pk_type)
    TextView tvPkType;
    @BindView(R.id.rl_select_pk_type)
    RelativeLayout rlSelectPkType;
    @BindView(R.id.tv_pk_time)
    TextView tvPkTime;
    @BindView(R.id.rl_select_pk_time)
    RelativeLayout rlSelectPkTime;
    @BindView(R.id.bu_pk_submit)
    Button buPkSubmit;

    private int pkTime = 60;
    private int pkType = 2;
    private int userSelectState;
    private IMChatRoomMember userData1;
    private IMChatRoomMember userData2;

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.riv_pk_user_icon_1:
                userSelectState = 1;
                ListDataDialog listDataDialog = ListDataDialog.newMicUserInstance(this);
                listDataDialog.setOnlineItemClick(new OnlineUserFragment.OnlineItemClick() {
                    @Override
                    public void onItemClick(IMChatRoomMember chatRoomMember) {
                        setUser(chatRoomMember, tvPkUserName1, rivPkUserIcon1, 1);
                        listDataDialog.dismiss();
                    }
                });
                listDataDialog.show(getSupportFragmentManager());
                break;
            case R.id.riv_pk_user_icon_2:
                userSelectState = 1;
                ListDataDialog listDataDialog2 = ListDataDialog.newMicUserInstance(this);
                listDataDialog2.setOnlineItemClick(new OnlineUserFragment.OnlineItemClick() {
                    @Override
                    public void onItemClick(IMChatRoomMember chatRoomMember) {
                        setUser(chatRoomMember, tvPkUserName2, rivPkUserIcon2, 2);
                        listDataDialog2.dismiss();
                    }
                });
                listDataDialog2.show(getSupportFragmentManager());
                break;
            case R.id.rl_select_pk_type:
                Map<String,Integer> jsonsType = new LinkedHashMap<>();
                jsonsType.put("按投票人数PK(1人=1pk值）", 1);
                jsonsType.put("按礼物价值PK(1金币=1pk值）",2);
                buildOptionDialog(jsonsType, pkType);
                break;
            case R.id.rl_select_pk_time:
                Map<String,Integer> jsonsTime = new LinkedHashMap<>();
                jsonsTime.put("30秒",30);
                jsonsTime.put("60秒",60);
                jsonsTime.put("90秒",90);
                jsonsTime.put("180秒",180);
                jsonsTime.put("5分钟",300);
                jsonsTime.put("10分钟",600);
                jsonsTime.put("20分钟",1200);
                buildOptionDialog(jsonsTime, pkTime);
                break;
            case R.id.bu_pk_submit:
                submit();
                break;
            default:

        }

    }

    private void submit() {
        if (userData1 == null || userData2 == null)
            return;
        PkVoteInfo info =  new PkVoteInfo();
        info.setPkType(pkType);
        info.setOpUid(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        info.setUid(Long.valueOf(userData1.getAccount()));
        info.setNick(userData1.getNick());
        info.setAvatar(userData1.getAvatar());
        info.setPkUid(Long.valueOf(userData2.getAccount()));
        info.setPkNick(userData2.getNick());
        info.setPkAvatar(userData2.getAvatar());
        info.setDuration(pkTime);
        info.setExpireSeconds(pkTime);
        info.setOpUid(CoreManager.getCore(IAuthCore.class).getCurrentUid());
        getDialogManager().showProgressDialog(this, "请稍后...");
        buPkSubmit.setEnabled(false);
        CoreManager.getCore(IPkCore.class).savePK(AvRoomDataManager.get().mCurrentRoomInfo == null?0:AvRoomDataManager.get().mCurrentRoomInfo.getRoomId(),info);
    }

    @CoreEvent(coreClientClass = IPKCoreClient.class)
    public void onSavePk(PkVoteInfo pkVoteInfo){
        buPkSubmit.setEnabled(true);
        getDialogManager().dismissDialog();
//        toast("发起PK成功");
        IMNetEaseManager.get().sendPkNotificationBySdk(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_FIRST, IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_SECOND_START,pkVoteInfo);
        finish();
    }

    @CoreEvent(coreClientClass = IPKCoreClient.class)
    public void onSavePkFail(String error){
        buPkSubmit.setEnabled(true);
        getDialogManager().dismissDialog();
        toast(error);
    }

    private void setUser(IMChatRoomMember data, TextView tvPkUserName, RoundedImageView rivPkUserIcon, int userNum) {
        if (data == null) {
            toast("数据异常，找不到该用户");
            return;
        }
        checkRepeat(data);
        if (userNum == 1) {
            userData1 = data;
        } else {
            userData2 = data;
        }
        tvPkUserName.setText(data.getNick() + "");
        ImageLoadUtils.loadImage(this, data.getAvatar(), rivPkUserIcon);

    }

    private boolean checkRepeat(IMChatRoomMember data) {
        if (userData1 != null) {
            if (userData1.getAccount().equals(data.getAccount())) {
                userData1 = null;
                tvPkUserName1.setText("");
                rivPkUserIcon1.setImageResource(R.drawable.ic_pk_left_avatar);
                return true;
            }
        }
        if (userData2 != null) {
            if (userData2.getAccount().equals(data.getAccount())) {
                userData2 = null;
                tvPkUserName2.setText("");
                rivPkUserIcon2.setImageResource(R.drawable.ic_pk_right_avatar);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pk_setting);
        ButterKnife.bind(this);
        initTitleBar("PK");
        if (mTitleBar != null) {
            TextView textView = new TextView(this);
            textView.setText("记录");
            textView.setTextSize(13);
            textView.setTextColor(ContextCompat.getColor(this,R.color.color_333333));
            mTitleBar.setDividerColor(ContextCompat.getColor(this,R.color.color_f7f7f7));
            mTitleBar.mRightLayout.addView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PkHistoryActivity.start(PkSettingActivity.this);
                }
            });
        }
        rivPkUserIcon1.setOnClickListener(this);
        rivPkUserIcon2.setOnClickListener(this);
        rlSelectPkTime.setOnClickListener(this);
        rlSelectPkType.setOnClickListener(this);
        buPkSubmit.setOnClickListener(this);

    }

    public void buildOptionDialog(Map<String,Integer> jsons, int type) {
        List<ButtonItem> buttonItems = new ArrayList<>();
        for (Map.Entry<String,Integer> entry: jsons.entrySet()) {
            ButtonItem msgBlackListItem = ButtonItemFactory.createMsgBlackListItem(entry.getKey(), new ButtonItemFactory.OnItemClick() {
                @Override
                public void itemClick() {
                    optionAction(entry.getValue(), entry.getKey());
                }
            });
            if (entry.getValue() == type)
                msgBlackListItem.textColor = "#09CAA2";
            buttonItems.add(msgBlackListItem);
        }
        getDialogManager().showCommonPopupDialog(buttonItems, getString(R.string.cancel));
    }

    private void optionAction(int type, String name) {
        if (type < 30) {
            pkType = type;
            if (type == 1) {
                tvPkType.setText("按投票人数PK ");
            }else {
                tvPkType.setText("按礼物价值PK ");
            }
        } else {
            pkTime = type;
            tvPkTime.setText(name + " ");
        }

    }
}
