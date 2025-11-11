//
//  YPRuleView.h
//  HJLive
//
//  Created by MacBook on 2020/8/21.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GGMaskView.h"
NS_ASSUME_NONNULL_BEGIN

@interface YPRuleView : GGMaskView
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_bgView;

+ (void)showRule;
@end

NS_ASSUME_NONNULL_END
