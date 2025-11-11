//
//  YPRedPacketInfoAttachment.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>
#import "HJCustomAttachmentInfo.h"
#import "YPAttachment.h"

@interface YPRedPacketInfoAttachment : YPAttachment<NIMCustomAttachment,HJCustomAttachmentInfo>

@property (copy, nonatomic) NSString *title;

- (NSString *)cellContent:(NIMMessage *)message;
- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width;

@end
