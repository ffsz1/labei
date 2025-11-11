//
//  HJHomeFollowCell.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^XBDHomeFollowBlock)(UserID uid,BOOL isFan);
@class HJHomeFollowCell;
NS_ASSUME_NONNULL_BEGIN

@interface HJHomeFollowCell : UITableViewCell
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *sexImageView;
@property (weak, nonatomic) IBOutlet UILabel *idLabel;
@property (weak, nonatomic) IBOutlet UIButton *followBtn;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *width_name;

@property (strong,nonatomic) UserInfo *model;

@property (copy,nonatomic) XBDHomeFollowBlock followBlock;
@property (weak, nonatomic) IBOutlet UIButton *voiceBtn;
@property (weak, nonatomic) IBOutlet UIImageView *gifImageview;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (nonatomic, strong) NSString *filePath;
@property  (nonatomic, copy)  void(^clickVoiceBtnBlock)(UserInfo* followModel, HJHomeFollowCell* voiceCell ) ;
@end

NS_ASSUME_NONNULL_END


