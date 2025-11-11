//
//  HJInviteFriendToRoomAttachment.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "Attachment.h"
#import <NIMSDK/NIMSDK.h>
#import "HJCustomAttachmentInfo.h"

@interface HJInviteFriendToRoomAttachment : Attachment<NIMCustomAttachment,HJCustomAttachmentInfo>
- (NSString *)cellContent:(NIMMessage *)message;
- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width;
@end
