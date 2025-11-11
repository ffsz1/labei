//
//  UIView+JXBase.m
//  JXCategories
//
//  Created by Colin on 16/12/31.
//  Copyright © 2016年 Colin. All rights reserved.
//

#import "UIView+JXBase.h"
#import "NSArray+JXBase.h"

@implementation UIView (JXBase)

#pragma mark - Base
- (CGFloat)jx_visibleAlpha {
    if ([self isKindOfClass:[UIWindow class]]) {
        if (self.hidden) return 0;
        return self.alpha;
    }
    
    if (!self.window) return 0;
    
    CGFloat alpha = 1;
    UIView *view = self;
    while (view) {
        if (view.hidden) {
            alpha = 0;
            break;
        }
        
        alpha *= view.alpha;
        view = view.superview;
    }
    return alpha;
}

- (UIView *)jx_topSuperView {
    UIView *topSuperView = self.superview;
    
    if (topSuperView == nil) {
        topSuperView = self;
    } else {
        while (topSuperView.superview) {
            topSuperView = topSuperView.superview;
        }
    }
    
    return topSuperView;
}

- (UIViewController *)jx_viewController {
    // 遍历自身及superview
    for (UIView *view = self; view; view = view.superview) {
        UIResponder *nextResponder = [view nextResponder]; // 响应链
        if ([nextResponder isKindOfClass:[UIViewController class]]) {
            return (UIViewController *)nextResponder;
        }
    }
    return nil;
}

- (void)jx_bringToFront {
    if (self.superview) {
        [self.superview bringSubviewToFront:self];
    }
}

- (void)jx_sendToBack {
    if (self.superview) {
        [self.superview sendSubviewToBack:self];
    }
}

- (void)jx_removeSubview:(UIView *)subview {
    if ([self.subviews containsObject:subview]) {
        [subview removeFromSuperview];
    }
}

- (void)jx_removeAllSubviews {
    while (self.subviews.count) {
        [self.subviews.lastObject removeFromSuperview];
    }
}

- (BOOL)jx_containsSubview:(UIView *)subview {
    if (!subview) return NO;
    if (![subview isKindOfClass:[UIView class]]) return NO;
    if (!subview.superview) return NO;
    if (!self.subviews.count) return NO;
    
    return [self.subviews jx_containsObjectAtIndexes:self.subviews.jx_indexesOfAll options:kNilOptions recursive:YES usingBlock:^BOOL(id  _Nonnull obj, NSIndexPath * _Nonnull indexPath, NSUInteger idx) {
        if ([obj isEqual:subview]) return YES;
        
        return NO;
    }];
}

#pragma mark - Layout
- (CGFloat)jx_x {
    return self.frame.origin.x;
}

- (void)setJx_x:(CGFloat)x {
    CGRect frame = self.frame;
    frame.origin.x = x;
    self.frame = frame;
}

- (CGFloat)jx_y {
    return self.frame.origin.y;
}

- (void)setJx_y:(CGFloat)y {
    CGRect frame = self.frame;
    frame.origin.y = y;
    self.frame = frame;
}

- (CGPoint)jx_origin {
    return self.frame.origin;
}

- (void)setJx_origin:(CGPoint)origin {
    CGRect frame = self.frame;
    frame.origin = origin;
    self.frame = frame;
}

- (CGFloat)jx_width {
    return self.frame.size.width;
}

- (void)setJx_width:(CGFloat)width {
    CGRect frame = self.frame;
    frame.size.width = width;
    self.frame = frame;
}

- (CGFloat)jx_height {
    return self.frame.size.height;
}

- (void)setJx_height:(CGFloat)height {
    CGRect frame = self.frame;
    frame.size.height = height;
    self.frame = frame;
}

- (CGSize)jx_size {
    return self.frame.size;
}

- (void)setJx_size:(CGSize)size {
    CGRect frame = self.frame;
    frame.size = size;
    self.frame = frame;
}

- (CGFloat)jx_centerX {
    return self.center.x;
}

- (void)setJx_centerX:(CGFloat)centerX {
    self.center = CGPointMake(centerX, self.center.y);
}

- (CGFloat)jx_centerY {
    return self.center.y;
}

- (void)setJx_centerY:(CGFloat)centerY {
    self.center = CGPointMake(self.center.x, centerY);
}

- (CGFloat)jx_left {
    return self.frame.origin.x;
}

- (void)setJx_left:(CGFloat)left {
    CGRect frame = self.frame;
    frame.origin.x = left;
    self.frame = frame;
}

- (CGFloat)jx_top {
    return self.frame.origin.y;
}

- (void)setJx_top:(CGFloat)top {
    CGRect frame = self.frame;
    frame.origin.y = top;
    self.frame = frame;
}

- (CGFloat)jx_right {
    return self.frame.origin.x + self.frame.size.width;
}

- (void)setJx_right:(CGFloat)right {
    CGRect frame = self.frame;
    frame.origin.x = right - frame.size.width;
    self.frame = frame;
}

- (CGFloat)jx_bottom {
    return self.frame.origin.y + self.frame.size.height;
}

- (void)setJx_bottom:(CGFloat)bottom {
    CGRect frame = self.frame;
    frame.origin.y = bottom - frame.size.height;
    self.frame = frame;
}

- (void)jx_originXEqualToView:(UIView *)view {
    UIView *superView = view.superview ? view.superview : view;
    CGPoint viewOrigin = [superView convertPoint:view.jx_origin toView:self.jx_topSuperView];
    CGPoint newOrigin = [self.jx_topSuperView convertPoint:viewOrigin toView:self.superview];
    
    self.jx_x = newOrigin.x;
}

- (void)jx_originYEqualToView:(UIView *)view {
    UIView *superView = view.superview ? view.superview : view;
    CGPoint viewOrigin = [superView convertPoint:view.jx_origin toView:self.jx_topSuperView];
    CGPoint newOrigin = [self.jx_topSuperView convertPoint:viewOrigin toView:self.superview];
    
    self.jx_y = newOrigin.y;
}

- (void)jx_originEqualToView:(UIView *)view {
    UIView *superView = view.superview ? view.superview : view;
    CGPoint viewOrigin = [superView convertPoint:view.jx_origin toView:self.jx_topSuperView];
    CGPoint newOrigin = [self.jx_topSuperView convertPoint:viewOrigin toView:self.superview];
    
    self.jx_origin = newOrigin;
}

- (void)jx_widthEqualToView:(UIView *)view {
    self.jx_width = view.jx_width;
}

- (void)jx_heightEqualToView:(UIView *)view {
    self.jx_height = view.jx_height;
}

- (void)jx_sizeEqualToView:(UIView *)view {
    self.jx_size = view.jx_size;
}

- (void)jx_centerXEqualToView:(UIView *)view {
    UIView *superView = view.superview ? view.superview : view;
    CGPoint viewCenterPoint = [superView convertPoint:view.center toView:self.jx_topSuperView];
    CGPoint centerPoint = [self.jx_topSuperView convertPoint:viewCenterPoint toView:self.superview];
    self.jx_centerX = centerPoint.x;
}

- (void)jx_centerYEqualToView:(UIView *)view {
    UIView *superView = view.superview ? view.superview : view;
    CGPoint viewCenterPoint = [superView convertPoint:view.center toView:self.jx_topSuperView];
    CGPoint centerPoint = [self.jx_topSuperView convertPoint:viewCenterPoint toView:self.superview];
    self.jx_centerY = centerPoint.y;
}

- (void)jx_centerEqualToView:(UIView *)view {
    UIView *superView = view.superview ? view.superview : view;
    CGPoint viewCenterPoint = [superView convertPoint:view.center toView:self.jx_topSuperView];
    CGPoint centerPoint = [self.jx_topSuperView convertPoint:viewCenterPoint toView:self.superview];
    self.jx_centerX = centerPoint.x;
    self.jx_centerY = centerPoint.y;
}

- (void)jx_leftEqualToView:(UIView *)view {
    UIView *superView = view.superview ? view.superview : view;
    CGPoint viewOrigin = [superView convertPoint:view.jx_origin toView:self.jx_topSuperView];
    CGPoint newOrigin = [self.jx_topSuperView convertPoint:viewOrigin toView:self.superview];
    
    self.jx_x = newOrigin.x;
}

- (void)jx_topEqualToView:(UIView *)view {
    UIView *superView = view.superview ? view.superview : view;
    CGPoint viewOrigin = [superView convertPoint:view.jx_origin toView:self.jx_topSuperView];
    CGPoint newOrigin = [self.jx_topSuperView convertPoint:viewOrigin toView:self.superview];
    
    self.jx_y = newOrigin.y;
}

- (void)jx_rightEqualToView:(UIView *)view {
    UIView *superView = view.superview ? view.superview : view;
    CGPoint viewOrigin = [superView convertPoint:view.jx_origin toView:self.jx_topSuperView];
    CGPoint newOrigin = [self.jx_topSuperView convertPoint:viewOrigin toView:self.superview];
    
    self.jx_x = newOrigin.x + view.jx_width - self.jx_width;
}

- (void)jx_bottomEqualToView:(UIView *)view {
    UIView *superView = view.superview ? view.superview : view;
    CGPoint viewOrigin = [superView convertPoint:view.jx_origin toView:self.jx_topSuperView];
    CGPoint newOrigin = [self.jx_topSuperView convertPoint:viewOrigin toView:self.superview];
    
    self.jx_y = newOrigin.y + view.jx_height - self.jx_height;
}

#pragma mark - Convert
- (CGPoint)jx_convertPoint:(CGPoint)point toViewOrWindow:(UIView *)view {
    if (!view) {
        if ([self isKindOfClass:[UIWindow class]]) {
            return [((UIWindow *)self) convertPoint:point toWindow:nil];
        } else {
            return [self convertPoint:point toView:nil];
        }
    }
    
    UIWindow *from = [self isKindOfClass:[UIWindow class]] ? (id)self : self.window;
    UIWindow *to = [view isKindOfClass:[UIWindow class]] ? (id)view : view.window;
    if ((!from || !to) || (from == to)) return [self convertPoint:point toView:view];
    point = [self convertPoint:point toView:from];
    point = [to convertPoint:point fromWindow:from];
    point = [view convertPoint:point fromView:to];
    return point;
}

- (CGPoint)jx_convertPoint:(CGPoint)point fromViewOrWindow:(UIView *)view {
    if (!view) {
        if ([self isKindOfClass:[UIWindow class]]) {
            return [((UIWindow *)self) convertPoint:point fromWindow:nil];
        } else {
            return [self convertPoint:point fromView:nil];
        }
    }
    
    UIWindow *from = [view isKindOfClass:[UIWindow class]] ? (id)view : view.window;
    UIWindow *to = [self isKindOfClass:[UIWindow class]] ? (id)self : self.window;
    if ((!from || !to) || (from == to)) return [self convertPoint:point fromView:view];
    point = [from convertPoint:point fromView:view];
    point = [to convertPoint:point fromWindow:from];
    point = [self convertPoint:point fromView:to];
    return point;
}

- (CGRect)jx_convertRect:(CGRect)rect toViewOrWindow:(UIView *)view {
    if (!view) {
        if ([self isKindOfClass:[UIWindow class]]) {
            return [((UIWindow *)self) convertRect:rect toWindow:nil];
        } else {
            return [self convertRect:rect toView:nil];
        }
    }
    
    UIWindow *from = [self isKindOfClass:[UIWindow class]] ? (id)self : self.window;
    UIWindow *to = [view isKindOfClass:[UIWindow class]] ? (id)view : view.window;
    if (!from || !to) return [self convertRect:rect toView:view];
    if (from == to) return [self convertRect:rect toView:view];
    rect = [self convertRect:rect toView:from];
    rect = [to convertRect:rect fromWindow:from];
    rect = [view convertRect:rect fromView:to];
    return rect;
}

- (CGRect)jx_convertRect:(CGRect)rect fromViewOrWindow:(UIView *)view {
    if (!view) {
        if ([self isKindOfClass:[UIWindow class]]) {
            return [((UIWindow *)self) convertRect:rect fromWindow:nil];
        } else {
            return [self convertRect:rect fromView:nil];
        }
    }
    
    UIWindow *from = [view isKindOfClass:[UIWindow class]] ? (id)view : view.window;
    UIWindow *to = [self isKindOfClass:[UIWindow class]] ? (id)self : self.window;
    if ((!from || !to) || (from == to)) return [self convertRect:rect fromView:view];
    rect = [from convertRect:rect fromView:view];
    rect = [to convertRect:rect fromWindow:from];
    rect = [self convertRect:rect fromView:to];
    return rect;
}

#pragma mark - Count Down
- (void)jx_changeWithCountDown:(NSInteger)seconds countDownHandler:(void (^)(id sender, NSInteger second, BOOL finished))countDownHandler {
    //倒计时时间
    __block NSInteger timeOut = seconds;
    __block BOOL finished = NO;
    __weak typeof(self) weakSelf = self;
    
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_source_t _timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    //每秒执行一次
    dispatch_source_set_timer(_timer, dispatch_walltime(NULL, 0), 1.0 * NSEC_PER_SEC, 0);
    dispatch_source_set_event_handler(_timer, ^{
        NSInteger second = timeOut;
        //倒计时结束，关闭
        if (timeOut <= 0) {
            dispatch_source_cancel(_timer);
            dispatch_async(dispatch_get_main_queue(), ^{
                finished = YES;
                !countDownHandler?:countDownHandler(weakSelf, second, finished);
            });
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                !countDownHandler?:countDownHandler(weakSelf, second, finished);
            });
            timeOut--;
        }
    });
    dispatch_resume(_timer);
}

#pragma mark - Nib
+ (instancetype)jx_viewFromNib {
    return  [[[NSBundle mainBundle] loadNibNamed:NSStringFromClass(self) owner:nil options:nil] lastObject];
}

+ (instancetype)jx_viewFromNib:(NSString *)name {
    return  [[[NSBundle mainBundle] loadNibNamed:name owner:nil options:nil] lastObject];
}

#pragma mark - Shadow
- (void)jx_setLayerShadow:(UIColor*)color offset:(CGSize)offset radius:(CGFloat)radius {
    self.layer.shadowColor = color.CGColor;
    self.layer.shadowOffset = offset;
    self.layer.shadowRadius = radius;
    self.layer.shadowOpacity = 1;
    self.layer.shouldRasterize = YES;
    self.layer.rasterizationScale = [UIScreen mainScreen].scale;
}

#pragma mark - Snapshot
- (UIImage *)jx_snapshotImage {
    UIGraphicsBeginImageContextWithOptions(self.bounds.size, self.opaque, 0);
    [self.layer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage *snapshot = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return snapshot;
}

- (UIImage *)jx_snapshotImageAfterScreenUpdates:(BOOL)afterUpdates {
    if (![self respondsToSelector:@selector(drawViewHierarchyInRect:afterScreenUpdates:)]) {
        return [self jx_snapshotImage];
    }
    UIGraphicsBeginImageContextWithOptions(self.bounds.size, self.opaque, 0);
    [self drawViewHierarchyInRect:self.bounds afterScreenUpdates:afterUpdates];
    UIImage *snap = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return snap;
}

@end
