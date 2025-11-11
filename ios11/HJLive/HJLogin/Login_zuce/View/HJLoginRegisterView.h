//
//  HJLoginRegisterView.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef void(^RegisteBlock)(void);
NS_ASSUME_NONNULL_BEGIN

@interface HJLoginRegisterView : UIView
@property (weak, nonatomic) IBOutlet UITextField *phoneTextF;
@property (weak, nonatomic) IBOutlet UITextField *codeTextF;
@property (weak, nonatomic) IBOutlet UITextField *passwordTextF;
@property (weak, nonatomic) IBOutlet UIButton *codeBtn;
@property (weak, nonatomic) IBOutlet UIButton *secuteBtn;
@property (weak, nonatomic) IBOutlet UIButton *registerBtn;

@property (assign,nonatomic) BOOL isShow;

@property (weak, nonatomic) IBOutlet GGImageView *phoneImgView;

@property (weak, nonatomic) IBOutlet GGImageView *passImgView;
@property (weak, nonatomic) IBOutlet GGImageView *codeImgView;
@property (copy,nonatomic) RegisteBlock closeBlock;


@end

NS_ASSUME_NONNULL_END
