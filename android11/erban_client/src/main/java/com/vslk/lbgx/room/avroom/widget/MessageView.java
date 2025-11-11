package com.vslk.lbgx.room.avroom.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.attachmsg.RoomQueueMsgAttachment;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.GiftReceiveInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.gift.MultiGiftReceiveInfo;
import com.tongdaxing.xchat_core.im.custom.bean.AuctionAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.BurstGiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.CustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.FaceAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.FingerGuessingGameAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.GiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.LotteryBoxAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.MultiGiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.PkCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.RoomMatchAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.RoomRuleAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.RoomTipAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.ChangeRoomNameAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.SendCallGiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.WanFaAttachment;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.manager.IMNetEaseManager;
import com.tongdaxing.xchat_core.manager.RoomEvent;
import com.tongdaxing.xchat_core.pk.bean.PkVoteInfo;
import com.tongdaxing.xchat_core.room.bean.PkFingerGuessingGameInfo;
import com.tongdaxing.xchat_core.room.face.FaceInfo;
import com.tongdaxing.xchat_core.room.face.FaceReceiveInfo;
import com.tongdaxing.xchat_core.room.face.IFaceCore;
import com.tongdaxing.xchat_core.room.model.FingerGuessingGameModel;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.im.IMReportRoute;
import com.tongdaxing.xchat_framework.util.TextViewDrawableUtils;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.RichTextUtil;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.avroom.other.ButtonItemFactory;
import com.vslk.lbgx.room.avroom.other.ScrollSpeedLinearLayoutManger;
import com.vslk.lbgx.room.avroom.widget.dialog.FailureFingerGuessingGameDialog;
import com.vslk.lbgx.room.avroom.widget.dialog.PkFingerGuessingGameDialog;
import com.vslk.lbgx.room.match.RoomMatchUtil;
import com.vslk.lbgx.ui.widget.LevelView;
import com.vslk.lbgx.ui.widget.UrlImageSpan;
import com.vslk.lbgx.ui.widget.itemdecotion.DividerItemDecoration;
import com.vslk.lbgx.ui.widget.magicindicator.buildins.UIUtil;
import com.vslk.lbgx.ui.widget.marqueeview.Utils;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.zhy.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.disposables.CompositeDisposable;

/**
 * 直播间消息界面
 *
 * @author chenran
 * @date 2017/7/26
 */
public class MessageView extends FrameLayout {

    private RecyclerView messageListView;
    private TextView tvBottomTip;
    public static MessageAdapter mMessageAdapter;
    private List<ChatRoomMessage> tempMessages;
    private ScrollSpeedLinearLayoutManger layoutManger;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public int selectFlag = 0;
    private ArrayList<ChatRoomMessage> messagesAll;


    public void setSelectFlag(int selectFlag) {
        this.selectFlag = selectFlag;
        selectMsgByList(messagesAll);
    }

    public void updateRoomTip(String playInfo) {
        if (mMessageAdapter.getData().size() > 1 && playInfo != null && !playInfo.isEmpty()) {
            IMCustomAttachment attachment = mMessageAdapter.getData().get(1).getAttachment();
            if (attachment instanceof RoomRuleAttachment) {
                ((RoomRuleAttachment) attachment).setRule(playInfo);
                mMessageAdapter.getData().get(1).setAttachment(attachment);
                mMessageAdapter.notifyItemChanged(1);
            }
        }
    }

    public MessageView(Context context) {
        this(context, null);
    }

    public MessageView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public MessageView(Context context, AttributeSet attr, int i) {
        super(context, attr, i);
        init(context);
    }

    public void refreshAdapter() {
        if (mMessageAdapter != null) {
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (compositeDisposable == null) {
            return;
        }
        compositeDisposable.add(IMNetEaseManager.get().getChatRoomMsgFlowable().subscribe(messages -> {
            if (messages.size() == 0) {
                return;
            }
            ChatRoomMessage chatRoomMessage = messages.get(0);
            messagesAll.addAll(messages);
            if (checkNoNeedMsg(chatRoomMessage)) {
                return;
            }
            onCurrentRoomReceiveNewMsg(messages);
        }));
        compositeDisposable.add(IMNetEaseManager.get().getChatRoomEventObservable().subscribe(roomEvent -> {
            if (roomEvent == null ||
                    roomEvent.getEvent() != RoomEvent.RECEIVE_MSG) {
                return;
            }
            ChatRoomMessage chatRoomMessage = roomEvent.getChatRoomMessage();

            if (checkNoNeedMsg(chatRoomMessage)) {
                return;
            }

            tempMessages.clear();
            tempMessages.add(chatRoomMessage);
            onCurrentRoomReceiveNewMsg(tempMessages);
        }));
    }

    /**
     * 检查是否是公屏需要的消息，送礼物和谁来了的消息不加入公屏
     */
    private boolean checkNoNeedMsg(ChatRoomMessage chatRoomMessage) {
        if (IMReportRoute.sendMessageReport.equalsIgnoreCase(chatRoomMessage.getRoute())) {
            IMCustomAttachment attachment = chatRoomMessage.getAttachment();
            //增加second判断避免将房间分享消息屏蔽掉（问题：分享成功后没显示分享消息，最小化后重新进入又出现）
            if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_ROOM_TIP
                    && attachment.getSecond() != IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_ROOM_TIP_SHARE_ROOM) {
                return true;
            }
            return attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM;
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
            messagesAll.clear();
        }
    }

    private void init(Context context) {
        messagesAll = new ArrayList<>();
        // 内容区域
        layoutManger = new ScrollSpeedLinearLayoutManger(context);
        FrameLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.rightMargin = Utils.dip2px(context, 30);
        messageListView = new RecyclerView(context);
        messageListView.setLayoutParams(params);
        messageListView.setOverScrollMode(OVER_SCROLL_NEVER);
        messageListView.setHorizontalScrollBarEnabled(false);
        addView(messageListView);
        messageListView.setLayoutManager(layoutManger);
        messageListView.addItemDecoration(
                new DividerItemDecoration(context, layoutManger.getOrientation(), 3, R.color.transparent));
        mMessageAdapter = new MessageAdapter();
        messageListView.setAdapter(mMessageAdapter);

        // 底部有新消息
        tvBottomTip = new TextView(context);
        FrameLayout.LayoutParams params1 = new LayoutParams(
                Utils.dip2px(context, 140), Utils.dip2px(context, 25));
        params1.gravity = Gravity.BOTTOM | Gravity.START;
        params1.bottomMargin = Utils.dip2px(context, 0);
        params1.leftMargin = Utils.dip2px(context, 15);
        tvBottomTip.setBackgroundResource(R.drawable.bg_btn_login_selected);
        tvBottomTip.setCompoundDrawablesRelative(TextViewDrawableUtils.getCompoundDrawables(context, R.drawable.ic_bottom_arrow_new_msg), null, null, null);
        tvBottomTip.setGravity(Gravity.CENTER);
        tvBottomTip.setText(context.getString(R.string.message_view_bottom_tip));
        tvBottomTip.setPadding(Utils.dip2px(context, 15), 0, Utils.dip2px(context, 15), 0);
        tvBottomTip.setTextColor(BasicConfig.INSTANCE.getAppContext().getResources().getColor(R.color.white));
        tvBottomTip.setLayoutParams(params1);
        tvBottomTip.setVisibility(GONE);
        tvBottomTip.setOnClickListener(v -> {
            tvBottomTip.setVisibility(GONE);
            messageListView.scrollToPosition(mMessageAdapter.getItemCount() - 1);
        });
        addView(tvBottomTip);
        tempMessages = new CopyOnWriteArrayList<>();
        initData();

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


    private void initData() {
        List<ChatRoomMessage> messages = IMNetEaseManager.get().messages;
        mMessageAdapter.setNewData(new CopyOnWriteArrayList<>());
        if (!ListUtils.isListEmpty(messages)) {
            messages = msgFilter(messages);
            mMessageAdapter.addData(messages);
            messageListView.scrollToPosition(messages.size() - 1);
        }
    }

    public void onCurrentRoomReceiveNewMsg(List<ChatRoomMessage> messages) {
        if (ListUtils.isListEmpty(messages)) return;
//        if (selectFlag != 0) {
//            messagesAll.addAll(messages);
//        }
        showTipsOrScrollToBottom(messages);
    }

    private boolean needUpdateDataSet = false;

    private List<ChatRoomMessage> msgFilter(List<ChatRoomMessage> chatRoomMessages) {
        List<ChatRoomMessage> messages = new CopyOnWriteArrayList<>();
        for (ChatRoomMessage message : chatRoomMessages) {
            if (message.getAttachment() instanceof IMCustomAttachment) {
                if (message.getAttachment().getFirst() == IMCustomAttachment.CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM) {
                    continue;
                }
                if ((message.getAttachment().getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_ROOM_TIP && message.getAttachment().getSecond() != IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_ROOM_TIP_SHARE_ROOM) ||
                        (message.getAttachment().getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_ROOM_TIP && message.getAttachment().getSecond() != IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_ROOM_TIP_ATTENTION_ROOM_OWNER)) {
                    continue;
                }
                if (message.getAttachment().getSecond() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_SECOND_ADD) {//pk投票消息
                    continue;
                }


            }
            if (selectFlag != 0) {
                if (message.getAttachment() instanceof IMCustomAttachment) {
                    if (selectFlag != 2 && (message.getAttachment().getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT || message.getAttachment().getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT)) {
                        continue;
                    }
                    if (selectFlag != 3 && message.getAttachment().getFirst() == IMCustomAttachment.CUSTOM_MSG_ROOM_CALL_GIFT) {
                        continue;
                    }
                } else if (selectFlag != 1 && message.getAttachment() instanceof RoomRuleAttachment) {
                    continue;
                } else if (selectFlag != 1 && IMReportRoute.sendTextReport.equalsIgnoreCase(message.getRoute())//聊天消息（普通文本消息）
                        || IMReportRoute.ChatRoomTip.equalsIgnoreCase(message.getRoute())) {//系统通知)
                    continue;
                }
            }
            messages.add(message);
        }
        return messages;
    }


    private void selectMsgByList(List<ChatRoomMessage> messages) {
        if (selectFlag == 0) {
            mMessageAdapter.setNewData(msgFilter(messages));
        } else {
            if (selectFlag == 1) {
                messagesAll.clear();
                messagesAll.addAll(mMessageAdapter.getData());
            }
            ArrayList<ChatRoomMessage> newMsg = new ArrayList<>();
            for (ChatRoomMessage message : messages) {
                switch (selectFlag) {
                    case 1:
                        if (IMReportRoute.sendTextReport.equalsIgnoreCase(message.getRoute())//聊天消息（普通文本消息）
                                || IMReportRoute.ChatRoomTip.equalsIgnoreCase(message.getRoute())) {//系统通知
                            newMsg.add(message);
                        }
                        if (message.getAttachment() instanceof RoomRuleAttachment) {//进房提示
                            newMsg.add(message);
                        }
                        break;
                    case 2:
                        if (message.getAttachment() instanceof IMCustomAttachment) {
                            if ((message.getAttachment().getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT || message.getAttachment().getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT)) {//单人送礼或者多人送礼
                                newMsg.add(message);
                            }
                        }
                        break;
                    case 3:
                        if (message.getAttachment() instanceof IMCustomAttachment) {
                            if (message.getAttachment().getFirst() == IMCustomAttachment.CUSTOM_MSG_ROOM_CALL_GIFT) {//打call送礼
                                newMsg.add(message);
                            }
                        }
                        break;
                }
            }
            mMessageAdapter.setNewData(newMsg);
        }
    }


    private void showTipsOrScrollToBottom(List<ChatRoomMessage> messages) {
        // 最后一个item是否显示出来
        int lastCompletelyVisibleItemPosition = layoutManger.findLastCompletelyVisibleItemPosition();
        if (lastCompletelyVisibleItemPosition == RecyclerView.NO_POSITION) {
            tvBottomTip.setVisibility(GONE);
            messages = msgFilter(messages);
            mMessageAdapter.addData(messages);
            messageListView.scrollToPosition(mMessageAdapter.getItemCount() - 1);
            return;
        }
        boolean needScroll = (lastCompletelyVisibleItemPosition == mMessageAdapter.getItemCount() - 1);
        messages = msgFilter(messages);
        mMessageAdapter.addData(messages);
        if (needScroll) {
            needUpdateDataSet = false;
            tvBottomTip.setVisibility(GONE);
            messageListView.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
        } else {
            needUpdateDataSet = true;
            tvBottomTip.setVisibility(VISIBLE);
        }
    }

    public void scrollToBottom() {
        messageListView.scrollToPosition(mMessageAdapter.getItemCount() - 1);
    }

    public void release() {
    }

    public static class MessageAdapter extends BaseQuickAdapter<ChatRoomMessage, BaseViewHolder> implements OnClickListener {
        public MessageAdapter() {
            super(R.layout.list_item_chatrrom_msg);
        }

        @Override
        public void addData(@NonNull Collection<? extends ChatRoomMessage> newData) {
            //原来没数据，就代表本次add导致所有数据产生了变更
            boolean isAllDataChanged = mData.size() == 0;
            mData.addAll(newData);
            try {
                int size = mData.size();
                //超出最大消息数限制
                if (size > Constants.MESSAGE_COUNT_LOCAL_LIMIT) {
                    //移除最前面的1/3消息，移除时new，避免ConcurrentModificationException
                    List<ChatRoomMessage> removeData = new CopyOnWriteArrayList<>(mData.subList(0, Constants.MESSAGE_COUNT_LOCAL_LIMIT / 3));
                    mData.removeAll(removeData);
                    //移除了前面的数据，那么所有的数据也是产生了变更
                    isAllDataChanged = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.w("messageView", " add data limit fail = " + e.getMessage());
            }
            if (isAllDataChanged) {
                notifyDataSetChanged();
            } else {
                notifyItemRangeInserted(mData.size() - newData.size() + getHeaderLayoutCount(), newData.size());
            }
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, ChatRoomMessage chatRoomMessage) {
            if (chatRoomMessage == null) {
                return;
            }
            //初始化布局  默认 显示基本消息容器 msgContainer：默认是带有等级和萌新标签的
            TextView tvContent = baseViewHolder.getView(R.id.tv_content);
            tvContent.setTextColor(mContext.getResources().getColor(R.color.white));
            tvContent.setBackgroundResource(R.drawable.shape_2b050505_10dp);
            tvContent.setOnClickListener(this);
            tvContent.setTag(chatRoomMessage);
            LinearLayout faceContainer = baseViewHolder.getView(R.id.face_container);
            faceContainer.setVisibility(View.GONE);
            faceContainer.removeAllViews();
            tvContent.setVisibility(VISIBLE);
            //提示消息  - 不显示等级
            if (IMReportRoute.ChatRoomTip.equalsIgnoreCase(chatRoomMessage.getRoute())) {
                tvContent.setTextColor(mContext.getResources().getColor(R.color.white));
                tvContent.setText(chatRoomMessage.getContent());
            } else if (IMReportRoute.sendTextReport.equalsIgnoreCase(chatRoomMessage.getRoute())) {//普通文本消息
                setMsgText(chatRoomMessage, tvContent);
            } else if (IMReportRoute.chatRoomMemberIn.equalsIgnoreCase(chatRoomMessage.getRoute())) {//进入房间消息提示
                setMsgNotification(chatRoomMessage, tvContent);
            } else if (IMReportRoute.sendMessageReport.equalsIgnoreCase(chatRoomMessage.getRoute())) {
                //自定义消息默认不显示等级
                IMCustomAttachment attachment = chatRoomMessage.getAttachment();
                //没有等级
                if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_ROOM_TIP) {
                    setMsgRoomTip(tvContent, (RoomTipAttachment) attachment);
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT) {//单人送礼
                    setMsgHeaderGift(chatRoomMessage, tvContent, (GiftAttachment) attachment);
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT) {//全麦送礼
                    setMsgMultiGift(chatRoomMessage, tvContent, (MultiGiftAttachment) attachment);
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_FACE) {
                    int experLevel = -1;
                    if (chatRoomMessage.getAttachment() instanceof IMCustomAttachment) {
                        experLevel = attachment.getExperLevel();
                    }
                    boolean newUserContent = false;
                    if (chatRoomMessage.getImChatRoomMember() != null) {
                        newUserContent = chatRoomMessage.getImChatRoomMember().isIs_new_user();
                    }
                    setMsgFace(faceContainer, tvContent, (FaceAttachment) attachment, experLevel, newUserContent);
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM) {
                    tvContent.setVisibility(View.GONE);
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_LOTTERY_BOX) {//捡海螺消息
                    setLotteryInfo(tvContent, (LotteryBoxAttachment) attachment);
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_WAN_FA) {//玩法
                    setWanFaInfo(tvContent, (WanFaAttachment) attachment);
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_CHANGE_ROOM_NAME) {//房间名字
                    setRoomNameInfo(tvContent, (ChangeRoomNameAttachment) attachment);
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_ROOM_CALL_GIFT) {//打call送礼
                    setSendCallGift(tvContent, (SendCallGiftAttachment) attachment);
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH) {//龙珠速配
                    int experLevel = -1;
                    if (chatRoomMessage.getAttachment() instanceof IMCustomAttachment) {
                        experLevel = attachment.getExperLevel();
                    }
                    boolean newUserContent = false;
                    if (chatRoomMessage.getImChatRoomMember() != null) {
                        newUserContent = chatRoomMessage.getImChatRoomMember().isIs_new_user();
                    }
                    setRoomMatchMsg(tvContent, faceContainer, (RoomMatchAttachment) attachment, experLevel, newUserContent);
                    //Pk消息  // 为兼容新的UI
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_FIRST) {
                    setPkMsg((PkCustomAttachment) attachment, tvContent);
                    //房间规则
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_TYPE_RULE_FIRST) {
                    if (attachment instanceof RoomRuleAttachment) {
                        tvContent.setTextColor(mContext.getResources().getColor(R.color.room_blue_color));
                        tvContent.setText(((RoomRuleAttachment) attachment).getRule());
                    }
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_TYPE_BURST_GIFT) {
                    setBurstGift(tvContent, (BurstGiftAttachment) attachment);
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_QUEUE) {
                    int second = attachment.getSecond();
                    RoomQueueMsgAttachment roomQueueMsgAttachment = (RoomQueueMsgAttachment) attachment;
                    setSystemMsg(second, roomQueueMsgAttachment, tvContent);
                } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_FINGER_GUESSING_GAME_FIRST) {
                    if (attachment.getSecond() == IMCustomAttachment.CUSTOM_MSG_FINGER_GUESSING_GAME_SECOND) {//发起猜拳
                        setStartFingerGuessingGame(faceContainer, tvContent, attachment);
                    } else if (attachment.getSecond() == IMCustomAttachment.CUSTOM_MSG_FINGER_GUESSING_GAME_WIN_SECOND) {//赢了
                        setWinFingerGuessingGame(faceContainer, tvContent, attachment);
                    } else if (attachment.getSecond() == IMCustomAttachment.CUSTOM_MSG_FINGER_GUESSING_GAME__DEUCESECOND) {//平局
                        setDeuceFingerGuessingGame(faceContainer, tvContent, attachment);
                    }
                }
            }
        }//新增显示在公屏中的自定义消息避免对msgContainer显示隐藏，这里没有做多布局处理，可能出现出现消息列表的重用bug

        private void setDeuceFingerGuessingGame(LinearLayout faceContainer, TextView tvContent, IMCustomAttachment attachment) {
            FingerGuessingGameAttachment attachment1 = (FingerGuessingGameAttachment) attachment;
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_start_finger_guessing_game, faceContainer);
            LevelView levelView = view.findViewById(R.id.level_view);
            TextView tvState = view.findViewById(R.id.tv_state);
            tvState.setText("平了 ");
            TextView tvOppositeName = view.findViewById(R.id.tv_opposite_name);
            TextView tvCount = view.findViewById(R.id.tv_count);
            tvOppositeName.setText(attachment1.getOpponentNick());
            ImageView ivUserHead = view.findViewById(R.id.iv_user_head);
            ImageView ivGift = view.findViewById(R.id.iv_gift);
            TextView tvName = view.findViewById(R.id.tv_name);
            TextView tvStartPk = view.findViewById(R.id.tv_start_pk);
            tvContent.setVisibility(GONE);
            tvCount.setVisibility(GONE);
            faceContainer.setVisibility(VISIBLE);
            ivGift.setVisibility(GONE);
            tvOppositeName.setVisibility(GONE);
            tvStartPk.setVisibility(GONE);

            levelView.setExperLevel(attachment1.getExperienceLevel());
            ImageLoadUtils.loadCircleImage(mContext, attachment1.getAvatar(), ivUserHead, R.drawable.ic_default_avatar);
            tvName.setText(subName(attachment1));
            ImageLoadUtils.loadCircleImage(mContext, attachment1.getGiftUrl(), ivGift, R.drawable.ic_default_avatar);
        }

        private void setWinFingerGuessingGame(LinearLayout faceContainer, TextView tvContent, IMCustomAttachment attachment) {
            FingerGuessingGameAttachment attachment1 = (FingerGuessingGameAttachment) attachment;
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_start_finger_guessing_game, faceContainer);
            LevelView levelView = view.findViewById(R.id.level_view);
            TextView tvState = view.findViewById(R.id.tv_state);
            tvState.setText("赢了 ");
            TextView tvOppositeName = view.findViewById(R.id.tv_opposite_name);
            TextView tvCount = view.findViewById(R.id.tv_count);
            tvOppositeName.setText(attachment1.getOpponentNick());
            ImageView ivUserHead = view.findViewById(R.id.iv_user_head);
            ImageView ivGift = view.findViewById(R.id.iv_gift);
            TextView tvName = view.findViewById(R.id.tv_name);
            TextView tvStartPk = view.findViewById(R.id.tv_start_pk);
            tvContent.setVisibility(GONE);
            tvCount.setText("X " + attachment1.getGiftNum());
            tvCount.setVisibility(VISIBLE);
            faceContainer.setVisibility(VISIBLE);
            tvOppositeName.setVisibility(VISIBLE);
            tvStartPk.setVisibility(GONE);

            levelView.setExperLevel(attachment1.getExperienceLevel());
            ImageLoadUtils.loadCircleImage(mContext, attachment1.getAvatar(), ivUserHead, R.drawable.ic_default_avatar);
            tvName.setText(subName(attachment1));
            ImageLoadUtils.loadCircleImage(mContext, attachment1.getGiftUrl(), ivGift, R.drawable.ic_default_avatar);
        }

        private String subName(FingerGuessingGameAttachment attachment1) {
            String nick = attachment1.getNick();
            if (nick.length() > 6) {
                nick = nick.substring(0, 6) + "...";
            }
            return nick;
        }

        private void setStartFingerGuessingGame(LinearLayout faceContainer, TextView tvContent, IMCustomAttachment attachment) {
            FingerGuessingGameAttachment attachment1 = (FingerGuessingGameAttachment) attachment;
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_start_finger_guessing_game, faceContainer);
            LevelView levelView = view.findViewById(R.id.level_view);
            TextView tvState = view.findViewById(R.id.tv_state);
            TextView tvCount = view.findViewById(R.id.tv_count);
            tvState.setText("发起猜拳，");
            TextView tvOppositeName = view.findViewById(R.id.tv_opposite_name);
            ImageView ivUserHead = view.findViewById(R.id.iv_user_head);
            ImageView ivGift = view.findViewById(R.id.iv_gift);
            TextView tvName = view.findViewById(R.id.tv_name);
            TextView tvStartPk = view.findViewById(R.id.tv_start_pk);
            tvStartPk.setOnClickListener(v -> clickPkOperation(attachment1));
            tvCount.setText("X " + attachment1.getGiftNum());
            tvOppositeName.setVisibility(GONE);
            tvCount.setVisibility(VISIBLE);
            if (attachment1.getUid() == CoreManager.getCore(IAuthCore.class).getCurrentUid()) {
                tvStartPk.setVisibility(GONE);
            } else {
                tvStartPk.setVisibility(VISIBLE);
            }
            tvContent.setVisibility(GONE);
            faceContainer.setVisibility(VISIBLE);

            levelView.setExperLevel(attachment1.getExperienceLevel());
            ImageLoadUtils.loadCircleImage(mContext, attachment1.getAvatar(), ivUserHead, R.drawable.ic_default_avatar);
            tvName.setText(subName(attachment1));
            ImageLoadUtils.loadCircleImage(mContext, attachment1.getGiftUrl(), ivGift, R.drawable.ic_default_avatar);
        }

        private FingerGuessingGameModel fingerGuessingGameModel = new FingerGuessingGameModel();

        private void clickPkOperation(FingerGuessingGameAttachment attachment) {
            fingerGuessingGameModel.pkFingerGuessingGame(attachment.getRecordId(),
                    1, new OkHttpManager.MyCallBack<ServiceResult<PkFingerGuessingGameInfo>>() {
                        @Override
                        public void onError(Exception e) {
                            if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                                SingleToastUtil.showShortToast(e.getMessage());
                            }
                        }

                        @Override
                        public void onResponse(ServiceResult<PkFingerGuessingGameInfo> response) {
                            if (response == null) {
                                onError(new Exception("数据错误"));
                                return;
                            }
                            if (response.isSuccess() && response.getData() != null) {
                                PkFingerGuessingGameDialog dialog = new PkFingerGuessingGameDialog();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(PkFingerGuessingGameDialog.KEY, response.getData());
                                dialog.setArguments(bundle);
                                dialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "");
                            } else if (!TextUtils.isEmpty(response.getMessage())) {
                                FailureFingerGuessingGameDialog failureFingerGuessingGameDialog = new FailureFingerGuessingGameDialog();
                                failureFingerGuessingGameDialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "");
                                onError(new Exception(response.getMessage()));
                            } else {
                                onError(new Exception("数据错误"));
                            }
                        }
                    });
        }

        /**
         * 屏蔽礼物特效等系统消息
         */
        private void setSystemMsg(int second, RoomQueueMsgAttachment roomQueueMsgAttachment, TextView tvContent) {
            String uid = roomQueueMsgAttachment.getUid();
            String handleNick = "";
            if (AvRoomDataManager.get().isRoomOwner(uid)) {
                handleNick = "房主";
            } else if (AvRoomDataManager.get().isRoomAdmin(uid)) {
                handleNick = "管理员";
            }
            List<HashMap<String, Object>> list = new CopyOnWriteArrayList<HashMap<String, Object>>();
            list.add(RichTextUtil.getRichTextMap("系统消息: ", 0x8affffff));
            if (second == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_OPEN) {
                list.add(RichTextUtil.getRichTextMap(handleNick, 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 已屏蔽该房间小礼物特效", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_CLOSE) {
                list.add(RichTextUtil.getRichTextMap(handleNick, 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 已开启该房间小礼物特效", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_OPEN) {
                list.add(RichTextUtil.getRichTextMap(handleNick, 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 已屏蔽该房间坐骑礼物特效", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_RIDE_GIFT_EFFECT_CLOSE) {
                list.add(RichTextUtil.getRichTextMap(handleNick, 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 已开启该房间坐骑礼物特效", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_CLOSE) {
                list.add(RichTextUtil.getRichTextMap(handleNick, 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 关闭了房间内聊天", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_OPEN) {
                list.add(RichTextUtil.getRichTextMap(handleNick, 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 开启了房间内聊天", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_CHANGE_ROOM_LOCK) {
                list.add(RichTextUtil.getRichTextMap(" 房间被锁住了，重新进入房间需要输密码", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_CHANGE_ROOM_NO_LOCK) {
                list.add(RichTextUtil.getRichTextMap(" 房间已解锁", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_ROOM_CLEAN_MEILI) {
                list.add(RichTextUtil.getRichTextMap(handleNick, 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 清除了麦上用户魅力值", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_QUEUE_INVITE) {
                list.add(RichTextUtil.getRichTextMap(AvRoomDataManager.get().isRoomOwner(roomQueueMsgAttachment.adminOrManagerUid) ? "房主" : "管理员", 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 把" + roomQueueMsgAttachment.micName + "抱上了" + (roomQueueMsgAttachment.micPosition + 1) + "号麦", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_QUEUE_KICK) {
                list.add(RichTextUtil.getRichTextMap(AvRoomDataManager.get().isRoomOwner(roomQueueMsgAttachment.adminOrManagerUid) ? "房主" : "管理员", 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 把" + roomQueueMsgAttachment.micName + "抱下麦", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_SET_MANAGER_OPEN) {
                list.add(RichTextUtil.getRichTextMap(AvRoomDataManager.get().isRoomOwner(roomQueueMsgAttachment.adminOrManagerUid) ? "房主" : "管理员", 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 把" + roomQueueMsgAttachment.micName + "设置为房间管理员", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_SET_MANAGER_CLOSE) {
                list.add(RichTextUtil.getRichTextMap(AvRoomDataManager.get().isRoomOwner(roomQueueMsgAttachment.adminOrManagerUid) ? "房主" : "管理员", 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 移除了" + roomQueueMsgAttachment.micName + "的房间管理员的权限", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_LET_SOMEONE_OUT_ROOM) {
                list.add(RichTextUtil.getRichTextMap(AvRoomDataManager.get().isRoomOwner(roomQueueMsgAttachment.adminOrManagerUid) ? "房主" : "管理员", 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 把" + roomQueueMsgAttachment.micName + "踢出了房间", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_ADD_ROOM_BLACK) {
                list.add(RichTextUtil.getRichTextMap(AvRoomDataManager.get().isRoomOwner(roomQueueMsgAttachment.adminOrManagerUid) ? "房主" : "管理员", 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 把" + roomQueueMsgAttachment.micName + "拉进了房间黑名单", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_OPEN_MIC_POISITION) {
                list.add(RichTextUtil.getRichTextMap(handleNick, 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 解封了" + (roomQueueMsgAttachment.micPosition + 1) + "号座位", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_CLOSE_MIC_POISITION) {
                list.add(RichTextUtil.getRichTextMap(handleNick, 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 封锁了" + (roomQueueMsgAttachment.micPosition + 1) + "号座位", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_ALLOW_MIC_POISITION) {
                list.add(RichTextUtil.getRichTextMap(handleNick, 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 解除了" + (roomQueueMsgAttachment.micPosition + 1) + "号座位的禁麦", 0x8affffff));
            } else if (second == IMCustomAttachment.CUSTOM_MSG_FORBIDDEN_MIC_POISITION) {
                list.add(RichTextUtil.getRichTextMap(handleNick, 0xFFFFFFFF));
                list.add(RichTextUtil.getRichTextMap(" 禁麦了" + (roomQueueMsgAttachment.micPosition + 1) + "号座位", 0x8affffff));
            }
            tvContent.setText(RichTextUtil.getSpannableStringFromList(list));
        }

        /**
         * 设置爆出礼物消息样式
         */
        private void setBurstGift(TextView tvContent, BurstGiftAttachment attachment) {
            List<HashMap<String, Object>> list = new CopyOnWriteArrayList<HashMap<String, Object>>();
            list.add(RichTextUtil.getRichTextMap("神秘爆出~", 0xFFFFFFFF));
            list.add(RichTextUtil.getRichTextMap(attachment.getNick(), 0xFFFFD800));
            list.add(RichTextUtil.getRichTextMap(" 通过 ", 0xFFFFFFFF));
            list.add(RichTextUtil.getRichTextMap(attachment.getSendNick(), 0xFFFFD800));
            list.add(RichTextUtil.getRichTextMap(" 送礼意外获得 ", 0xFFFFFFFF));
            list.add(RichTextUtil.getRichTextMap("【" + attachment.getGiftName() + "】", 0xFFFFD800));
            list.add(RichTextUtil.getRichTextMap("X" + attachment.getGiftNum(), 0xFFFFFFFF));
            tvContent.setText(RichTextUtil.getSpannableStringFromList(list));
        }


        /**
         * 设置PK消息结果
         */
        private void setPkMsg(PkCustomAttachment chatRoomMessage, TextView tvContent) {
            PkVoteInfo info = chatRoomMessage.getPkVoteInfo();
            LogUtil.d("chatRoomMessage", "first = " + chatRoomMessage.getFirst() + " second = " + chatRoomMessage.getSecond());
            if (chatRoomMessage.getSecond() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_SECOND_START) {
                String nick = (AvRoomDataManager.get().isRoomOwner(info.getOpUid()) ? "房主" : "管理员") + "发起了";
                String targetNick = info.getNick();
                String targetNick2 = info.getPkNick();
                if (!TextUtils.isEmpty(targetNick) && targetNick.length() > 6) {
                    targetNick = targetNick.substring(0, 6) + "...";
                }
                if (!TextUtils.isEmpty(targetNick2) && targetNick2.length() > 6) {
                    targetNick2 = targetNick2.substring(0, 6) + "..." + "的PK";
                } else {
                    targetNick2 = targetNick2 + "的PK";
                }
                String content = nick + targetNick + "和" + targetNick2;
                SpannableStringBuilder builder = new SpannableStringBuilder(content);
                ForegroundColorSpan redSpan = new ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.color_ffd800));
                ForegroundColorSpan redSpan1 = new ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.color_ffd800));
                if (!TextUtils.isEmpty(targetNick)) {
                    builder.setSpan(redSpan, nick.length(), nick.length() + targetNick.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (!TextUtils.isEmpty(targetNick2)) {
                    builder.setSpan(redSpan1, content.length() - targetNick2.length(), content.length() - 3,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tvContent.setText(builder);
            } else if (chatRoomMessage.getSecond() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_SECOND_END) {
                String content = "本场PK结果：";
                String result = "";
                if (info.getVoteCount() == info.getPkVoteCount()) {
                    result = "平局!";
                } else if (info.getVoteCount() > info.getPkVoteCount()) {
                    result = info.getNick() + "胜利!";
                } else {
                    result = info.getPkNick() + "胜利!";
                }
                content = content + result;
                SpannableStringBuilder builder = new SpannableStringBuilder(content);
                ForegroundColorSpan redSpan1 = new ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.color_ffd800));
                if (!TextUtils.isEmpty(result)) {
                    builder.setSpan(redSpan1, content.length() - result.length(), content.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tvContent.setText(builder);
            } else if (chatRoomMessage.getSecond() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_SECOND_CANCEL) {
                String nick = (AvRoomDataManager.get().isRoomOwner(info.getOpUid()) ? "房主" : "管理员") + "取消了";
                String targetNick = info.getNick();
                String targetNick2 = info.getPkNick();
                if (!TextUtils.isEmpty(targetNick) && targetNick.length() > 6) {
                    targetNick = targetNick.substring(0, 6) + "...";
                }
                if (!TextUtils.isEmpty(targetNick2) && targetNick2.length() > 6) {
                    targetNick2 = targetNick2.substring(0, 6) + "..." + "的PK";
                } else {
                    targetNick2 = targetNick2 + "的PK";
                }
                String content = nick + targetNick + "和" + targetNick2;
                SpannableStringBuilder builder = new SpannableStringBuilder(content);
                ForegroundColorSpan redSpan = new ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.color_ffd800));
                ForegroundColorSpan redSpan1 = new ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.color_ffd800));
                if (!TextUtils.isEmpty(targetNick)) {
                    builder.setSpan(redSpan, nick.length(), nick.length() + targetNick.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (!TextUtils.isEmpty(targetNick2)) {
                    builder.setSpan(redSpan1, content.length() - targetNick2.length(), content.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tvContent.setText(builder);
            }
        }

        /**
         * 房间速配信息
         */
        private void setRoomMatchMsg(TextView tvContent, LinearLayout faceContainer, RoomMatchAttachment attachment, int experLevel, boolean newUserContent) {
            if (attachment == null) {
                faceContainer.setVisibility(View.GONE);
                return;
            }
            tvContent.setVisibility(View.GONE);
            faceContainer.removeAllViews();
            faceContainer.setVisibility(View.VISIBLE);

            FlowLayout flowLayout = new FlowLayout(faceContainer.getContext());
            LinearLayout.LayoutParams fllParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            fllParams.gravity = Gravity.CENTER_VERTICAL;
            flowLayout.setLayoutParams(fllParams);

            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    UIUtil.dip2px(mContext, Constants.MATCH_SIZE));
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            //等级
            LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
            linearLayout.setLayoutParams(layoutParams);
            if (experLevel > 0) {
                LevelView levelView = new LevelView(linearLayout.getContext());
                levelView.seatView.setVisibility(GONE);
                if (newUserContent) {
                    levelView.addView(getImgNewUser());
                }
                levelView.setExperLevel(experLevel);
                levelView.setVisibility(VISIBLE);
                textViewLayoutParams.setMarginEnd(ConvertUtils.dp2px(5));
                levelView.setLayoutParams(textViewLayoutParams);
                linearLayout.addView(levelView);
            }
            //昵称 + 前部分文案
            TextView textView = new TextView(mContext);
            String nick = attachment.getNick();
            if (!TextUtils.isEmpty(nick) && nick.length() > 6) {
                nick = nick.substring(0, 6) + "...";
            }
            int margin = Utils.dip2px(mContext, 5);
            List<HashMap<String, Object>> list = new CopyOnWriteArrayList<HashMap<String, Object>>();
            HashMap<String, Object> map;
            map = new HashMap<String, Object>();
            map.put(RichTextUtil.RICHTEXT_STRING, nick);
            map.put(RichTextUtil.RICHTEXT_COLOR, 0xFFFFFFFF);
            list.add(map);
            map = new HashMap<String, Object>();

            //求签
            if (attachment.getSecond() == CustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH_SPEED) {
                map.put(RichTextUtil.RICHTEXT_STRING, attachment.isShowd() ? "  解签" : "  开始求签了");
                map.put(RichTextUtil.RICHTEXT_COLOR, attachment.isShowd() ? 0xFFFFD800 : 0xFFFFFFFF);
            } else if (attachment.getSecond() == CustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH_CHOICE) {
                //抽签
                map.put(RichTextUtil.RICHTEXT_STRING, attachment.isShowd() ? "  心动选择了" : "  已选择了");
                map.put(RichTextUtil.RICHTEXT_COLOR, attachment.isShowd() ? 0xFFFFD800 : 0xFFFFFFFF);
            } else {
                //放弃解签
                map.put(RichTextUtil.RICHTEXT_STRING, "  放弃了解签");
                map.put(RichTextUtil.RICHTEXT_COLOR, 0xFFFFD800);
            }
            list.add(map);
            textView.setTextSize(12);
            textView.setText(RichTextUtil.getSpannableStringFromList(list));
            textView.setLayoutParams(textViewLayoutParams);
            linearLayout.addView(textView);
            flowLayout.addView(linearLayout);
            //图片
            if (attachment.getNumArr() != null) {
                for (int i = 0; i < attachment.getNumArr().length; i++) {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    LinearLayout.LayoutParams imgLayoutParams = new LinearLayout.LayoutParams(
                            UIUtil.dip2px(mContext, Constants.MATCH_SIZE), UIUtil.dip2px(mContext, Constants.MATCH_SIZE));
                    imgLayoutParams.setMargins(margin, 0, 0, 0);
                    imageView.setLayoutParams(imgLayoutParams);
                    if (!attachment.isShowd()) {
                        imageView.setImageResource(R.drawable.ic_match_question_mark);
                    } else {
                        imageView.setImageResource(RoomMatchUtil.getMatchResId(attachment.getNumArr()[i]));
                    }
                    flowLayout.addView(imageView);
                }
            }

            if (attachment.getSecond() == CustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH_CHOICE && !attachment
                    .isShowd()) {
                TextView end = new TextView(mContext);
                end.setTextColor(mContext.getResources().getColor(R.color.white));
                end.setTextSize(12);
                end.setText("  等待TA的命中注定");
                end.setLayoutParams(layoutParams);
                flowLayout.addView(end);
            }
            faceContainer.addView(flowLayout);
        }

        private final String EGG_HINT = "在开盲盒中获得了";

        /***
         * 砸金蛋
         */
        private void setLotteryInfo(TextView tvContent, LotteryBoxAttachment attachment) {
            String unit = mContext.getString(R.string.gift_expend_gold);
            String newContent = "";
            SpannableStringBuilder builder1 = new SpannableStringBuilder(newContent);
            setBgStyle(tvContent, attachment.getGoldPrice());
            String nick = attachment.getNick();
            String str = "";
            if (!attachment.getGiftName().contains("【全服】")) {
                str = "【全服】";
            }
            String giftNameAndPrice = "“" + str + attachment.getGiftName() + "（" + attachment.getGoldPrice() + unit+"）”";
            String wa = "哇塞，";
            String content = wa + nick + EGG_HINT + giftNameAndPrice;
            SpannableStringBuilder builder = new SpannableStringBuilder(content);
            ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_FFF35B));
            ForegroundColorSpan redSpan1 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_FFF35B));
            if (!TextUtils.isEmpty(nick)) {
                builder.setSpan(redSpan, wa.length(), wa.length() + nick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            String giftNumber = "x" + attachment.getCount();
            builder.append(giftNumber);
            builder.setSpan(redSpan1, builder.length() - (giftNumber.length() + giftNameAndPrice.length()), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder1.append(builder);
            tvContent.setText(builder1);
        }

        private void setWanFaInfo(TextView tvContent, WanFaAttachment attachment) {
            String roomDesc = attachment.getRoomDesc();
            String roomNotice = attachment.getRoomNotice();
            tvContent.setTextColor(mContext.getResources().getColor(R.color.room_blue_color));
            tvContent.setText(roomDesc + "：\n" + roomNotice);
        }

        private void setRoomNameInfo(TextView tvContent, ChangeRoomNameAttachment attachment) {
            String roomName = attachment.getRoomName();
            String uid = attachment.getUId() + "";
            String handleNick = "";
            if (AvRoomDataManager.get().isRoomOwner(uid)) {
                handleNick = "房主";
            } else if (AvRoomDataManager.get().isRoomAdmin(uid)) {
                handleNick = "管理员";
            }
            tvContent.setTextColor(0x8affffff);
            tvContent.setText("系统消息:  " + handleNick + "更改房名为" + roomName);
        }

        private void setSendCallGift(TextView tvContent, SendCallGiftAttachment attachment) {
            String sendName = attachment.getSendName();
            String adminName = attachment.getTargetName();
            String giftName = attachment.getGiftName();
            String giftUrl = attachment.getGiftUrl();

            String newContent = "";
            SpannableStringBuilder builder1 = new SpannableStringBuilder(newContent);
            String giftNameAndPrice = "为" + adminName;
            String content = sendName + giftNameAndPrice;
            String giftNumber = "“" + giftName + "”x1";
            String callStr = " 打call ";
            String iconStr = Constants.GIFT_PLACEHOLDER;

            SpannableStringBuilder builder = new SpannableStringBuilder(content);
            builder.append(callStr);
            builder.append(iconStr);//插入小图标占位符
            UrlImageSpan imageSpan = new UrlImageSpan(mContext, giftUrl, tvContent);
            imageSpan.setImgWidth(ScreenUtil.dip2px(Constants.GIFT_PLACEHOLDER_ICON_SIZE));
            imageSpan.setImgHeight(ScreenUtil.dip2px(Constants.GIFT_PLACEHOLDER_ICON_SIZE));
            builder.setSpan(imageSpan, builder.toString().length() - Constants.GIFT_PLACEHOLDER.length(), builder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            builder.append(giftNumber);

            ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_2ACEF5));
            ForegroundColorSpan redSpan1 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_2ACEF5));
            ForegroundColorSpan redSpan2 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_FFF35B));
            if (!TextUtils.isEmpty(sendName)) {
                builder.setSpan(redSpan, 0, sendName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (!TextUtils.isEmpty(adminName)) {
                builder.setSpan(redSpan1, builder.length() - (adminName.length() + callStr.length() + iconStr.length() + giftNumber.length()), builder.length() - (giftNumber.length() + callStr.length() + iconStr.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            builder.setSpan(redSpan2, builder.length() - giftNumber.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder1.append(builder);
            tvContent.setText(builder1);
        }


        /**
         * 设置全服通知样式
         *
         * @param tvContent
         * @param price
         */
        private void setBgStyle(TextView tvContent, int price) {
            if (price >= 13140) {
                tvContent.setBackgroundResource(R.drawable.egg_bg_full_three_style);
            }
//            if (price >= 520 && price < 1000) {
//                tvContent.setBackgroundResource(R.drawable.egg_bg_full_one_style);
//            } else if (price >= 1000 && price < 1888) {
//                tvContent.setBackgroundResource(R.drawable.egg_bg_full_two_style);
//            } else if (price >= 1888 && price < 33440) {
//                tvContent.setBackgroundResource(R.drawable.egg_bg_full_three_style);
//            } else {
//                tvContent.setBackgroundResource(R.drawable.egg_bg_full_four_style);
//            }
        }


        private void setMsgAuction(ChatRoomMessage chatRoomMessage, TextView tvContent, IMCustomAttachment attachment) {
            String senderNick = chatRoomMessage.getImChatRoomMember().getNick();
            String unit = mContext.getString(R.string.gift_expend_gold);
            AuctionAttachment auctionAttachment = (AuctionAttachment) attachment;
            if (attachment.getSecond() == IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_AUCTION_START) {
                senderNick = "房主";
                String content = senderNick + " 开启了竞拍";
                tvContent.setTextColor(mContext.getResources().getColor(R.color.roomTextNick));
                tvContent.setText(content);
            } else if (attachment.getSecond() == IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_AUCTION_FINISH) {
                if (auctionAttachment.getAuctionInfo().getCurMaxUid() > 0) {
                    UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(
                            auctionAttachment.getAuctionInfo().getCurMaxUid());
                    if (userInfo != null) {
                        senderNick = userInfo.getNick();
                    } else {
                        senderNick = "";
                    }
                    String coin = " 以" + auctionAttachment.getAuctionInfo().getRivals().get(0).getAuctMoney() + unit+"拍下 ";
                    String voiceActorNick = "";
                    UserInfo voiceActor = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(
                            auctionAttachment.getAuctionInfo().getAuctUid());
                    if (voiceActor != null) {
                        voiceActorNick = voiceActor.getNick();
                    }
                    String content = senderNick + coin + voiceActorNick;
                    SpannableStringBuilder builder = new SpannableStringBuilder(content);
                    ForegroundColorSpan redSpan = new ForegroundColorSpan(
                            mContext.getResources().getColor(R.color.roomTextNick));
                    ForegroundColorSpan yellowSpan = new ForegroundColorSpan(
                            mContext.getResources().getColor(R.color.white));
                    builder.setSpan(redSpan, 0, senderNick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(yellowSpan, senderNick.length(), senderNick.length() + coin.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvContent.setTextColor(mContext.getResources().getColor(R.color.roomTextNick));
                    tvContent.setText(builder);
                } else {
                    senderNick = "房主";
                    String content = senderNick + " 结束了竞拍，当前暂无人出价";
                    tvContent.setTextColor(mContext.getResources().getColor(R.color.roomTextNick));
                    tvContent.setText(content);
                }
            } else {
                if (StringUtil.isEmpty(senderNick)) {
                    senderNick = "";
                }
                String content = senderNick + " 出价" + auctionAttachment.getAuctionInfo().getRivals().get(0)
                        .getAuctMoney() + unit;
                SpannableStringBuilder builder = new SpannableStringBuilder(content);
                ForegroundColorSpan redSpan = new ForegroundColorSpan(
                        mContext.getResources().getColor(R.color.roomTextNick));
                if (!TextUtils.isEmpty(senderNick)) {
                    builder.setSpan(redSpan, 0, senderNick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tvContent.setText(builder);
            }
        }

        private void setMsgMultiGift(ChatRoomMessage chatRoomMessage, TextView tvContent, MultiGiftAttachment attachment) {
            MultiGiftReceiveInfo multiGiftRecieveInfo = attachment.getMultiGiftRecieveInfo();
            if (multiGiftRecieveInfo == null) {
                return;
            }
            GiftInfo giftInfo = CoreManager.getCore(IGiftCore.class).findGiftInfoById(multiGiftRecieveInfo.getGiftId());
            if (giftInfo != null) {
                boolean newUserContent = false;
                if (chatRoomMessage != null && chatRoomMessage.getImChatRoomMember() != null) {
                    newUserContent = chatRoomMessage.getImChatRoomMember().isIs_new_user();
                }
                String newContent = "";
                SpannableStringBuilder builder1 = new SpannableStringBuilder(newContent);
                setNewLabel(builder1, newUserContent);
                setUserLevel(builder1, multiGiftRecieveInfo.getExperLevel());
                String nick = multiGiftRecieveInfo.getNick();
                String content = nick + Constants.ALL_MIC_SEND + giftInfo.getGiftName();
                SpannableStringBuilder builder = new SpannableStringBuilder(content);
                ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_ffd800));
                ForegroundColorSpan redSpan1 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_ffd800));
                builder.setSpan(redSpan, 0, nick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(Constants.GIFT_PLACEHOLDER);//插入小图标占位符
                UrlImageSpan imageSpan = new UrlImageSpan(mContext, giftInfo.getGiftUrl(), tvContent);
                imageSpan.setImgWidth(ScreenUtil.dip2px(Constants.GIFT_PLACEHOLDER_ICON_SIZE));
                imageSpan.setImgHeight(ScreenUtil.dip2px(Constants.GIFT_PLACEHOLDER_ICON_SIZE));
                builder.setSpan(imageSpan, builder.toString().length() - Constants.GIFT_PLACEHOLDER.length(), builder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                String str = "x" + multiGiftRecieveInfo.getGiftNum();
                builder.append(str);
                builder.setSpan(redSpan1, builder.length() - str.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder1.append(builder);
                tvContent.setText(builder1);
            }
        }

        private void setMsgHeaderGift(ChatRoomMessage chatRoomMessage, TextView tvContent, GiftAttachment attachment) {
            GiftReceiveInfo giftRecieveInfo = attachment.getGiftRecieveInfo();
            if (giftRecieveInfo == null)
                return;
            GiftInfo giftInfo = CoreManager.getCore(IGiftCore.class).findGiftInfoById(giftRecieveInfo.getGiftId());
            if (giftInfo != null) {
                boolean newUserContent = false;
                if (chatRoomMessage != null && chatRoomMessage.getImChatRoomMember() != null) {
                    newUserContent = chatRoomMessage.getImChatRoomMember().isIs_new_user();
                }
                String newContent = "";
                SpannableStringBuilder builder1 = new SpannableStringBuilder(newContent);
                setNewLabel(builder1, newUserContent);
                setUserLevel(builder1, giftRecieveInfo.getExperLevel());
                String nick = giftRecieveInfo.getNick();
                String targetNick = giftRecieveInfo.getTargetNick();
                String content = nick + Constants.SEND_TO + targetNick + giftInfo.getGiftName();
                SpannableStringBuilder builder = new SpannableStringBuilder(content);
                ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_ffd800));
                ForegroundColorSpan redSpan1 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_ffd800));
                ForegroundColorSpan redSpan2 = new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_ffd800));
                if (!TextUtils.isEmpty(nick)) {
                    builder.setSpan(redSpan, 0, nick.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (!TextUtils.isEmpty(targetNick)) {
                    builder.setSpan(redSpan1, content.length() - (targetNick.length() + giftInfo.getGiftName().length()),
                            content.length() - giftInfo.getGiftName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                builder.append(Constants.GIFT_PLACEHOLDER);//插入小图标占位符
                UrlImageSpan imageSpan = new UrlImageSpan(mContext, giftInfo.getGiftUrl(), tvContent);
                imageSpan.setImgWidth(ScreenUtil.dip2px(Constants.GIFT_PLACEHOLDER_ICON_SIZE));
                imageSpan.setImgHeight(ScreenUtil.dip2px(Constants.GIFT_PLACEHOLDER_ICON_SIZE));
                builder.setSpan(imageSpan, builder.toString().length() - Constants.GIFT_PLACEHOLDER.length(), builder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                String str = "x" + giftRecieveInfo.getGiftNum();
                builder.append(str);
                builder.setSpan(redSpan2, builder.length() - str.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder1.append(builder);
                tvContent.setText(builder1);
            }
        }

        // 新的IM 需要对Notification单独处修改
        private void setMsgNotification(ChatRoomMessage chatRoomMessage, TextView tvContent) {
            if (chatRoomMessage == null) {
                return;
            }
            boolean newUserContent = false;
            if (chatRoomMessage.getImChatRoomMember() != null) {
                newUserContent = chatRoomMessage.getImChatRoomMember().isIs_new_user();
            }
            IMChatRoomMember member = chatRoomMessage.getImChatRoomMember();
            String carName = member.getCar_name();
            int level = member.getExperLevel();
            String senderNick = "";
            if (!TextUtils.isEmpty(member.getNick())) {
                senderNick = member.getNick();
            }
            if (!TextUtils.isEmpty(carName)) {
                tvContent.setVisibility(VISIBLE);
                String content = "";
                SpannableStringBuilder builder = new SpannableStringBuilder(content);
                setNewLabel(builder, newUserContent);
                setUserLevel(builder, level);
                builder.append(senderNick);
                builder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_2ACEF5)),
                        builder.toString().length() - senderNick.length(), builder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(" 驾着").append("“");
                builder.append(carName);
                builder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_ffd800)),
                        builder.toString().length() - carName.length(), builder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("”").append("来了");
                tvContent.setText(builder);
            } else {
                tvContent.setVisibility(VISIBLE);
                String content = "";
                SpannableStringBuilder builder = new SpannableStringBuilder(content);
                setNewLabel(builder, newUserContent);
                setUserLevel(builder, level);
                builder.append(senderNick);
                builder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_2ACEF5)),
                        builder.toString().length() - senderNick.length(), builder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(" ").append("来了");
                tvContent.setText(builder);
                tvContent.setBackgroundResource(R.drawable.shape_80000000_r_10);
            }
        }

        private void setMsgRoomTip(TextView tvContent, RoomTipAttachment roomTipAttachment) {
            String content = "";
            if (StringUtil.isEmpty(roomTipAttachment.getNick())) {
                roomTipAttachment.setNick(" ");
            }
            if (roomTipAttachment.getSecond() == IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_ROOM_TIP_SHARE_ROOM) {
                content = roomTipAttachment.getNick() + " 分享了房间";
            } else if (roomTipAttachment.getSecond()
                    == IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_ROOM_TIP_ATTENTION_ROOM_OWNER) {
                content = roomTipAttachment.getNick() + " 关注了房主";
            }
            SpannableStringBuilder builder = new SpannableStringBuilder(content);
            ForegroundColorSpan redSpan = new ForegroundColorSpan(
                    mContext.getResources().getColor(R.color.common_color_11_transparent_54));
            if (!TextUtils.isEmpty(roomTipAttachment.getNick()) && roomTipAttachment.getNick().length() <= builder.length()) {
                builder.setSpan(redSpan, 0, roomTipAttachment.getNick().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tvContent.setText(builder);
        }

        private void setMsgFace(LinearLayout faceContainer, TextView tvContent, FaceAttachment attachment, int experLevel, boolean newUserContent) {
            faceContainer.removeAllViews();
            faceContainer.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.GONE);

            LogUtil.d("setMsgFace", experLevel + "");
            List<FaceReceiveInfo> faceReceiveInfos = attachment.getFaceReceiveInfos();
            for (int i = 0; i < faceReceiveInfos.size(); i++) {
                FaceReceiveInfo faceReceiveInfo = faceReceiveInfos.get(i);
                FaceInfo faceInfo = CoreManager.getCore(IFaceCore.class).findFaceInfoById(faceReceiveInfo.getFaceId());
                if (faceReceiveInfo.getResultIndexes().size() <= 0 || faceInfo == null) {
                    continue;
                }
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.CENTER_VERTICAL;
                linearLayout.setLayoutParams(layoutParams);

                LinearLayout llLevel = new LinearLayout(mContext);
                llLevel.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams lpLevel = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.CENTER_VERTICAL;
                llLevel.setLayoutParams(lpLevel);

                LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
                if (experLevel > 0 || newUserContent) {
                    LevelView levelView = new LevelView(linearLayout.getContext());
                    levelView.seatView.setVisibility(GONE);
                    if (newUserContent) {
                        levelView.addView(getImgNewUser());
                    }
                    levelView.setExperLevel(experLevel);
                    levelView.setVisibility(VISIBLE);

                    levelView.setLayoutParams(textViewLayoutParams);
                    llLevel.addView(levelView);
                }
                TextView textView = new TextView(mContext);
                textView.setTextColor(mContext.getResources().getColor(R.color.color_2ACEF5));
                String nick = faceReceiveInfo.getNick();
                if (StringUtil.isEmpty(nick)) {
                    nick = "";
                }
                textView.setTextSize(12);
                textView.setText(nick);
                textViewLayoutParams.setMarginStart(ConvertUtils.dp2px(5));
                textView.setLayoutParams(textViewLayoutParams);
                llLevel.addView(textView);
                linearLayout.addView(llLevel);
                faceContainer.addView(linearLayout);
                LinearLayout llFaceContainer = new LinearLayout(mContext);
                llFaceContainer.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER_VERTICAL;
                llFaceContainer.setLayoutParams(layoutParams);
                llFaceContainer.setBackgroundResource(R.drawable.ic_room_msg_bg);
                int margin = Utils.dip2px(mContext, 5);
                for (Integer index : faceReceiveInfo.getResultIndexes()) {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    LinearLayout.LayoutParams imgLayoutParams = new LinearLayout.LayoutParams(
                            UIUtil.dip2px(mContext, 30), UIUtil.dip2px(mContext, 30));
                    imgLayoutParams.gravity = Gravity.CENTER_VERTICAL;
                    if (faceInfo.getId() == 17 && faceReceiveInfo.getResultIndexes().size() > 1) {
                        // 骰子
                        imgLayoutParams = new LinearLayout.LayoutParams(UIUtil.dip2px(mContext, 22),
                                UIUtil.dip2px(mContext, 22));
                        imgLayoutParams.setMargins(0, margin, margin, margin);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } else if (faceInfo.getId() == 23) {
                        // 纸牌
                        imageView.setScaleType(ImageView.ScaleType.FIT_START);
                        imgLayoutParams.setMargins(0, margin, 0, margin);
                    }
                    imageView.setLayoutParams(imgLayoutParams);
                    llFaceContainer.addView(imageView);
                    ImageLoadUtils.loadImage(mContext, faceInfo.getFacePath(index), imageView);
                }
                linearLayout.addView(llFaceContainer);
            }
        }

        private final String ME = "我: ";

        //: 2018/3/12
        private void setMsgText(ChatRoomMessage chatRoomMessage, TextView tvContent) {
            String senderNick;
            int experLevel = -1;
            boolean newUserContent = false;
            IMChatRoomMember member = chatRoomMessage.getImChatRoomMember();
            if (member == null) {
                senderNick = ME;
                UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
                if (userInfo != null) {
                    experLevel = userInfo.getExperLevel();
                }
            } else {
                if (AvRoomDataManager.get().isOwner(member.getAccount())) {
                    senderNick = ME;
                } else {
                    senderNick = member.getNick() + ": ";
                }
                experLevel = member.getExperLevel();
                newUserContent = member.isIs_new_user();
            }

            String content = "";
            SpannableStringBuilder builder = new SpannableStringBuilder(content);
            setNewLabel(builder, newUserContent);
            setUserLevel(builder, experLevel);
            builder.append(senderNick);
            builder.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_2ACEF5)),
                    builder.toString().length() - senderNick.length(), builder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setStrColor(builder, builder.toString().length() - senderNick.length(), builder.toString().length(), R.color.common_color_11_transparent_54);
            builder.append(chatRoomMessage.getContent());
            tvContent.setText(builder);
        }

        private void setUserLevel(SpannableStringBuilder builder, int experLevel) {
            int levelRes = mContext.getResources().getIdentifier("lv" + experLevel, "drawable", mContext.getPackageName());
            if (experLevel >= 0 && levelRes >= 0) {
                builder.append(Constants.LEVEL_PLACEHOLDER);
                UrlImageSpan urlImageSpan = new UrlImageSpan(mContext, levelRes);
                urlImageSpan.setImgWidth(mContext.getResources().getDimensionPixelOffset(R.dimen.level_icon_width));
                urlImageSpan.setImgHeight(mContext.getResources().getDimensionPixelOffset(R.dimen.level_icon_height));
                builder.setSpan(urlImageSpan, builder.toString().length() - Constants.LEVEL_PLACEHOLDER.length(),
                        builder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(" ");//添加空格
            }
        }


        private void setNewLabel(SpannableStringBuilder builder, boolean userLabelUrl) {
            if (userLabelUrl) {
                builder.append(Constants.LABEL_PLACEHOLDER);
                int start = builder.toString().length() - Constants.LABEL_PLACEHOLDER.length();
                int end = builder.toString().length();
                UrlImageSpan imageSpan = new UrlImageSpan(mContext, R.mipmap.new_user_msg_icon_small);
//                imageSpan.setImgWidth(mContext.getResources().getDimensionPixelOffset(R.dimen.newbie_icon_width));
//                imageSpan.setImgHeight(mContext.getResources().getDimensionPixelOffset(R.dimen.newbie_icon_height));
                builder.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(" ");//添加空格
            }
        }


        private ImageView getImgNewUser() {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.mipmap.new_user_msg_icon_small);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    mContext.getResources().getDimensionPixelOffset(R.dimen.newbie_icon_height));
            imageView.setLayoutParams(params);
            return imageView;
        }

        private void setStrColor(SpannableStringBuilder builder, int start, int end, int color) {
            ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext.getResources().getColor(color));
            builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 新版IM 这里需要处理各种类型消息的点击事件
        @Override
        public void onClick(View v) {
            ChatRoomMessage chatRoomMessage = (ChatRoomMessage) v.getTag();
            if (chatRoomMessage == null) {
                return;
            }
            String account = "";
            String nick = "";
            String avatar = "";
            boolean isBox = false;
            if (!IMReportRoute.ChatRoomTip.equalsIgnoreCase(chatRoomMessage.getRoute())) {
                if (IMReportRoute.sendMessageReport.equalsIgnoreCase(chatRoomMessage.getRoute())) {
                    IMCustomAttachment attachment = chatRoomMessage.getAttachment();
                    if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_ROOM_TIP) {
                        account = ((RoomTipAttachment) attachment).getUid() + "";
                        nick = ((RoomTipAttachment) attachment).getNick();
                    } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_LOTTERY_BOX) {
                        account = ((LotteryBoxAttachment) attachment).getAccount() + "";
                        nick = ((LotteryBoxAttachment) attachment).getNick();
                        isBox = true;
                    } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT) {
                        account = ((GiftAttachment) attachment).getGiftRecieveInfo().getUid() + "";
                        nick = ((GiftAttachment) attachment).getGiftRecieveInfo().getNick();
                        isBox = true;
                    } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT) {
                        account = ((MultiGiftAttachment) attachment).getMultiGiftRecieveInfo().getUid() + "";
                        nick = ((MultiGiftAttachment) attachment).getMultiGiftRecieveInfo().getNick();
                        isBox = true;
                    } else if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_ROOM_CALL_GIFT) {
                        account = ((SendCallGiftAttachment) attachment).getUid() + "";
                        nick = ((SendCallGiftAttachment) attachment).getSendName();
                        isBox = true;
                    }
                } else if (IMReportRoute.chatRoomMemberIn.equalsIgnoreCase(chatRoomMessage.getRoute())) {
                    isBox = true;
                    account = chatRoomMessage.getImChatRoomMember().getAccount();
                    nick = chatRoomMessage.getImChatRoomMember().getNick();
                    avatar = chatRoomMessage.getImChatRoomMember().getAvatar();
                } else {
                    if (chatRoomMessage.getImChatRoomMember() != null) {
                        account = chatRoomMessage.getImChatRoomMember().getAccount();
                        nick = chatRoomMessage.getImChatRoomMember().getNick();
                        avatar = chatRoomMessage.getImChatRoomMember().getAvatar();
                    }
                }
                if (TextUtils.isEmpty(account)) {
                    return;
                }
//                if (!isBox) {
                final List<ButtonItem> buttonItems = new CopyOnWriteArrayList<>();
                List<ButtonItem> items = ButtonItemFactory.createAllRoomPublicScreenButtonItems(mContext, account, nick, avatar);
                if (items == null) {
                    return;
                }
                buttonItems.addAll(items);
                ((BaseMvpActivity) mContext).getDialogManager().showCommonPopupDialog(buttonItems, "取消");
//                } else {
                //弹出个人资料
//                        new UserInfoDialog(mContext, JavaUtil.str2long(account)).show();
//                }
            }
        }
    }
}
