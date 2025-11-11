//
//  HJNewsInfoAttachment.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJNewsInfoAttachment.h"

@implementation HJNewsInfoAttachment

- (NSString *)cellContent:(NIMMessage *)message {
    return @"HJNewsNoticeContentMessageView";
}

- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width {
    NIMCustomObject *object = (NIMCustomObject *)message.messageObject;
    HJNewsInfoAttachment *customObject = (HJNewsInfoAttachment*)object.attachment;
    CGSize textSize = [customObject.title boundingRectWithSize:CGSizeMake(220, CGFLOAT_MAX) options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : [UIFont systemFontOfSize:16.f]} context:nil].size;
    
    CGFloat oneLineH = [@"消息" boundingRectWithSize:CGSizeMake(220, CGFLOAT_MAX) options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : [UIFont systemFontOfSize:16.f]} context:nil].size.height;
    
    if (textSize.height > oneLineH + 1) {
        return CGSizeMake(240, 205 - oneLineH + textSize.height);
    }
    else {
        
        return CGSizeMake(240, 205);
    }
    
}


@end
