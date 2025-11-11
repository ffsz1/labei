//
//  UISwipeGestureRecognizer+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/10.
//
//

#import "UISwipeGestureRecognizer+JXBase.h"
#import "UIGestureRecognizer+JXBase.h"

const UISwipeGestureRecognizerDirection JXUISwipeGestureRecognizerDirectionHorizontal = UISwipeGestureRecognizerDirectionLeft | UISwipeGestureRecognizerDirectionRight;

const UISwipeGestureRecognizerDirection JXUISwipeGestureRecognizerDirectionVertical = UISwipeGestureRecognizerDirectionUp | UISwipeGestureRecognizerDirectionDown;

@implementation UISwipeGestureRecognizer (JXBase)

#pragma mark - Base
+ (instancetype)jx_swipeGestureRecognizerWithDirection:(UISwipeGestureRecognizerDirection)direction target:(id)target action:(SEL)action {
    return [[self alloc] initWithDirection:direction target:target action:action];
}

+ (instancetype)jx_swipeGestureRecognizerWithDirection:(UISwipeGestureRecognizerDirection)direction actionBlock:(void (^)(id sender))block {
    return [[self alloc] initWithDirection:direction actionBlock:block];
}

- (instancetype)initWithDirection:(UISwipeGestureRecognizerDirection)direction target:(id)target action:(SEL)action {
    self = [super init];
    if (self) {
        self = [self initWithTarget:target action:action];
        [self setDirection:direction];
    }
    return self;
}

- (instancetype)initWithDirection:(UISwipeGestureRecognizerDirection)direction actionBlock:(void (^)(id sender))block {
    self = [super init];
    if (self) {
        if (block) {
            self = [self initWithActionBlock:block];
        }
        [self setDirection:direction];
    }
    return self;
}

@end
