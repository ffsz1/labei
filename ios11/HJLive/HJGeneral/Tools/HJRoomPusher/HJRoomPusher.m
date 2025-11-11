//
//  HJRoomPusher.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomPusher.h"
#import "HJRoomViewControllerCenter.h"
#import "HJHttpRequestHelper+Room.h"
@implementation HJRoomPusher

+ (void)pushRoomByID:(UserID)userID
{
    [MBProgressHUD showMessage:@"请稍后..."];
    [[HJRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomOwnerUid:userID succ:^(ChatRoomInfo *roomInfo) {
        if (roomInfo != nil && roomInfo.valid) {
            [[HJRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomInfo:roomInfo];
        }else {
            [MBProgressHUD showError:NSLocalizedString(XCPersonalInfoNoRoom, nil)];
        }
    } fail:^(NSString *errorMsg) {
        [MBProgressHUD showError:errorMsg];
    }];
}

+ (void)pushUserInRoomByID:(UserID)userID
{
    [MBProgressHUD showMessage:@"请稍后..."];
    [HJHttpRequestHelper requestUserInRoomInfoBy:userID Success:^(ChatRoomInfo *roomInfo) {
        
        [MBProgressHUD hideHUD];
            if (roomInfo.uid > 0 && roomInfo.valid) {
                [self pushRoomByID:roomInfo.uid];
            }else {
                
//              [MBProgressHUD showError:@"对方不在房间内，已为你跳转私聊页面~"];
              [[NSNotificationCenter defaultCenter] postNotificationName:@"jumpPrivateChatPageNotification" object:[NSNumber numberWithDouble:userID]];
            }
        
    } failure:^(NSNumber *resCode, NSString *message) {
        [MBProgressHUD hideHUD];
    }];
}

@end

