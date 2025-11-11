//
//  CUITipView.h
//  Commons
//
//  Created by daixiang on 28-3-25.
//  Copyright (c) 2013年 YY Inc. All rights reserved.
//

#if ! __has_feature(objc_arc)
#error This file must be compiled with ARC. Use -fobjc-arc flag (or convert project to ARC).
#endif

#import "CUITipView.h"
#import <QuartzCore/QuartzCore.h>

#define TEXT_LABEL_PADDING_LEFT         20
#define HINT_TEXT_LABEL_PADDING_TOP     20 
#define TEXT_LABEL_HEIGHT               50
#define ImageHeight 34
#define ImageMarginY 15


#define GROWTH_SCALE_INITIAL_VALUE      0.5
#define SHRINK_SCALE_INITIAL_VALUE      2.0
#define GROWTH_BOUNCE_SCALE             1.2
#define SHRINK_BOUNCE_SCALE             0.8

@interface CUITipView ()
{
    CUITipViewType _tipType;
    BOOL _animated;
    CUITipViewAnimationOptions _animationOptions;
    NSTimer *_timeoutTimer;
}

@property (nonatomic, strong) UIView *tipView;
@property (nonatomic, strong) UILabel *textLabel;
@property (nonatomic, strong) UIActivityIndicatorView *spinner;
@property (nonatomic, strong) UIImageView *imageView;
@property (nonatomic, strong) UIButton *button;

@end

@implementation CUITipView

+ (CUITipView *)currentTipView
{
    CUITipView *tipView = (CUITipView *)[[UIApplication sharedApplication].keyWindow viewWithTag:TIP_VIEW_TAG];
    return tipView;
}

+ (void)dismissCurrentTipViewAnimated:(BOOL)animated
{
    CUITipView *tipView = [CUITipView currentTipView];
    if (tipView)
    {
        [tipView dismissAnimated:animated];
    }
}

- (id)initWithFrame:(CGRect)frame type:(CUITipViewType)type
{
    CGRect bgFrame = [UIScreen mainScreen].bounds;
    if (UIInterfaceOrientationIsLandscape([UIApplication sharedApplication].statusBarOrientation))
    {
        bgFrame = CGRectMake(0, 0, bgFrame.size.height, bgFrame.size.width);
    }
    if (self = [super initWithFrame:bgFrame])
    {
        self.tag = TIP_VIEW_TAG;
        self.autoresizingMask = UIViewAutoresizingFlexibleHeight | UIViewAutoresizingFlexibleWidth;
        _tipType = type;
        self.animationDuration = 0.2;
        
//        CGFloat x = (bgFrame.size.width - frame.size.width) / 2.0;
//        CGFloat y = (bgFrame.size.height - frame.size.height) / 2.0;
//        frame = CGRectMake(x, y, frame.size.width, frame.size.height);
        self.tipView = [[UIView alloc] initWithFrame:frame];
        self.tipView.center = CGPointMake(bgFrame.size.width/2.0, bgFrame.size.height/2.0);
        self.tipView.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleBottomMargin;
        [self addSubview:self.tipView];
        
        UIView *tipBackgroundView = [[UIView alloc] initWithFrame:self.tipView.bounds];
        tipBackgroundView.backgroundColor = [UIColor blackColor];
        tipBackgroundView.alpha = 0.5;
        tipBackgroundView.layer.cornerRadius = 8;
        [self.tipView addSubview:tipBackgroundView];
        
        switch (type) {
            case CUITipViewTypeHint:
            {
                self.shouldAutoDismiss = YES;
                self.autoDismissDuration = 2;
                break;
            }
            case CUITipViewTypeLoading:
            {
                self.shouldAutoDismiss = NO;
                //self.autoDismissDuration = 60;
                self.spinner = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
                self.spinner.center = CGPointMake(self.tipView.frame.size.width/2.0, self.tipView.frame.size.height/2.0);
                [self.tipView addSubview:self.spinner];
                break;
            }
            case CUITipViewTypeImage:
            {
                self.shouldAutoDismiss = YES;
                self.autoDismissDuration = 2;
                self.imageView = [[UIImageView alloc] initWithFrame:self.tipView.bounds];
                [self.tipView addSubview:self.imageView];
                break;
            }
            case CUITipViewTypeProgress:
            {
                break;
            }
            default:
                break;
        }
        
        [self createTextLabel];
        
//        self.button = [UIButton buttonWithType:UIButtonTypeCustom];
//        self.button.frame = CGRectMake((self.tipView.frame.size.width-80)/2.0, self.tipView.frame.size.height-40, 80, 30);
//        self.button.titleLabel.font = [UIFont systemFontOfSize:15];
//        self.button.titleLabel.textColor = [UIColor whiteColor];
//        [self.tipView addSubview:self.button];
    }
    return self;
}

- (void)setText:(NSString *)text
{
    if ([text length] > 0)
    {
        self.textLabel.text = text;
        if(_tipType != CUITipViewTypeHint)
        {
            [self.textLabel sizeToFit];
        }
        self.textLabel.center = CGPointMake(self.tipView.frame.size.width/2, self.textLabel.center.y);
        self.spinner.center = CGPointMake(self.spinner.center.x, ImageMarginY + self.spinner.frame.size.height/2);
        if (self.imageView.image) {
            [self setImageViewRect:self.imageView.image.size];
        }
        
    }
}

- (void)setTextFont:(UIFont *)font
{
    self.textLabel.font = font;
}

- (void)setImageWithName:(NSString *)name
{
    UIImage *image = [UIImage imageNamed:name];
    CGSize size = image.size;
    
    [self setImageViewRect:size];
    self.imageView.image = image;
}

- (void)setButtonText:(NSString *)text target:(id)target action:(SEL)action
{
    if ([text length] > 0)
    {
        if (self.textLabel)
        {
            [self.textLabel removeFromSuperview];
        }
        self.button.layer.borderColor = [UIColor darkGrayColor].CGColor;
        self.button.layer.borderWidth = 1;
        self.button.backgroundColor = [UIColor darkGrayColor];
        [self.button setTitle:text forState:UIControlStateNormal];
    }
}

- (void)showAnimated:(BOOL)animated animationOptions:(CUITipViewAnimationOptions)options dismissBlock:(CUITipViewDismissBlock)block
{
    UIView *parent = nil;
    
    //we try to use the topmost view so we are good in landscape mode && orientation change
    if ([[[UIApplication sharedApplication].keyWindow subviews] count] > 0)
    {
        parent = [[[UIApplication sharedApplication].keyWindow subviews] lastObject];
    }
    else
    {
        parent = [UIApplication sharedApplication].keyWindow;
    }
    
    [self showInView:parent animated:animated animationOptions:options dismissBlock:block];
}

- (void)showInView:(UIView *)view animated:(BOOL)animated animationOptions:(CUITipViewAnimationOptions)options dismissBlock:(CUITipViewDismissBlock)block
{
    _animated = animated;
    _animationOptions = options;
    self.dismissBlock = block;
    
    if (_tipType == CUITipViewTypeLoading)
    {
        [self.spinner startAnimating];
    }
    
    //we try to make the tip view position at the center of the window
    CGFloat y = 0;
    CGRect frame = [view convertRect:view.bounds toView:[UIApplication sharedApplication].keyWindow];
    //the window's coordinate doesn't change in landscape mode, so in landscape mode we use x value instead of y
    if (UIInterfaceOrientationIsLandscape([UIApplication sharedApplication].statusBarOrientation))
    {
        y = frame.origin.x;
    }
    else
    {
        y = frame.origin.y;
    }
    self.frame = CGRectMake(0, -y, self.frame.size.width, self.frame.size.height);
    
    [view addSubview:self];
    if (animated)
    {
        [self animatedShowOrHide:YES];
    }
    
    if (self.shouldAutoDismiss)
        _timeoutTimer = [NSTimer scheduledTimerWithTimeInterval:self.autoDismissDuration target:self selector:@selector(timeout:) userInfo:nil repeats:NO];
}

- (void)dismissAnimated:(BOOL)animated
{
    [self dismissAnimated:animated autoDismissed:NO];
}


#pragma mark - private

- (void)setImageViewRect:(CGSize)imageSize
{
    CGRect imageRect;
    if ([self.textLabel.text length] > 0)
    {
        //imageRect = CGRectMake((self.tipView.frame.size.width-imageSize.width)/2.0, (self.tipView.frame.size.height-TEXT_LABEL_HEIGHT/2.0-imageSize.height)/2.0, imageSize.width, imageSize.height);
        imageRect = CGRectMake((self.tipView.frame.size.width-imageSize.width)/2.0, ImageMarginY, imageSize.width, imageSize.height);
    }
    else
    {
        imageRect = CGRectMake((self.tipView.frame.size.width-imageSize.width)/2.0, (self.tipView.frame.size.height-imageSize.height)/2.0, imageSize.width, imageSize.height);
    }
    
    self.imageView.frame = imageRect;
}

- (void)createTextLabel
{
    self.textLabel = [[UILabel alloc] initWithFrame:self.tipView.bounds];
    self.textLabel.backgroundColor = [UIColor clearColor];
    self.textLabel.font = [UIFont systemFontOfSize:15];
    self.textLabel.textColor = [UIColor whiteColor];
    self.textLabel.textAlignment = NSTextAlignmentCenter;
    
    switch (_tipType) {
        case CUITipViewTypeHint:
            self.textLabel.frame = CGRectMake(TEXT_LABEL_PADDING_LEFT, HINT_TEXT_LABEL_PADDING_TOP, self.tipView.frame.size.width-TEXT_LABEL_PADDING_LEFT*2, self.tipView.frame.size.height-HINT_TEXT_LABEL_PADDING_TOP * 2);
            self.textLabel.lineBreakMode = NSLineBreakByCharWrapping;
            self.textLabel.numberOfLines = 0;  //use multiple lines if needed
            break;
        case CUITipViewTypeLoading:
        case CUITipViewTypeImage:
        case CUITipViewTypeProgress:
            self.textLabel.frame = CGRectMake(TEXT_LABEL_PADDING_LEFT, ImageHeight+2*ImageMarginY,self.tipView.frame.size.width-TEXT_LABEL_PADDING_LEFT*2, TEXT_LABEL_HEIGHT);
            self.textLabel.lineBreakMode = NSLineBreakByTruncatingTail;
            self.textLabel.textColor = [UIColor whiteColor];
            self.textLabel.adjustsFontSizeToFitWidth = YES;
            self.textLabel.textAlignment = NSTextAlignmentCenter;
            self.textLabel.minimumScaleFactor = 14;
            self.textLabel.font = [UIFont systemFontOfSize:14];
            //行数限制在三行。
            self.textLabel.numberOfLines = 3;
            break;
        default:
            break;
    }
    
    [self.tipView addSubview:self.textLabel];
}

- (void)dismissAnimated:(BOOL)animated autoDismissed:(BOOL)autoDismissed
{
    if (animated)
    {
        [self animatedShowOrHide:NO];
    }
    else
    {
        [self removeFromSuperview];
    }
    if (self.dismissBlock)
    {
        self.dismissBlock(autoDismissed);
    }
    
    self.dismissBlock = nil;
}

- (void)animatedShowOrHide:(BOOL)isShow
{
    BOOL shouldFade = _animationOptions & CUITipViewAnimationOptionFadeIn;
    BOOL shouldScale = _animationOptions & CUITipViewAnimationOptionScaleIn;
    BOOL shouldBounce = _animationOptions & CUITipViewAnimationOptionBounce;
    BOOL shouldShrink = _animationOptions & CUITipViewAnimationOptionShrink;
        
    if (isShow)
    {
        if (shouldFade)
        {
            self.tipView.alpha = 0;
        }
        if (shouldScale)
        {
            if (shouldShrink)
                self.tipView.transform = CGAffineTransformMakeScale(SHRINK_SCALE_INITIAL_VALUE, SHRINK_SCALE_INITIAL_VALUE);
            else
                self.tipView.transform = CGAffineTransformMakeScale(GROWTH_SCALE_INITIAL_VALUE, GROWTH_SCALE_INITIAL_VALUE);
        }
        NSTimeInterval duration = self.animationDuration;

        [UIView animateWithDuration:duration animations:^{
            if (shouldBounce)
            {
                if (shouldShrink)
                    self.tipView.transform = CGAffineTransformMakeScale(SHRINK_BOUNCE_SCALE, SHRINK_BOUNCE_SCALE);
                else
                    self.tipView.transform = CGAffineTransformMakeScale(GROWTH_BOUNCE_SCALE, GROWTH_BOUNCE_SCALE);
            }
            else
            {
                self.tipView.transform = CGAffineTransformIdentity;
            }
            self.tipView.alpha = 1;
        } completion:^(BOOL finished) {
            if (shouldBounce)
            {
                [UIView animateWithDuration:0.1 animations:^{
                    self.tipView.transform = CGAffineTransformIdentity;
                }];
            }
        }];
    }
    else
    {
        void (^block)(BOOL finished) = ^(BOOL finished) {
            [UIView animateWithDuration:self.animationDuration animations:^{
                if (shouldScale)
                {
                    self.tipView.transform = CGAffineTransformMakeScale(GROWTH_SCALE_INITIAL_VALUE, GROWTH_SCALE_INITIAL_VALUE);
                }
                else
                {
                    self.tipView.transform = CGAffineTransformIdentity;
                }
                if (shouldFade)
                {
                    self.tipView.alpha = 0;
                }
            } completion:^(BOOL finished) {
                [self removeFromSuperview];
            }];
        };
        
        if (shouldBounce)
        {
            [UIView animateWithDuration:0.1 animations:^{
                self.tipView.transform = CGAffineTransformMakeScale(GROWTH_BOUNCE_SCALE, GROWTH_BOUNCE_SCALE);
            } completion:block];
        }
        else
        {
            block(YES);
        }
    }
}

- (void)timeout:(NSTimer *)timer
{
    _timeoutTimer = nil;
    [self dismissAnimated:_animated autoDismissed:YES];
}

@end
