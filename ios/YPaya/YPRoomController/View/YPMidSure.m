//
//  YPMidSure.m
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMidSure.h"
#import "YPRoomViewControllerCenter.h"
#import "YPImRoomCoreV2.h"

@implementation YPMidSure
+ (void)kickUser:(UserID)beKickedUid didKickFinish:(void(^)())didKickFinish {
    
    UIAlertController *alertDialog = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCAlertNiceTip, nil) message:NSLocalizedString(XCRoomIfKickHimOutOfTheRoom, nil) preferredStyle:UIAlertControllerStyleAlert];
    // 创建操作
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        [GetCore(YPImRoomCoreV2) kickUser:beKickedUid didFinish:^{
            if (didKickFinish) {
                didKickFinish();
            }
        }];
    }];

    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {}];

    [alertDialog addAction:cancelAction];
    // 添加操作
    [alertDialog addAction:okAction];
    // 呈现警告视图
    [[YPRoomViewControllerCenter defaultCenter].current presentViewController:alertDialog animated:YES completion:nil];
}


@end
