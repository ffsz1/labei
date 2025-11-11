//
//  IPhoneUIUtils.m
//  Commons
//
//  Created by daixiang on 13-3-28.
//  Copyright (c) 2013年 YY Inc. All rights reserved.
//

#if ! __has_feature(objc_arc)
#error This file must be compiled with ARC. Use -fobjc-arc flag (or convert project to ARC).
#endif

#import "IPhoneUIUtils.h"
#import "CUITipView.h"

@implementation IPhoneUIUtils

#pragma mark - public

+ (void)showHintViewWithText:(NSString *)text
{
    CUITipView *tipView = [IPhoneUIUtils tipViewWithType:CUITipViewTypeHint text:text];
    CUITipViewAnimationOptions options = CUITipViewAnimationOptionFadeInOut | CUITipViewAnimationOptionScaleInOut | CUITipViewAnimationOptionBounce | CUITipViewAnimationOptionShrink;
    
    [tipView showAnimated:YES animationOptions:options dismissBlock:nil];
}

+ (void)showLoadingViewWithText:(NSString *)text
{
    [IPhoneUIUtils showLoadingViewWithText:text dismissBlock:nil];
}

+ (void)showLoadingViewWithText:(NSString *)text dismissBlock:(void (^)(BOOL isAutoDismissed))block
{
    CUITipView *tipView = [IPhoneUIUtils tipViewWithType:CUITipViewTypeLoading text:text];
    tipView.dismissBlock = block;
    
    CUITipViewAnimationOptions options = CUITipViewAnimationOptionFadeInOut | CUITipViewAnimationOptionScaleInOut | CUITipViewAnimationOptionBounce;
    [tipView showAnimated:YES animationOptions:options dismissBlock:block];
}

+ (void)dismissLoadingView
{
    [CUITipView dismissCurrentTipViewAnimated:YES];
}

+ (void)showImageHintViewWithImageName:(NSString *)imageName hintText:(NSString *)text
{
    CUITipView *tipView = [IPhoneUIUtils tipViewWithType:CUITipViewTypeImage text:text];
    [tipView setImageWithName:imageName];
    CUITipViewAnimationOptions options = CUITipViewAnimationOptionFadeInOut | CUITipViewAnimationOptionScaleInOut | CUITipViewAnimationOptionBounce;
    [tipView showAnimated:YES animationOptions:options dismissBlock:nil];
}

#pragma mark - private

+ (CUITipView *)tipViewWithType:(CUITipViewType)type
{
    return [IPhoneUIUtils tipViewWithType:type text:nil];
}

+ (CUITipView *)tipViewWithType:(CUITipViewType)type text:(NSString *)text
{
    [CUITipView dismissCurrentTipViewAnimated:NO];
    
    CGRect tipFrame = CGRectMake(0, 0, 160, 120);
    CUITipView *tipView = [[CUITipView alloc] initWithFrame:tipFrame type:type];
    [tipView setText:text];
    return tipView;
}

@end
