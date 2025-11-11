//
//  RoomQueueCustomAttachment.h
//  HJLive
//
//  Created by FF on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseObject.h"
#import "Attachment.h"
#import <NIMSDK/NIMSDK.h>
#import "HJRoomMicInfo.h"
#import "BaseObject.h"

@interface HJRoomQueueCustomAttachment : BaseObject<NIMCustomAttachment>
@property (assign, nonatomic) UserID uid;
@property (nonatomic, assign) int micPosition;
@property (nonatomic, strong) NSString* micName;
@property (strong, nonatomic) HJRoomMicInfo *HJRoomMicInfo;
@property (copy, nonatomic) NSDictionary *encodeAttachment;

@property (copy, nonatomic) NSDictionary *encodeAttachmentForManager;
@property (copy, nonatomic) NSDictionary *encodeAttachmentForRoomLock;

@end
