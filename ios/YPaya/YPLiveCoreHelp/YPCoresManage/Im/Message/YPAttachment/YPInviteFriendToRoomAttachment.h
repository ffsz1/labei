//
//  YPInviteFriendToRoomAttachment.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAttachment.h"
#import <NIMSDK/NIMSDK.h>
#import "HJCustomAttachmentInfo.h"

@interface YPInviteFriendToRoomAttachment : YPAttachment<NIMCustomAttachment,HJCustomAttachmentInfo>
- (NSString *)cellContent:(NIMMessage *)message;
- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width;
@end
