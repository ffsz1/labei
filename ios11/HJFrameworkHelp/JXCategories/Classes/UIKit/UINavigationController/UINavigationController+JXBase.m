//
//  UINavigationController+JXBase.m
//  Pods
//
//  Created by Colin on 17/7/19.
//
//

#import "UINavigationController+JXBase.h"

@implementation UINavigationController (JXBase)

#pragma mark - Base

- (void)jx_removeViewControllerFromClassName:(NSString *)className {
    [self jx_removeViewControllerFromClassName:className options:0];
}

- (void)jx_removeViewControllerFromClassName:(NSString *)className options:(NSEnumerationOptions)opts {
    if (!className || !className.length) return;
    
    Class class = NSClassFromString(className);
    [self jx_removeViewControllerFromClass:class options:opts];
}

- (void)jx_removeViewControllerFromClass:(Class)aClass  {
    [self jx_removeViewControllerFromClass:aClass options:0];
}

- (void)jx_removeViewControllerFromClass:(Class)aClass options:(NSEnumerationOptions)opts {
    if (!self.viewControllers || !self.viewControllers.count) return;
    if (![aClass isSubclassOfClass:[UIViewController class]]) return;
    
    __block NSMutableArray *viewControllers = [NSMutableArray arrayWithArray:self.viewControllers];
    __block BOOL changed = NO;
    [self.viewControllers enumerateObjectsWithOptions:opts usingBlock:^(__kindof UIViewController * _Nonnull viewController, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([viewController isKindOfClass:[aClass class]]) {
            changed = YES;
            [viewControllers removeObject:viewController];
            *stop = YES;
        }
    }];
    
    if (!changed) return;
    self.viewControllers = viewControllers.copy;
}

- (void)jx_removeViewControllersFromClassNames:(NSSet<NSString *> *)classNames {
    if (!classNames || !classNames.count ) return;
    
    NSMutableSet *buffer = [NSMutableSet set];
    NSEnumerator *enumerator = [classNames objectEnumerator];
    for (NSString *className in enumerator) {
        Class aClass = NSClassFromString(className);
        [buffer addObject:aClass];
    }
    if (!buffer.count) return;
    
    [self jx_removeViewControllersFromClasses:buffer.copy];
}

- (void)jx_removeViewControllersFromClasses:(NSSet<Class> *)classes {
    if (!classes || !classes.count) return;
    
    NSMutableSet *buffer = [NSMutableSet set];
    NSEnumerator *enumerator = [classes objectEnumerator];
    for (Class aClass in enumerator) {
        if (![aClass isSubclassOfClass:[UIViewController class]]) continue;
        
        [buffer addObject:aClass];
    }
    if (!buffer.count) return;
    
    NSMutableArray *viewControllers = [NSMutableArray arrayWithArray:self.viewControllers];
    BOOL changed = NO;
    for (UIViewController *viewController in self.viewControllers) {
        if ([buffer containsObject:viewController.class]) {
            changed = YES;
            [viewControllers removeObject:viewController];
        }
    }
    
    if (!changed) return;
    self.viewControllers = viewControllers.copy;
}

#pragma mark - Pop
- (nullable NSArray<__kindof UIViewController *> *)jx_popToViewControllerFromClassName:(NSString *)className animated:(BOOL)animated {
    if (!className || !className.length) return nil;
    
    Class class = NSClassFromString(className);
    return [self jx_popToViewControllerFromClass:class animated:animated];
}

- (nullable NSArray<__kindof UIViewController *> *)jx_popToViewControllerFromClass:(Class)aClass animated:(BOOL)animated {
    if (!self.viewControllers || !self.viewControllers.count) return nil;
    if (![aClass isSubclassOfClass:[UIViewController class]]) return nil;
    
    for (UIViewController *viewController in self.viewControllers) {
        if ([viewController isKindOfClass:[aClass class]]) {
            if (self.topViewController == viewController) continue;
            
            return [self popToViewController:viewController animated:animated];
        }
    }
    return nil;
}

@end
