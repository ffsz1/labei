//
//  HJSendGoldAttachment.h
//  HJLive
//
//  Created by feiyin on 2020/7/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>
#import "HJCustomAttachmentInfo.h"

#import "Attachment.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJSendGoldAttachment : Attachment<NIMCustomAttachment,HJCustomAttachmentInfo>
@property (copy, nonatomic) NSString *goldPic;
@property (copy, nonatomic) NSString *recvName;
@property (copy, nonatomic) NSString *goldNum;

- (NSString *)cellContent:(NIMMessage *)message;
- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width;
@end

NS_ASSUME_NONNULL_END
