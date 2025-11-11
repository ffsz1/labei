//
//  YPOpenLiveAttachment.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPOpenLiveAttachment.h"

@implementation YPOpenLiveAttachment

- (NSString *)cellContent:(NIMMessage *)message {
    return @"YPOpenLiveAlertMessageContentView";
}


- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width {

    return CGSizeMake(200, 50);
}



@end


