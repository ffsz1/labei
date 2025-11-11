//
//  FaceSendInfo.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFaceSendInfo.h"
#import "FaceInfo.h"
#import "HJFaceReceiveInfo.h"
#import "NSObject+YYModel.h"

@implementation HJFaceSendInfo


- (NSDictionary *)encodeAttachemt {
    NSDictionary *dict = @{
                           @"data"          :  self.data,
                           @"uid"           :  @(self.uid)
                           };
    //    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict
    //                                                       options:0
    //                                                         error:nil];
    //    return [[NSString alloc] initWithData:jsonData
    //                                 encoding:NSUTF8StringEncoding];
    return dict;
}

+ (nullable NSDictionary<NSString *, id> *)modelContainerPropertyGenericClass{
    return @{
             @"data"       : [HJFaceReceiveInfo class],
             };
}


@end
