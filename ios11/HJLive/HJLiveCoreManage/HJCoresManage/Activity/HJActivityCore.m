//
//  HJActivityCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJActivityCore.h"
#import "HJHttpRequestHelper+Activity.h"
#import "HJActivityCoreClient.h"
#import "HJImMessageCoreClient.h"
#import "Attachment.h"
#import "NSObject+YYModel.h"
#import "HJRedInfo.h"

@interface HJActivityCore ()
<
    HJImMessageCoreClient
>


@end

@implementation HJActivityCore

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

- (void)getActivity:(NSInteger)type {
    [HJHttpRequestHelper getActivityWithType:type success:^(ActivityInfo *info) {
        self.activityInfo = info;
        NotifyCoreClient(HJActivityCoreClient, @selector(getActivityInfoSuccess), getActivityInfoSuccess);
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}

//获取所有活动
- (void)getAllActivity {
    [HJHttpRequestHelper getAllActivitySuccess:^(NSArray *infoArr) {
        NotifyCoreClient(HJActivityCoreClient, @selector(getAllActivityInfoSuccess:), getAllActivityInfoSuccess:infoArr);
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}

#pragma mark - ImMessageCoreClient
- (void)onRecvP2PCustomMsg:(NIMMessage *)msg {
    NIMCustomObject *obj = (NIMCustomObject *)msg.messageObject;
    if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
        Attachment *attachment = (Attachment *)obj.attachment;
        if (attachment.first == Custom_Noti_Header_RedPacket) {
            HJRedInfo *info = [HJRedInfo yy_modelWithJSON:attachment.data];
            NotifyCoreClient(HJActivityCoreClient, @selector(onReceiveP2PRedPacket:), onReceiveP2PRedPacket:info);
        }
        
    }
}

@end
