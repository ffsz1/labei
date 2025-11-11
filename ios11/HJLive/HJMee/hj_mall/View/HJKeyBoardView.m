//
//  HJKeyBoardView.m
//  HJLive
//
//  Created by MacBook on 2020/8/19.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJKeyBoardView.h"
@interface HJKeyBoardView ()

@property (nonatomic, strong) UIButton *sureBtn;
@property (nonatomic, strong) UITextField *commentTF;
@property (nonatomic, strong) UIView *topLine;
@property (nonatomic, copy) KeyBoardViewSureActionHandler sureActionHandler;
@end

@implementation HJKeyBoardView

+ (instancetype)showKeyboardWithSureBtnImgStr:(NSString *)sureBtnImgStr sureBtnTitle:(NSString *)sureBtnTitle sureActionHandler:(KeyBoardViewSureActionHandler)didTapActionHandler
  {
      return [[self alloc] initWithSureBtnImgStr:sureBtnImgStr sureBtnTitle:sureBtnTitle sureActionHandler:didTapActionHandler];
  }

- (instancetype)initWithSureBtnImgStr:(NSString *)sureBtnImgStr sureBtnTitle:(NSString *)sureBtnTitle sureActionHandler:(KeyBoardViewSureActionHandler)didTapActionHandler
{
    if (self = [super init]) {
        
        self.frame = CGRectMake(0, XC_SCREE_H, XC_SCREE_W, 50);
        self.backgroundColor = [UIColor whiteColor];
        
        [self addSubview:self.commentTF];
        [self addSubview:self.sureBtn];
        [self addSubview:self.commentTF];
        [self addSubview:self.topLine];
        
//        [superView addSubview:self];
        [[UIApplication sharedApplication].keyWindow addSubview:self];
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyBoardWillShow:) name:UIKeyboardWillShowNotification object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyBoardWillHide:) name:UIKeyboardWillHideNotification object:nil];
        
        if (sureBtnImgStr.length>0) [_sureBtn setBackgroundImage:[UIImage imageNamed:sureBtnImgStr] forState:UIControlStateNormal];
        if (sureBtnTitle.length>0) [_sureBtn setTitle:sureBtnTitle forState:UIControlStateNormal];
        //@"hj_prop_icon_goumai"
        if (didTapActionHandler) self.sureActionHandler = didTapActionHandler;
        
        [self.commentTF becomeFirstResponder];
    }
    return self;
}

- (void)sureAction:(UIButton*)btn {
    if (self.commentTF.text.length == 0 || self.commentTF.text.integerValue == 0) {
        [MBProgressHUD showError:@"请输入打赏数量" toView:[UIApplication sharedApplication].keyWindow];
//        [MBProgressHUD showError:@"请输入打赏数量"];
        return;
    }
    if (self.sureActionHandler != nil) {
        self.sureActionHandler(self.commentTF.text.integerValue);
    }
    [self.commentTF resignFirstResponder];
}

-(void)keyBoardWillShow:(NSNotification *)notification
{
    // 获取用户信息
    NSDictionary *userInfo = [NSDictionary dictionaryWithDictionary:notification.userInfo];
    // 获取键盘高度
    CGRect keyBoardBounds = [[userInfo objectForKey:UIKeyboardFrameEndUserInfoKey] CGRectValue];
    CGFloat keyBoardHeight = keyBoardBounds.size.height;
    // 获取键盘动画时间
    CGFloat animationTime = [[userInfo objectForKey:UIKeyboardAnimationDurationUserInfoKey] floatValue];
    // 定义好动作
    void (^animation)(void) = ^void(void) {
        self.transform = CGAffineTransformMakeTranslation(0, -(keyBoardHeight + self.bounds.size.height));
//        self.transform = CGAffineTransformMakeTranslation(0, - self.bounds.size.height);
    };
    
    
    if (animationTime > 0) {
        [UIView animateWithDuration:animationTime animations:animation];
    } else {
        animation();
    }
}

- (void)keyBoardWillHide:(NSNotification *)notificaiton
{
    // 获取用户信息
    NSDictionary *userInfo = [NSDictionary dictionaryWithDictionary:notificaiton.userInfo];
    // 获取键盘动画时间
    CGFloat animationTime  = [[userInfo objectForKey:UIKeyboardAnimationDurationUserInfoKey] floatValue];
    
    // 定义好动作
    void (^animation)(void) = ^void(void) {
        self.transform = CGAffineTransformIdentity;
        [self removeFromSuperview];
    };
    
    if (animationTime > 0) {
        [UIView animateWithDuration:animationTime animations:animation];
    } else {
        animation();
    }
    
}

- (void)textFieldEditChanged:(UITextField *)textField {
    if (textField.text.integerValue > 9999) {
        textField.text = @"9999";
    }
}

- (UIButton *)sureBtn {
    if (_sureBtn == nil) {
        _sureBtn = [[UIButton alloc] initWithFrame:CGRectMake(XC_SCREE_W-66-12, 8, 60, 34)];
        [_sureBtn setTitle:@"确定" forState:UIControlStateNormal];
        [_sureBtn setBackgroundImage:[UIImage imageNamed:@"hj_prop_icon_goumai"] forState:UIControlStateNormal];
        [_sureBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_sureBtn.titleLabel setFont:[UIFont boldSystemFontOfSize:14]];
        [_sureBtn addTarget:self action:@selector(sureAction:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _sureBtn;
}

- (UITextField *)commentTF {
    if (_commentTF == nil) {
        _commentTF = [[UITextField alloc] init];
           _commentTF.frame = CGRectMake(15, 8, XC_SCREE_W - 15 - 60 - 24, 34);
           _commentTF.placeholder = @"输入打赏数量，最多9999";
           
           _commentTF.layer.cornerRadius = 17;
           _commentTF.layer.masksToBounds = YES;
           _commentTF.layer.borderWidth = 0.5;
           _commentTF.layer.borderColor = [UIColor lightGrayColor].CGColor;
           _commentTF.keyboardType = UIKeyboardTypeNumberPad;
           _commentTF.font = [UIFont systemFontOfSize:14];
           UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 15, 34)];
           _commentTF.leftViewMode = UITextFieldViewModeAlways;
           _commentTF.leftView = view;
           _commentTF.backgroundColor = [UIColor whiteColor];
           [_commentTF addTarget:self action:@selector(textFieldEditChanged:) forControlEvents:UIControlEventEditingChanged];
    }
    return _commentTF;
}

-(UIView *)topLine {
    if (_topLine == nil) {
        _topLine = [[UIView alloc] initWithFrame:CGRectMake(0, 0, XC_SCREE_W, 0.5)];
        _topLine.backgroundColor = [UIColor lightGrayColor];
    }
    return _topLine;
}

@end
