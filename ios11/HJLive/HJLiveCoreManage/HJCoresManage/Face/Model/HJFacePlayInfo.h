//
//  HJFacePlayInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HJFaceReceiveInfo.h"
#import "BaseObject.h"

@interface HJFacePlayInfo : BaseObject

@property (assign, nonatomic) double delay;
@property (strong, nonatomic) NIMMessage *message;
@property (strong, nonatomic) HJFaceReceiveInfo *HJFaceReceiveInfo;

@end
