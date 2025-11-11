//
//  TurntableInfo.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPTurntableAttachment.h"

@implementation YPTurntableAttachment

- (NSString *)cellContent:(NIMMessage *)message {
    return @"YPTurntableContentView";
}


- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width {
    
    CGFloat maxHeight = [self sizeWithText:NSLocalizedString(TurntableAttachmentDes, nil) font:[UIFont systemFontOfSize:15 weight:UIFontWeightRegular] maxWidth:200].height;
    return CGSizeMake(200, maxHeight);
}

- (CGSize)sizeWithText:(NSString *)text font:(UIFont *)font maxWidth:(CGFloat)width
{
    NSMutableDictionary *attrDict = [NSMutableDictionary dictionary];
    attrDict[NSFontAttributeName] = font;
    CGSize size = [text boundingRectWithSize:CGSizeMake(width, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin attributes:attrDict context:nil].size;
    return size;
}

@end
