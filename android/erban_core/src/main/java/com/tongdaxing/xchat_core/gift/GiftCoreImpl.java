package com.tongdaxing.xchat_core.gift;

import android.os.Handler;
import android.os.Message;

import com.netease.nim.uikit.session.module.Container;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.DemoCache;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.common.ICommonClient;
import com.tongdaxing.xchat_core.im.custom.bean.CustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.GiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.LotteryBoxAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.MultiGiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.ChangeRoomNameAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.NimGiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.SendCallGiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.WanFaAttachment;
import com.tongdaxing.xchat_core.im.room.IIMRoomCore;
import com.tongdaxing.xchat_core.im.room.IIMRoomCoreClient;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.pay.IPayCore;
import com.tongdaxing.xchat_core.pay.IPayCoreClient;
import com.tongdaxing.xchat_core.pk.IPKCoreClient;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.im.IMReportRoute;
import com.tongdaxing.xchat_framework.util.util.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author chenran
 * @date 2017/7/27
 */

public class GiftCoreImpl extends AbstractBaseCore implements IGiftCore {
    private GiftListInfo giftListInfo;
    private static List<IMCustomAttachment> giftQueue;

    public GiftCoreImpl() {
        CoreManager.addClient(this);
        giftListInfo = DemoCache.readGiftList();
        giftQueue = new ArrayList<>();
        requestGiftInfos();
    }

    private GiftCoreHandler handler = new GiftCoreHandler();

    static class GiftCoreHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (giftQueue.size() > 0) {
                IMCustomAttachment attachment = giftQueue.remove(0);
                if (attachment != null)
                    parseChatRoomAttachMent(attachment);
            }

            if (giftQueue.size() > 0) {
                sendEmptyMessageDelayed(0, 150);
            }
        }
    }

    private void addGiftMessage(IMCustomAttachment attachment) {
        giftQueue.add(attachment);
        if (giftQueue.size() == 1) {
            handler.sendEmptyMessageDelayed(0, 150);
        }
    }

    @Override
    public void requestGiftInfos() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        OkHttpManager.getInstance().getRequest(UriProvider.getGiftList(), params, new OkHttpManager.MyCallBack<ServiceResult<GiftListInfo>>() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(ServiceResult<GiftListInfo> response) {
                if (response.isSuccess()) {
                    giftListInfo = response.getData();
                    DemoCache.saveGiftList(giftListInfo);
                    if (response.getData() != null && !ListUtils.isListEmpty(response.getData().getGift())) {
                        notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_REQUEST_GIFT_LIST, response.getData().getGift());
                    }
                }
            }
        });
    }

    private List<GiftInfo> filterNobleGifts(List<GiftInfo> originals) {
        List<GiftInfo> noNobleFaces = new ArrayList<>(originals.size());
        for (GiftInfo giftInfo : originals) {
            if (giftInfo.isNobleGift()) continue;
            noNobleFaces.add(giftInfo);
        }
        return noNobleFaces;
    }

    @Override
    public List<GiftInfo> getGiftInfosByType(int type) {
        if (giftListInfo != null && giftListInfo.getGift() != null && giftListInfo.getGift().size() > 0) {
            List<GiftInfo> giftInfos = new ArrayList<>();
            for (int i = 0; i < giftListInfo.getGift().size(); i++) {
                GiftInfo giftInfo = giftListInfo.getGift().get(i);
                if (giftInfo.getGiftType() == type) {
                    if (type == 3) {
                        if (giftInfo.getUserGiftPurseNum() > 0)
                            giftInfos.add(giftInfo);
                    } else if (type == 2) {
                        giftInfos.add(giftInfo);
                    } else if (type == 4) {//活动礼物
                        giftInfos.add(giftInfo);
                    } else if (type == 5) {
                        giftInfos.add(giftInfo);
                    } else if (type == 1) {
                        giftInfos.add(giftInfo);
                    }
                }
            }
            Collections.sort(giftInfos, new Comparator<GiftInfo>() {
                @Override
                public int compare(GiftInfo o1, GiftInfo o2) {
                    return o1.getGoldPrice() - o2.getGoldPrice();
                }
            });
            return filterNobleGifts(giftInfos);
        } else {
            requestGiftInfos();
        }
        return new ArrayList<>();
    }

    private boolean filterGift(int i) {
        return giftListInfo.getGift().get(i).getGiftType() == 1 ||
                giftListInfo.getGift().get(i).getGiftType() == 2 ||
                giftListInfo.getGift().get(i).getGiftType() == 3 ||
                giftListInfo.getGift().get(i).getGiftType() == 4 ||
                giftListInfo.getGift().get(i).getGiftType() == 5;
    }

    @Override
    public List<GiftInfo> getGiftInfosByType2And3() {
        if (giftListInfo != null && giftListInfo.getGift() != null && giftListInfo.getGift().size() > 0) {
            List<GiftInfo> giftInfos = new ArrayList<>();
            for (int i = 0; i < giftListInfo.getGift().size(); i++) {
                if (filterGift(i)) {
                    giftInfos.add(giftListInfo.getGift().get(i));
                }
            }
            return filterNobleGifts(giftInfos);
        } else {
            requestGiftInfos();
        }
        return new ArrayList<>();
    }

    @Override
    public void onReceiveChatRoomMessages(List<ChatRoomMessage> chatRoomMessageList) {
        if (chatRoomMessageList != null && chatRoomMessageList.size() > 0) {
            for (ChatRoomMessage msg : chatRoomMessageList) {
                if (IMReportRoute.sendMessageReport.equalsIgnoreCase(msg.getRoute())) {
                    IMCustomAttachment attachment = (IMCustomAttachment) msg.getAttachment();
                    if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT
                            || attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT
                            || attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_SUPER_GIFT)
                        addGiftMessage(attachment);
                }
            }
        }
    }

    @CoreEvent(coreClientClass = IIMRoomCoreClient.class)
    public void onSendRoomMessageSuccess(ChatRoomMessage msg) {
        if (IMReportRoute.sendMessageReport.equalsIgnoreCase(msg.getRoute())) {
            IMCustomAttachment attachment = (IMCustomAttachment) msg.getAttachment();
            if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT
                    || attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT)
                addGiftMessage(attachment);
        }
    }


    private static void parseChatRoomAttachMent(IMCustomAttachment attachMent) {
        if (attachMent.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT) {
            GiftAttachment giftAttachment = (GiftAttachment) attachMent;
            CoreManager.notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_RECIEVE_GIFT_MSG, giftAttachment.getGiftRecieveInfo());
        } else if (attachMent.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT) {
            MultiGiftAttachment multiGiftAttachment = (MultiGiftAttachment) attachMent;
            CoreManager.notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_MULTI_GIFT_MSG, multiGiftAttachment.getMultiGiftRecieveInfo());
        } else if (attachMent.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_SUPER_GIFT) {
            GiftAttachment giftAttachment = (GiftAttachment) attachMent;
            CoreManager.notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_SUPER_GIFT_MSG, giftAttachment.getGiftRecieveInfo());
        }
    }

    private void parsePersonalAttachMent(IMCustomAttachment attachMent) {
        if (attachMent.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT) {
            GiftAttachment giftAttachment = (GiftAttachment) attachMent;
            notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_RECEIVE_PERSONAL_GIFT, giftAttachment.getGiftRecieveInfo());
        }
    }

    private void parsePersonalNimAttachMent(CustomAttachment attachMent) {
        if (attachMent.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT) {
            NimGiftAttachment giftAttachment = (NimGiftAttachment) attachMent;
            notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_RECEIVE_PERSONAL_GIFT, giftAttachment.getGiftRecieveInfo());
        }
    }


    private void sendGiftMessage(GiftReceiveInfo giftReceiveInfo, int price) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null || giftReceiveInfo == null) return;
        long myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();

        GiftAttachment giftAttachment = new GiftAttachment(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT, IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_SEND_GIFT);
        giftAttachment.setUid(myUid + "");
        giftAttachment.setExperLevel(giftReceiveInfo.getExperLevel());
        giftAttachment.setGiftRecieveInfo(giftReceiveInfo);
        ChatRoomMessage message = new ChatRoomMessage();
        message.setRoom_id(roomInfo.getRoomId() + "");
        message.setAttachment(giftAttachment);
        message.setImChatRoomMember(AvRoomDataManager.get().mOwnerMember);
        CoreManager.getCore(IIMRoomCore.class).sendMessage(message);
    }


    private void sendMultiGiftMessage(MultiGiftReceiveInfo giftReceiveInfo, int price) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null && giftReceiveInfo != null) {
            long myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();

            MultiGiftAttachment giftAttachment = new MultiGiftAttachment(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT, IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_SEND_MULTI_GIFT);
            giftAttachment.setUid(myUid + "");

            giftAttachment.setMultiGiftAttachment(giftReceiveInfo);
            giftAttachment.setExperLevel(giftReceiveInfo.getExperLevel());
            ChatRoomMessage message = new ChatRoomMessage();
            message.setRoom_id(roomInfo.getRoomId() + "");
            message.setAttachment(giftAttachment);
            message.setImChatRoomMember(AvRoomDataManager.get().mOwnerMember);
            CoreManager.getCore(IIMRoomCore.class).sendMessage(message);
        }
    }

    @Override
    public void sendLotteryMeg(GiftInfo giftInfo, int count) {
        if (giftInfo == null)
            return;
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        LotteryBoxAttachment lotteryBoxAttachment = new LotteryBoxAttachment(IMCustomAttachment.CUSTOM_MSG_LOTTERY_BOX, IMCustomAttachment.CUSTOM_MSG_LOTTERY_BOX);
        String nick = "";
        long account = 0;
        if (CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo() != null) {
            nick = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo().getNick();
            account = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo().getUid();
        }
        lotteryBoxAttachment.setAccount(account);
        lotteryBoxAttachment.setNick(nick);
        lotteryBoxAttachment.setGoldPrice(giftInfo.getGoldPrice());
        lotteryBoxAttachment.setCount(count);
        lotteryBoxAttachment.setGiftName(giftInfo.getGiftName());
        ChatRoomMessage message = new ChatRoomMessage();
        message.setRoom_id(roomInfo.getRoomId() + "");
        message.setAttachment(lotteryBoxAttachment);
        CoreManager.getCore(IIMRoomCore.class).sendMessage(message);
        com.netease.nim.uikit.common.util.log.LogUtil.d("PoundEggDialog", "发送公屏礼物消息" + giftInfo.toString());
    }

    @Override
    public void sendWanFaMeg(String roomDesc, String roomNotice, long roomId) {
        WanFaAttachment wanFaAttachment = new WanFaAttachment(IMCustomAttachment.CUSTOM_MSG_WAN_FA, IMCustomAttachment.CUSTOM_MSG_WAN_FA);
        wanFaAttachment.setRoomDesc(roomDesc);
        wanFaAttachment.setRooNotice(roomNotice);
        wanFaAttachment.setRoomId(roomId);
        ChatRoomMessage message = new ChatRoomMessage();
        message.setRoom_id(roomId + "");
        message.setAttachment(wanFaAttachment);
        CoreManager.getCore(IIMRoomCore.class).sendMessage(message);
    }

    @Override
    public void sendRoomNameMeg(long uid,String roomName , long roomId) {
        ChangeRoomNameAttachment roomNameAttachment = new ChangeRoomNameAttachment(IMCustomAttachment.CUSTOM_MSG_CHANGE_ROOM_NAME, IMCustomAttachment.CUSTOM_MSG_CHANGE_ROOM_NAME);
        roomNameAttachment.setRoomName(roomName);
        roomNameAttachment.setRoomId(roomId);
        roomNameAttachment.setUId(uid);
        ChatRoomMessage message = new ChatRoomMessage();
        message.setRoom_id(roomId + "");
        message.setAttachment(roomNameAttachment);
        CoreManager.getCore(IIMRoomCore.class).sendMessage(message);
    }

    @Override
    public void sendCallGiftMeg(long uid, String giftName, String nick, String targetNick, String giftPic, long roomId) {
        SendCallGiftAttachment roomNameAttachment = new SendCallGiftAttachment(IMCustomAttachment.CUSTOM_MSG_ROOM_CALL_GIFT, IMCustomAttachment.CUSTOM_MSG_ROOM_CALL_GIFT);
        roomNameAttachment.setSendName(nick);
        roomNameAttachment.setTargetName(targetNick);
        roomNameAttachment.setGiftUrl(giftPic);
        roomNameAttachment.setGiftName(giftName);
        roomNameAttachment.setUid(uid);
        ChatRoomMessage message = new ChatRoomMessage();
        message.setRoom_id(roomId + "");
        message.setAttachment(roomNameAttachment);
        CoreManager.getCore(IIMRoomCore.class).sendMessage(message);
    }


    @Override
    public void sendRoomGift(int giftId, final long targetUid, long roomUid, final int giftNum, final int goldPrice, int currentP) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        String ticket = CoreManager.getCore(IAuthCore.class).getTicket();
        params.put("giftId", giftId + "");
        params.put("targetUid", targetUid + "");
        params.put("uid", uid + "");
        params.put("ticket", ticket);
        params.put("roomUid", roomUid + "");
        params.put("giftNum", giftNum + "");
        if (targetUid == roomUid) {
            params.put("type", "1");
        } else {
            params.put("type", "3");
        }

        OkHttpManager.getInstance().doPostRequest(currentP == 2 ? UriProvider.sendGiftProp() : UriProvider.sendGiftV3(),
                params, new OkHttpManager.MyCallBack<ServiceResult<GiftReceiveInfo>>() {

                    @Override
                    public void onError(Exception e) {
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_WALLET_INFO_UPDATE, e.getMessage());
                        notifyClients(IPKCoreClient.class, IPKCoreClient.METHOD_ON_PK_GIFT_FAIL, e.getMessage());
                    }

                    @Override
                    public void onResponse(ServiceResult<GiftReceiveInfo> response) {
                        if (response.isSuccess()) {
                            GiftReceiveInfo data = response.getData();
                            //: 2018/3/1 送礼物成功
                            int price = goldPrice * giftNum;
                            updatePrice(currentP, data.getUseGiftPurseGold());
                            notifyClients(IPKCoreClient.class, IPKCoreClient.METHOD_ON_PK_GIFT, targetUid);
                            sendGiftMessage(data, price);
                            updateUserGiftPurseNum(data.getGiftId(), data.getUserGiftPurseNum());
                        } else if (response.getCode() == 2103) {
                            notifyClients(ICommonClient.class, ICommonClient.METHOD_ON_RECIEVE_NEED_RECHARGE);
                        } else if (response.getCode() == 8000) {
                            notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_GIFT_PAST_DUE);
                            requestGiftInfos();
                        } else if (response.getCode() == 8001) {
                            notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_GIFT_MYSTERY_NOT_ENOUGH);
                        } else {
                            notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_GIFT_SEND_FAIL, response.getCode(), response.getMessage());
                        }
                    }
                });
    }

    private void updatePrice(int currentP, int price) {
        if (currentP == 2) {
            CoreManager.getCore(IPayCore.class).updatePrice(price);
        } else {
            CoreManager.getCore(IPayCore.class).minusGold(price);
        }
    }

    /**
     * 刷新库存数量
     *
     * @param giftId
     * @param userGiftPurseNum
     */
    private void updateUserGiftPurseNum(int giftId, int userGiftPurseNum) {
        List<GiftInfo> giftInfos = CoreManager.getCore(IGiftCore.class).getGiftInfosByType2And3();
        boolean needRefresh = false;
        if (!ListUtils.isListEmpty(giftInfos)) {
            for (int i = 0; i < giftInfos.size(); i++) {
                GiftInfo giftInfo = giftInfos.get(i);
                if (giftInfo == null) {
                    continue;
                }
                if (giftInfo.getGiftId() == giftId) {
                    giftInfo.setUserGiftPurseNum(userGiftPurseNum);
                    needRefresh = true;
                }

            }
        }
        if (needRefresh) {
            notifyClients(IGiftCoreClient.class, IGiftCoreClient.refreshFreeGift);
        }
    }

    //  新版IM 私聊送礼物需要修改
    @Override
    public void sendPersonalGiftToNIM(final int giftId, final long targetUid, final int giftNum, final int goldPrice, final WeakReference<Container> reference, int currentP) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        LogUtils.d("sendRoomGift", 3 + "");
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        String ticket = CoreManager.getCore(IAuthCore.class).getTicket();
        params.put("giftId", giftId + "");
        params.put("targetUid", targetUid + "");
        params.put("uid", uid + "");
        params.put("giftNum", giftNum + "");
        params.put("ticket", ticket);
        params.put("type", "2");

        OkHttpManager.getInstance().doPostRequest(currentP == 2 ? UriProvider.sendGiftProp() : UriProvider.sendGiftV3(),
                params, new OkHttpManager.MyCallBack<ServiceResult<GiftReceiveInfo>>() {
                    @Override
                    public void onError(Exception e) {
                        notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_SEND_PERSONAL_GIFT_FAIL, 0);
                    }

                    @Override
                    public void onResponse(ServiceResult<GiftReceiveInfo> response) {
                        if (response.isSuccess()) {
                            GiftReceiveInfo data = response.getData();
                            LogUtils.d("sendRoomGift", data + "");
                            Container container = reference.get();
                            if (container == null) {
                                return;
                            }
                            long myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
                            UserInfo myUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(myUid);
                            NimGiftAttachment giftAttachment = new NimGiftAttachment(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT, IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_SEND_GIFT);
                            giftAttachment.setUid(myUid + "");
                            GiftReceiveInfo giftRecieveInfo = new GiftReceiveInfo();
                            giftRecieveInfo.setNick(myUserInfo.getNick());
                            giftRecieveInfo.setTargetUid(targetUid);
                            giftRecieveInfo.setAvatar(myUserInfo.getAvatar());
                            giftRecieveInfo.setGiftId(giftId);
                            giftRecieveInfo.setUid(myUid);
                            giftRecieveInfo.setGiftNum(giftNum);
                            giftAttachment.setGiftRecieveInfo(giftRecieveInfo);
                            CustomMessageConfig customMessageConfig = new CustomMessageConfig();
                            customMessageConfig.enablePush = false;
                            IMMessage imMessage = MessageBuilder.createCustomMessage(targetUid + "", SessionTypeEnum.P2P, "", giftAttachment, customMessageConfig);
                            container.proxy.sendMessage(imMessage);

                            parsePersonalNimAttachMent((CustomAttachment) imMessage.getAttachment());
                            updatePrice(currentP, data.getUseGiftPurseGold());
                            updateUserGiftPurseNum(data.getGiftId(), data.getUserGiftPurseNum());
                        } else {
                            if (response.getCode() == 2103) {
                                notifyClients(ICommonClient.class, ICommonClient.METHOD_ON_RECIEVE_NEED_RECHARGE);
                            } else if (response.getCode() == 8000) {
                                notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_GIFT_PAST_DUE);
                                requestGiftInfos();
                            } else if (response.getCode() == 8001) {
                                notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_GIFT_MYSTERY_NOT_ENOUGH);
                            } else {
                                notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_GIFT_SEND_FAIL, response.getCode(), response.getMessage());
                            }
                            notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_SEND_PERSONAL_GIFT_FAIL, response.getCode());
                        }
                    }
                });
    }

    @Override
    public void sendRoomMultiGift(int giftId, final List<Long> targetUids, long roomUid, final int giftNum, final int goldPrice, int currentP) {
        if (targetUids == null || targetUids.size() <= 0) {
            return;
        }
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        String ticket = CoreManager.getCore(IAuthCore.class).getTicket();
        params.put("giftId", giftId + "");
        params.put("uid", uid + "");
        params.put("ticket", ticket);
        params.put("roomUid", roomUid + "");
        params.put("giftNum", giftNum + "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < targetUids.size(); i++) {
            long targetUid = targetUids.get(i);
            sb.append(targetUid + "");
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        params.put("targetUids", sb.toString());

        OkHttpManager.getInstance().doPostRequest(currentP == 2 ? UriProvider.sendWholeGiftProp() : UriProvider.sendWholeGiftV3(),
                params, new OkHttpManager.MyCallBack<ServiceResult<MultiGiftReceiveInfo>>() {
                    @Override
                    public void onError(Exception e) {
                    }

                    @Override
                    public void onResponse(ServiceResult<MultiGiftReceiveInfo> response) {
                        if (response.isSuccess()) {
                            MultiGiftReceiveInfo data = response.getData();
                            //: 2018/3/1 送礼物成功
                            int price = goldPrice * giftNum * targetUids.size();
                            updatePrice(currentP, data.getUseGiftPurseGold());
                            notifyClients(IPKCoreClient.class, IPKCoreClient.METHOD_ON_PK_MULTI_GIFT, targetUids);
                            sendMultiGiftMessage(data, price);
                            updateUserGiftPurseNum(data.getGiftId(), data.getUserGiftPurseNum());
                        } else if (response.getCode() == 2103) {
                            notifyClients(ICommonClient.class, ICommonClient.METHOD_ON_RECIEVE_NEED_RECHARGE);
                        } else if (response.getCode() == 8000) {
                            notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_GIFT_PAST_DUE);
                            requestGiftInfos();
                        } else if (response.getCode() == 8001) {
                            notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_GIFT_MYSTERY_NOT_ENOUGH);
                        } else {
                            notifyClients(IGiftCoreClient.class, IGiftCoreClient.METHOD_ON_GIFT_SEND_FAIL, response.getCode(), response.getMessage());
                        }
                    }
                });
    }

    @Override
    public GiftInfo findGiftInfoById(int giftId) {
        GiftInfo giftInfo = null;
        if (giftListInfo != null && !ListUtils.isListEmpty(giftListInfo.getGift())) {
            for (int i = 0; i < giftListInfo.getGift().size(); i++) {
                if (giftListInfo.getGift().get(i).getGiftId() == giftId) {
                    giftInfo = giftListInfo.getGift().get(i);
                    return giftInfo;
                }
            }
        }
        return giftInfo;
    }
}
