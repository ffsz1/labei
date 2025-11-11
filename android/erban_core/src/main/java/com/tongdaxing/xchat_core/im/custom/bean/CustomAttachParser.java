package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;
import com.tongdaxing.xchat_core.im.custom.bean.nim.LotteryAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.nim.NimGiftAttachment;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

/**
 * 云信自定义消息解析器
 *
 * @author polo
 *         自定义消息类型
 *         包装，解析
 */
public class CustomAttachParser implements MsgAttachmentParser {
    private static final String TAG = "CustomAttachParser";

    // 根据解析到的消息类型，确定附件对象类型
    @Override
    public MsgAttachment parse(String json) {
        CustomAttachment attachment = null;
        MLog.info(TAG, json);
        try {
            JSONObject object = JSON.parseObject(json);
            int first = object.getInteger("first");
            int second = object.getInteger("second");

            switch (first) {
                case CustomAttachment.CUSTOM_MSG_HEADER_TYPE_GIFT:
                    attachment = new NimGiftAttachment(first, second);
                    break;
                case CustomAttachment.CUSTOM_MSG_HEADER_TYPE_OPEN_ROOM_NOTI:
                    attachment = new OpenRoomNotiAttachment(first, second);
                    break;
                case CustomAttachment.CUSTOM_MSG_HEADER_TYPE_NOTICE:
                    attachment = new NoticeAttachment(first, second);
                    break;
                case CustomAttachment.CUSTOM_MSG_HEADER_TYPE_PACKET:
                    attachment = new RedPacketAttachment(first, second);
                    break;
                case CustomAttachment.CUSTOM_MSG_HEADER_TYPE_LOTTERY:
                    attachment = new LotteryAttachment(first, second);
                    break;
                case CustomAttachment.CUSTOM_MSG_SHARE_FANS:
                    attachment = new ShareFansAttachment(first, second);
                    break;
                default:
                    break;
            }
            JSONObject data = object.getJSONObject("data");
            if (attachment != null) {
                attachment.fromJson(data);
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
}