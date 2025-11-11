//
//  UINavigationController+JXBase.h
//  Pods
//
//  Created by Colin on 17/7/19.
//
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UINavigationController (JXBase)

#pragma mark - Base
/**
 根据类名，移除导航栏ViewControllers栈区内指定的ViewController(依据ViewControllers栈区顺序查找, 单次移除)
 
 @param className 类名
 */
- (void)jx_removeViewControllerFromClassName:(NSString *)className;

/**
根据类名，移除导航栏ViewControllers栈区内指定的ViewController(单次移除)

 @param className 类名
 @param opts      遍历方式
 */
- (void)jx_removeViewControllerFromClassName:(NSString *)className options:(NSEnumerationOptions)opts;

/**
 根据类别，移除导航栏ViewControllers栈区内指定的ViewController(依据ViewControllers栈区顺序查找, 单次移除)
 
 @param aClass 类别
 */
- (void)jx_removeViewControllerFromClass:(Class)aClass;

/**
 根据类别，移除导航栏ViewControllers栈区内指定的ViewController(单次移除)

 @param aClass 类别
 @param opts   遍历方式
 */
- (void)jx_removeViewControllerFromClass:(Class)aClass options:(NSEnumerationOptions)opts;

/**
 根据类名集合，移除导航栏ViewControllers栈区所有对应的ViewController(无序查找, 多次移除)
 
 @param classNames 类名集合
 */
- (void)jx_removeViewControllersFromClassNames:(NSSet<NSString *> *)classNames;

/**
 根据类别集合，移除导航栏ViewControllers栈区所有对应的ViewController(无序查找, 多次移除)
 
 @param classes 类别集合
 */
- (void)jx_removeViewControllersFromClasses:(NSSet<Class> *)classes;

#pragma mark - Pop
/**
 根据类名，Pop到导航栏ViewControllers栈区内指定的ViewController(依据ViewControllers栈区顺序查找)
 
 @param className 类名
 @param animated  动画
 */
- (nullable NSArray<__kindof UIViewController *> *)jx_popToViewControllerFromClassName:(NSString *)className animated:(BOOL)animated;

/**
 根据类别，Pop到导航栏ViewControllers栈区内指定的ViewController(依据ViewControllers栈区顺序查找)
 
 @param aClass   类别
 @param animated 动画
 */
- (nullable NSArray<__kindof UIViewController *> *)jx_popToViewControllerFromClass:(Class)aClass animated:(BOOL)animated;

@end

NS_ASSUME_NONNULL_END
