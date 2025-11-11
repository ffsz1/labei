//
//  WebSocketCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import "HJWebSocketMeetHelp.h"

#define SocktCore GetCore(WebSocketCore).HJWebSocketMeetHelp

@interface WebSocketCore : BaseCore

@property (nonatomic, strong) HJWebSocketMeetHelp * _Nullable HJWebSocketMeetHelp;

//@property (nonatomic, strong) NSMutableArray *noDeallocSocketList;

- (void)connect;

- (void)disConnect;

/**
 *  发送请求
 */
- (void)send:(NSString *_Nullable)route
     content:(NSDictionary *_Nonnull)content
     success:(void(^_Nullable)(id _Nullable data))success
     failure:(void(^_Nullable)(NSInteger code, NSString * _Nullable errmsg))failure;

@end
