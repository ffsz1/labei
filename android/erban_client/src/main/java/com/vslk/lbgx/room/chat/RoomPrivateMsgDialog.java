package com.vslk.lbgx.room.chat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vslk.lbgx.base.adapter.BaseIndicatorAdapter;
import com.vslk.lbgx.base.fragment.BaseDialogFragment;
import com.vslk.lbgx.ui.find.fragment.SquareFragment;
import com.vslk.lbgx.ui.home.adpater.CommonMagicIndicatorAdapter;
import com.netease.nim.uikit.recent.RecentContactsCallback;
import com.netease.nim.uikit.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.im.custom.bean.CustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.NoticeAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.RedPacketAttachment;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfoV2;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 房间私聊
 */
public class RoomPrivateMsgDialog extends BaseDialogFragment implements View.OnClickListener, CommonMagicIndicatorAdapter.OnItemSelectListener {
    private RecentContactsFragment recentContactsFragment;
    private ViewPager viewPager;
    private RadioGroup rgTab;
    private RadioButton rbMsg;
    private RadioButton rbSquare;
    private ImageView ivLeft;
    private ImageView ivRight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoreManager.addClient(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        // setup window and width
        View view = inflater.inflate(R.layout.dialog_room_private_msg, window.findViewById(android.R.id.content), false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        setCancelable(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.view_pager);
        rgTab = view.findViewById(R.id.rg_tab);
        rbMsg = view.findViewById(R.id.rb_msg);
        rbSquare = view.findViewById(R.id.rb_square);
        ivLeft = view.findViewById(R.id.iv_left);
        ivRight = view.findViewById(R.id.iv_right);
        recentContactsFragment = new RecentContactsFragment();
        List<Fragment> mTabs = new ArrayList<>(2);
        recentContactsFragment = new RecentContactsFragment();
        mTabs.add(recentContactsFragment);
        SquareFragment chatHallFragment = new SquareFragment();
        mTabs.add(chatHallFragment);
        viewPager.setAdapter(new BaseIndicatorAdapter(getChildFragmentManager(), mTabs));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                stateTab(position);
                rgTab.check(position == 0 ? R.id.rb_msg : R.id.rb_square);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
        recentContactsFragment.setCallback(new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {

            }

            @Override
            public void onUnreadCountChange(int unreadCount) {

            }

            @Override
            public void onItemClick(RecentContact recent) {
                if (recent.getSessionType() == SessionTypeEnum.P2P) {
                    RoomPrivateChatDialog chat = RoomPrivateChatDialog.newInstance(recent.getContactId());
                    chat.show(getFragmentManager(), null);
                    dismiss();
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recent, MsgAttachment attachment) {
                if (attachment instanceof CustomAttachment) {
                    CustomAttachment customAttachment = (CustomAttachment) attachment;
                    if (customAttachment.getFirst() == CustomAttachment.CUSTOM_MSG_HEADER_TYPE_OPEN_ROOM_NOTI) {
                        return "您关注的TA上线啦，快去围观吧~~~";
                    } else if (customAttachment.getFirst() == CustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT) {
                        return "[礼物]";
                    } else if (customAttachment.getFirst() == CustomAttachment.CUSTOM_MSG_HEADER_TYPE_NOTICE) {
                        NoticeAttachment noticeAttachment = (NoticeAttachment) attachment;
                        return noticeAttachment.getTitle();
                    } else if (customAttachment.getFirst() == CustomAttachment.CUSTOM_MSG_HEADER_TYPE_PACKET) {
                        RedPacketAttachment redPacketAttachment = (RedPacketAttachment) attachment;
                        RedPacketInfoV2 redPacketInfo = redPacketAttachment.getRedPacketInfo();
                        if (redPacketInfo == null) {
                            return "您收到一个红包哦!";
                        }
                        return "您收到一个" + redPacketInfo.getPacketName() + "红包哦!";
                    } else if (customAttachment.getFirst() == CustomAttachment.CUSTOM_MSG_HEADER_TYPE_LOTTERY) {
                        return "恭喜您，获得抽奖机会";
                    } else if (customAttachment.getFirst() == CustomAttachment.CUSTOM_MSG_SHARE_FANS) {
                        return "[房间邀请]";
                    }
                } else if (attachment instanceof AudioAttachment) {
                    return "[语音]";
                }
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }
        });
        rgTab.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_msg:
                    stateTab(0);
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.rb_square:
                    stateTab(1);
                    viewPager.setCurrentItem(1);
                    break;
            }
        });
        rgTab.check(R.id.rb_msg);
        stateTab(0);
    }


    private void stateTab(int index) {
        if (index == 0) {
            rbMsg.setTextSize(20f);
            rbSquare.setTextSize(14f);
            ivLeft.setVisibility(View.VISIBLE);
            ivRight.setVisibility(View.GONE);
        } else {
            rbMsg.setTextSize(14f);
            rbSquare.setTextSize(20f);
            ivLeft.setVisibility(View.GONE);
            ivRight.setVisibility(View.VISIBLE);
        }
    }


    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdate(UserInfo userInfo) {
        recentContactsFragment.requestMessages(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CoreManager.removeClient(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onItemSelect(int position) {
        viewPager.setCurrentItem(position);
    }
}
