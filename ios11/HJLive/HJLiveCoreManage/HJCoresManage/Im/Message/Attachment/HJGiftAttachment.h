//
//  HJGiftAttachment.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NIMSDK/NIMSDK.h>
#import "HJCustomAttachmentInfo.h"
#import "Attachment.h"

@interface HJGiftAttachment : Attachment<NIMCustomAttachment,HJCustomAttachmentInfo>

//avatar = "https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83Nzg4OTc1NTFfMTUwNjg0NzEzNTI5MV83OTFmMWZlYS1iNjc4LTQ5YzQtOGZkYS01YjhlOTA5YTEzNWY=";
//giftId = 1010;
//giftNum = 1;
//nick = "\U5c0f\U83ab";
//targetUid = 90649;
//uid = 90663;

@property (copy, nonatomic) NSString *giftPic;
@property (copy, nonatomic) NSString *giftName;
@property (copy, nonatomic) NSString *giftNum;

- (NSString *)cellContent:(NIMMessage *)message;
- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width;
@end
