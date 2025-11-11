//
//  HJHomeHeaderView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "SDCycleScrollView.h"


typedef void(^ClickAction)(void);

@interface HJHomeHeaderView : UIView
@property (weak, nonatomic) IBOutlet UIControl *controlView;

@property (weak, nonatomic) IBOutlet SDCycleScrollView *bannerContainerView;
@property (nonatomic, strong) UINavigationController *naviController;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *centerY_msgView1;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *centerY_msgView2;
@property (weak, nonatomic) IBOutlet UIView *msgView1;
@property (weak, nonatomic) IBOutlet UIView *msgView2;

//动画view1
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView1;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView2;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImage3;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel1;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel2;

//动画view2
@property (weak, nonatomic) IBOutlet GGImageView *avatar1;
@property (weak, nonatomic) IBOutlet GGImageView *avatar2;
@property (weak, nonatomic) IBOutlet GGImageView *avatar3;
@property (weak, nonatomic) IBOutlet UILabel *name2;
@property (weak, nonatomic) IBOutlet UILabel *name1;


@property (nonatomic, strong) NSMutableArray *imRoomMsgArr;//交友大厅消息数组
@property (nonatomic, strong) NSTimer *timer;

@property (nonatomic, assign) NSInteger index;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *hj_jiaoyou_trailing_layout;

- (void)startTimer;

@end

