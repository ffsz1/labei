//
//  ShareSendInfo.m
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPShareSendInfo.h"

//@property (assign, nonatomic) UserID uid;
//@property (copy, nonatomic) NSString *nick;
//@property (assign, nonatomic) UserID targetUid;
//@property (copy, nonatomic) NSString *targetNick;

@implementation YPShareSendInfo

- (NSDictionary *)encodeAttachemt {
    
    NSDictionary *dict = @{@"uid" :@(self.uid),
                           @"data":@{
                                   @"nick":self.nick == nil ? @"" : self.nick,
                                     @"targetUid":@(self.targetUid),
                                     @"targetNick":self.targetNick == nil ? @"" : self.targetNick,
                             }
                           };
    return dict;
}

@end
