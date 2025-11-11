//
//  HJTabBarController.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJTabBarController : UITabBarController
- (void)showBadgeOnItemIndex:(int)index num:(NSInteger)num;
@end

NS_ASSUME_NONNULL_END
