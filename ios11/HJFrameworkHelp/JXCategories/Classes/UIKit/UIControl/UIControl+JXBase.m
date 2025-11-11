//
//  UIControl+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/10.
//
//

#import "UIControl+JXBase.h"
#import "NSObject+JXBase.h"

static const int JX_UI_CONTROL_BLOCK_KEY;

@interface JXUIControlBlockTarget : NSObject

@property (nonatomic, copy) void (^block)(id sender);
@property (nonatomic, assign) UIControlEvents events;

- (id)initWithBlock:(void (^)(id sender))block events:(UIControlEvents)events;
- (void)invoke:(id)sender;

@end

@implementation JXUIControlBlockTarget

- (id)initWithBlock:(void (^)(id sender))block events:(UIControlEvents)events {
    self = [super init];
    if (self) {
        _block = [block copy];
        _events = events;
    }
    return self;
}

- (void)invoke:(id)sender {
    !_block?:_block(sender);
}

@end


@implementation UIControl (JXBase)

#pragma mark - Base
- (void)jx_setTarget:(id)target action:(SEL)action forControlEvents:(UIControlEvents)controlEvents {
    if (!target || !action || !controlEvents) return;
    NSSet *targets = [self allTargets];
    // deal with controlEvents
    for (id currentTarget in targets) {
        NSArray *actions = [self actionsForTarget:currentTarget forControlEvent:controlEvents]; // actions for currentTarget of controlEvents
        for (NSString *currentAction in actions) {
            [self removeTarget:currentTarget action:NSSelectorFromString(currentAction)
              forControlEvents:controlEvents];
        }
    }
    [self addTarget:target action:action forControlEvents:controlEvents];
}

- (void)jx_setBlockForControlEvents:(UIControlEvents)controlEvents block:(void (^)(id sender))block {
    [self jx_removeAllBlocksForControlEvents:UIControlEventAllEvents];
    [self jx_addBlockForControlEvents:controlEvents block:block];
}

- (void)jx_addBlockForControlEvents:(UIControlEvents)controlEvents
                           block:(void (^)(id sender))block {
    if (!controlEvents) return;
    JXUIControlBlockTarget *target = [[JXUIControlBlockTarget alloc] initWithBlock:block events:controlEvents];
    [self addTarget:target action:@selector(invoke:) forControlEvents:controlEvents];
    NSMutableArray *targets = [self _jx_allUIControlBlockTargets];
    [targets addObject:target];
}

- (void)jx_removeAllBlocksForControlEvents:(UIControlEvents)controlEvents {
    if (!controlEvents) return;
    
    NSMutableArray *targets = [self _jx_allUIControlBlockTargets];
    NSMutableArray *removes = [NSMutableArray array];
    for (JXUIControlBlockTarget *target in targets) {
        /*
         假定(target.events = 0010 | 0100, controlEvents = 0010 | 1000)
         target.events & controlEvents = 0010 -> target.events与controlEvents存在重叠
         target.events & (~controlEvents) = 0100 -> 获取target.events与controlEvents不重叠的部分
         */
        // target.events 与 controlEvents 存在重叠
        if (target.events & controlEvents)  {
            UIControlEvents newEvent = target.events & (~controlEvents);
            if (newEvent) {
                [self removeTarget:target action:@selector(invoke:) forControlEvents:target.events];
                target.events = newEvent;
                [self addTarget:target action:@selector(invoke:) forControlEvents:target.events];
            } else {
                [self removeTarget:target action:@selector(invoke:) forControlEvents:target.events];
                [removes addObject:target];
            }
        }
    }
    [targets removeObjectsInArray:removes];
}

- (NSMutableArray *)_jx_allUIControlBlockTargets {
    NSMutableArray *targets = [self jx_getAssociatedValueForKey:&JX_UI_CONTROL_BLOCK_KEY];
    if (!targets) {
        targets = [NSMutableArray array];
        [self jx_setAssociatedValue:targets withKey:&JX_UI_CONTROL_BLOCK_KEY];
    }
    return targets;
}

- (void)jx_removeAllTargets {
    [[self allTargets] enumerateObjectsUsingBlock: ^(id object, BOOL *stop) {
        [self removeTarget:object action:NULL forControlEvents:UIControlEventAllEvents];
    }];
    [[self _jx_allUIControlBlockTargets] removeAllObjects];
}

#pragma mark - Touch Up Inside
- (void)jx_setTarget:(id)target actionForTouchUpInsideControlEvent:(SEL)action {
    [self jx_setTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
}

- (void)jx_addTarget:(id)target actionForTouchUpInsideControlEvent:(SEL)action {
    [self addTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
}

- (void)jx_removeTarget:(id)target actionForTouchUpInsideControlEvent:(SEL)action {
    [self removeTarget:target action:action forControlEvents:UIControlEventTouchUpInside];
}

- (void)jx_setBlockForTouchUpInsideControlEvent:(void (^)(id sender))block {
    [self jx_setBlockForControlEvents:UIControlEventTouchUpInside block:block];
}

- (void)jx_addBlockForTouchUpInsideControlEvent:(void (^)(id sender))block {
    [self jx_addBlockForControlEvents:UIControlEventTouchUpInside block:block];
}

- (void)jx_removeAllBlocksForTouchUpInsideControlEvent {
    [self jx_removeAllBlocksForControlEvents:UIControlEventTouchUpInside];
}

@end
