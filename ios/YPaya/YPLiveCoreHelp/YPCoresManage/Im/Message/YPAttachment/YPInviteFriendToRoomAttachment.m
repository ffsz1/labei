//
//  YPInviteFriendToRoomAttachment.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPInviteFriendToRoomAttachment.h"

@implementation YPInviteFriendToRoomAttachment
- (NSString *)cellContent:(NIMMessage *)message {
    return @"YPAnchorSharingContentMessageView";
}

- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width {
    return CGSizeMake(220, 131);
}
@end
