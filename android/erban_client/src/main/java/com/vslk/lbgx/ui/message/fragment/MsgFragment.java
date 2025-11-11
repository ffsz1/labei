package com.vslk.lbgx.ui.message.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.uikit.NimUIKit;
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
import com.tongdaxing.xchat_core.im.message.IIMMessageCore;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfoV2;
import com.tongdaxing.xchat_core.user.IUserClient;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.vslk.lbgx.base.fragment.BaseLazyFragment;
import com.vslk.lbgx.ui.home.TabLayoutAnimUtils;
import com.vslk.lbgx.ui.home.adpater.HomeVpAdapter;
import com.vslk.lbgx.ui.message.activity.AttentionListActivity;
import com.vslk.lbgx.ui.message.activity.BlackFriendActivity;
import com.vslk.lbgx.ui.message.activity.FansListActivity;
import com.vslk.lbgx.ui.message.activity.FriendListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 主页消息界面  </p>
 *
 * @author Administrator
 * @date 2017/11/14
 */
public class MsgFragment extends BaseLazyFragment implements RecentContactsCallback {

    public static final String TAG = "MsgFragment";


    private RecentContactsFragment recentContactsFragment;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitle = new ArrayList<>();

    private ViewPager mViewPager;
    private TextView tv_ignore_unread;
    private TabLayout msgTab;
    private HomeVpAdapter fmPagerAdapter;
    private TabLayoutAnimUtils tabLayoutAnimUtils;

    @Override
    public void onFindViews() {

        msgTab = mView.findViewById(R.id.msg_tab);
        mViewPager = ((ViewPager) mView.findViewById(R.id.vp_container));
        tv_ignore_unread = ((TextView) mView.findViewById(R.id.tv_ignore_unread));
        tv_ignore_unread.setOnClickListener(this);
    }

    @Override
    public void onSetListener() {
    }


    @Override
    public void initiate() {

    }

    @Override
    protected void onLazyLoadData() {
        recentContactsFragment = new RecentContactsFragment();
        FriendListFragment friendListFragment = new FriendListFragment();
        AttentionFragment attentionFragment = new AttentionFragment();
        FansListFragment fansListFragment = new FansListFragment();
        recentContactsFragment.setCallback(this);
        mFragments.add(recentContactsFragment);
        mFragments.add(friendListFragment);
        mFragments.add(attentionFragment);
        mFragments.add(fansListFragment);
        mTitle.add("消息");
        mTitle.add("好友");
        mTitle.add("关注");
        mTitle.add("粉丝");
        fmPagerAdapter = new HomeVpAdapter(mFragments, mTitle, getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(mFragments.size());
        msgTab.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(fmPagerAdapter);
        tabLayoutAnimUtils = new TabLayoutAnimUtils(getContext(), msgTab);
        msgTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mTitle.size() > 0) {
                    tabLayoutAnimUtils.changeTabSelectMsg(tab, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (mTitle.size() > 0) tabLayoutAnimUtils.changeTabNormalMsg(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        tabLayoutAnimUtils.setTitleList(mTitle, true);
        tabLayoutAnimUtils.changeTabIndicatorWidth(msgTab, 20);

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_msg_new;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ignore_unread:
                CoreManager.getCore(IIMMessageCore.class).clearAllUnreadMsg();
                SingleToastUtil.showToast("未读消息已清除!");
                break;
            case R.id.tv_friend:
                FriendListActivity.start(getActivity());
                break;
            case R.id.tv_attention:
                AttentionListActivity.start(getActivity());
                break;
            case R.id.tv_fans:
                FansListActivity.start(getActivity());
                break;
            case R.id.tv_black:
                BlackFriendActivity.start(getActivity());
                break;
            default:
                break;
        }
    }

    @CoreEvent(coreClientClass = IUserClient.class)
    public void onCurrentUserInfoUpdate(UserInfo userInfo) {
        recentContactsFragment.requestMessages(true);
    }

    @Override
    public void onRecentContactsLoaded() {

    }

    @Override
    public void onUnreadCountChange(int unreadCount) {

    }

    @Override
    public void onItemClick(RecentContact recent) {
        if (recent.getSessionType() == SessionTypeEnum.Team) {
            NimUIKit.startTeamSession(getActivity(), recent.getContactId());
        } else if (recent.getSessionType() == SessionTypeEnum.P2P) {
            NimUIKit.startP2PSession(getActivity(), recent.getContactId());
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

}