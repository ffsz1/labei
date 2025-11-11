//
//  VersionCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VersionInfo.h"

@protocol HJVersionCoreClient <NSObject>
@optional
- (void)appNeedUpdateWithDesc:(NSString *)desc version:(NSString *)version;
- (void)appNeedNoticeWithDesc:(NSString *)desc version:(NSString *)version;
- (void)onRequestVersionStatusSuccess:(VersionInfo *)versionInfo;
@end
