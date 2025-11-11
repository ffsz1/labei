//
//  UIApplication+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/9.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import "UIApplication+JXBase.h"
#import <mach/mach.h>
#import "NSObject+JXBase.h"
#import "NSString+JXBase.h"
#import <sys/sysctl.h>
#import "UIDevice+JXMachineInfo.h"
#import "UIScreen+JXBase.h"

#define JXUIApplicationNetworkIndicatorDelay (1/30.0)

@interface JXUIApplicationNetworkIndicatorInfo : NSObject
@property (nonatomic, assign) NSInteger count;
@property (nonatomic, strong) NSTimer *timer;
@end

@implementation JXUIApplicationNetworkIndicatorInfo
@end


@implementation UIApplication (JXBase)

#pragma mark - Base
- (NSString *)jx_appBundleName {
    return [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleName"];
}

- (NSString *)jx_appBundleDisplayName {
    return [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleDisplayName"];
}

- (NSString *)jx_appBundleID {
    return [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleIdentifier"];
}

- (NSString *)jx_appVersion {
    return [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleShortVersionString"];
}

- (NSString *)jx_appBuildVersion {
    return [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleVersion"];
}

- (NSURL *)jx_documentsURL {
    return [[[NSFileManager defaultManager] URLsForDirectory:NSDocumentDirectory inDomains:NSUserDomainMask] lastObject];
}

- (NSString *)jx_documentsPath {
    return JXNSDocumentsDirectory();
}

- (NSURL *)jx_cachesURL {
    return [[[NSFileManager defaultManager] URLsForDirectory:NSCachesDirectory inDomains:NSUserDomainMask] lastObject];
}

- (NSString *)jx_cachesPath {
    return JXNSCachesDirectory();
}

- (NSURL *)jx_libraryURL {
    return [[[NSFileManager defaultManager] URLsForDirectory:NSLibraryDirectory inDomains:NSUserDomainMask] lastObject];
}

- (NSString *)jx_libraryPath {
    return JXNSLibraryDirectory();
}

- (NSURL *)jx_temporaryURL {
    return [NSURL fileURLWithPath:JXNSTemporaryDirectory() isDirectory:YES];
}

- (NSString *)jx_temporaryPath {
    return JXNSTemporaryDirectory();
}

- (CGFloat)jx_statusBarHeight {
    CGSize statusBarSize = [UIApplication sharedApplication].statusBarFrame.size;
    return MIN(statusBarSize.height, statusBarSize.width);
}

- (CGFloat)jx_statusBarWidth {
    CGSize statusBarSize = [UIApplication sharedApplication].statusBarFrame.size;
    return MAX(statusBarSize.height, statusBarSize.width);
}

#pragma mark - Check
- (BOOL)_jx_isFileExistInMainBundle:(NSString *)name {
    NSString *bundlePath = [[NSBundle mainBundle] bundlePath];
    NSString *path = [NSString stringWithFormat:@"%@/%@", bundlePath, name];
    return [[NSFileManager defaultManager] fileExistsAtPath:path];
}

- (BOOL)jx_isPirated {
    if ([[UIDevice currentDevice] jx_isSimulator]) return YES; // Simulator is not from appstore
    
    if (getgid() <= 10) return YES; // 目前进程的组识别码 process ID shouldn't be root
    // 破解版包含标签
    if ([[[NSBundle mainBundle] infoDictionary] objectForKey:@"SignerIdentity"]) return YES;
    // 签名信息
    if (![self _jx_isFileExistInMainBundle:@"_CodeSignature"]) return YES;
    
    if (![self _jx_isFileExistInMainBundle:@"SC_Info"]) return YES;
    
    //if someone really want to crack your app, this method is useless..
    //you may change this method's name, encrypt the code and do more check..
    return NO;
}

- (BOOL)jx_isBeingDebugged {
    size_t size = sizeof(struct kinfo_proc);
    struct kinfo_proc info;
    int ret = 0, name[4];
    memset(&info, 0, sizeof(struct kinfo_proc));
    
    name[0] = CTL_KERN;
    name[1] = KERN_PROC;
    name[2] = KERN_PROC_PID;
    name[3] = getpid();
    
    if (ret == (sysctl(name, 4, &info, &size, NULL, 0))) {
        return ret != 0;
    }
    return (info.kp_proc.p_flag & P_TRACED) ? YES : NO;
}

#pragma mark - Top View Controller
- (UIViewController *)jx_topViewController {
    __block UIWindow *normalWindow = [self.delegate window];
    if (normalWindow.windowLevel != UIWindowLevelNormal) {
        [self.windows enumerateObjectsUsingBlock:^(__kindof UIWindow * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            if (obj.windowLevel == UIWindowLevelNormal) {
                normalWindow = obj;
                *stop = YES;
            }
        }];
    }
    return [self _jx_nextTopForViewController:normalWindow.rootViewController];
}

- (UIViewController *)_jx_nextTopForViewController:(UIViewController *)inViewController {
    while (inViewController.presentedViewController) {
        inViewController = inViewController.presentedViewController;
    }
    
    if ([inViewController isKindOfClass:[UITabBarController class]]) {
        UIViewController *selectedViewController = [self _jx_nextTopForViewController:((UITabBarController *)inViewController).selectedViewController];
        return selectedViewController;
    } else if ([inViewController isKindOfClass:[UINavigationController class]]) {
        UIViewController *selectedViewController = [self _jx_nextTopForViewController:((UINavigationController *)inViewController).visibleViewController];
        return selectedViewController;
    } else {
        return inViewController;
    }
}

#pragma mark - Network Activity Indicator
- (void)_jx_setNetworkActivityInfo:(JXUIApplicationNetworkIndicatorInfo *)info {
    [self willChangeValueForKey:NSStringFromSelector(@selector(_jx_networkActivityInfo))];
    [self jx_setAssociatedValue:info withKey:_cmd];
    [self didChangeValueForKey:NSStringFromSelector(@selector(_jx_networkActivityInfo))];
}

- (JXUIApplicationNetworkIndicatorInfo *)_jx_networkActivityInfo {
    return [self jx_getAssociatedValueForKey:@selector(_jx_setNetworkActivityInfo:)];
}

- (void)_jx_delaySetActivity:(NSTimer *)timer {
    NSNumber *visiable = timer.userInfo;
    if (self.networkActivityIndicatorVisible != visiable.boolValue) {
        [self setNetworkActivityIndicatorVisible:visiable.boolValue];
    }
    [timer invalidate];
}

- (void)_jx_changeNetworkActivityCount:(NSInteger)delta {
    @synchronized(self) {
        dispatch_async(dispatch_get_main_queue(), ^{
            JXUIApplicationNetworkIndicatorInfo *info = [self _jx_networkActivityInfo];
            if (!info) {
                info = [JXUIApplicationNetworkIndicatorInfo new];
                [self _jx_setNetworkActivityInfo:info];
            }
            NSInteger count = info.count;
            count += delta;
            info.count = count;
            [info.timer invalidate];
            info.timer = [NSTimer timerWithTimeInterval:JXUIApplicationNetworkIndicatorDelay target:self selector:@selector(_jx_delaySetActivity:) userInfo:@(info.count > 0) repeats:NO];
            [[NSRunLoop mainRunLoop] addTimer:info.timer forMode:NSRunLoopCommonModes];
        });
    }
}

- (void)jx_incrementNetworkActivityCount {
    [self _jx_changeNetworkActivityCount:1];
}

- (void)jx_decrementNetworkActivityCount {
    [self _jx_changeNetworkActivityCount:-1];
}

#pragma mark - Lanuch Image
- (UIImage *)jx_appLanuchImage {
    return [self jx_appLanuchImageForOrientation:self.statusBarOrientation];
}

- (UIImage *)jx_appLanuchImageForOrientation:(UIInterfaceOrientation)orientation {
    NSString *orientationType = nil;
    switch (orientation) {
        case UIInterfaceOrientationUnknown:
        case UIInterfaceOrientationPortrait:
        case UIInterfaceOrientationPortraitUpsideDown:
        {
            orientationType = @"Portrait";
        }
            break;
        case UIInterfaceOrientationLandscapeLeft:
        case UIInterfaceOrientationLandscapeRight:
        {
            orientationType = @"Landscape";
        }
            break;
    }
    
    NSString *lanuchImageName = nil;
    NSArray *imageDatas = [[[NSBundle mainBundle] infoDictionary] valueForKey:@"UILaunchImages"];
    CGSize screenSize = [[UIScreen mainScreen] jx_boundsForOrientation:orientation].size;
    for (NSDictionary *imageData in imageDatas) {
        NSString *aOrientationType = [imageData objectForKey:@"UILaunchImageOrientation"];
        if (![orientationType isEqualToString:aOrientationType]) continue;
        
        //        NSString *aVersion = [imageData objectForKey:@"UILaunchImageMinimumOSVersion"];
        //        CGSize imageSize = CGSizeFromString(dict[@"UILaunchImageSize"])
        // TODO:Version Check
        
        CGSize imageSize = CGSizeFromString([imageData objectForKey:@"UILaunchImageSize"]);
        if (!CGSizeEqualToSize(screenSize, imageSize)) continue;
        
        lanuchImageName = [imageData objectForKey:@"UILaunchImageName"];
    }
    if (!lanuchImageName.length) return nil;
    
    return [UIImage imageNamed:lanuchImageName];
}

@end
