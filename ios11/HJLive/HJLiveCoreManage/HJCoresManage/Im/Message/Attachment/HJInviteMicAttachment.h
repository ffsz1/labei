//
//  HJInviteMicAttachment.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "Attachment.h"
#import <NIMSDK/NIMSDK.h>
#import "HJCustomAttachmentInfo.h"

@interface HJInviteMicAttachment : Attachment<NIMCustomAttachment,HJCustomAttachmentInfo>

//service
@property (nonatomic, copy) NSString *inviteUid;
@property (nonatomic, copy) NSString *position;
//NIMSDK
@property (nonatomic, copy) NSString *uid;
@property (nonatomic, copy) NSString *micPosition;
- (NSString *)cellContent:(NIMMessage *)message;
- (CGSize)contentSize:(NIMMessage *)message cellWidth:(CGFloat)width;

@end
