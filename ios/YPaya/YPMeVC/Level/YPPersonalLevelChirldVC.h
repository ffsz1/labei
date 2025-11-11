//
//  YPPersonalLevelChirldVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPYYViewController.h"
#import "ZJScrollPageViewDelegate.h"
#import "UserInfo.h"
#import "YPLevelModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPPersonalLevelChirldVC : YPYYViewController<ZJScrollPageViewChildVcDelegate>
@property (nonatomic, assign) NSInteger type;
@property (weak, nonatomic) IBOutlet UIImageView *avatarImg;
@property (weak, nonatomic) IBOutlet UIImageView *lvImg;
@property (weak, nonatomic) IBOutlet UIButton *shengjiTip;
@property (nonatomic, strong) UserInfo *userInfo;

@property (nonatomic, strong) YPLevelModel *richLevel;
@property (nonatomic, strong) YPLevelModel *meliLevel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *leftCons;

@property (weak, nonatomic) IBOutlet UIView *bView;
@property (weak, nonatomic) IBOutlet UILabel *tipLabel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *height_layout;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *upview_height_layout;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *pregress_top_layout;

@end

NS_ASSUME_NONNULL_END
