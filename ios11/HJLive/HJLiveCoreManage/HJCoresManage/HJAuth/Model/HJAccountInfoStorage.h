//
//  DataUtils.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AccountInfo.h"
@interface HJAccountInfoStorage : NSObject

+ (AccountInfo *)getCurrentAccountInfo;
+ (void) saveAccountInfo:(AccountInfo *)accountInfo;
@end
