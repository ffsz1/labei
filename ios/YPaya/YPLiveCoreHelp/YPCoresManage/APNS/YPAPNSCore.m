//
//  YPAPNSCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAPNSCore.h"
#import "HJAPNSCoreClient.h"
#import "HJImLoginCoreClient.h"
#import "NSObject+YYModel.h"

@interface YPAPNSCore () <HJImLoginCoreClient>



@end

@implementation YPAPNSCore

- (instancetype)init {
    
    self = [super init];
    if (self) {
        AddCoreClient(HJImLoginCoreClient, self);
    }
    return self;
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

#pragma mark - ImLoginCoreClient

- (void)onRecieveRemoteNotification:(NSDictionary *)payload {
    NSInteger type = [[payload objectForKey:@"skiptype"] integerValue];
    
    if (type == 1) { //转跳APP原生界面
        if (payload[@"data"]) {
            NSString *dataStr = [payload objectForKey:@"data"];
            NSData *jsonData = [dataStr dataUsingEncoding:NSUTF8StringEncoding];

            NSDictionary *data = [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers error:nil];
            NSInteger skiproute = [[data objectForKey:@"skiproute"]integerValue];

            if (skiproute == 1) { //跳密聊列表
                NotifyCoreClient(HJAPNSCoreClient, @selector(onRequestToPushChatList), onRequestToPushChatList);
            } else if (skiproute == 2) { //直接拉起打电话
            }
            
        }
    } else if (type == 2) { //转跳房间内
        NSNumber *uid;
        if ([payload[@"data"] isKindOfClass:[NSDictionary class]]) {
            uid = payload[@"data"][@"uid"];
        } else {
            uid = payload[@"data"];
        }
        NotifyCoreClient(HJAPNSCoreClient, @selector(onRequestToOpenRoomWithUid:), onRequestToOpenRoomWithUid:uid.userIDValue);
    } else if (type == 3) { //转跳H5
        
    }
}

@end
