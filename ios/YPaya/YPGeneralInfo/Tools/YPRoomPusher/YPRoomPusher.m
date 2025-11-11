//
//  YPRoomPusher.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomPusher.h"
#import "YPRoomViewControllerCenter.h"
#import "YPHttpRequestHelper+Room.h"
@implementation YPRoomPusher

+ (void)pushRoomByID:(UserID)userID
{
    [MBProgressHUD showMessage:@"请稍后..."];
    [[YPRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomOwnerUid:userID succ:^(YPChatRoomInfo *roomInfo) {
        if (roomInfo != nil && roomInfo.valid) {
            [[YPRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomInfo:roomInfo];
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
    [YPHttpRequestHelper requestUserInRoomInfoBy:userID Success:^(YPChatRoomInfo *roomInfo) {
        
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

