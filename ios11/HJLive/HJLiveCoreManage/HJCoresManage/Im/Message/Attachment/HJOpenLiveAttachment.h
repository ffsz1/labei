//
//  HJOpenLiveAttachment.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HJCustomAttachmentInfo.h"
#import <NIMSDK/NIMSDK.h>
#import "Attachment.h"

@interface HJOpenLiveAttachment : Attachment <NIMCustomAttachment,HJCustomAttachmentInfo>

@property (nonatomic, assign) UserID uid;//用户ID
@property (nonatomic, copy) NSString *avatar;
@property (nonatomic, copy) NSString *nick;


- (NSString *)cellContent:(NIMMessage *)message;
- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width;

@end
