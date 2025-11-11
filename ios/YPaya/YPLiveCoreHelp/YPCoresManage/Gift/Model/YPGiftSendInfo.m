//
//  YPGiftSendInfo.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGiftSendInfo.h"

@implementation YPGiftSendInfo

- (NSDictionary *)encodeAttachemt {
    NSDictionary *dict = @{
                           @"uid"          :  @(self.uid),
                           @"targetUid"    :  @(self.targetUid),
                           @"giftId"       :  @(self.giftId),
                           @"nick"         :  self.nick,
                           @"avatar"       : self.avatar,
                           @"targetNick" : self.targetNick,
                           @"targetAvatar" : self.targetAvatar,
                           @"giftNum"   : @(self.giftNum)
                           };
//    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict
//                                                       options:0
//                                                         error:nil];
//    return [[NSString alloc] initWithData:jsonData
//                                 encoding:NSUTF8StringEncoding];
    return dict;
}

@end
