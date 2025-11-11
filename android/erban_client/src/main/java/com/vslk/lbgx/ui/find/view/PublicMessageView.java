package com.vslk.lbgx.ui.find.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vslk.lbgx.room.avroom.other.ScrollSpeedLinearLayoutManger;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.ui.widget.itemdecotion.DividerItemDecoration;
import com.vslk.lbgx.ui.widget.marqueeview.Utils;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.vslk.lbgx.ui.widget.LevelView;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.PublicChatRoomAttachment;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_framework.im.IMReportRoute;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 直播间消息界面
 *
 * @author chenran
 * @date 2017/7/26
 */
public class PublicMessageView extends FrameLayout {
    private RecyclerView messageListView;
    private TextView tvBottomTip;
    private MessageAdapter mMessageAdapter;
    private List<ChatRoomMessage> chatRoomMessages;
    private List<ChatRoomMessage> tempMessages;
    private ScrollSpeedLinearLayoutManger layoutManger;
    private CompositeDisposable compositeDisposable;

    public PublicMessageView(Context context) {
        this(context, null);
    }

    public PublicMessageView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public PublicMessageView(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        init(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (compositeDisposable == null) {
            return;
        }
        Disposable crMsgDisposable = IMNetEaseManager.get().getChatRoomMsgFlowable()
                .subscribe(messages -> {
                    if (messages.size() == 0) return;
                    for (ChatRoomMessage chatRoomMessage : messages) {
                        if (checkNeedMsg(chatRoomMessage)) {
                            onCurrentRoomReceiveNewMsg(messages);
                        }
                    }
                });
        compositeDisposable.add(crMsgDisposable);
        compositeDisposable.add(IMNetEaseManager.get().getChatRoomEventObservable()
                .subscribe(roomEvent -> {
                    if (roomEvent == null ||
                            roomEvent.getEvent() != RoomEvent.RECEIVE_MSG) {
                        return;
                    }
                    ChatRoomMessage chatRoomMessage = roomEvent.getChatRoomMessage();
                    if (checkNeedMsg(chatRoomMessage)) {
                        tempMessages.clear();
                        tempMessages.add(chatRoomMessage);
                        onCurrentRoomReceiveNewMsg(tempMessages);
                    }
                }));
    }

    /**
     * 检查是否是公屏需要的消息
     */
    private boolean checkNeedMsg(ChatRoomMessage chatRoomMessage) {
        if (IMReportRoute.sendPublicMsgNotice.equalsIgnoreCase(chatRoomMessage.getRoute())) {
            IMCustomAttachment attachment = chatRoomMessage.getAttachment();
            return attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM;//公屏消息
        }
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    public void clear() {
        if (mMessageAdapter != null) {
            mMessageAdapter.getData().clear();
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    private void init(Context context) {
        compositeDisposable = new CompositeDisposable();
        // 内容区域
        layoutManger = new ScrollSpeedLinearLayoutManger(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.rightMargin = Utils.dip2px(context, 5);
        messageListView = new RecyclerView(context);
        messageListView.setLayoutParams(params);
//        messageListView.setOverScrollMode(OVER_SCROLL_NEVER);
        messageListView.setHorizontalScrollBarEnabled(false);
        addView(messageListView);
        messageListView.setLayoutManager(layoutManger);
        messageListView.addItemDecoration(
                new DividerItemDecoration(context, layoutManger.getOrientation(), 3, R.color.transparent));
        mMessageAdapter = new MessageAdapter();
        messageListView.setAdapter(mMessageAdapter);

        // 底部有新消息
        tvBottomTip = new TextView(context);
        LayoutParams params1 = new LayoutParams(
                Utils.dip2px(context, 100F), Utils.dip2px(context, 30));
        params1.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params1.bottomMargin = Utils.dip2px(context, 0);
        tvBottomTip.setBackgroundResource(R.drawable.bg_messge_view_bottom_tip);
        tvBottomTip.setGravity(Gravity.CENTER);
        tvBottomTip.setText(context.getString(R.string.message_view_bottom_tip));
        tvBottomTip.setTextColor(BasicConfig.INSTANCE.getAppContext().getResources().getColor(R.color.sq_text));
        tvBottomTip.setLayoutParams(params1);
        tvBottomTip.setVisibility(GONE);
        tvBottomTip.setOnClickListener(v -> {
            tvBottomTip.setVisibility(GONE);
            messageListView.scrollToPosition(mMessageAdapter.getItemCount() - 1);
        });
        addView(tvBottomTip);

        chatRoomMessages = new ArrayList<>();
        tempMessages = new ArrayList<>();

        messageListView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (needUpdateDataSet && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    needUpdateDataSet = false;
                    // 如果用户在滑动,并且显示的是tip,则需要跟新
                    mMessageAdapter.notifyDataSetChanged();
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (layoutManger.findLastCompletelyVisibleItemPosition() == mMessageAdapter.getItemCount() - 1) {
                        tvBottomTip.setVisibility(GONE);
                    }
                }
            }
        });

    }

    public void onCurrentRoomReceiveNewMsg(List<ChatRoomMessage> messages) {
        if (ListUtils.isListEmpty(messages)) return;
        if (chatRoomMessages.size() == 0) {
            mMessageAdapter.setNewData(chatRoomMessages);
        }
        showTipsOrScrollToBottom(messages);
    }

    private boolean needUpdateDataSet = false;

    private void showTipsOrScrollToBottom(List<ChatRoomMessage> messages) {
        // 最后一个item是否显示出来
        int lastCompletelyVisibleItemPosition = layoutManger.findLastCompletelyVisibleItemPosition();
        if (lastCompletelyVisibleItemPosition == RecyclerView.NO_POSITION) {
            tvBottomTip.setVisibility(GONE);
            chatRoomMessages.addAll(messages);
            mMessageAdapter.notifyDataSetChanged();
            messageListView.scrollToPosition(mMessageAdapter.getItemCount() - 1);
            return;
        }
        boolean needScroll = (lastCompletelyVisibleItemPosition == mMessageAdapter.getItemCount() - 1);
        chatRoomMessages.addAll(messages);
        if (needScroll) {
            needUpdateDataSet = false;
            tvBottomTip.setVisibility(GONE);
            mMessageAdapter.notifyDataSetChanged();
            messageListView.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
        } else {
            needUpdateDataSet = true;
            tvBottomTip.setVisibility(VISIBLE);
        }
    }

    public void release() {
    }

    private static class MessageAdapter extends BaseQuickAdapter<ChatRoomMessage, BaseViewHolder>
            implements OnClickListener {
        private String account;

        MessageAdapter() {
            super(R.layout.list_item_public_chatrrom_msg);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, ChatRoomMessage chatRoomMessage) {
            if (chatRoomMessage == null) return;
            ImageView userIcon = baseViewHolder.getView(R.id.iv_user_icon_chat_room);
            TextView userNick = baseViewHolder.getView(R.id.tv_user_nick_chat_room);
            TextView content = baseViewHolder.getView(R.id.tv_content_chat_room);
            ImageView ivOfficial = baseViewHolder.getView(R.id.iv_chat_hall_official);
            LevelView levelView = baseViewHolder.getView(R.id.level_chat_room);
            LinearLayout linearLayout = baseViewHolder.getView(R.id.ll_content_chat_room_bg);
            if (ivOfficial.getVisibility() == View.VISIBLE) {
                ivOfficial.setVisibility(View.GONE);
            }
            if (IMReportRoute.sendPublicMsgNotice.equalsIgnoreCase(chatRoomMessage.getRoute())) {
                IMCustomAttachment attachment = (IMCustomAttachment) chatRoomMessage.getAttachment();
                int experLevel = -1;
                LogUtil.d("chatRoomMessage", 2 + "");
                if (chatRoomMessage.getAttachment() instanceof IMCustomAttachment) {
                    experLevel = attachment.getExperLevel();
                    LogUtil.d("chatRoomMessage", experLevel + ";;;;");
                    if (experLevel > 0 && !(chatRoomMessage.getContent() + "").contains("海角星球工作人员")) {
                        levelView.setVisibility(VISIBLE);
                        levelView.setExperLevel(experLevel);
                    } else {
                        levelView.setVisibility(GONE);
                    }
                }
                if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM) {
                    baseViewHolder.getView(R.id.rl_chate_hall_msg).setVisibility(VISIBLE);
                    setPublicChatRoom(chatRoomMessage, content, levelView, userNick, userIcon, linearLayout);
                } else {
                    baseViewHolder.getView(R.id.rl_chate_hall_msg).setVisibility(GONE);
                }

            } else {
                baseViewHolder.getView(R.id.rl_chate_hall_msg).setVisibility(GONE);
//                baseViewHolder.getView(R.id.line_chat_room).setVisibility(GONE);
            }
        }

        private void setPublicChatRoom(ChatRoomMessage chatRoomMessage, TextView tvContent, LevelView levelView,
                                       TextView userNick, ImageView userIcon, LinearLayout linearLayout) {
            if (chatRoomMessage.getAttachment() instanceof PublicChatRoomAttachment) {
                PublicChatRoomAttachment attachment = (PublicChatRoomAttachment) chatRoomMessage.getAttachment();
                userIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoActivity.start(mContext, attachment.getUid());
                    }
                });
                linearLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoActivity.start(mContext, attachment.getUid());
                    }
                });
                if (attachment.getExperLevel() > 0 || attachment.getCharmLevel() > 0) {
                    levelView.setVisibility(VISIBLE);
                    levelView.setExperLevel(attachment.getExperLevel());
                    levelView.setCharmLevel(attachment.getCharmLevel());
                } else {
                    levelView.setVisibility(GONE);
                }
                ImageLoadUtils.loadCircleImage(mContext, attachment.getAvatar(), userIcon,
                        R.drawable.ic_default_avatar);
                userNick.setText(attachment.getNick());
                tvContent.setText(attachment.getMsg());
                tvContent.setTextColor(Color.parseColor(StringUtils.isNotEmpty(attachment.getTxtColor()) ? attachment.getTxtColor() : "#316AFF"));
            }
        }


        // 新版IM 带有通知类型消息，需要单独处理
        @Override
        public void onClick(View v) {
//            IMChatRoomMember chatRoomMessage = (IMChatRoomMember) v.getTag();
//            if (chatRoomMessage.getMsgType() != MsgTypeEnum.tip) {
//                if (chatRoomMessage.getMsgType() == MsgTypeEnum.text) {
//                    account = chatRoomMessage.getFromAccount();
//                } else if (chatRoomMessage.getMsgType() == MsgTypeEnum.notification) {
//                    account = chatRoomMessage.getFromAccount();
//                } else if (chatRoomMessage.getMsgType() == MsgTypeEnum.custom) {
//                    IMCustomAttachment attachment = (IMCustomAttachment) chatRoomMessage.getAttachment();
//                    if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT) {
//                        GiftAttachment giftAttachment = (GiftAttachment) attachment;
//                        if (giftAttachment != null) {
//                            GiftReceiveInfo giftRecieveInfo = giftAttachment.getGiftRecieveInfo();
//                            if (giftRecieveInfo != null) {
//                                account = giftRecieveInfo.getUid() + "";
//                            }
//
//                        }
//
//
//                    } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT) {
//                        MultiGiftAttachment giftAttachment = (MultiGiftAttachment) attachment;
//                        account = giftAttachment.getMultiGiftRecieveInfo().getUid() + "";
//                    } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_ROOM_TIP) {
//                        account = ((RoomTipAttachment) attachment).getUid() + "";
//                    }
//                }
//                if (TextUtils.isEmpty(account)) return;
//                final List<ButtonItem> buttonItems = new ArrayList<>();
//                List<ButtonItem> items = ButtonItemFactory.createAllRoomPublicScreenButtonItems(mContext, account);
//                if (items == null) return;
//                buttonItems.addAll(items);
//                ((BaseMvpActivity) mContext).getDialogManager().showCommonPopupDialog(buttonItems, "取消");
//            }
        }
    }

    public List<ChatRoomMessage> getChatRoomMessages() {
        return chatRoomMessages;
    }

    public void setChatRoomMessages(List<ChatRoomMessage> chatRoomMessages) {
        this.chatRoomMessages = chatRoomMessages;
    }
}
