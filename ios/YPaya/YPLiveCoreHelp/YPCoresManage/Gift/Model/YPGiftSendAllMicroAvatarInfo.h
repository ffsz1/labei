//
//  YPGiftSendAllMicroAvatarInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>
#import "YPChatRoomMember.h"

@interface YPGiftSendAllMicroAvatarInfo : NSObject

@property (assign, nonatomic) BOOL isSelected;
@property (copy, nonatomic) NSString *position;
@property (strong, nonatomic) YPChatRoomMember *member;
@end
