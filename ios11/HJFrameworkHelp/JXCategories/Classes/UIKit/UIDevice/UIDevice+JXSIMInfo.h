//
//  UIDevice+JXSIMInfo.h
//  Pods
//
//  Created by Colin on 17/6/2.
//
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIDevice (JXSIMInfo)

@property (nullable, nonatomic, readonly) NSString *jx_SIMCarrierName; ///< 获取设备SIM卡运营商名字

@end

NS_ASSUME_NONNULL_END
