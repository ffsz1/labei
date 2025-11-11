//
//  HJSendGoldAttachment.m
//  HJLive
//
//  Created by feiyin on 2020/7/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSendGoldAttachment.h"

@implementation HJSendGoldAttachment
- (NSString *)cellContent:(NIMMessage *)message {
    return @"HJSendGoldContentMessageView";
}

- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width {
    
    return CGSizeMake(200, 65);
}

@end
