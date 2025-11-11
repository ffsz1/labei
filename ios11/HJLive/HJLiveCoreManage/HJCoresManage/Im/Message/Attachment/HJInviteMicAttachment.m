//
//  HJInviteMicAttachment.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJInviteMicAttachment.h"

@implementation HJInviteMicAttachment


- (NSString *)cellContent:(NIMMessage *)message{
    return @"HJInviteMicAttachmentView";
}

- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width{
    return CGSizeMake(240, 205);
}

@end
