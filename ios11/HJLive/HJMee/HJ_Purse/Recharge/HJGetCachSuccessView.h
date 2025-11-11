//
//  HJGetCachSuccessView.h
//  HJLive
//
//  Created by feiyin on 2020/7/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef NS_ENUM(NSUInteger, HJCachSuccessType) {
   
    HJCachSuccessCancelType,//cancel
  
};
typedef void(^CachSuccessBlock)(HJCachSuccessType);
NS_ASSUME_NONNULL_BEGIN

@interface HJGetCachSuccessView : UIView
@property (weak, nonatomic) IBOutlet UILabel *desLabel;
@property (weak, nonatomic) IBOutlet UIView *bgview;

@property (weak, nonatomic) IBOutlet UIVisualEffectView *effectView;

@property (weak, nonatomic) IBOutlet UIButton *canelBtn;
@property (copy, nonatomic) CachSuccessBlock menuBlock;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *mobileCenterYLayout;

@property (strong, nonatomic)NSString* cashNum;

+ (void)show:(CachSuccessBlock)menuBlock cashNum:(NSString*)cashNum;


@end

NS_ASSUME_NONNULL_END
