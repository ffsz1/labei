//
//  UIControl+JXRepeatClickPrevention.m
//  Pods
//
//  Created by Colin on 17/2/11.
//
//

#import "UIControl+JXRepeatClickPrevention.h"
#import "NSObject+JXBase.h"

static const int JX_UI_CONTROL_REPEAT_CLICK_PREVENTION_KEY;
static const int JX_UI_CONTROL_ACCEPT_EVENT_INTERVAL_KEY;
static const int JX_UI_CONTROL_IGNORE_EVENT_KEY;

@implementation UIControl (JXRepeatClickPrevention)

+ (void)load {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        SEL selectors[] = {
            @selector(sendAction:to:forEvent:),
        };
        JXNSObjectSwizzleInstanceMethodsWithNewMethodPrefix(self, selectors, @"_jx_ui_control_");
    });
}

- (BOOL)jx_repeatClickPrevention {
    return [[self jx_getAssociatedValueForKey:&JX_UI_CONTROL_REPEAT_CLICK_PREVENTION_KEY] boolValue];
}

- (void)setJx_repeatClickPrevention:(BOOL)repeatClickPrevention {
    [self jx_setAssociatedValue:@(repeatClickPrevention) withKey:&JX_UI_CONTROL_REPEAT_CLICK_PREVENTION_KEY];
}

- (NSTimeInterval)jx_acceptEventInterval {
    return [[self jx_getAssociatedValueForKey:&JX_UI_CONTROL_ACCEPT_EVENT_INTERVAL_KEY] doubleValue];
}

- (void)setJx_acceptEventInterval:(NSTimeInterval)acceptEventInterval {
    [self jx_setAssociatedValue:@(acceptEventInterval) withKey:&JX_UI_CONTROL_ACCEPT_EVENT_INTERVAL_KEY];
}

- (BOOL)jx_ignoreEvent {
    return [[self jx_getAssociatedValueForKey:&JX_UI_CONTROL_IGNORE_EVENT_KEY] boolValue];
}

- (void)setJx_ignoreEvent:(BOOL)ignoreEvent {
    [self jx_setAssociatedValue:@(ignoreEvent) withKey:&JX_UI_CONTROL_IGNORE_EVENT_KEY];
}

+ (NSMutableSet *)_jx_ui_control_getRepeatClickPreventionClassForTargetsOfWhitelist {
    static dispatch_once_t onceToken;
    static NSMutableSet *set;
    dispatch_once(&onceToken, ^{
        set = [NSMutableSet set];
    });
    return set;
}

+ (void)jx_addRepeatClickPreventionClassForTargetsToWhitelist:(Class)aClass {
    if (!aClass) return;
    
    NSMutableSet *set = [self _jx_ui_control_getRepeatClickPreventionClassForTargetsOfWhitelist];
    [set addObject:aClass];
}

+ (void)jx_removeRepeatClickPreventionClassForTargetsFromWhitelist:(Class)aClass {
    if (!aClass) return;
    
    NSMutableSet *set = [self _jx_ui_control_getRepeatClickPreventionClassForTargetsOfWhitelist];
    [set removeObject:aClass];
}

#pragma mark - Swizzle
- (void)_jx_ui_control_sendAction:(SEL)action to:(id)target forEvent:(UIEvent *)event {
    NSMutableSet *whitelist = [[self class] _jx_ui_control_getRepeatClickPreventionClassForTargetsOfWhitelist];
    if ([whitelist containsObject:[target class]]) {
        [self _jx_ui_control_sendAction:action to:target forEvent:event];
        return;
    }
    
    if (!self.jx_repeatClickPrevention) {
        [self _jx_ui_control_sendAction:action to:target forEvent:event];
        return;
    }
    
    if (self.jx_ignoreEvent) return;
    if (self.jx_acceptEventInterval > 0) {
        self.jx_ignoreEvent = YES;
        [self performSelector:@selector(_jx_setupIgnoreEvent:) withObject:@(NO) afterDelay:self.jx_acceptEventInterval];
    }
    
    [self _jx_ui_control_sendAction:action to:target forEvent:event];
}

- (void)_jx_setupIgnoreEvent:(NSNumber *)value {
    BOOL ignoreEvent = [value boolValue];
    [self setJx_ignoreEvent:ignoreEvent];
}

@end
