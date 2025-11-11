//
//  UIDevice+JXMachineInfo.h
//  JXCategories
//
//  Created by Colin on 2019/1/30.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIDevice (JXMachineInfo)

#pragma mark - Check
@property (nonatomic, readonly) BOOL jx_isPhone;                    ///< 设备是否为iPhone
@property (nonatomic, readonly) BOOL jx_isPod;                      ///< 设备是否为iPod
@property (nonatomic, readonly) BOOL jx_isPad;                      ///< 设备是否为iPad
@property (nonatomic, readonly) BOOL jx_isSimulator;                ///< 设备是否为模拟器
@property (nonatomic, readonly) BOOL jx_isJailbroken;               ///< 设备是否为越狱设备
@property (nonatomic, readonly) BOOL jx_isTV NS_AVAILABLE_IOS(9_0); ///< 设备是否为iTV
@property (nonatomic, readonly) BOOL jx_canMakePhoneCalls;          ///< 设备是否能打电话

#pragma mark - Machine Model Info
@property (nonatomic, readonly) BOOL jx_isiPhone4;      ///< 设备是否为iPhone4
@property (nonatomic, readonly) BOOL jx_isiPhone4s;     ///< 设备是否为iPhone4s
@property (nonatomic, readonly) BOOL jx_isiPhone5;      ///< 设备是否为iPhone5
@property (nonatomic, readonly) BOOL jx_isiPhone5c;     ///< 设备是否为iPhone5c
@property (nonatomic, readonly) BOOL jx_isiPhone5s;     ///< 设备是否为iPhone5s
@property (nonatomic, readonly) BOOL jx_isiPhone6;      ///< 设备是否为iPhone6
@property (nonatomic, readonly) BOOL jx_isiPhone6Plus;  ///< 设备是否为iPhone6 Plus
@property (nonatomic, readonly) BOOL jx_isiPhone6s;     ///< 设备是否为iPhone6s
@property (nonatomic, readonly) BOOL jx_isiPhone6sPlus; ///< 设备是否为iPhone6s Plus
@property (nonatomic, readonly) BOOL jx_isiPhoneSE;     ///< 设备是否为iPhoneSE
@property (nonatomic, readonly) BOOL jx_isiPhone7;      ///< 设备是否为iPhone7
@property (nonatomic, readonly) BOOL jx_isiPhone7Plus;  ///< 设备是否为iPhone7 Plus
@property (nonatomic, readonly) BOOL jx_isiPhone8;      ///< 设备是否为iPhone8
@property (nonatomic, readonly) BOOL jx_isiPhone8Plus;  ///< 设备是否为iPhone8 Plus
@property (nonatomic, readonly) BOOL jx_isiPhoneX;      ///< 设备是否为iPhoneX
@property (nonatomic, readonly) BOOL jx_isiPhoneXR;     ///< 设备是否为iPhoneXR
@property (nonatomic, readonly) BOOL jx_isiPhoneXS;     ///< 设备是否为iPhoneXS
@property (nonatomic, readonly) BOOL jx_isiPhoneXSMax;  ///< 设备是否为iPhoneXSMax

#pragma mark - Operation System Info
@property (nonatomic, readonly) BOOL jx_isiOS6Later;  ///< 设备系统版本是否iOS6及以上
@property (nonatomic, readonly) BOOL jx_isiOS7Later;  ///< 设备系统版本是否iOS7及以上
@property (nonatomic, readonly) BOOL jx_isiOS8Later;  ///< 设备系统版本是否iOS8及以上
@property (nonatomic, readonly) BOOL jx_isiOS9Later;  ///< 设备系统版本是否iOS9及以上
@property (nonatomic, readonly) BOOL jx_isiOS10Later; ///< 设备系统版本是否iOS10及以上
@property (nonatomic, readonly) BOOL jx_isiOS11Later; ///< 设备系统版本是否iOS11及以上
@property (nonatomic, readonly) BOOL jx_isiOS12Later; ///< 设备系统版本是否iOS12及以上

@end

NS_ASSUME_NONNULL_END
