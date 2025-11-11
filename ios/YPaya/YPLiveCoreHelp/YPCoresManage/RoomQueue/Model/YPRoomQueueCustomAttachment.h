//
//  RoomQueueCustomAttachment.h
//  HJLive
//
//  Created by FF on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseObject.h"
#import "YPAttachment.h"
#import <NIMSDK/NIMSDK.h>
#import "YPRoomMicInfo.h"
#import "YPBaseObject.h"

@interface YPRoomQueueCustomAttachment : YPBaseObject<NIMCustomAttachment>
@property (assign, nonatomic) UserID uid;
@property (nonatomic, assign) int micPosition;
@property (nonatomic, strong) NSString* micName;
@property (strong, nonatomic) YPRoomMicInfo *YPRoomMicInfo;
@property (copy, nonatomic) NSDictionary *encodeAttachment;

@property (copy, nonatomic) NSDictionary *encodeAttachmentForManager;
@property (copy, nonatomic) NSDictionary *encodeAttachmentForRoomLock;

@end
