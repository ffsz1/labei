//
//  HJGiftAttachment.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGiftAttachment.h"


@implementation HJGiftAttachment


- (NSString *)cellContent:(NIMMessage *)message {
    return @"HJGiftContentMessageView";
}

- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width {
    
    return CGSizeMake(200, 65);
}

@end
