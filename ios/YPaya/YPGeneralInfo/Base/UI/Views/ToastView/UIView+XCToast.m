//
//  UIView+Toast.m
//  YYMobile
//
//  Created by 武帮民 on 14-7-21.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "UIView+XCToast.h"
#import "UIImageView+YYWebImage.h"
#import <objc/runtime.h>

#import "YPYYEmptyContentToastView.h"
#import "YPYYHudToastView.h"
#import "YPYYLoadingToastView.h"
#import "YPYYImageToastView.h"

const CGFloat VerticalPadding = 44.0;
static char kToastViewTapBlock;
static char kToastViewCompletionBlock;
static char kToastViewTapGestureRecognizer;
static char kToastViewTag;


@interface TapGestureRecognizerDelegateImpl : NSObject<UIGestureRecognizerDelegate>

+ (instancetype)sharedDelegateImpl;

@end

@implementation TapGestureRecognizerDelegateImpl

+ (instancetype)sharedDelegateImpl
{
    static TapGestureRecognizerDelegateImpl *sharedDelegateImpl = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedDelegateImpl = [[self alloc] init];
    });
    
    return sharedDelegateImpl;
}


#pragma mark - UIGestureRecognizerDelegate

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer
shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {
    return YES;
}

@end



@interface UIView (SetGetToast)<UIGestureRecognizerDelegate>


@end

@implementation UIView (SetGetToast)

#pragma mark - Action Handle

- (void)onToastViewTap:(UIGestureRecognizer*)recognizer
{
    UIView *toastView = [self getToastView];
    if ([self toastViewTapBlock] && toastView) {
        [self toastViewTapBlock]();
    }
}


#pragma mark - Getter & Setter

- (void)setToastView:(UIView *)toast {
    
    objc_setAssociatedObject(self, &kToastViewTag,
                             toast, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    
}

- (UIView *)getToastView {
    UIView *toastView = objc_getAssociatedObject(self, &kToastViewTag);
    // 避免找不到toast的情况
    if(toastView == nil){
        for(UIView *subView in self.subviews){
            if([subView isKindOfClass:[YPYYEmptyContentToastView class]]){
                toastView = subView;
                break;
            }
        }
    }
    return toastView;
}

- (void)setCompletion:(void (^)(void))completion {
    objc_setAssociatedObject(self, &kToastViewCompletionBlock,
                             completion, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (void (^)(void))completion {
    return objc_getAssociatedObject(self, &kToastViewCompletionBlock);
}

- (void (^)(void))toastViewTapBlock
{
    return objc_getAssociatedObject(self, &kToastViewTapBlock);
}

- (void)setToastViewTapBlock:(void (^)(void))toastViewTapBlock
{
    objc_setAssociatedObject(self, &kToastViewTapBlock,
                             toastViewTapBlock, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (UITapGestureRecognizer *)toastViewTapGestureRecognizer
{
    return objc_getAssociatedObject(self, &kToastViewTapGestureRecognizer);
}

- (void)setToastViewTapGestureRecognizer:(UITapGestureRecognizer *)tapGestureRecognizer
{
    objc_setAssociatedObject(self, &kToastViewTapGestureRecognizer,
                             tapGestureRecognizer, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}


#pragma mark - Add & Remove TapGestureRecognizer

- (void)addTapGestureRecognizerWithTapBlock:(void (^)(void))tapBlock
{
    [self removeTapGestureRecognizerAndTapBlock];
    
    [self setToastViewTapBlock:tapBlock];
    UITapGestureRecognizer *tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self
                                                                                           action:@selector(onToastViewTap:)];
    tapGestureRecognizer.delegate = [TapGestureRecognizerDelegateImpl sharedDelegateImpl];
    [self addGestureRecognizer:tapGestureRecognizer];
    [self setToastViewTapGestureRecognizer:tapGestureRecognizer];
}

- (void)removeTapGestureRecognizerAndTapBlock
{
    if([self toastViewTapGestureRecognizer]) {
        [self toastViewTapGestureRecognizer].delegate = nil;
        [self removeGestureRecognizer:[self toastViewTapGestureRecognizer]];
        [self setToastViewTapGestureRecognizer:nil];
    }
    
    if ([self toastViewTapBlock]) {
        [self setToastViewTapBlock:NULL];
    }
}


@end


@implementation UIView (XCToast)

- (void)showToastView:(UIView *)toastView
             duration:(CGFloat)interval
             position:(YYToastPosition)position
{
    [self showToastView:toastView duration:interval position:position animated:YES];
}

- (void)showToastView:(UIView *)toastView
             duration:(CGFloat)interval
             position:(YYToastPosition)position
             animated:(BOOL)animated
{
    CGPoint viewCenter = CGPointZero;
    
    if (position == YYToastPositionBottom) {
        viewCenter = CGPointMake(self.bounds.size.width/2,
                                 (self.bounds.size.height - (toastView.frame.size.height / 2)) - 10);
    } else if (position == YYToastPositionBottomWithTabbar) {
        viewCenter = CGPointMake(self.bounds.size.width/2,
                                 (self.bounds.size.height - (toastView.frame.size.height / 2)) - 64);
    } else if (position == YYToastPositionBottomWithRecordButton) {
        viewCenter = CGPointMake(self.bounds.size.width/2,
                                 (self.bounds.size.height - (toastView.frame.size.height / 2)) - 100);
    } else if (position == YYToastPositionCenter) {
        viewCenter = CGPointMake(self.bounds.size.width / 2, self.bounds.size.height / 2);
    } else if (position == YYToastPositionTop) {
        viewCenter = CGPointMake(self.bounds.size.width / 2, 69+(toastView.frame.size.height)/2);
    } else if (position == YYToastPositionAboveKeyboard) {
        viewCenter = CGPointMake(CGRectGetWidth(self.bounds) / 2.0,
                                 (CGRectGetHeight(self.bounds) - 252.0) - CGRectGetHeight(toastView.bounds) / 2.0 - VerticalPadding);
    } else {
        viewCenter = CGPointMake(self.bounds.size.width/2, (toastView.frame.size.height / 2) + 10);
    }
    
    if ([self isKindOfClass:[UIScrollView class]]) {
        viewCenter.y += ((UIScrollView *)self).contentOffset.y;
    }
    
    toastView.center = viewCenter;
    
    [self showToastView:toastView duration:interval animated:animated];
}

- (void)showToastView:(UIView *)toastView
             duration:(CGFloat)interval
             position:(YYToastPosition)position
              offsetY:(NSInteger)offsetY
             animated:(BOOL)animated
{
    CGPoint viewCenter = CGPointZero;
    
    if (position == YYToastPositionBottom) {
        viewCenter = CGPointMake(self.bounds.size.width/2,
                                 (self.bounds.size.height - (toastView.frame.size.height / 2)) - 10);
    } else if (position == YYToastPositionBottomWithTabbar) {
        viewCenter = CGPointMake(self.bounds.size.width/2,
                                 (self.bounds.size.height - (toastView.frame.size.height / 2)) - 64);
    } else if (position == YYToastPositionCenter) {
        viewCenter = CGPointMake(self.bounds.size.width / 2, self.bounds.size.height / 2);
    } else if (position == YYToastPositionTop) {
        viewCenter = CGPointMake(self.bounds.size.width / 2, 69+(toastView.frame.size.height)/2);
    } else if (position == YYToastPositionAboveKeyboard) {
        viewCenter = CGPointMake(CGRectGetWidth(self.bounds) / 2.0,
                                 (CGRectGetHeight(self.bounds) - 252.0) - CGRectGetHeight(toastView.bounds) / 2.0 - VerticalPadding);
    } else {
        viewCenter = CGPointMake(self.bounds.size.width/2, (toastView.frame.size.height / 2) + 10);
    }
    
    if ([self isKindOfClass:[UIScrollView class]]) {
        viewCenter.y += ((UIScrollView *)self).contentOffset.y;
    }
    if(offsetY){
        viewCenter.y += offsetY;
    }
    toastView.center = viewCenter;
    
    [self showToastView:toastView duration:interval animated:animated];
}

    
- (void)showToastView:(UIView *)toastView
             duration:(CGFloat)interval
             animated:(BOOL)animated
{
    
    if (![NSThread isMainThread]) {
        return;
    }
    
    [self hideToastViewAnimated:NO];
    
    if (self == nil || toastView == nil)
    {
        return ;
    }

    __weak __typeof__(self) weakSelf= self;
//    __weak __typeof__(toastView) weakToast = toastView;
    
    dispatch_async(dispatch_get_main_queue(), ^{
        toastView.alpha = 1.0;
        [weakSelf setToastView:toastView];
        if (toastView.center.x <= 0 && toastView.center.y <= 0) {
            return;
        }
        if (toastView != weakSelf) {
            [weakSelf addSubview:toastView];
        }
        
        if (interval != MAXFLOAT) {
            
            [UIView animateWithDuration:0.2
                                  delay:interval
                                options:UIViewAnimationOptionCurveEaseIn
                             animations:^{
                                 toastView.alpha = 0.0;
                             } completion:^(BOOL finished) {
                                 
                                 if (!finished) {
                                     return ;
                                 }
                                 
                                 if ([weakSelf completion]) {
                                     [weakSelf completion]();
                                 }
                                 
                                 [weakSelf hideToastViewAnimated:NO];
                             }];
        }
    });
    
}

- (void)hideToastViewAnimated:(BOOL)animated
{
    
    [CATransaction begin];
    [self.layer removeAllAnimations];
    [CATransaction commit];
    [CATransaction flush];
    
    UIView *tempView = [self getToastView];
    if (tempView) {

        [tempView removeFromSuperview];
        tempView = nil;
        [self setToastView:nil];
    }
    
    // 移掉completion
    [self setCompletion:nil];
    
    // 如果有 TapGestureRecognizer，则移除
    [self removeTapGestureRecognizerAndTapBlock];
}

- (void)hideToastView
{
//    NSLog(@"UIView+Toast hideToastView ======= %@", NSStringFromClass([self class]));
    [self hideToastViewAnimated:YES];
}

@end

@implementation UIView (LoadingToast)

- (void)showLoadingToastWithOffsetY:(NSInteger)offsetY
{
    UIView *toast = [YPYYLoadingToastView instantiateLoadingToast];
    [self showToastView:toast duration:MAXFLOAT position:YYToastPositionCenter offsetY:offsetY animated:YES];
}

- (void)showLoadingToast {
//    NSLog(@"UIView+Toast showLoadingToast ======= %@", NSStringFromClass([self class]));
    [self showLoadingToastDuration:MAXFLOAT];
}

- (void)showLoadingToastDuration:(CGFloat)interval {
    UIView *toast = [YPYYLoadingToastView instantiateLoadingToast];
    [self showToastView:toast duration:interval position:YYToastPositionCenter];
}

- (void)showLoadingToastDuration:(CGFloat)interval completion:(void (^)(void))completion {
    
    
    UIView *toast = [YPYYLoadingToastView instantiateLoadingToast];
    [self showToastView:toast duration:interval position:YYToastPositionCenter];
    
    [self setCompletion:completion];
}

@end

@implementation UIView (HudToast)

+ (void)showToastInKeyWindow:(NSString *)message
{
    [[UIApplication sharedApplication].keyWindow showToast:message];
}

+ (void)showToastInKeyWindow:(NSString *)message duration:(CGFloat)interval
{
    [self showToastInKeyWindow:message duration:interval position:YYToastPositionBottomWithTabbar];
}

+ (void)showToastInKeyWindow:(NSString *)message
                    duration:(CGFloat)interval
                    position:(YYToastPosition)position {
    if (message && message.length > 0) {
        NSMutableAttributedString *string = [[NSMutableAttributedString alloc] initWithString:message];
        UIView *toast = [YPYYHudToastView HudToastViewWithAttributedMessage:string inRect: [[[UIApplication sharedApplication] keyWindow]bounds]];
        
        [[UIApplication sharedApplication].keyWindow showToastView:toast
                                                          duration:interval
                                                          position:position];
    }
}

- (void)showToast:(NSString *)message
{
    [self showToast:message highlightText:@"" highlightColor:nil position:YYToastPositionDefault];
}

- (void)showToast:(NSString *)message position:(YYToastPosition)position
{
    [self showToast:message highlightText:@"" highlightColor:nil position:position];
}

- (void)showToastWithMessage:(NSString *)message
                    duration:(CGFloat)interval
                    position:(YYToastPosition)position
{
    if (message && message.length > 0) {
        NSMutableAttributedString *string = [[NSMutableAttributedString alloc] initWithString:message];
        UIView *toast = [YPYYHudToastView HudToastViewWithAttributedMessage:string
                                                                   inRect:[[[UIApplication sharedApplication] keyWindow]bounds]];
        [self showToastView:toast duration:interval position:position];
    }
}

- (void)showToast:(NSString *)message
    highlightText:(NSString *)text
   highlightColor:(UIColor *)color {
    [self showToast:message highlightText:text highlightColor:color position:YYToastPositionDefault];
}

- (void)showToast:(NSString *)message
    highlightText:(NSString *)text
   highlightColor:(UIColor *)color
         position:(YYToastPosition)position
{
    if (message && message.length > 0) {
        NSMutableAttributedString *string = [[NSMutableAttributedString alloc] initWithString:message];
        [string addAttribute:NSForegroundColorAttributeName value:color range:[message rangeOfString:text]];
        
        UIView *toast = [YPYYHudToastView HudToastViewWithAttributedMessage:string inRect:self.bounds];
        [self showToastView:toast duration:3 position:position];
    }
}

@end

@implementation UIView (EmptyToast)

- (void)showToast:(NSString*)message duration:(CGFloat)dur position:(YYToastPosition)position image:(UIImage*)image
{
    UIView *toast = [YPYYImageToastView ImageToastViewWithMessage:message Image:image];
    
    [self showToastView:toast duration:dur position:position];
}

- (void)showEmptyContentToastWithTitle:(NSString *)title
{
    [self showEmptyContentToastWithTitle:title tapBlock:NULL];
}

- (void)showEmptyContentToastWithTitle:(NSString *)title andImage:(UIImage *)image
{
    [self hideToastView];
    
    YPYYEmptyContentToastView *toast = [YPYYEmptyContentToastView instantiateEmptyContentToast];
    toast.titleLabel.text = title;
    toast.imageView.image = image;
    
    if (!toast) {
        return;
    }
    
    [self showToastView:toast duration:MAXFLOAT position:YYToastPositionCenter];
}

- (void)showEmptyContentToastWithTitle:(NSString *)title tapBlock:(void (^)(void))tapBlock
{
    [self hideToastView];
    
    YPYYEmptyContentToastView *toast = [YPYYEmptyContentToastView instantiateEmptyContentToast];
    toast.titleLabel.text = title;
    
    if (!toast) {
        return;
    }
    
    [self showToastView:toast duration:MAXFLOAT position:YYToastPositionCenter];
    
    if (tapBlock != NULL) {
        [self addTapGestureRecognizerWithTapBlock:tapBlock];
    }
}

- (void)showEmptyContentToastWithAttributeString:(NSAttributedString *)attrStr
{
    [self showEmptyContentToastWithAttributeString:attrStr tapBlock:NULL];
}

- (void)showEmptyContentToastWithAttributeString:(NSAttributedString *)attrStr tapBlock:(void (^)(void))tapBlock
{
    [self hideToastView];
    
    YPYYEmptyContentToastView *toast = [YPYYEmptyContentToastView instantiateEmptyContentToast];
    toast.titleLabel.attributedText = attrStr;
    
    if (!toast) {
        return;
    }
    
    [self showToastView:toast duration:MAXFLOAT position:YYToastPositionCenter];
    
    if (tapBlock != NULL) {
        [self addTapGestureRecognizerWithTapBlock:tapBlock];
    }
}

- (void)showNoSearchResultToastWithSearchKey:(NSString *)searchKey tapBlock:(void (^)(void))tapBlock
{
    UIColor *blackColor = RGBACOLOR(40, 40, 40, 1);
    UIColor *blueColor = RGBACOLOR(50, 161, 232, 1);
    
    // 无搜索结果的提示
    NSString *noResultTip = getLocalizedStringFromTable(@"gamevoice_search_no_XXX_result", @"GameVoice", nil);
    noResultTip = [noResultTip stringByReplacingOccurrencesOfString:@"XXX" withString:searchKey];
    
    NSMutableAttributedString *attrStr = [[NSMutableAttributedString alloc] initWithString:noResultTip];
    
    if(noResultTip.length > 0){
        [attrStr addAttribute:NSForegroundColorAttributeName
                        value:blackColor
                        range:NSMakeRange(0, attrStr.length)];
        
        NSRange highlightRange = [[noResultTip uppercaseString] rangeOfString:[searchKey uppercaseString]];
        
        [attrStr addAttribute:NSForegroundColorAttributeName
                        value:blueColor
                        range:highlightRange];
    }
    
    [self showEmptyContentToastWithAttributeString:attrStr tapBlock:tapBlock];
}


#pragma mark - Network Toast

- (void)showNetworkDisconnectToastWithPosition:(YYToastPosition)position
{
    [self showToast:getLocalizedStringFromTable(@"network_disconnect", @"Common", nil) position:position];
}

+ (void)showNetworkDisconnectToastInKeyWindowWithPosition:(YYToastPosition)position
{
    NSString *tips = getLocalizedStringFromTable(@"network_disconnect", @"Common", nil);
    [[UIApplication sharedApplication].keyWindow showToast:tips position:position];
}

- (void)showNetworkErrorToastWithTitle:(NSString *)title
{
    [self showNetworkErrorToastWithTitle:title tapBlock:NULL];
}

- (void)showNetworkErrorToastWithTitle:(NSString *)title tapBlock:(void (^)(void))tapBlock
{
    [self hideToastView];
    
    YPYYEmptyContentToastView *toast = [YPYYEmptyContentToastView instantiateNetworkErrorToast];
    toast.titleLabel.text = title;
    
    if (!toast) {
        return;
    }
    
    [self showToastView:toast duration:MAXFLOAT position:YYToastPositionCenter animated:NO];
    
    if (tapBlock != NULL) {
        [self addTapGestureRecognizerWithTapBlock:tapBlock];
    }
}

@end


