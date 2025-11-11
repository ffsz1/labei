//
//  HJImMessageCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>
#import "YPIMMessage.h"

@protocol HJImMessageCoreClient <NSObject>
@optional
- (void)onRecvChatRoomTextMsg:(YPIMMessage *)msg;
- (void)onRecvChatRoomNotiMsg:(NIMMessage *)msg;
- (void)onRecvChatRoomCustomMsg:(YPIMMessage *)msg;
- (void)onRecvP2PCustomMsg:(NIMMessage *)msg;
- (void)onRecvAnMsg:(NIMMessage *)msg;

@end
