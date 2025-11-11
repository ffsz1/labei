//
//  YPBaseViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPBaseViewController : UIViewController
// 是否隐藏导航 默认是不隐藏的
@property(nonatomic,assign,getter=isHiddenNavBar) BOOL hiddenNavBar;
@end

NS_ASSUME_NONNULL_END
