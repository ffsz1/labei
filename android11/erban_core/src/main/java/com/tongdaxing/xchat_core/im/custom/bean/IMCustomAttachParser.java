package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.bean.attachmsg.RoomQueueMsgAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.ChangeRoomNameAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.SendCallGiftAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.WanFaAttachment;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import org.greenrobot.eventbus.EventBus;

/**
 * 云信自定义消息解析器
 *
 * @author polo
 * 自定义消息类型
 * 包装，解析
 */
public class IMCustomAttachParser {
    private static final String TAG = "IMCustomAttachParser";

    // 根据解析到的消息类型，确定附件对象类型
    public static IMCustomAttachment parse(String json) {
        IMCustomAttachment attachment = null;
        MLog.info(TAG, json);
        try {
            JSONObject object = JSON.parseObject(json);
            int first = object.getInteger("first");
            int second = object.getInteger("second");

            switch (first) {
                case IMCustomAttachment.CUSTOM_MSG_FINGER_GUESSING_GAME_FIRST:
                    attachment = new FingerGuessingGameAttachment(first, second);
                    break;
                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_AUCTION:
                    attachment = new AuctionAttachment(first, second);
                    break;
                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT:
                    attachment = new GiftAttachment(first, second);
                    break;
                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT:
                    attachment = new MultiGiftAttachment(first, second);
                    break;
//                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_OPEN_ROOM_NOTI:
//                    attachment = new OpenRoomNotiAttachment(first, second);
//                    break;
                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_QUEUE:
                    attachment = new RoomQueueMsgAttachment(first, second);
                    break;
                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_FACE:
                    attachment = new FaceAttachment(first, second);
                    break;
//                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_NOTICE:
//                    attachment = new NoticeAttachment(first, second);
//                    break;
                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_ROOM_TIP:
                    attachment = new RoomTipAttachment(first, second);
                    break;
//                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PACKET:
//                    attachment = new RedPacketAttachment(first, second);
//                    break;
//                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_LOTTERY:
//                    attachment = new LotteryAttachment(first, second);
//                    break;
                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_SUPER_GIFT:
                    attachment = new GiftAttachment(first, second);
                    break;
                case IMCustomAttachment.CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM:
                    attachment = new PublicChatRoomAttachment(first, second);
                    break;

                case IMCustomAttachment.CUSTOM_MSG_LOTTERY_BOX:
                    attachment = new LotteryBoxAttachment(first, second);
                    break;
                case IMCustomAttachment.CUSTOM_MSG_WAN_FA:
                    attachment = new WanFaAttachment(first, second);
                    break;
                case IMCustomAttachment.CUSTOM_MSG_TYPE_RULE_FIRST:
                    attachment = new RoomRuleAttachment(first, second);
                    break;
                case IMCustomAttachment.CUSTOM_MSG_CHANGE_ROOM_NAME:
                    attachment = new ChangeRoomNameAttachment(first, second);
                    break;
                case IMCustomAttachment.CUSTOM_MSG_ROOM_CALL_GIFT:
                    attachment = new SendCallGiftAttachment(first, second);
                    break;

                case IMCustomAttachment.CUSTOM_MSG_MIC_IN_LIST:
                    attachment = new MicInListAttachment(first, second);
                    break;
                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_MATCH:
                    attachment = new RoomMatchAttachment(first, second);
                    break;
//                case IMCustomAttachment.CUSTOM_MSG_SHARE_FANS:
//                    attachment = new ShareFansAttachment(first, second);
//                    break;
                case IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PK_FIRST:
                    attachment = new PkCustomAttachment(first, second);
                    break;
                case IMCustomAttachment.CUSTOM_MSG_TYPE_BURST_GIFT:
                    attachment = new BurstGiftAttachment(first, second);
                    break;
                case IMCustomAttachment.CUSTOM_MSG_FIRST_ROOM_CHARM:
                    if (second == IMCustomAttachment.CUSTOM_MSG_SECOND_ROOM_CHARM_UPDATE) {
                        attachment = new RoomCharmAttachment(first, second);
                    }
                    break;
                default:
                    break;
            }
            JSONObject data = object.getJSONObject("data");
            if (attachment != null) {
                attachment.fromJson(data);
            }
            if (first == IMCustomAttachment.CUSTOM_MSG_LOTTERY_BOX) {
                EventBus.getDefault().post(attachment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return attachment;
    }

    public static String packData(int first, int second, JSONObject data) {
        JSONObject object = new JSONObject();
        object.put("first", first);
        object.put("second", second);
        if (data != null) {
            object.put("data", data);
        }
        return object.toJSONString();
    }

    public static Json packData(int first, int second, Json data) {
        Json object = new Json();
        object.set("first", first);
        object.set("second", second);
        if (data != null) {
            object.set("data", data);
        }
        return object;
    }
}