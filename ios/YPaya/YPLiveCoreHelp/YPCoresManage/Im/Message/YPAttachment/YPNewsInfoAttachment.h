//
//  YPNewsInfoAttachment.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>
#import "HJCustomAttachmentInfo.h"
#import "YPAttachment.h"

@interface YPNewsInfoAttachment : YPAttachment<NIMCustomAttachment,HJCustomAttachmentInfo>
@property (copy, nonatomic) NSString *title;
@property (copy, nonatomic) NSString *pic;
@property (copy, nonatomic) NSString *subTitle;
@property (copy, nonatomic) NSString *skipUrl;

- (NSString *)cellContent:(NIMMessage *)message;
- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width;
@end
