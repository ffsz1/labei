//
//  YYStoryboardBlockSegue.h
//  YYMobile
//
//  Created by wuwei on 14/7/4.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YYStoryboardBlockSegue : UIStoryboardSegue

@property (strong) void(^performBlock)(YYStoryboardBlockSegue *segue, UIViewController *sourceViewController, UIViewController *destViewController);

@end
