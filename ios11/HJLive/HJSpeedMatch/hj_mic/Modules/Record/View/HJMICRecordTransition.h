//
//  HJMICRecordTransition.h
//  HJLive
//
//  Created by feiyin on 2020/7/7.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSInteger, HJMICRecordTransitionStyle) { // 转场动画类型
    HJMICRecordTransitionStylePresenting,
    HJMICRecordTransitionStyleDismissing
};

@interface HJMICRecordTransition : NSObject <UIViewControllerAnimatedTransitioning>

@property (nonatomic, assign) HJMICRecordTransitionStyle animationStyle; ///< 转场动画类型

+ (instancetype)animatedTransitionWithStyle:(HJMICRecordTransitionStyle)style;

@end
