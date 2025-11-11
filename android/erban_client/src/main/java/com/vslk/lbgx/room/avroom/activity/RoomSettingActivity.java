package com.vslk.lbgx.room.avroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donkingliang.labels.LabelsView;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.hncxco.library_ui.widget.AppToolBar;
import com.kyleduo.switchbutton.SwitchButton;
import com.netease.nimlib.sdk.StatusCode;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.home.TabInfo;
import com.tongdaxing.xchat_core.im.login.IIMLoginClient;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.presenter.RoomSettingPresenter;
import com.tongdaxing.xchat_core.room.view.IRoomSettingView;
import com.tongdaxing.xchat_core.utils.StringUtils;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenran
 * @date 2017/9/26
 */
@CreatePresenter(RoomSettingPresenter.class)
public class RoomSettingActivity extends BaseMvpActivity<IRoomSettingView, RoomSettingPresenter> implements
        LabelsView.OnLabelClickListener, View.OnClickListener, IRoomSettingView, CompoundButton.OnCheckedChangeListener {

    private EditText nameEdit;
    private EditText topicEdit;
    private EditText pwdEdit;
    private LabelsView labelsView;
    private RoomInfo roomInfo;
    private List<String> labels;
    private String selectLabel;
    private TextView managerLayout;
    private TextView blackLayout;
    private TextView rlRoomPlay;
    private LinearLayout mLabelLayout;

    private TabInfo mSelectTabInfo;
    private List<TabInfo> mTabInfoList;
    private TextView bgLayout;
    private String mBackPic;
    /**
     * 新增字段网络背景图片地址
     */
    private String mBackPicUrl = "";
    /**
     * 小礼物特效开关
     */
    private SwitchButton sbSetting;
    /**
     * 坐骑特效开关
     */
    private SwitchButton rideSetting;
    private int changeGiftEffect = -1;
    /**
     * 座驾礼物特效开关，默认关闭过滤（坐骑礼物会展示）
     */
    private int rideGiftEffect = 0;

    public static void start(Context context, RoomInfo roomInfo) {
        Intent intent = new Intent(context, RoomSettingActivity.class);
        intent.putExtra("roomInfo", roomInfo);
        context.startActivity(intent);
    }

    public static void start(Context context, RoomInfo roomInfo, int isPermitRoom) {
        Intent intent = new Intent(context, RoomSettingActivity.class);
        intent.putExtra("roomInfo", roomInfo);
        intent.putExtra("isPermitRoom", isPermitRoom);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_setting);
//        roomInfo = getIntent().getParcelableExtra("roomInfo");
        roomInfo = (RoomInfo)getIntent().getSerializableExtra("roomInfo");
        int isPermitRoom = getIntent().getIntExtra("isPermitRoom", -2);
        if (isPermitRoom != -2) {
            roomInfo.setIsPermitRoom(isPermitRoom);
        }
        //初始化界面要在获取数据之后
        initView();
        mBackPic = roomInfo.getBackPic() + "";
        getMvpPresenter().requestTagAll();
        if (!AvRoomDataManager.get().isRoomOwner(
                String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()))) {
            managerLayout.setVisibility(View.GONE);
        } else {
            managerLayout.setVisibility(View.VISIBLE);
        }
        labelsView.setOnLabelClickListener(this);
    }

    /**
     * 隐藏虚拟键盘
     */
    public void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    private void initView() {
        nameEdit = (EditText) findViewById(R.id.name_edit);
        topicEdit = (EditText) findViewById(R.id.topic_edit);
        pwdEdit = (EditText) findViewById(R.id.pwd_edit);
        labelsView = (LabelsView) findViewById(R.id.labels_view);
        managerLayout = (TextView) findViewById(R.id.manager_layout);
        bgLayout = (TextView) findViewById(R.id.bg_layout);
        blackLayout = (TextView) findViewById(R.id.black_layout);
        mLabelLayout = (LinearLayout) findViewById(R.id.label_layout);
        rlRoomPlay = (TextView) findViewById(R.id.rl_room_tip);
        sbSetting = (SwitchButton) findViewById(R.id.sb_small_gift_effect);
        rideSetting = (SwitchButton) findViewById(R.id.sb_ride_effect);
        changeGiftEffect = roomInfo.getGiftEffectSwitch();
        sbSetting.setChecked(roomInfo.getGiftEffectSwitch() == 0);
        sbSetting.setOnCheckedChangeListener(this);
        rideGiftEffect = roomInfo.getGiftCardSwitch();
        rideSetting.setChecked(roomInfo.getGiftCardSwitch() == 0);
        rideSetting.setOnCheckedChangeListener(this);
        rlRoomPlay.setOnClickListener(this);
        managerLayout.setOnClickListener(this);
        blackLayout.setOnClickListener(this);
        bgLayout.setOnClickListener(this);

        if (roomInfo != null) {
            nameEdit.setText(roomInfo.getTitle());
            topicEdit.setText(roomInfo.getRoomDesc());
            pwdEdit.setText(roomInfo.getRoomPwd());
            selectLabel = roomInfo.getRoomTag();
        }
        if (selectLabel == null) {
            selectLabel = "";
        }
        AppToolBar toolBar = (AppToolBar) findViewById(R.id.toolbar);
        toolBar.setOnBackBtnListener(view -> finish());
        toolBar.setOnRightBtnClickListener(view -> save());
    }

    private void save() {
        String name;
        String desc;
        String pwd;
        String label;
        String backPic;
        desc = topicEdit.getText().toString();
        name = nameEdit.getText().toString();
        pwd = pwdEdit.getText().toString();
        label = selectLabel;
        backPic = mBackPic;
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null) {
            hideKeyboard(nameEdit);
            getDialogManager().showProgressDialog(this, "请稍后...");
            int id = roomInfo.tagId;
            if (mSelectTabInfo != null) {
                id = mSelectTabInfo.getId();
            }

            if (AvRoomDataManager.get().isRoomOwner()) {
                getMvpPresenter().updateRoomInfo(name, desc, pwd, label, id, backPic, changeGiftEffect, rideGiftEffect);
            } else if (AvRoomDataManager.get().isRoomAdmin()) {
                getMvpPresenter().updateByAdmin(roomInfo.getUid(), name, desc, pwd, label, id, backPic, changeGiftEffect, rideGiftEffect);
            }
        }
    }


    @Override
    public void onLabelClick(View label, String labelText, int position) {
        if (!ListUtils.isListEmpty(mTabInfoList)) {
            mSelectTabInfo = mTabInfoList.get(position);
        }
        selectLabel = labelText;
        labelsView.setSelects(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manager_layout:
                RoomManagerListActivity.start(this);
                break;
            //设置进入房间提示
            case R.id.rl_room_tip:
                RoomPlayTipActivity.start(this);
                break;
            case R.id.black_layout:
                RoomBlackListActivity.start(this);
                break;
            case R.id.bg_layout:
                if (roomInfo == null) {
                    return;
                }
                Intent intent = new Intent(this, RoomSelectBgActivity.class);
                intent.putExtra("backPic", roomInfo.getBackPic());
                startActivityForResult(intent, 2);
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 2 && data != null) {
            String selectIndex = data.getStringExtra("selectIndex");
            mBackPicUrl = data.getStringExtra("selectUrl");
            mBackPic = StringUtils.isEmpty(selectIndex) ? "0" : selectIndex;
        }
    }

    @Override
    public void onResultRequestTagAllSuccess(List<TabInfo> tabInfoList) {
        mTabInfoList = tabInfoList;
        if (ListUtils.isListEmpty(tabInfoList)) {
//            mLabelLayout.setVisibility(View.GONE);
            return;
        }
//        mLabelLayout.setVisibility(View.VISIBLE);

        labels = new ArrayList<>();
        for (TabInfo tabInfo : tabInfoList) {
            labels.add(tabInfo.getName());
        }

        labelsView.setLabels((ArrayList<String>) labels);
        if (roomInfo != null && !TextUtils.isEmpty(roomInfo.getRoomTag())) {
            if (labels.contains(roomInfo.getRoomTag())) {
                labelsView.setSelects(labels.indexOf(roomInfo.getRoomTag()));
            }
        }
    }

    @Override
    public void onResultRequestTagAllFail(String error) {
        toast(error);
    }

    @Override
    public void updateRoomInfoSuccess(RoomInfo roomInfo) {
        getDialogManager().dismissDialog();
        finish();
    }

    @Override
    public void updateRoomInfoFail(String error) {
        getDialogManager().dismissDialog();
        toast(error);
    }


    @CoreEvent(coreClientClass = IIMLoginClient.class)
    public void onKickedOut(StatusCode code) {
        finish();
    }

    @Override
    protected void onReceiveChatRoomEvent(RoomEvent roomEvent) {
        super.onReceiveChatRoomEvent(roomEvent);
        int event = roomEvent.getEvent();
        switch (event) {
            case RoomEvent.KICK_OUT_ROOM:
                finish();
                break;
            case RoomEvent.ROOM_MANAGER_REMOVE:
                long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
                if (!AvRoomDataManager.get().isRoomOwner(uid)) {
                    toast(R.string.remove_room_manager);
                    finish();
                }
            default:
                break;
        }
    }

    /**
     * 小礼物特效开关
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sb_small_gift_effect:
                if (!isChecked) {
                    changeGiftEffect = 1;
                } else {
                    changeGiftEffect = 0;
                }
                break;
            case R.id.sb_ride_effect:
                if (!isChecked) {
                    rideGiftEffect = 1;
                } else {
                    rideGiftEffect = 0;
                }
                break;
        }
    }
}
