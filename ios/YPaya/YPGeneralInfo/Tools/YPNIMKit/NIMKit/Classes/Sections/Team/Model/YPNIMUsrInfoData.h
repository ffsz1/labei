//
//  YPNIMUsrInfoData.h
//  NIM
//
//  Created by Xuhui on 15/3/19.
//  Copyright (c) 2015年 Netease. All rights reserved.
//

#import "NIMContactDefines.h"
@class YPNIMKitInfo;

@interface NIMUsrInfo : NSObject <NIMGroupMemberProtocol>

@property (nonatomic,strong) YPNIMKitInfo *info;

- (BOOL)isFriend;

@end