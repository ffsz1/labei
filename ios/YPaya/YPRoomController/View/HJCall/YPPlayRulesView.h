//
//  YPPlayRulesView.h
//  HJLive
//
//  Created by feiyin on 2020/8/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPPlayRulesView : UIView

+ (void)showPlayRulesView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_manager_layout;

@property (weak, nonatomic) IBOutlet UIView *manageView;

@end

NS_ASSUME_NONNULL_END
