//
//  UIControl+JXRepeatClickPrevention.h
//  Pods
//
//  Created by Colin on 17/2/11.
//
//  重复点击保护处理

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIControl (JXRepeatClickPrevention)

@property (nonatomic, assign) BOOL jx_repeatClickPrevention;         ///< 是否开启重复点击保护, 默认为NO
@property (nonatomic, assign) NSTimeInterval jx_acceptEventInterval; ///< 指定秒数间隔后响应事件, 仅当jx_repeatClickPrevention为YES时有效

/**
 根据Target对应的类, 添加重复点击保护白名单
 
 @param aClass Target对应的类
 */
+ (void)jx_addRepeatClickPreventionClassForTargetsToWhitelist:(Class)aClass;

/**
 根据Target对应的类, 移除重复点击保护白名单
 
 @param aClass Target对应的类
 */
+ (void)jx_removeRepeatClickPreventionClassForTargetsFromWhitelist:(Class)aClass;


@end

NS_ASSUME_NONNULL_END
