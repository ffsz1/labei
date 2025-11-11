//
//  HJLoginPhoneView.h
//  HJLive
//
//  Created by feiyin on 2020/6/24.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^RegisterBlock)(void);
typedef void(^ForgetBlock)(void);

NS_ASSUME_NONNULL_BEGIN

@interface HJLoginPhoneView : UIView<UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UITextField *phoneTextF;
@property (weak, nonatomic) IBOutlet UITextField *passwordTextF;
@property (weak, nonatomic) IBOutlet UIButton *loginBtn;
@property (weak, nonatomic) IBOutlet GGButton *wechatBtn;
@property (weak, nonatomic) IBOutlet GGButton *qqBtn;
@property (weak, nonatomic) IBOutlet UIButton *selBtn;


@property (weak, nonatomic) IBOutlet NSLayoutConstraint *center_qq;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *center_wechat;

@property (copy,nonatomic) RegisterBlock registerBlock;
@property (copy,nonatomic) ForgetBlock forgetBlock;

@property (weak, nonatomic) IBOutlet GGImageView *phoneImgView;
@property (weak, nonatomic) IBOutlet GGImageView *passImgView;
@property (weak, nonatomic) IBOutlet UIButton *zhuceBtn;

@end

NS_ASSUME_NONNULL_END
