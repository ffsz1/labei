//
//  YPNotiFriendToolView.h
//  HJLive
//
//  Created by feiyin on 2020/7/19.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPNotiFriendToolView : UIView
@property (weak, nonatomic) IBOutlet UILabel *timeTextLabel;
@property (weak, nonatomic) IBOutlet UITextField *chatTextField;
@property (weak, nonatomic) IBOutlet UIView *contentView;
@property (weak, nonatomic) IBOutlet UIView *TimeView;

@property (nonatomic, copy) void(^didClickSendBtnActionBlock)();

- (void)startCountDownTime;


@end
