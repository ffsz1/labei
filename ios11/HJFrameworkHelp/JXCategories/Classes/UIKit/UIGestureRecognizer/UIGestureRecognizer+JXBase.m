//
//  UIGestureRecognizer+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/10.
//
//

#import "UIGestureRecognizer+JXBase.h"
#import "NSObject+JXBase.h"

static const int JX_UI_GESTURE_RECOGNIZER_BLOCK_KEY;

@interface JXUIGestureRecognizerBlockTarget : NSObject

@property (nonatomic, copy) void (^block)(id sender);

- (id)initWithBlock:(void (^)(id sender))block;
- (void)invoke:(id)sender;

@end

@implementation JXUIGestureRecognizerBlockTarget

- (id)initWithBlock:(void (^)(id sender))block {
    self = [super init];
    if (self) {
        _block = [block copy];
    }
    return self;
}

- (void)invoke:(id)sender {
    !_block?:_block(sender);
}

@end


@implementation UIGestureRecognizer (JXBase)

#pragma mark - Base
- (UIView *)jx_targetView {
    CGPoint location = [self locationInView:self.view];
    return [self.view hitTest:location withEvent:nil];
}

+ (instancetype)jx_gestureRecognizerWithActionBlock:(void (^)(id sender))block {
    return [[self alloc] initWithActionBlock:block];
}

- (instancetype)initWithActionBlock:(void (^)(id sender))block {
    self = [self init];
    if (self) {
        [self jx_addActionBlock:block];
    }
    return self;
}

- (void)jx_addActionBlock:(void (^)(id sender))block {
    JXUIGestureRecognizerBlockTarget *target = [[JXUIGestureRecognizerBlockTarget alloc] initWithBlock:block];
    [self addTarget:target action:@selector(invoke:)];
    NSMutableArray *targets = [self _jx_allUIGestureRecognizerBlockTargets];
    [targets addObject:target];
}

- (void)jx_removeAllActionBlocks {
    NSMutableArray *targets = [self _jx_allUIGestureRecognizerBlockTargets];
    [targets enumerateObjectsUsingBlock:^(id target, NSUInteger idx, BOOL *stop) {
        [self removeTarget:target action:@selector(invoke:)];
    }];
    [targets removeAllObjects];
}

- (NSMutableArray *)_jx_allUIGestureRecognizerBlockTargets{
    NSMutableArray *targets = [self jx_getAssociatedValueForKey:&JX_UI_GESTURE_RECOGNIZER_BLOCK_KEY];
    if (!targets) {
        targets = [NSMutableArray array];
        [self jx_setAssociatedValue:targets withKey:&JX_UI_GESTURE_RECOGNIZER_BLOCK_KEY];
    }
    return targets;
}

@end
