//
//  YPGameRoomPeiDuiEnterView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPGameRoomPeiDuiEnterView : UIView

@property (nonatomic, copy) void(^peiduiBtnActionBlock)(BOOL isOpen);
@property (nonatomic, copy) void(^supeiBtnActionBlock)();
@property (nonatomic, copy) void(^chooseBtnActionBlock)();
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *right_red;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *ringht_yellow;
@property (weak, nonatomic) IBOutlet UIButton *supeiOpenBtn;

- (void)close;
@property (nonatomic, assign) BOOL didChoose;
@end
