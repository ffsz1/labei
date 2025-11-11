//
//  HJGameRoomVC+Gesture.m
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC+Gesture.h"
#import "HJRoomViewControllerCenter.h"
#import "MMSheetView.h"
#import "MMAlertView.h"
#import "HJGameRoomVC+Alert.h"
#import "HJOnlineListVC.h"
#import "HJRoomViewControllerFactory.h"

@implementation HJGameRoomVC (Gesture)

- (void)initGesture {

    self.onlineNumberLabel.userInteractionEnabled = YES;
    UITapGestureRecognizer *viewOnlineTap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(viewOnlineTapClick)];
    [self.onlineNumberLabel addGestureRecognizer:viewOnlineTap];
    [self.onlineNumberBtn addGestureRecognizer:viewOnlineTap];
}

- (void)viewOnlineTapClick {
    [self showOnlineUsersView];
}






@end
