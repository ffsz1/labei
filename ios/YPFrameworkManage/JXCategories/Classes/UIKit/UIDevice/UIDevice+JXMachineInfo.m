//
//  UIDevice+JXMachineInfo.m
//  JXCategories
//
//  Created by Colin on 2019/1/30.
//

#import "UIDevice+JXMachineInfo.h"
#import "JXCoreGraphicHelper.h"
#import "NSString+JXBase.h"
#import "UIDevice+JXBase.h"
#import "UIScreen+JXBase.h"

@implementation UIDevice (JXMachineInfo)

#pragma mark - Check
- (BOOL)jx_isPhone {
    static dispatch_once_t one;
    static BOOL phone;
    dispatch_once(&one, ^{
        phone = UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone && [UIDevice currentDevice].jx_canMakePhoneCalls;
    });
    return phone;
}

- (BOOL)jx_isPod {
    static dispatch_once_t one;
    static BOOL pod;
    dispatch_once(&one, ^{
        pod = UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone && ![UIDevice currentDevice].jx_canMakePhoneCalls && ![UIDevice currentDevice].jx_isSimulator;
    });
    return pod;
}

- (BOOL)jx_isPad {
    static dispatch_once_t one;
    static BOOL pad;
    dispatch_once(&one, ^{
        pad = UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad;
    });
    return pad;
}

- (BOOL)jx_isSimulator {
    static dispatch_once_t one;
    static BOOL simulator = NO;
    dispatch_once(&one, ^{
        NSString *model = [self jx_machineModel];
        if ([model isEqualToString:@"x86_64"] || [model isEqualToString:@"i386"]) {
            simulator = YES;
        }
    });
    return simulator;
}

- (BOOL)jx_isJailbroken {
    if ([self jx_isSimulator]) return NO; // Dont't check simulator
    
    // iOS9 URL Scheme query changed ...
    // NSURL *cydiaURL = [NSURL URLWithString:@"cydia://package"];
    // if ([[UIApplication sharedApplication] canOpenURL:cydiaURL]) return YES;
    
    NSArray *paths = @[@"/Applications/Cydia.app",
                       @"/private/var/lib/apt/",
                       @"/private/var/lib/cydia",
                       @"/private/var/stash"];
    for (NSString *path in paths) {
        if ([[NSFileManager defaultManager] fileExistsAtPath:path]) return YES;
    }
    
    FILE *bash = fopen("/bin/bash", "r");
    if (bash != NULL) {
        fclose(bash);
        return YES;
    }
    
    NSString *path = [NSString stringWithFormat:@"/private/%@", [NSString jx_stringWithUUID]];
    if ([@"test" writeToFile : path atomically : YES encoding : NSUTF8StringEncoding error : NULL]) {
        [[NSFileManager defaultManager] removeItemAtPath:path error:nil];
        return YES;
    }
    
    return NO;
}

- (BOOL)jx_isTV {
    static dispatch_once_t one;
    static BOOL TV;
    dispatch_once(&one, ^{
        TV = UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomTV;
    });
    return TV;
}

#ifdef __IPHONE_OS_VERSION_MIN_REQUIRED // 仅在iOS下
- (BOOL)jx_canMakePhoneCalls {
    static dispatch_once_t onceToken;
    static BOOL can = NO;
    dispatch_once(&onceToken, ^{
        can = [[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:@"tel://"]];
    });
    return can;
}
#endif

#pragma mark - Machine Model Info
#define JXDeviceIsMachineModelInSet( _objs... ) \
if (!self.jx_machineModel) return NO; \
static BOOL isConstained = NO; \
static dispatch_once_t onceToken; \
dispatch_once(&onceToken, ^{ \
id objs[] = { _objs }; \
NSUInteger count = sizeof(objs) / sizeof(id); \
if (count > 1) { \
NSSet *set = [NSSet setWithObjects: objs count: count]; \
isConstained = [set containsObject:self.jx_machineModel]; \
} else { \
isConstained = [self.jx_machineModel isEqualToString:objs[0]]; \
} \
}); \
return isConstained; \

- (BOOL)jx_isiPhone4 {
    /*
     https://www.theiphonewiki.com
     */
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint320X480);

    JXDeviceIsMachineModelInSet(@"iPhone3,1", @"iPhone3,2", @"iPhone3,3");
}

- (BOOL)jx_isiPhone4s {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint320X480);
    
    JXDeviceIsMachineModelInSet(@"iPhone4,1");
}

- (BOOL)jx_isiPhone5 {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint320X568);
    
    JXDeviceIsMachineModelInSet(@"iPhone5,1", @"iPhone5,2");
}

- (BOOL)jx_isiPhone5c {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint320X568);
    
    JXDeviceIsMachineModelInSet(@"iPhone5,3", @"iPhone5,4");
}

- (BOOL)jx_isiPhone5s {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint320X568);
    
    JXDeviceIsMachineModelInSet(@"iPhone6,1", @"iPhone6,2");
}

- (BOOL)jx_isiPhone6 {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint375X667);
    
    JXDeviceIsMachineModelInSet(@"iPhone7,2");
}

- (BOOL)jx_isiPhone6Plus {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint414X736);
    
    JXDeviceIsMachineModelInSet(@"iPhone7,1");
}

- (BOOL)jx_isiPhone6s {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint375X667);
    
    JXDeviceIsMachineModelInSet(@"iPhone8,1");
}

- (BOOL)jx_isiPhone6sPlus {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint414X736);
    
    JXDeviceIsMachineModelInSet(@"iPhone8,2");
}

- (BOOL)jx_isiPhoneSE {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint320X568);
    
    JXDeviceIsMachineModelInSet(@"iPhone8,4");
}

- (BOOL)jx_isiPhone7 {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint375X667);
    
    JXDeviceIsMachineModelInSet(@"iPhone9,1", @"iPhone9,3");
}

- (BOOL)jx_isiPhone7Plus {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint414X736);
    
    JXDeviceIsMachineModelInSet(@"iPhone9,2", @"iPhone9,4");
}

- (BOOL)jx_isiPhone8 {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint375X667);
    
    JXDeviceIsMachineModelInSet(@"iPhone10,1", @"iPhone10,4");
}

- (BOOL)jx_isiPhone8Plus {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint414X736);
    
    JXDeviceIsMachineModelInSet(@"iPhone10,2", @"iPhone10,5");
}

- (BOOL)jx_isiPhoneX {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint375X812);
    
    JXDeviceIsMachineModelInSet(@"iPhone10,3", @"iPhone10,6");
}

- (BOOL)jx_isiPhoneXR {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint414X896);
    
    JXDeviceIsMachineModelInSet(@"iPhone11,8");
}

- (BOOL)jx_isiPhoneXS {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint375X812);
    
    JXDeviceIsMachineModelInSet(@"iPhone11,2");
}

- (BOOL)jx_isiPhoneXSMax {
    if ([UIDevice currentDevice].jx_isSimulator) return CGSizeEqualToSize(JXScreenSize(), JXUIScreenSizeInPoint414X896);
    
    JXDeviceIsMachineModelInSet(@"iPhone11,4", @"iPhone11,6");
}

#undef JXDeviceIsMachineModelInSet

#pragma mark - Operation System Info
- (BOOL)jx_isiOS6Later {
    return [UIDevice jx_systemVersion] >= 6;
}

- (BOOL)jx_isiOS7Later {
    return [UIDevice jx_systemVersion] >= 7;
}

- (BOOL)jx_isiOS8Later {
    return [UIDevice jx_systemVersion] >= 8;
}

- (BOOL)jx_isiOS9Later {
    return [UIDevice jx_systemVersion] >= 9;
}

- (BOOL)jx_isiOS10Later {
    return [UIDevice jx_systemVersion] >= 10;
}

- (BOOL)jx_isiOS11Later {
    return [UIDevice jx_systemVersion] >= 11;
}

- (BOOL)jx_isiOS12Later {
    return [UIDevice jx_systemVersion] >= 12;
}

@end
