//
//  YPPublicRechargeView.h
//  HJLive
//
//  Created by feiyin on 2020/7/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSUInteger, HJPublicRechargeType) {
    HJPublicRechargeConfirmType,//确定
    HJPublicRechargeCancelType,//cancel
  
};
typedef void(^PublicRechargeBlock)(HJPublicRechargeType);
NS_ASSUME_NONNULL_BEGIN

@interface YPPublicRechargeView : UIView

@property (weak, nonatomic) IBOutlet UIButton *showWeixinBtn;
@property (weak, nonatomic) IBOutlet UIView *bgview;

@property (weak, nonatomic) IBOutlet UIVisualEffectView *effectView;

@property (weak, nonatomic) IBOutlet UIButton *canelBtn;
@property (copy, nonatomic) PublicRechargeBlock menuBlock;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *mobileCenterYLayout;



+ (void)show:(PublicRechargeBlock)menuBlock;
@end

NS_ASSUME_NONNULL_END
