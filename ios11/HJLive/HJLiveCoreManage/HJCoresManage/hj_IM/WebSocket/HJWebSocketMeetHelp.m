//
//  WebSocketCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJWebSocketMeetHelp.h"
#import "NSData+JXEncryptAndDecrypt.h"
#import "HJVersionCoreHelp.h"

#define JX_WS_DATA_KEY_ED @"ed"

/**
 *  connet超时时间
 */
#define JX_Connect_Time 10

/**
 *  心跳超时时间
 */
#define JX_HeartBeat_Time 5

/**
 *  连接标记
 */
typedef NS_ENUM(NSInteger, JXMConnectState) {
    /**
     *  连接成功
     */
    JX_Connected   = 0,
    /**
     *  连接中
     */
    JX_Connecting   = 1,
    /**
     *  连接关闭
     */
    JX_Closed       = 2
};

/**
 *  定时器
 */
static void dispatchTimer(id target, double timeInterval,void (^handler)(dispatch_source_t timer))
{
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_source_t timer =dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER,0, 0, queue);
    dispatch_source_set_timer(timer, dispatch_walltime(NULL, 0), (uint64_t)(timeInterval *NSEC_PER_SEC), 0);
    __weak __typeof(target) weaktarget  = target;
    dispatch_source_set_event_handler(timer, ^{
        if (!weaktarget)  {
            dispatch_cancel(timer);
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                if (handler) handler(timer);
            });
        }
    });
    /**
     *  启动定时器
     */
    dispatch_resume(timer);
}

/**
 *  存储回调Map
 */
static NSMapTable *IMCallBlockMap() {
    static NSMapTable *table = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        table = [NSMapTable mapTableWithKeyOptions:NSPointerFunctionsStrongMemory valueOptions:NSPointerFunctionsStrongMemory];
    });
    return table;
}

@interface HJWebSocketMeetHelp()<SRWebSocketDelegate>
/**
 *  socket连接标记
 */
@property (nonatomic, assign) JXMConnectState connectState;

/**
 *  socket计时器
 */
@property (nonatomic, strong) dispatch_source_t timer;

/**
 *  msgId
 */
@property (nonatomic, copy) NSString *msgId;

/**
 *  心跳包 
 */
@property (nonatomic, assign) BOOL heartBeatSwitch;


@end

@implementation HJWebSocketMeetHelp

- (instancetype)init
{
    self = [super init];
    if (self) {
        self.msgId = @"-1";
        self.heartBeatSwitch = YES;
        self.closeReason = JX_Other;
        self.isDestroy = false;
        
        _configuration = [HJWebSocketConfiguration new];
    }
    return self;
}

- (void)dealloc {
    
}

/**
 获取SocketURL
 */
- (NSString *)getSocketURL {
    if (GetCore(HJVersionCoreHelp).isReleaseEnv) return JX_IM_WSS_SOCKET_URL_RELEASE;
    
    return JX_IM_WS_SOCKET_URL_DEBUG;
}

/**
 *  创建socket懒加载
 */
- (SRWebSocket *)socketClient {
    if (!_socketClient) {
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:[self getSocketURL]]];
        request.timeoutInterval = JX_Connect_Time;
        _socketClient = [[SRWebSocket alloc] initWithURLRequest:request];
        _socketClient.delegate = self;
    }
    return _socketClient;
}

/**
 *  socket connect
 */
- (void)openScoket {
    if (self.socketClient.readyState != SR_OPEN || self.socketClient.readyState != SR_CONNECTING) {
        [self.socketClient open];
        NotifyCoreClient(HJWebSocketCoreClient, @selector(createNewConnectSocket), createNewConnectSocket);
    }
}

/**
 *  构建参数
 */
- (NSString *)setSendRoute:(NSString *)route content:(NSDictionary *)content {
    NSString *msgId = self.msgId;
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    if (msgId) {
        [dic setObject:msgId forKey:@"id"];
    }
    
    if (route) {
        [dic setObject:route forKey:@"route"];
    }
    
    if (content) {
        [dic setObject:content forKey:@"req_data"];
    }
    
    return [self encryptFromData:dic];
}

/**
 *  保存发送闭包
 */
- (void)savaBlockWithBlock:(JXSendMessageHandler)handler withMsgId:(NSString *)msgId{
    if (![IMCallBlockMap() objectForKey:msgId]) {
        [IMCallBlockMap() setObject:handler forKey:msgId];
    }
}

/**
 *  删除回调闭包
 */
- (void)deleBlockWithMsg:(NSString *)msgId {
    [IMCallBlockMap() removeObjectForKey:msgId];
}

/**
 *  清空回调闭包
 */
- (void)cleanBlock {
    [IMCallBlockMap() removeAllObjects];
}

/**
 *  ondisconnet
 */
- (void)ondisconnet {
    self.connectState = JX_Closed;
    [self.socketClient close];
    [self cleanBlock];
    [self clearHearBeat];
    self.socketClient = nil;
    self.socketClient.delegate = nil;
}

/**
 *  发送请求
 */
- (void)send:(NSString *)route content:(NSDictionary *)content completion:(nullable JXSendMessageHandler)completion {
    if (self.connectState == JX_Connected && self.socketClient && self.socketClient.readyState == SR_OPEN) {
        [self.socketClient send:[self setSendRoute:route content:content]];
        [self savaBlockWithBlock:completion withMsgId:self.msgId];
        [self checkHasTimeOutWithMsg:self.msgId];
        [self callBackIdIncrease];
    } else {
        NSLog(@"当前socket处于断开状态");
    }
}

/**
 *  延迟5s去查询是否超时
 */
- (void)checkHasTimeOutWithMsg:(NSString *)msgId {
    @weakify(self);
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(JX_HeartBeat_Time * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        @strongify(self);
        [self getBlockAndUserWithMsgId:msgId isFromTimeOut:YES];
    });
}

/**
 *  取出闭包执行
 */
- (void)getBlockAndUserWithMsgId:(id)message isFromTimeOut:(BOOL)timeOut {
    NSString *messageId;
    if (timeOut) {
        messageId = message;
    } else {
        messageId = [NSString stringWithFormat:@"%@",[self getCallBackId:message]];
    }
    if ([IMCallBlockMap() objectForKey:messageId]) {
        JXSendMessageHandler handler = [IMCallBlockMap() objectForKey:messageId];
        if (handler) {
            if (timeOut) {
                handler(JX_HeartBeat_TimeOut, message);
            } else {
                handler(0, message);
            }
        }
        //删除闭包id
        [self deleBlockWithMsg:messageId];
    }
}

/**
 *  创建计时器(心跳包)
 */
- (void)openHeartBeat {
    if (self.heartBeatSwitch) {
        if (_timer != nil) {
            dispatch_cancel(_timer);
        }
        dispatchTimer(self, JX_HeartBeat_Time, ^(dispatch_source_t timer) {
            [self sendHeartBeatData];
        });
    }
}

/**
 *  发送心跳包数据
 */
- (void)sendHeartBeatData {
    if (self.connectState == JX_Connected) {
        [self send:@"heartbeat" content:nil completion:^(NSInteger sockErrCode, id  _Nullable message) {
            if (sockErrCode == JX_HeartBeat_TimeOut) {
                [self ondisconnet];
                self.closeReason = JX_HeartBeat_TimeOut;
                NotifyCoreClient(HJWebSocketCoreClient, @selector(onDisconnect:reason:), onDisconnect:self.closeReason reason:@"心跳包超时");
            }
        }];
    }
}

/**
 *  心跳包的Data
 */
- (NSString *)getHeartBeatData {
    return @"";
}

/**
 *  清空定时器
 */
- (void)clearHearBeat {
    if (self.timer) {
        dispatch_cancel(self.timer);
    }
    self.timer = nil;
}

/**
 *  msgId自增
 */
- (void)callBackIdIncrease {
    NSInteger msgId = [self.msgId integerValue];
    if (msgId == NSIntegerMin) {
        self.msgId = @"-1";
        return;
    }
    msgId--;
    self.msgId = [NSString stringWithFormat:@"%ld",(long)msgId];
}

/**
 *  收到消息
 */
- (void)receiveMessage:(id)message {
    if (self.connectState == JX_Connecting || self.connectState == JX_Closed) {return;}
    NSDictionary *dic = [JSONTools ll_dictionaryWithJSON:message];
    NSString *route = dic[@"route"];
    if (![route isEqualToString:@"notciefromsvr"]) {
        [self getBlockAndUserWithMsgId:message isFromTimeOut:false];
    }
    NotifyCoreClient(HJWebSocketCoreClient, @selector(onReceiveMessage:), onReceiveMessage:message);
}

- (NSDictionary *)decryptFromData:(id)data {
    NSDictionary *buffer = [JSONTools ll_dictionaryWithJSON:data];
    NSDictionary *dic = nil;
    if (self.configuration.decryptType == JXCryptAlgorithmAES) {
        NSString *encryptString = buffer[JX_WS_DATA_KEY_ED];
        NSData *data = [NSData dataWithBase64EncodedString:encryptString];
        data = [data jx_AES256DecryptWithKey:[self.configuration.decryptKey dataUsingEncoding:NSUTF8StringEncoding] iv:[self.configuration.decryptIv dataUsingEncoding:NSUTF8StringEncoding]];
        NSString *decryptString = data.utf8String;
        
        dic = [JSONTools ll_dictionaryWithJSON:decryptString];
    } else {
        dic = [JSONTools ll_dictionaryWithJSON:buffer];
    }
    return dic;
}

- (NSString *)encryptFromData:(id)dic {
    NSString *jsonString = [dic yy_modelToJSONString];
    if (self.configuration.encryptType == JXCryptAlgorithmAES) {
        NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
        data = [data jx_AES256EncryptWithKey:[self.configuration.encryptKey dataUsingEncoding:NSUTF8StringEncoding] iv:[self.configuration.encryptIv dataUsingEncoding:NSUTF8StringEncoding]];
        NSString *encryptString = [data base64EncodedString];
        NSDictionary *buffer = @{
                                 JX_WS_DATA_KEY_ED : JX_STR_AVOID_nil(encryptString),
                                 };
        
        return [buffer yy_modelToJSONString];
    } else {
        return jsonString;
    }
}

/**
 *  解析返回的messageId
 */
- (NSString *)getCallBackId:(id)message {
    NSDictionary *messageDic = [JSONTools ll_dictionaryWithJSON:message];
    return (NSString *)[messageDic objectForKey:@"id"];
}

/**
 *  原生socket收到消息回调
 */
- (void)webSocket:(SRWebSocket *)webSocket didReceiveMessage:(id)message {
//    NSLog(@"\n\n\n===================webSocketDidReceiveMessage===================\n%@ \n didReceiveMessage%@\n\n\n%@\n\n\n",webSocket, [self decryptFromData:message], message);
    if (self.isDestroy) {return;}
    NSDictionary *dict = [self decryptFromData:message];
    [self receiveMessage:dict];
}

/**
 *  原生socket连接成功
 */
- (void)webSocketDidOpen:(SRWebSocket *)webSocket {
//    NSLog(@"\n\n\n==================webSocketDidOpen==================\n%@\n\n\n",webSocket);
    self.connectState = JX_Connected;
    if (!_timer) {
        //开启心跳
        [self openHeartBeat];
    }
    NotifyCoreClient(HJWebSocketCoreClient, @selector(onConnectSuccess), onConnectSuccess);
}

/**
 *  原生socket关闭
 */
- (void)webSocket:(SRWebSocket *)webSocket didCloseWithCode:(NSInteger)code reason:(NSString *)reason wasClean:(BOOL)wasClean {
//    NSLog(@"\n\n\n==================webSocketDidClose==================\n%@\n\n\n",webSocket);
    if (self.isDestroy) {return;}
//    if (!wasClean) {
//        [GetCore(WebSocketCore).noDeallocSocketList addObject:self];
//        if (self.closeReason != JX_Self_Close) {self.closeReason = JX_Other;}
//        self.connectState = JX_Closed;
//        [self cleanBlock];
//        [self clearHearBeat];
//        NotifyCoreClient(HJWebSocketCoreClient, @selector(onDisconnect:reason:), onDisconnect:self.closeReason reason:reason);
//        return;
//    }
    if (self.closeReason != JX_Self_Close) {self.closeReason = JX_Other;}
    [self ondisconnet];
    NotifyCoreClient(HJWebSocketCoreClient, @selector(onDisconnect:reason:), onDisconnect:self.closeReason reason:reason);
}

/**
 *  原生socket报错
 */
- (void)webSocket:(SRWebSocket *)webSocket didFailWithError:(NSError *)error {
//    NSLog(@"\n\n\n==================webSocketDidFail===================\n%@ didFailWithError%@\n\n\n",webSocket, error);
    if (self.isDestroy) {return;}
    if (self.closeReason != JX_Self_Close) {self.closeReason = JX_Other;}
    [self ondisconnet];
    NotifyCoreClient(HJWebSocketCoreClient, @selector(onDisconnect:reason:), onDisconnect:self.closeReason reason:error.localizedDescription);
}

@end

