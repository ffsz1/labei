//
//  WebSocketCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"
#import <SocketRocket.h>
#import "HJWebSocketCoreClient.h"
#import <YYModel.h>
#import "HJWebSocketConfiguration.h"

/**
 *  发送消息请求回调
 */
typedef void(^JXSendMessageHandler)(NSInteger sockErrCode, id _Nullable message);
/**
 *  断开原因
 */
typedef NS_ENUM(NSInteger, JXMConnectCloseReason) {
    /**
     *  心跳包超时
     */
    JX_HeartBeat_TimeOut   = 408,
    /**
     *  手动关闭
     */
    JX_Self_Close    = 410,
    /*
     *其他原因
     */
    JX_Other = 409
};

@interface HJWebSocketMeetHelp : BaseCore
/**
 *  socket断线原因
 */
@property (nonatomic, assign) JXMConnectCloseReason closeReason;
/**
 *  socket对象
 */
@property (nonatomic, strong) SRWebSocket * _Nullable socketClient;
/**
 *  是否销毁
 */
@property (nonatomic, assign) BOOL isDestroy;

/**
 配置信息
 */
@property (nonatomic, strong) HJWebSocketConfiguration *configuration;

/**
 *  发送请求
 */
- (void)send:(NSString *_Nullable)route content:(NSDictionary *)content completion:(nullable JXSendMessageHandler)completion;

/**
 *  ondisconnet
 */
- (void)ondisconnet;

/**
 *  socket connect
 */
- (void)openScoket;
@end
