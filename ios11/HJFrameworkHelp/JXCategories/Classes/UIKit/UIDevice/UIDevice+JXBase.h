//
//  UIDevice+JXBase.h
//  Pods
//
//  Created by Colin on 17/1/9.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

/**
 设备网络传输类型:
 
 WWAN: Wireless Wide Area Network. -> 2G/3G/4G.
 
 WIFI: Wi-Fi.
 
 AWDL: Apple Wireless Direct Link (peer-to-peer connection). -> AirDrop, AirPlay, GameKit
 */
typedef NS_OPTIONS(NSUInteger, JXUIDeviceNetworkTrafficType) {
    JXUIDeviceNetworkTrafficTypeWWANSent     = 1 << 0,
    JXUIDeviceNetworkTrafficTypeWWANReceived = 1 << 1,
    JXUIDeviceNetworkTrafficTypeWIFISent     = 1 << 2,
    JXUIDeviceNetworkTrafficTypeWIFIReceived = 1 << 3,
    JXUIDeviceNetworkTrafficTypeAWDLSent     = 1 << 4,
    JXUIDeviceNetworkTrafficTypeAWDLReceived = 1 << 5,
    
    JXUIDeviceNetworkTrafficTypeWWAN = JXUIDeviceNetworkTrafficTypeWWANSent | JXUIDeviceNetworkTrafficTypeWWANReceived,
    JXUIDeviceNetworkTrafficTypeWIFI = JXUIDeviceNetworkTrafficTypeWIFISent | JXUIDeviceNetworkTrafficTypeWIFIReceived,
    JXUIDeviceNetworkTrafficTypeAWDL = JXUIDeviceNetworkTrafficTypeAWDLSent | JXUIDeviceNetworkTrafficTypeAWDLReceived,
    
    JXUIDeviceNetworkTrafficTypeALL = JXUIDeviceNetworkTrafficTypeWWAN |
    JXUIDeviceNetworkTrafficTypeWIFI |
    JXUIDeviceNetworkTrafficTypeAWDL,
};

@interface UIDevice (JXBase)

#pragma mark - Base
/**
 获取设备系统版本

 @return 设备系统版本
 */
+ (float)jx_systemVersion;

@property (nonatomic, readonly) NSString *jx_currentLanguage; ///< 获取设备当前语言

/**
 设备的机器型号("iPhone6,1", http://theiphonewiki.com/wiki/Models )
 */
@property (nonatomic, readonly) NSString *jx_machineModel;

/**
 设备的机器型号名称("iPhone 5s", http://theiphonewiki.com/wiki/Models )
 */
@property (nullable, nonatomic, readonly) NSString *jx_machineModelName;

/**
 设备的开机时间
 */
@property (nonatomic, readonly) NSDate *jx_systemUptime;

#pragma mark - CPU Info
@property (nonatomic, readonly) NSUInteger jx_cpuCount;                       ///< 获取运行该进程的系统的处于激活状态的CPU处理器(内核)数量
@property (nonatomic, readonly) float jx_cpuUsage;                            ///< 获取当前CPU的占用率(1.0 -> 100%. error -> -1)
@property (nullable, nonatomic, readonly) NSArray<NSNumber *> *jx_cpuUsagePerProcessor; ///< 获取当前CPU每个处理器(内核)的占用率(1.0 -> 100%. error -> nil)

#pragma mark - Disk Info
@property (nonatomic, readonly) int64_t jx_diskSpace;     ///< 当前硬盘总空间(单位字节, error -> -1)
@property (nonatomic, readonly) int64_t jx_diskSpaceFree; ///< 当前硬盘空闲空间(单位字节, error -> -1)
@property (nonatomic, readonly) int64_t jx_diskSpaceUsed; ///< 当前硬盘已使用空间(单位字节, error -> -1)

#pragma mark - Memory Info
@property (nonatomic, readonly) int64_t jx_memoryTotal;     ///< 当前内存物理总空间(单位字节, error -> -1)
@property (nonatomic, readonly) int64_t jx_memoryUsed;      ///< 当前内存已使用空间(单位字节, active + inactive + wired, error -> -1)
@property (nonatomic, readonly) int64_t jx_memoryFree;      ///< 当前内存空闲空间(单位字节, error -> -1)
@property (nonatomic, readonly) int64_t jx_memoryActive;    ///< 当前内存活动空间(单位字节, 已使用, 但可被分页. error -> -1)
@property (nonatomic, readonly) int64_t jx_memoryInactive;  ///< 当前内存不活跃空间(单位字节, 内存不足时, 应用可抢占这部分内存, 可看作空闲内存. error -> -1)
@property (nonatomic, readonly) int64_t jx_memoryWired;     ///< 当前内存系统核心占用空间(单位字节, 已使用, 且不可被分页. error -> -1)
@property (nonatomic, readonly) int64_t jx_memoryPurgeable; ///< 当前内存可回收内存空间(单位字节, error -> -1)

#pragma mark - Network Info
@property (nullable, nonatomic, readonly) NSString *jx_ipAddressWIFI; ///< 设备当前WIFI IP地址(或为nil, @"192.168.1.111");
@property (nullable, nonatomic, readonly) NSString *jx_ipAddressCell; ///< 设备当前Cell IP地址(可为nil, @"10.2.2.222");
@property (nullable, nonatomic, readonly) NSString *jx_macAddress;    ///< 设备的Mac地址(iOS 7以后，无法通过该接口获取有效Mac地址)

/**
 根据设备网络传输类型, 获取设备网络传输数据大小(单位字节, 从上次启动时间算起)
 Usage:
 
 uint64_t bytes = [[UIDevice currentDevice] getNetworkTrafficBytes:JXNetworkTrafficTypeALL];
 NSTimeInterval time = CACurrentMediaTime();
 
 uint64_t bytesPerSecond = (bytes - _lastBytes) / (time - _lastTime);
 
 _lastBytes = bytes;
 _lastTime = time;
 
 @param types 设备网络传输类型
 @return 设备网络传输数据大小
 */
- (uint64_t)jx_getNetworkTrafficBytes:(JXUIDeviceNetworkTrafficType)types;

@end

NS_ASSUME_NONNULL_END
