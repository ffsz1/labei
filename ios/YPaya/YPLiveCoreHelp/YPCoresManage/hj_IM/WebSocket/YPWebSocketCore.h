//
//  YPWebSocketCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseCore.h"
#import "YPWebSocketMeetHelp.h"

#define SocktCore GetCore(YPWebSocketCore).YPWebSocketMeetHelp

@interface YPWebSocketCore : YPBaseCore

@property (nonatomic, strong) YPWebSocketMeetHelp * _Nullable YPWebSocketMeetHelp;

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
