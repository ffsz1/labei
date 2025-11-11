//
//  YPCallInfo.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPCallInfo.h"

@implementation YPCallInfo

- (NSDictionary *)encodeAttchment {
    NSDictionary *dict = @{
                           @"nick": self.nick,
                           @"uid":@(self.uid)};
    return dict;
}

@end
