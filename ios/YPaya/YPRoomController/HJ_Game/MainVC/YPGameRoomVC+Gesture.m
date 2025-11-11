//
//  YPGameRoomVC+Gesture.m
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomVC+Gesture.h"
#import "YPRoomViewControllerCenter.h"
#import "MMSheetView.h"
#import "MMAlertView.h"
#import "YPGameRoomVC+Alert.h"
#import "YPOnlineListVC.h"
#import "YPRoomViewControllerFactory.h"

@implementation YPGameRoomVC (Gesture)

- (void)initGesture {

    self.onlineNumberLabel.userInteractionEnabled = YES;
    UITapGestureRecognizer *viewOnlineTap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(viewOnlineTapClick)];
    [self.onlineNumberLabel addGestureRecognizer:viewOnlineTap];
//    [self.onlineNumberBtn addGestureRecognizer:viewOnlineTap];
}

- (void)viewOnlineTapClick {
    [self showOnlineUsersView];
}






@end
