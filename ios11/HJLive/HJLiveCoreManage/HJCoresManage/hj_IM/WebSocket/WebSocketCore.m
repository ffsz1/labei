//
//  WebSocketCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "WebSocketCore.h"
#import <AFNetworkReachabilityManager.h>
#import "HJWebSocketCoreClient.h"
#import "NSString+JsonToDic.h"
#import "HJIMRequestManager+Login.h"
#import "HJImLoginCoreClient.h"

typedef NS_ENUM(NSUInteger, WebSocketError) {
    
    WebSocketError_NoError                      = 0,    ///< 成功
    WebSocketError_NeedLogin                    = 10002, ///< 需要登录
    WebSocketError_SystemError                  = 10003, ///< 系统错误
    WebSocketError_TokenExpire                  = 10004, ///< Token过期
    WebSocketError_GetUserInfoError             = 100101, ///< 获取用户信息失败
    WebSocketError_RoomNotFound                 = 10021, ///< 进入房间失败 找不到房间信息or房间关闭
    WebSocketError_CashFailure                  = 10027, ///< 远程扣费接口调用失败
    WebSocketError_RoomClosed                   = 10029, ///< 房间已经关闭
    WebSocketError_BekickOff                    = 10037, ///< 被挤掉线 默认通知内容
    WebSocketError_NotAuthorized                = 10039, ///< 不能操作 不是对应的userid
    WebSocketError_RoomNotExit                  = 16104, ///< 直播间不存在
    WebSocketError_RoomBeClosed                 = 16106, ///< 直播间已关闭
    WebSocketError_RoomOwnerError               = 16108, ///< 主播id与直播场次的主播id不合
    WebSocketError_CloseRoomError               = 16110, ///< 关闭直播场次,数据表操作出错
    WebSocketError_CantCloseRoom                = 16122, ///< 主播立即关闭私密直播间, 不满足关闭条件
};

@interface WebSocketCore()<HJWebSocketCoreClient,HJImLoginCoreClient>

@end

@implementation WebSocketCore

- (instancetype)init
{
    self = [super init];
    if (self) {
        AddCoreClient(HJWebSocketCoreClient, self);
        AddCoreClient(HJImLoginCoreClient, self);
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(connectWhenWillEnterForeground) name:UIApplicationWillEnterForegroundNotification object:nil];
    }
    return self;
}

- (void)dealloc {
    RemoveCoreClientAll(self);
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)disConnect {
    SocktCore.closeReason = JX_Self_Close;
    [SocktCore.socketClient close];
}

//- (NSMutableArray *)noDeallocSocketList {
//    if (!_noDeallocSocketList) {
//        _noDeallocSocketList = [NSMutableArray array];
//    }
//    return _noDeallocSocketList;
//}

/**
 *  移除所有未clean的socket对象
 */
- (void)onConnectSuccess {
    [HJIMRequestManager autoLoginWithSuccess:^{
        NSLog(@"自动登录成功");
        NotifyCoreClient(HJWebSocketCoreClient, @selector(onSocLoginSuccess), onSocLoginSuccess);
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        NSLog(@"自动登录失败");
        NotifyCoreClient(HJWebSocketCoreClient, @selector(onSocLoginFail), onSocLoginFail);
        NotifyCoreClient(HJImLoginCoreClient, @selector(onImLoginFailth), onImLoginFailth);
    }];
//    if (self.noDeallocSocketList.count == 0) {return;}
//    for (HJWebSocketMeetHelp *mi in self.noDeallocSocketList) {
//        [mi ondisconnet];
//    }
//    [self.noDeallocSocketList removeAllObjects];
}

/**
 *  销毁HJWebSocketMeetHelp对象
 */
- (void)destroy {
    if (self.HJWebSocketMeetHelp) {
        self.HJWebSocketMeetHelp.isDestroy = true;
        [self.HJWebSocketMeetHelp ondisconnet];
        self.HJWebSocketMeetHelp = nil;
    }
}

/**
 *  是否在clean中
 */
//- (BOOL)isInNoCleanList:(HJWebSocketMeetHelp *)HJWebSocketMeetHelp {
//    if ([self.noDeallocSocketList containsObject:HJWebSocketMeetHelp]) {
//        return YES;
//    }
//        return false;
//}

- (void)reOpenSocket {
    return;
}

- (void)onDisconnect:(NSInteger)code reason:(NSString *)reason {
    @weakify(self);
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        @strongify(self);
        if (code == JX_HeartBeat_TimeOut) {
            [self connect];
        } else if (code == JX_Self_Close) {
        } else if (code == JX_Other) {
            [self connect];
        }
    });
}

- (void)connectWhenWillEnterForeground {
    if (SocktCore.closeReason == JX_Self_Close) return;
    
    [self connect];
}

/**
 *  连接
 */
- (void)connect {
    if (![AFNetworkReachabilityManager sharedManager].reachable) {
        NotifyCoreClient(HJWebSocketCoreClient, @selector(onDisconnect:reason:), onDisconnect:JX_Other reason:@"连接失败，网络不可用");
        return;
    }
    
    if (SocktCore.socketClient.readyState == SR_OPEN) {return;}
    
//    if (![self isInNoCleanList:self.HJWebSocketMeetHelp]) {[self destroy];};
    [self destroy];
    self.HJWebSocketMeetHelp = [[HJWebSocketMeetHelp alloc] init];
    [self.HJWebSocketMeetHelp openScoket];
}


/**
 *  发送请求
 */
- (void)send:(NSString *_Nullable)route
     content:(NSDictionary *)content
     success:(void(^)(id data))success
     failure:(void(^)(NSInteger code, NSString *errmsg))failure {
    
    [SocktCore send:route content:content completion:^(NSInteger sockErrCode, id  _Nullable message) {
        
        if (sockErrCode == 0) {
            
            NSDictionary *mesDic = nil;
            
            if ([message isKindOfClass:[NSDictionary class]]) {
                mesDic = message;
            }
            else if ([message isKindOfClass:[NSString class]]) {
                
                mesDic = [JSONTools ll_dictionaryWithJSON:message];
            }
                NSInteger errorCode = [mesDic[@"res_data"][@"errno"] integerValue];
                NSString *errorMessage = [mesDic[@"res_data"][@"errmsg"] description];
                
                switch (errorCode) {
                    case WebSocketError_NoError: {
                        // 成功
                        id data = mesDic[@"res_data"][@"data"];
                        if (success) {
                            success(data);
                        }
                        break;
                    }
                    case WebSocketError_GetUserInfoError: {
                        // 获取用户信息失败
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                        
                    case WebSocketError_NeedLogin: {
                        // 需要登录
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                    case WebSocketError_SystemError: {
                        // 系统错误
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                    case WebSocketError_TokenExpire: {
                        // Token过期
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                    case WebSocketError_RoomNotFound: {
                        // 进入房间失败 找不到房间信息or房间关闭
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                    case WebSocketError_CashFailure: {
                        // 远程扣费接口调用失败
                        if (failure) {
                            failure(errorCode,errorMessage);
                        };
                    }
                    case WebSocketError_RoomClosed: {
                        // 房间已经关闭
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                    case WebSocketError_BekickOff: {
                        // 被挤掉线 默认通知内容
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                    case WebSocketError_NotAuthorized: {
                        // 不能操作 不是对应的userid
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                        
                    case WebSocketError_RoomNotExit: {
                        // 直播间不存在
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                    case WebSocketError_RoomBeClosed: {
                        // 直播间已关闭
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                    case WebSocketError_RoomOwnerError: {
                        // 主播id与直播场次的主播id不合
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                    case WebSocketError_CloseRoomError: {
                        // 关闭直播场次,数据表操作出错
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                    case WebSocketError_CantCloseRoom: {
                        // 主播立即关闭私密直播间, 不满足关闭条件
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                    default:
                    {
                        if (failure) {
                            failure(errorCode,errorMessage);
                        }
                        break;
                    }
                    break;
                }
        }
        else {
            if (failure) {
                failure(sockErrCode, @"");
            }
        }
    }];
}

@end
