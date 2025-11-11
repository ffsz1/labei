//
//  FaceSendInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPBaseObject.h"

@interface YPFaceSendInfo : YPBaseObject

@property (strong, nonatomic) NSArray *data; //faceRecieveInfos
@property (assign, nonatomic) UserID uid;

@property (copy, nonatomic) NSDictionary *encodeAttachemt;
@end
