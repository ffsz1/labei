//
//  YPLoginResetView.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^ResetBlock)(void);


NS_ASSUME_NONNULL_BEGIN

@interface YPLoginResetView : UIView
@property (weak, nonatomic) IBOutlet UITextField *phoneTextF;
@property (weak, nonatomic) IBOutlet UITextField *codeTextF;
@property (weak, nonatomic) IBOutlet UITextField *passwordTextF;
@property (weak, nonatomic) IBOutlet UITextField *passwordTextF2;
@property (weak, nonatomic) IBOutlet UIButton *codebtn;
@property (weak, nonatomic) IBOutlet UIButton *changeBtn;

@property (copy,nonatomic) ResetBlock closeBlock;

@property (assign,nonatomic) BOOL isShow;

@property (weak, nonatomic) IBOutlet GGImageView *phoneImgView;

@property (weak, nonatomic) IBOutlet GGImageView *yanzhengImgView;
@property (weak, nonatomic) IBOutlet GGImageView *passImgView;
@property (weak, nonatomic) IBOutlet GGImageView *rePassImgView;
@end

NS_ASSUME_NONNULL_END
