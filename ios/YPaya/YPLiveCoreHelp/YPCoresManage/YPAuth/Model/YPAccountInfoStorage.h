//
//  DataUtils.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPAccountInfo.h"
@interface YPAccountInfoStorage : NSObject

+ (YPAccountInfo *)getCurrentAccountInfo;
+ (void) saveAccountInfo:(YPAccountInfo *)accountInfo;
@end
