//
//  UIDevice+JXSIMInfo.m
//  Pods
//
//  Created by Colin on 17/6/2.
//
//

#import "UIDevice+JXSIMInfo.h"
#import <CoreTelephony/CTTelephonyNetworkInfo.h>
#import <CoreTelephony/CTCarrier.h>

@implementation UIDevice (JXSIMInfo)

- (NSString *)jx_SIMCarrierName {
    CTTelephonyNetworkInfo *info = [[CTTelephonyNetworkInfo alloc] init];
    return info.subscriberCellularProvider.carrierName;
}


@end
