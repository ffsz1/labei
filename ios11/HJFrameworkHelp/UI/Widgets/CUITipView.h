//
//  CUITipView.h
//  Commons
//
//  a common tip view with various type and animation
//
//  Created by daixiang on 28-3-25.
//  Copyright (c) 2013年 YY Inc. All rights reserved.
//

#import <UIKit/UIKit.h>

#define TIP_VIEW_TAG     -2938578

typedef enum
{
    CUITipViewTypeHint,
    CUITipViewTypeLoading,
    CUITipViewTypeImage,
    CUITipViewTypeProgress,       //not implemented
} CUITipViewType;

typedef enum
{
    CUITipViewAnimationOptionNoFade             = 0 << 0,    //default
    CUITipViewAnimationOptionFadeIn             = 1 << 0,
    CUITipViewAnimationOptionFadeOut            = 2 << 0,
    CUITipViewAnimationOptionFadeInOut          = 3 << 0,
    
    CUITipViewAnimationOptionNoScale            = 0 << 2,    //default
    CUITipViewAnimationOptionScaleIn            = 1 << 2,
    CUITipViewAnimationOptionScaleOut           = 2 << 2,
    CUITipViewAnimationOptionScaleInOut         = 3 << 2,
    
    CUITipViewAnimationOptionBounce             = 1 << 4,
    
    CUITipViewAnimationOptionGrowth             = 0 << 5,    //default, only used when has scale
    CUITipViewAnimationOptionShrink             = 1 << 5,
} CUITipViewAnimationOptions;

typedef void (^CUITipViewDismissBlock) (BOOL isAutoDismissed);

@interface CUITipView : UIView

//for hit & image, default YES, for loading, default NO
@property (nonatomic) BOOL shouldAutoDismiss;

//default: hit 2s, loading 60s, image 2s, progress ignored
@property (nonatomic) NSTimeInterval autoDismissDuration;

@property (nonatomic, copy) CUITipViewDismissBlock dismissBlock;

//default 0.3
@property (nonatomic) NSTimeInterval animationDuration;

- (id)initWithFrame:(CGRect)frame type:(CUITipViewType)type;

- (void)setText:(NSString *)text;

- (void)setTextFont:(UIFont *)font;

- (void)setImageWithName:(NSString *)name;

//if set a button text, the text label will be replaced by the button
//- (void)setButtonText:(NSString *)text target:(id)target action:(SEL)action;

//this function will try to add tip view on the top most view of the current window
- (void)showAnimated:(BOOL)animated animationOptions:(CUITipViewAnimationOptions)options dismissBlock:(CUITipViewDismissBlock)block;

- (void)showInView:(UIView *)view animated:(BOOL)animated animationOptions:(CUITipViewAnimationOptions)options dismissBlock:(CUITipViewDismissBlock)block;

//if animated = YES, dismiss with the same animation option as show
- (void)dismissAnimated:(BOOL)animated;

+ (CUITipView *)currentTipView;
+ (void)dismissCurrentTipViewAnimated:(BOOL)animated;

@end
