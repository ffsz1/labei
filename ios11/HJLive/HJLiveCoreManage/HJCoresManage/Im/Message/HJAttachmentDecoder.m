//
//  HJAttachmentDecoder.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAttachmentDecoder.h"
#import "Attachment.h"

@implementation HJAttachmentDecoder

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
            Attachment *myAttachment = [[Attachment alloc] init];
            myAttachment.first = first.intValue;
            myAttachment.second = second.intValue;
            myAttachment.data = data;
            attachment = myAttachment;
        }
    }
    return attachment;
}

@end
