//
//  VersionCore.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPBaseCore.h"
#import "YPVersionInfo.h"


@interface YPVersionCoreHelp : YPBaseCore
@property(nonatomic, assign) BOOL checkIn;
@property(nonatomic, strong) YPVersionInfo *versionInfo;
@property(nonatomic, assign) BOOL isReleaseEnv;//是否debug模式

- (void)getVersionData;

@end
