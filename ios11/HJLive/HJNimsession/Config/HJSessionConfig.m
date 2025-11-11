//
//  HJSessionConfig.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSessionConfig.h"
#import "NIMSessionConfig.h"
#import "NSString+NTES.h"

#import "UIImage+Utils.h"


@implementation HJSessionConfig

- (NSArray<NIMMediaItem *> *)mediaItems {
//    NSArray *defaultMediaItems = [NIMKitUIConfig sharedConfig].defaultMediaItems;
    NIMMediaItem *sendPic = [NIMMediaItem item:@"onTapMediaItemPicture:"
                                    normalImage:[UIImage imageNamed:@"hj_message_item_send_pic"]
                                  selectedImage:[UIImage imageNamed:@"hj_message_item_send_pic"]
                                          title:@"   图片"];
    NIMMediaItem *sendGift = [NIMMediaItem item:@"onTapSendGift:"
                                     normalImage:[UIImage imageNamed:@"hj_xiaoxiliwu"]
                                   selectedImage:[UIImage imageNamed:@"hj_xiaoxiliwu"]
                                           title:@"   礼物"];
    NIMMediaItem *sendGold = [NIMMediaItem item:@"onTapSendGold:"
      normalImage:[UIImage imageNamed:@"yp_hongbao_message"]
    selectedImage:[UIImage imageNamed:@"yp_hongbao_message"]
            title:@"   红包"];
    
    NSArray *items = @[];
    BOOL isMe   = _session.sessionType == NIMSessionTypeP2P && [_session.sessionId isEqualToString:[[NIMSDK sharedSDK].loginManager currentAccount]];
    if (!isMe) {
        items = @[sendPic,sendGift,sendGold];
    }
    return items;
}

- (BOOL)shouldHandleReceipt{
    return YES;
}

- (BOOL)shouldHandleReceiptForMessage:(NIMMessage *)message
{
    //文字，语音，图片，视频，文件，地址位置和自定义消息都支持已读回执，其他的不支持
    NIMMessageType type = message.messageType;
//    if (type == NIMMessageTypeCustom) {
//        NIMCustomObject *object = (NIMCustomObject *)message.messageObject;
//        id attachment = object.attachment;
        
//    }
    
    
    
    return type == NIMMessageTypeText ||
    type == NIMMessageTypeAudio ||
    type == NIMMessageTypeImage ||
    type == NIMMessageTypeVideo ||
    type == NIMMessageTypeFile ||
    type == NIMMessageTypeLocation ||
    type == NIMMessageTypeCustom;
}

- (UIImage *)sessionBackgroundImage {
    return [UIImage imageWithColor:UIColorHex(F5F5F5)];
}


@end

