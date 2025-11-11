package com.vslk.lbgx.room.avroom.binding;

import android.databinding.BindingAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;

/**
 * Created by huangmeng1 on 2018/1/19.
 */

public class ViewAdapter {

    // 新版IM 通知类型消息，需要单独处理
    @BindingAdapter(value = {"chatRoomMessage"},requireAll = false)
    public static void setMsgNotification(TextView tvContent,ChatRoomMessage chatRoomMessage) {
//        if (chatRoomMessage==null) return;
//        ChatRoomNotificationAttachment attachment = (ChatRoomNotificationAttachment) chatRoomMessage.getAttachment();
//        String senderNick = "";
//        List<String> nicks = attachment.getTargetNicks();
//        if (nicks != null && nicks.size() > 0) {
//            senderNick = attachment.getTargetNicks().get(0);
//        }
//
//        if (attachment.getType() == NotificationType.ChatRoomMemberIn) {
//            if (senderNick.length()>4){
//                tvContent.setText(senderNick.substring(0,4)+"..");
//            }else{
//                tvContent.setText(senderNick);
//            }
//        }
    }

    @BindingAdapter(value = {"attachment"},requireAll = false)
    public static void setMsgHeaderGift(LinearLayout llContent, IMCustomAttachment attachment) {
       /* if (attachment==null) return;
        TextView tvNick= (TextView) llContent.getChildAt(0);
        TextView tvContent= (TextView) llContent.getChildAt(1);
        TextView tvNickTarget= (TextView) llContent.getChildAt(2);
        TextView tvGift= (TextView) llContent.getChildAt(3);
        TextView tvGiftNumber= (TextView) llContent.getChildAt(4);
        if (attachment instanceof GiftAttachment){
            GiftAttachment giftAttachment = (GiftAttachment) attachment;
            String nick = giftAttachment.getGiftRecieveInfo().getNick();
            String targetNick = giftAttachment.getGiftRecieveInfo().getTargetNick();
            if (!TextUtils.isEmpty(nick) && nick.length() > 6) {
                nick = nick.substring(0, 6) + "...";
            }
            if (!TextUtils.isEmpty(targetNick) && targetNick.length() > 6) {
                targetNick = targetNick.substring(0, 6) + "...";
            }
            tvNick.setText(nick);
            tvContent.setText("送给");
            tvNickTarget.setText(targetNick);
            GiftInfo giftInfo = CoreManager.getCore(IGiftCore.class).findGiftInfoById(giftAttachment.getGiftRecieveInfo().getGiftId());
            if (giftInfo != null) {
                tvGiftNumber.setText("X" + giftAttachment.getGiftRecieveInfo().getGiftNum());
                tvGift.setText(giftInfo.getGiftName());
            }
        }else if (attachment instanceof MultiGiftAttachment){
            MultiGiftAttachment giftAttachment = (MultiGiftAttachment) attachment;
            String nick = giftAttachment.getMultiGiftRecieveInfo().getNick();
            if (!TextUtils.isEmpty(nick) && nick.length() > 6) {
                nick = nick.substring(0, 6) + "...";
            }
            tvNick.setText(nick);
            tvContent.setText("全麦送出");
            tvNickTarget.setText("");
            GiftInfo giftInfo = CoreManager.getCore(IGiftCore.class).findGiftInfoById(giftAttachment.getMultiGiftRecieveInfo().getGiftId());
            if (giftInfo != null) {
                tvGiftNumber.setText("X" + giftAttachment.getMultiGiftRecieveInfo().getGiftNum());
                tvGift.setText(giftInfo.getGiftName());
            }
        }*/
    }
}
