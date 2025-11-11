//
//  Attachment.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "Attachment.h"
#import "NSObject+YYModel.h"

@implementation Attachment

- (NSString *)encodeAttachment {
    
    return [self yy_modelToJSONString];
}
@end
