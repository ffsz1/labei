//
//  WebSocketReceiveCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "WebSocketReceiveCore.h"
#import "HJIMDefines.h"
#import "NSDictionary+YYAdd.h"
#import "HJIMReceiveMessageInfo.h"

#import "HJImRoomCoreV2.h"

@interface WebSocketReceiveCore()<HJWebSocketCoreClient>

@end

@implementation WebSocketReceiveCore

- (instancetype)init
{
    self = [super init];
    if (self) {
        AddCoreClient(HJWebSocketCoreClient, self);
    }
    return self;
}

- (void)onReceiveMessage:(id)msg {
    NSDictionary *dic = [JSONTools ll_dictionaryWithJSON:msg];
    HJIMReceiveMessageInfo *messageInfo = [HJIMReceiveMessageInfo yy_modelWithJSON:dic];
    
    if (!messageInfo.route.length) return;
    
#pragma mark 登录/注销
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_KICK_OFF]) {
        NSInteger code = [messageInfo.req_data[@"errno"] integerValue];
        NSString *message = messageInfo.req_data[@"errmsg"];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onSocketWillKickOffNotifyMessage:errmsg:), onSocketWillKickOffNotifyMessage:code errmsg:message);
        [GetCore(WebSocketCore) disConnect];
        return;
    }
    
#pragma mark 房间管理
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_ENTER_ROOM]) {
        ChatRoomMember *member = [ChatRoomMember yy_modelWithJSON:messageInfo.req_data[@"member"]];
        
        if ([messageInfo.req_data containsObjectForKey:@"online_num"]) {
            NSString *num = messageInfo.req_data[@"online_num"];
            GetCore(HJImRoomCoreV2).onlineNumber = [num integerValue];
        }
        
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onEnterRoomNotifyMessage:), onEnterRoomNotifyMessage:member);
        return;
    }
    
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_EXIT_ROOM]) {
        NSString *userId = messageInfo.req_data[@"uid"];
        NSString *nickName = messageInfo.req_data[@"nickname"];
        NSString *roomId = messageInfo.req_data[@"room_id"];
        
        if ([messageInfo.req_data containsObjectForKey:@"online_num"]) {
            NSString *num = messageInfo.req_data[@"online_num"];
            GetCore(HJImRoomCoreV2).onlineNumber = [num integerValue];
        }
        
        
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onExitRoomNotifyMessage:uid:nickName:), onExitRoomNotifyMessage:roomId uid:userId nickName:nickName);
        return;
    }
    
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_ROOM_INFO_UPDATE]) {
        ChatRoomInfo *roomInfo = [ChatRoomInfo yy_modelWithJSON:messageInfo.req_data[@"room_info"]];
        if ([messageInfo.req_data containsObjectForKey:@"online_num"]) {
            NSString *num = messageInfo.req_data[@"online_num"];
            GetCore(HJImRoomCoreV2).onlineNumber = [num integerValue];
        }

        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onRoomInfoUpdatedNotifyMessage:), onRoomInfoUpdatedNotifyMessage:roomInfo);
        return;
    }
    
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_ROOM_BLACKLIST_ADD]) {
        ChatRoomMember *member = [ChatRoomMember yy_modelWithJSON:messageInfo.req_data[@"member"]];
        
        if ([messageInfo.req_data containsObjectForKey:@"online_num"]) {
            NSString *num = messageInfo.req_data[@"online_num"];
            GetCore(HJImRoomCoreV2).onlineNumber = [num integerValue];
        }
        
        NSString *roomId = messageInfo.req_data[@"room_id"];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onRoomBlacklistAddNotifyMessage:member:), onRoomBlacklistAddNotifyMessage:roomId member:member);
        return;
    }
    
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_ROOM_BLACKLIST_REMOVE]) {
        ChatRoomMember *member = [ChatRoomMember yy_modelWithJSON:messageInfo.req_data[@"member"]];
        NSString *roomId = messageInfo.req_data[@"room_id"];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onRoomBlacklistRemoveNotifyMessage:member:), onRoomBlacklistRemoveNotifyMessage:roomId member:member);
        return;
    }
    
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_ROOM_MANAGER_ADD]) {
        ChatRoomMember *member = [ChatRoomMember yy_modelWithJSON:messageInfo.req_data[@"member"]];
        NSString *roomId = messageInfo.req_data[@"room_id"];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onRoomManagerAddNotifyMessage:member:), onRoomManagerAddNotifyMessage:roomId member:member);
        return;
    }
    
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_ROOM_MANAGER_REMOVE]) {
        ChatRoomMember *member = [ChatRoomMember yy_modelWithJSON:messageInfo.req_data[@"member"]];
        NSString *roomId = messageInfo.req_data[@"room_id"];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onRoomManagerRemoveNotifyMessage:member:), onRoomManagerRemoveNotifyMessage:roomId member:member);
        return;
    }
    
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_ROOM_MEMBER_KICKED]) {
        
        if ([messageInfo.req_data containsObjectForKey:@"online_num"]) {
            NSString *num = messageInfo.req_data[@"online_num"];
            GetCore(HJImRoomCoreV2).onlineNumber = [num integerValue];
        }
        
        NSString *userId = messageInfo.req_data[@"uid"];
        NSString *nickName = messageInfo.req_data[@"nickname"];
        NSString *roomId = messageInfo.req_data[@"room_id"];
        NSString *avatar = messageInfo.req_data[@"avatar"];
        NSInteger reasonType = [messageInfo.req_data[@"reason_no"] integerValue];
        NSString *reasonmsg = messageInfo.req_data[@"reason_msg"];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onRoomMemberKickedNotifyMessage:uid:nickName:avater:reasonType:reasonMessage:), onRoomMemberKickedNotifyMessage:roomId uid:userId nickName:nickName avater:avatar reasonType:reasonType reasonMessage:reasonmsg);
        return;
    }
    
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_ROOM_MUTE_ADD]) {
        ChatRoomMember *member = [ChatRoomMember yy_modelWithJSON:messageInfo.req_data[@"member"]];
        NSString *roomId = messageInfo.req_data[@"room_id"];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onRoomMemberMuteAddNotifyMessage:member:), onRoomMemberMuteAddNotifyMessage:roomId member:member);
        return;
    }
    
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_ROOM_MUTE_REMOVE]) {
        ChatRoomMember *member = [ChatRoomMember yy_modelWithJSON:messageInfo.req_data[@"member"]];
        NSString *roomId = messageInfo.req_data[@"room_id"];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onRoomMemberMuteRemoveNotifyMessage:member:), onRoomMemberMuteRemoveNotifyMessage:roomId member:member);
        return;
    }
    
#pragma mark 房间队列管理
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_ROOM_MIC_UPDATE]) {
        NSString *key = nil;
        id keyData = messageInfo.req_data[@"key"];
        if ([keyData isKindOfClass:[NSString class]]) {
            key = keyData;
        } else if ([keyData isKindOfClass:[NSNumber class]]) {
            key = [keyData stringValue];
        }
        NSString *roomId = messageInfo.req_data[@"room_id"];
        HJRoomMicInfo *micInfo = [HJRoomMicInfo yy_modelWithJSON:messageInfo.req_data[@"mic_info"]];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onQueueMicUpdateNotifyMessageRoomId:key:micInfo:), onQueueMicUpdateNotifyMessageRoomId:roomId key:key micInfo:micInfo);
        return;
    }
    
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_ROOM_QUEUE_UPDATE]) {
        NSInteger type = [messageInfo.req_data[@"type"] integerValue];
        NSString *roomId = messageInfo.req_data[@"room_id"];
        NSString *key = nil;
        id keyData = messageInfo.req_data[@"key"];
        if ([keyData isKindOfClass:[NSString class]]) {
            key = keyData;
        } else if ([keyData isKindOfClass:[NSNumber class]]) {
            key = [keyData stringValue];
        }
        ChatRoomMember *member = [ChatRoomMember yy_modelWithJSON:messageInfo.req_data[@"value"]];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onQueueMemberUpdateNotifyMessageRoomId:type:position:member:), onQueueMemberUpdateNotifyMessageRoomId:roomId type:type position:key member:member);
        return;
    }
    
#pragma mark 房间消息管理
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_SEND_MESSAGE_REPORT]) {
        NSString *roomId = messageInfo.req_data[@"room_id"];
        id data = messageInfo.req_data[@"custom"];
        ChatRoomMember *member = [ChatRoomMember yy_modelWithJSON:messageInfo.req_data[@"member"]];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onReceiveMessageNotifyMessage:member:data:), onReceiveMessageNotifyMessage:roomId member:member data:data);
        return;
    }
    
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_SEND_TEXT_REPORT]) {
        NSString *roomId = messageInfo.req_data[@"room_id"];
        NSString *content = messageInfo.req_data[@"content"];
        ChatRoomMember *member = [ChatRoomMember yy_modelWithJSON:messageInfo.req_data[@"member"]];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onReceiveTextNotifyMessage:member:content:), onReceiveTextNotifyMessage:roomId member:member content:content);
        return;
    }
    
#pragma mark 公屏管理
    if ([messageInfo.route isEqualToString:JX_IM_RECEIVE_PUBLIC_ROOM_MESSAGRE_NOTICE]) {
        NSString *roomId = messageInfo.req_data[@"room_id"];
        id data = messageInfo.req_data[@"custom"];
        NotifyCoreClient(HJWebSocketReceiveCoreClient, @selector(onReceivePublicRoomMessageNotifyMessage:data:), onReceivePublicRoomMessageNotifyMessage:roomId data:data);
        return;
    }
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}
@end
