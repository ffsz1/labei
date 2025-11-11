//
//  AppDelegate+HJConfiguration.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "AppDelegate+HJConfiguration.h"
#import <Bugly/Bugly.h>

#import "NIMKit.h"
#import "HJCellLayoutConfig.h"
#import "HJCustomAttachmentDecoder.h"
#import "HJAttachmentDecoder.h"
//闪屏
#import "WMAdImageTool.h"
#import "WMAdvertiseView.h"
#import <AFNetworkReachabilityManager.h>
#import "CommonFileUtils.h"
//core
#import "HJHomeCore.h"
#import "HJHomeCoreClient.h"

#import <IQKeyboardManager.h>
#import "HJWKContionViewConfig.h"
#import <AvoidCrash/AvoidCrash.h>

@implementation AppDelegate (HJConfiguration)
- (void)config{
    [IQKeyboardManager sharedManager].toolbarDoneBarButtonItemText = NSLocalizedString(XCRoomCancel, nil);
    [self configBugly]; //Bugly
    [self configNIMKit]; //配置云信
    [self configAFN];
    [HJWKContionViewConfig progressWKContentViewCrash];
    AddCoreClient(HJHomeCoreClient, self);
    AddCoreClient(HJFaceCoreClient, self);
    
    
    [self isfirstLaunch];
}

- (void)isfirstLaunch {
    NSUserDefaults *de = [NSUserDefaults standardUserDefaults];
    if ([de objectForKey:@"FirstLaunchApp"]) {
        [de setBool:false forKey:@"FirstLaunchApp"];
    } else {
        [de setBool:YES forKey:@"FirstLaunchApp"];
    }
}

- (void)configAFN {
    [[AFNetworkReachabilityManager sharedManager] startMonitoring];
}

- (void)configBugly{
     NSString* appid = @"";
#ifdef DEBUG
     appid = @"d1f7132047";
#else
    [AvoidCrash makeAllEffective];
    NSArray *noneSelClassStrings = @[@"NSString"];
    [AvoidCrash setupNoneSelClassStringsArr:noneSelClassStrings];
     appid = @"2ba94685fc";
#endif
    
    BuglyConfig *buglyConfig = [[BuglyConfig alloc]init];
    buglyConfig.blockMonitorEnable = NO;
    buglyConfig.blockMonitorTimeout = 1;
    buglyConfig.reportLogLevel = BuglyLogLevelWarn;
    
    [Bugly startWithAppId:appid config:buglyConfig];

}

- (void)configADView{
    // 判断沙盒中是否存在广告图片，如果存在，直接显示
    NSString *filePath = [WMAdImageTool getFilePathWithImageName:[kUserDefaults valueForKey:adImageName]];
    BOOL isExist = [WMAdImageTool isFileExistWithFilePath:filePath];
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    if ([userDefault integerForKey:@"adShow"]) {
        [userDefault setInteger:[userDefault integerForKey:@"adShow"]+1 forKey:@"adShow"];
    }else {
        [userDefault setInteger:1 forKey:@"adShow"];
    }
    if (isExist) {// 图片存在
        
        if ([userDefault integerForKey:@"adShow"] > 4) {
            @weakify(self);
            NSString *imageName = [kUserDefaults valueForKey:adImageName];
            AdInfo *info = (AdInfo *)[[HJAdCache shareCache]getAdInfoFromCacheInMainWith:imageName];
            WMAdvertiseView *advertiseView = [[WMAdvertiseView alloc] initWithFrame:self.window.bounds];
            advertiseView.filePath = filePath;
            advertiseView.oldAdInfo = info;
            [advertiseView show];
        }
    }else {
        if ([userDefault integerForKey:@"adShow"] > 4) {
            
            WMAdvertiseView *advertiseView = [[WMAdvertiseView alloc] initWithFrame:self.window.bounds];
            if (iPhoneX) {
//                NSString *fullPath = [[NSBundle mainBundle] pathForResource:@"screen1" ofType:@"png"];
//                UIImage *splashImage=[UIImage imageNamed:@"hj_qidong_x"];
//                advertiseView.adImage = splashImage;
            }else {
//                advertiseView.adImage = [UIImage imageNamed:@"hj_qidong"];
            }
            [advertiseView show];
        }

    }
    
//    // 无论沙盒中是否存在广告图片，都需要重新调用广告接口，判断广告是否更新,这一步在首页中完成。
//     [WMAdImageTool getAdvertisingImage];
    
    
}

- (void)configNIMKit{
    [[NIMKit sharedKit] registerLayoutConfig:[HJCellLayoutConfig new]];
    [NIMCustomObject registerCustomDecoder:[HJCustomAttachmentDecoder new]];//注册自定义message
}

#pragma mark - FaceCoreClient
- (void)onGetFaceJsonSuccess {
    [WMAdImageTool getAdvertisingImage];
}

@end
