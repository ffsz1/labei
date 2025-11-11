//
//  YPFacePlayInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPFaceReceiveInfo.h"
#import "YPBaseObject.h"

@interface YPFacePlayInfo : YPBaseObject

@property (assign, nonatomic) double delay;
@property (strong, nonatomic) NIMMessage *message;
@property (strong, nonatomic) YPFaceReceiveInfo *YPFaceReceiveInfo;

@end
