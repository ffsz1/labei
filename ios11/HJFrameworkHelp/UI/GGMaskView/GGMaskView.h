//
//  GGMaskView.h
//  HJLive
//
//  Created by apple on 2018/10/15.
//  Copyright © 2018年 XC. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^DismissBlock)(void);

NS_ASSUME_NONNULL_BEGIN

@interface GGMaskView : UIView

@property (nonatomic,copy)DismissBlock dismissBlock;

- (void)show;
- (void)dismiss;
- (void)disableTap;

@end

NS_ASSUME_NONNULL_END
