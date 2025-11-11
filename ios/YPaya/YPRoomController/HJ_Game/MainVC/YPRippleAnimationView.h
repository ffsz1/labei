//
//  YPRippleAnimationView.h
//  HJLive
//
//  Created by apple on 2020/9/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPIMDefines.h"

#define ColorWithAlpha(r,g,b,a) [UIColor colorWithRed:r/255.0 green:g/255.0 blue:b/255.0 alpha:a]

@protocol HJRippleAnimationDelagte <NSObject>

- (void)rippleAnimationFinishedWithAnimationView:(UIView *)animationView;

@end

@interface YPRippleAnimationView : UIView

//设置扩散倍数，默认1.0
@property (nonatomic, assign) CGFloat multiple;
@property (nonatomic, weak) id<HJRippleAnimationDelagte> delegate;
@property (nonatomic, assign) JXIMUserGenderType gender;

- (instancetype)initWithFrame:(CGRect)frame gender:(JXIMUserGenderType)gender;

@end

