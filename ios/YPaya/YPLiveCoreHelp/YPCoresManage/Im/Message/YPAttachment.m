//
//  YPAttachment.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAttachment.h"
#import "NSObject+YYModel.h"

@implementation YPAttachment

- (NSString *)encodeAttachment {
    
    return [self yy_modelToJSONString];
}
@end
