//
//  VersionCore.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseCore.h"
#import "VersionInfo.h"


@interface HJVersionCoreHelp : BaseCore
@property(nonatomic, assign) BOOL checkIn;
@property(nonatomic, strong) VersionInfo *versionInfo;
@property(nonatomic, assign) BOOL isReleaseEnv;//是否debug模式

- (void)getVersionData;

@end
