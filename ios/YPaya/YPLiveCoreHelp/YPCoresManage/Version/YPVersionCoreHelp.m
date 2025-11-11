//
//  VersionCore.m
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPVersionCoreHelp.h"
#import "HJVersionCoreClient.h"
#import "YPHttpRequestHelper+version.h"
#import "YYUtility.h"
#import <AFNetworkReachabilityManager.h>
#import "YPVersionInfo.h"
#import "NSObject+YYModel.h"

@implementation YPVersionCoreHelp

- (BOOL)isReleaseEnv {
#if DEBUG
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    if ([[userDefault objectForKey:EnvID] isEqualToString:@"1"]) {
        return YES;
    }
    return false;
#else
    return YES;
#endif
}

- (instancetype)init
{
    self = [super init];
    if (self) {
        @weakify(self);

    }
    return self;
}

- (void)configJson {
    [YPHttpRequestHelper configClientSuccess:^(id json) {
        NSUserDefaults *ud = [NSUserDefaults standardUserDefaults];
        [ud setObject:json[@"isExchangeAwards"] forKey:@"isExchangeAwards"];
        [ud setObject:json[@"micInListOption"] forKey:@"micInListOption"];
        [ud setObject:json[@"lottery_box_option"] forKey:@"lottery_box_option"];
        [ud setObject:json[@"greenRoomIndex"] forKey:@"greenRoomIndex"];
        [ud setObject:json[@"lotteryBoxBigGift"] forKey:@"lotteryBoxBigGift"];
        [ud setObject:json[@"publicChatHallTime"] forKey:@"publicChatHallTime"];
        [ud setObject:json[@"prohibitModification"] forKey:@"prohibitModification"];
        
        [ud setObject:json[@"sendPicLeftLevel"] forKey:@"sendPicLeftLevel"];

        [ud synchronize];
    } failure:^(NSNumber *resCode, NSString *message) {}];
}

- (void)getVersionData
{
    [self configJson];
    
    
    __weak typeof(self)weakSelf = self;
    
    [YPHttpRequestHelper checkVersion:[YYUtility appVersion] success:^(YPVersionInfo *info) {
        
        weakSelf.versionInfo = info;

        if (info.kickWaiting) {
            NSUserDefaults *ur = [NSUserDefaults standardUserDefaults];
            [ur setObject:[NSString stringWithFormat:@"%ld",(long)info.kickWaiting] forKey:@"kickWaiting"];
            [ur synchronize];
        }
        
        switch (info.status) {
            case Version_Online:
                self.checkIn = NO;
                break;
            case Version_Audting:
                self.checkIn = YES;
                break;
            case Version_Notice:
                self.checkIn = NO;
                NotifyCoreClient(HJVersionCoreClient, @selector(appNeedNoticeWithDesc:version:), appNeedNoticeWithDesc:info.versionDesc version:info.updateVersion);
                break;
            case Version_Suggest:
                self.checkIn = NO;
                NotifyCoreClient(HJVersionCoreClient, @selector(appNeedUpdateWithDesc:version:), appNeedUpdateWithDesc:info.versionDesc version:info.updateVersion);
                break;
            case Version_IsDeleted:
                self.checkIn = NO;
                NotifyCoreClient(HJVersionCoreClient, @selector(appNeedNoticeWithDesc:version:), appNeedNoticeWithDesc:info.versionDesc version:info.updateVersion);
                break;
            default:
                break;
        }
        
        NotifyCoreClient(HJVersionCoreClient, @selector(onRequestVersionStatusSuccess:), onRequestVersionStatusSuccess:info);
        
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];

}



@end
