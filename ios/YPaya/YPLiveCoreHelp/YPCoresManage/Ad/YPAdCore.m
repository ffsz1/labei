//
//  YPAdCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAdCore.h"
#import "HJAdCoreClient.h"
#import "YPAdCache.h"
#import "HJImMessageCoreClient.h"
#import "YPImMessageCore.h"

@interface YPAdCore()
<
    HJImMessageCoreClient
>


@end

@implementation YPAdCore

- (instancetype)init
{
    self = [super init];
    if (self) {
        AddCoreClient(HJImMessageCoreClient, self);
    }
    return self;
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}


- (void)setSplash:(YPAdInfo *)splash {
    _splash = splash;
    if (splash.pict.length > 0) {
        [[YPAdCache shareCache]saveAdInfo:splash];
    }
   
}


#pragma mark - ImMessageCoreClient

- (void)onRecvP2PCustomMsg:(NIMMessage *)msg {
    NIMCustomObject *obj = (NIMCustomObject *)msg.messageObject;
    if (obj.attachment != nil && [obj.attachment isKindOfClass:[YPAttachment class]]) {
        YPAttachment *attachment = (YPAttachment *)obj.attachment;
        if (attachment.first == Custom_Noti_Header_Turntable) {
            NotifyCoreClient(HJAdCoreClient, @selector(onReceiveTurntableMessage), onReceiveTurntableMessage);
            //            YPGiftAllMicroSendInfo *info = [YPGiftAllMicroSendInfo yy_modelWithJSON:attachment.data];
            //            NotifyCoreClient(GiftCoreClient, @selector(onReceiveGift:), onReceiveGift:info);
        }
    }
}

@end
