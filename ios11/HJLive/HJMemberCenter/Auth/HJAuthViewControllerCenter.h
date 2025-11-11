//
//  HJAuthViewControllerCenter.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJAuthViewControllerCenter : NSObject
+ (instancetype)defaultCenter;
- (void)toLogin;
@end

NS_ASSUME_NONNULL_END
