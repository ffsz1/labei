//
//  HJAdCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAdCore.h"
#import "HJAdCoreClient.h"
#import "HJAdCache.h"
#import "HJImMessageCoreClient.h"
#import "HJImMessageCore.h"

@interface HJAdCore()
<
    HJImMessageCoreClient
>


@end

@implementation HJAdCore

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


- (void)setSplash:(AdInfo *)splash {
    _splash = splash;
    if (splash.pict.length > 0) {
        [[HJAdCache shareCache]saveAdInfo:splash];
    }
   
}


#pragma mark - ImMessageCoreClient

- (void)onRecvP2PCustomMsg:(NIMMessage *)msg {
    NIMCustomObject *obj = (NIMCustomObject *)msg.messageObject;
    if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
        Attachment *attachment = (Attachment *)obj.attachment;
        if (attachment.first == Custom_Noti_Header_Turntable) {
            NotifyCoreClient(HJAdCoreClient, @selector(onReceiveTurntableMessage), onReceiveTurntableMessage);
            //            HJGiftAllMicroSendInfo *info = [HJGiftAllMicroSendInfo yy_modelWithJSON:attachment.data];
            //            NotifyCoreClient(GiftCoreClient, @selector(onReceiveGift:), onReceiveGift:info);
        }
    }
}

@end
