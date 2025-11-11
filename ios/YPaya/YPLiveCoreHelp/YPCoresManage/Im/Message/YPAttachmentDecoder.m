//
//  YPAttachmentDecoder.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAttachmentDecoder.h"
#import "YPAttachment.h"

@implementation YPAttachmentDecoder

- (id<NIMCustomAttachment>)decodeAttachment:(NSString *)content
{
    id<NIMCustomAttachment> attachment;
    NSData *data = [content dataUsingEncoding:NSUTF8StringEncoding];
    if (data) {
        NSDictionary *dict = [NSJSONSerialization JSONObjectWithData:data options:0 error:nil];
        if ([dict isKindOfClass:[NSDictionary class]]) {
            NSNumber* first = dict[@"first"];
            NSNumber* second = dict[@"second"];
            NSDictionary *data = dict[@"data"];
            YPAttachment *myAttachment = [[YPAttachment alloc] init];
            myAttachment.first = first.intValue;
            myAttachment.second = second.intValue;
            myAttachment.data = data;
            attachment = myAttachment;
        }
    }
    return attachment;
}

@end
