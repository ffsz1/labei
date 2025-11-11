//
//  YPRankCharmListVC.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^DayChangeBlock)(NSInteger dayType);

NS_ASSUME_NONNULL_BEGIN

@interface YPRankCharmListVC : UIViewController

@property (copy,nonatomic) DayChangeBlock dayBlock;



@end

NS_ASSUME_NONNULL_END
