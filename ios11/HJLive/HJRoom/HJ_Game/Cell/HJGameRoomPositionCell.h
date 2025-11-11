//
//  HJGameRoomPositionCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UserInfo.h"
//#import "RoomQueue.h"
#import "HJRoomMicInfo.h"
#import <FLAnimatedImage/FLAnimatedImageView.h>
#import <NIMSDK/NIMSDK.h>
#import "ChatRoomMember.h"

@interface HJGameRoomPositionCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIButton *sexBtn;

@property (weak, nonatomic) IBOutlet UIImageView *headwearImg;
@property (strong, nonatomic) UserInfo *info;
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UIImageView *speakingAnimate;
@property (weak, nonatomic) IBOutlet UIImageView *be_BlockMicro;
@property (weak, nonatomic) IBOutlet UIImageView *positionStatusNormalBg;
@property (weak, nonatomic) IBOutlet UILabel *nicknameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *positionStatusLockBg;
@property (copy, nonatomic) NSString *position;
@property (weak, nonatomic) IBOutlet UIImageView *genderImageview;
@property (weak, nonatomic) UINavigationController * navigationController;
@property (weak, nonatomic) IBOutlet UILabel *micOrderLabel;

@property (weak, nonatomic) IBOutlet FLAnimatedImageView *playingImageView;
@property (strong, nonatomic) HJRoomMicInfo *HJRoomMicInfo;
@property (strong, nonatomic) ChatRoomMember *member;
@property (weak, nonatomic) IBOutlet UIView *charmView;
@property (weak, nonatomic) IBOutlet UILabel *charmLabel;
@property (weak, nonatomic) IBOutlet UIImageView *hatImageView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *nickName_leading_layout;



@end
