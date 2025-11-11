//
//  RoomQueueCustomAttachment.m
//  HJLive
//
//  Created by FF on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomQueueCustomAttachment.h"

@implementation HJRoomQueueCustomAttachment

- (NSDictionary *)encodeAttachment {
    NSDictionary *dict = @{
                           @"uid"             :  @(self.uid),
                           @"micPosition"     :  @(self.micPosition),
                           @"data"    :  @{@"posState":@(self.HJRoomMicInfo.posState),
                                           @"micState":@(self.HJRoomMicInfo.micState)
                                           },
                           };
    return dict;
}

+ (nullable NSDictionary<NSString *, id> *)modelCustomPropertyMapper{
    
    return @{@"uid":@"uid",
             @"HJRoomMicInfo":@"data"};
}

- (nullable NSDictionary<NSString *, id> *)modelContainerPropertyGenericClass{
    return @{
             @"HJRoomMicInfo" : HJRoomMicInfo.class
             };
}

- (NSDictionary *)encodeAttachmentForManager {
    NSDictionary *dict = @{
                           @"uid"             :  @(self.uid),
                           @"micName"     :  self.micName,
                           @"data"    :  @{@"micName":self.micName
                                           
                                           },
                           };
    return dict;
}

- (NSDictionary *)encodeAttachmentForRoomLock {
    NSDictionary *dict = @{
                           @"uid"             :  @(self.uid),
//                           @"micName"     :  self.micName,
//                           @"data"    :  @{@"micName":self.micName
//
//                                           },
                           };
    return dict;
}

@end
