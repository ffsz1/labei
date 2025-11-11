//
//  HJInviteFriendToRoomAttachment.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJInviteFriendToRoomAttachment.h"

@implementation HJInviteFriendToRoomAttachment
- (NSString *)cellContent:(NIMMessage *)message {
    return @"HJAnchorSharingContentMessageView";
}

- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width {
    return CGSizeMake(220, 131);
}
@end
